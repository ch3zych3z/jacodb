/*
 *  Copyright 2022 UnitTestBot contributors (utbot.org)
 * <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.jacodb.panda.staticvm.classpath

import org.jacodb.api.common.Project
import org.jacodb.panda.staticvm.cfg.PandaGraph
import org.jacodb.panda.staticvm.ir.PandaBasicBlockIr
import org.jacodb.panda.staticvm.ir.PandaProgramIr

class PandaProject : Project {
    private data class TreeEntry<T>(
        val depth: Int,
        val parent: TreeEntry<T>?,
        val data: T,
    )

    private val autoInitializedClasses = mutableListOf<PandaClass>()
    private fun newBlankClass(
        name: String,
        parent: PandaClass?,
        interfaces: Collection<PandaInterface> = emptySet(),
        accessFlags: AccessFlags = AccessFlags(1),
    ) = PandaClass(this, name, parent, interfaces.toSet(), accessFlags)
        .also(autoInitializedClasses::add)

    val objectClass = newBlankClass("std.core.Object", null)
    val stringClass = newBlankClass("std.core.String", objectClass)
    val undefinedClass = newBlankClass("std.core.Undefined", objectClass)
    val typeClass = newBlankClass("std.core.Type", objectClass)
    val voidClass = newBlankClass("std.core.Void", objectClass)

    private val classesIndex = hashMapOf<String, TreeEntry<PandaClass>>()
    private val classesPool = hashSetOf<PandaClass>()

    private val interfacesIndex = hashMapOf<String, PandaInterface>()
    private val interfacesPool = hashSetOf<PandaInterface>()

    private val methodIndex = hashMapOf<String, PandaMethod>()

    fun addClass(node: PandaClass): Boolean {
        if (classesIndex.containsKey(node.name))
            return false

        require(interfacesPool.containsAll(node.directSuperInterfaces))
        val parentNode = node.directSuperClass?.let { requireNotNull(classesIndex[it.name]) }
            ?: classesIndex[objectClass.name]

        classesIndex[node.name] = TreeEntry(parentNode?.depth?.inc() ?: 0, parentNode, node)
        classesPool.add(node)
        return true
    }

    init {
        autoInitializedClasses.forEach(this::addClass)
    }

    fun addInterface(node: PandaInterface): Boolean {
        require(interfacesPool.containsAll(node.directSuperInterfaces))
        interfacesPool.add(node)
        return interfacesIndex.putIfAbsent(node.name, node) == null
    }

    fun addField(field: PandaField): Boolean {
        require(field.enclosingClass in classesPool)
        return field.enclosingClass.declaredFields.putIfAbsent(field.name, field) == null
    }

    fun addMethod(method: PandaMethod): Boolean {
        require(method.enclosingClass in classesPool || method.enclosingClass in interfacesPool)
        method.parameterTypes.forEach { requireNotNull(findTypeOrNull(it.typeName)) }
        if (methodIndex.containsKey(method.signature))
            return false
        methodIndex[method.signature] = method
        method.enclosingClass.declaredMethods[method.signature] = method
        return true
    }

    fun addFlowGraph(method: PandaMethod, basicBlocksInfo: List<PandaBasicBlockIr>): Boolean {
        require(method.enclosingClass in classesPool || method.enclosingClass in interfacesPool)
        return flowGraphs.putIfAbsent(method, PandaGraph.of(method, basicBlocksInfo)) == null
    }

    fun findClassOrNull(name: String): PandaClass? = classesIndex[name]?.data
    fun findInterfaceOrNull(name: String): PandaInterface? = interfacesIndex[name]
    fun findClassOrInterfaceOrNull(name: String): PandaClassOrInterface? =
        classesIndex[name]?.data ?: interfacesIndex[name]

    fun findMethod(name: String) = requireNotNull(methodIndex[name]) {
        "Not found method $name"
    }

    companion object {
        fun fromProgramInfo(programInfo: PandaProgramIr): PandaProject {
            val pandaProject = PandaProject()
            programInfo.addInterfacesHierarchyToPandaClasspath(pandaProject)
            programInfo.addClassesHierarchyToPandaClasspath(pandaProject)
            programInfo.addFieldsToPandaClasspath(pandaProject)
            programInfo.addMethodsToPandaClasspath(pandaProject)
            programInfo.addFlowGraphsToPandaClasspath(pandaProject)
            return pandaProject
        }
    }

    override fun findTypeOrNull(name: String): PandaType? =
        if (name.endsWith("[]"))
            findTypeOrNull(name.removeSuffix("[]"))?.array
        else PandaPrimitives.findPrimitiveOrNull(name) ?: findClassOrInterfaceOrNull(name)?.type

    fun findType(name: String) = requireNotNull(findTypeOrNull(name)) {
        "Not found type $name"
    }

    fun findClass(name: String) = requireNotNull(findClassOrNull(name)) {
        "Not found class $name"
    }

    fun findClassOrInterface(name: String) = requireNotNull(findClassOrInterfaceOrNull(name))
    fun getElementType(name: String) = if (name.endsWith("[]")) findType(name.removeSuffix("[]"))
    else throw IllegalArgumentException("Expected array type")

    override fun close() = Unit

    private val flowGraphs: MutableMap<PandaMethod, PandaGraph> = hashMapOf()

    val classes: List<PandaClass>
        get() = classesIndex.values.map { it.data }

    val methods: List<PandaMethod>
        get() = methodIndex.values.toList()

    fun flowGraph(method: PandaMethod): PandaGraph = flowGraphs.getOrPut(method) { PandaGraph.empty() }

    private fun ancestors(node: TreeEntry<PandaClass>): List<PandaClassType> =
        (node.parent?.let { ancestors(it) } ?: emptyList()).plus(node.data.type)

    fun commonClassType(types: Collection<PandaClassType>): PandaClassType? {
        if (types.isEmpty())
            return objectClass.type
        val nodes = types.map { requireNotNull(classesIndex[it.typeName]) }
        val paths = nodes.map { ancestors(it) }

        return paths.reduce { p1, p2 ->
            p1.zip(p2) { t1, t2 -> t1.takeIf { t1 == t2 } }.filterNotNull()
        }.lastOrNull()
    }

    fun commonType(types: Collection<PandaType>): PandaType? {
        if (types.any { it is PandaArrayType }) {
            return types.takeIf { it.all { it is PandaArrayType } }
                ?.filterIsInstance<PandaArrayType>()
                ?.map { it.elementType }
                ?.let(this::commonType)
                ?.array
                ?: objectClass.type
        }
        if (types.any { it is PandaPrimitiveType }) {
            return types.takeIf { it.all { it is PandaPrimitiveType } }
                ?.filterIsInstance<PandaPrimitiveType>()
                ?.max()
        }
        return commonClassType(types.filterIsInstance<PandaClassType>())
    }

    fun resolveIntrinsic(id: String) = Intrinsics.resolve(id)?.let { (className, methodName) ->
        findClassOrInterface(className).findMethodBySimpleName(methodName)
    }
}