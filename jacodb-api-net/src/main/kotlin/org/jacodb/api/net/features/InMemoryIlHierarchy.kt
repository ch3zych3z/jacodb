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

package org.jacodb.api.net.features

import com.sun.org.apache.xpath.internal.operations.Bool
import org.jacodb.api.net.IlDatabase
import org.jacodb.api.net.IlPublication
import org.jacodb.api.net.generated.models.IlTypeDto
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.generated.models.getIlTypeDto
import org.jacodb.api.net.ilinstances.IlType
import org.jacodb.api.net.storage.asTypeId
import org.jacodb.api.net.storage.interned
import org.jacodb.api.net.storage.txn
import org.jacodb.api.storage.ers.compressed
import org.jacodb.api.storage.ers.links
import java.util.concurrent.ConcurrentHashMap
import org.jacodb.impl.util.Sequence

typealias InMemoryIlHierarchyCache = ConcurrentHashMap<Long, MutableSet<Long>>

data class InMemoryIlHierarchyReq(
    val typeId: TypeId,
    val transitive: Boolean = true,
    val includeBytecode: Boolean = false
)

object InMemoryIlHierarchy : IlFeature<InMemoryIlHierarchyReq, IlTypeDto> {

    private val hierarchies = ConcurrentHashMap<IlDatabase, InMemoryIlHierarchyCache>()

    override suspend fun query(
        publication: IlPublication,
        request: InMemoryIlHierarchyReq
    ): Sequence<IlTypeDto> {
        return syncQuery(publication, request)
    }

    override fun onSignal(signal: IlSignal) {
        when (signal) {
            is IlSignal.BeforeIndexing -> {
                signal.db.persistence.read { context ->
                    val cache = InMemoryIlHierarchyCache().also {
                        hierarchies[signal.db] = it
                    }
                    val result = mutableListOf<Pair<Long, Long>>()
                    context.txn.all("Type").forEach { type ->
                        if (type.getCompressed<Boolean>("isGenericType")!! && !type.getCompressed<Boolean>("isGenericDefinition")!!) {
                            return@forEach
                        }
                        val tId: Long = type.getCompressed<Long>("typeId")!!
                        val parents = mutableListOf<Long>()
                        // TODO use genericDefn here instead of real type
                        links(type, "baseType").asIterable.singleOrNull()?.let {
//                            val isGenericType = it.getCompressed<Boolean>("isGenericType")!!
//                            val isGenericDefn = it.getCompressed<Boolean>("isGenericDefinition")!!
                            parents += it.getCompressed<Long>("typeId")!!
//                                if (isGenericType && !isGenericDefn)
//                                    (links(it, "genericDefinition")).asIterable.single()
//                                        .getCompressed<Long>("typeId")!!
//                                else

                        }
                        links(type, "implements").asIterable.forEach {
//                            val isGenericType = it.getCompressed<Boolean>("isGenericType")!!
//                            val isGenericDefn = it.getCompressed<Boolean>("isGenericDefinition")!!

                            parents += it.getCompressed<Long>("typeId")!!
//                                if (isGenericType && !isGenericDefn)
//                                (links(it, "genericDefinition")).asIterable.single()
//                                    .getCompressed<Long>("typeId")!!
//                            else

                        }
                        parents.forEach {
                            result += Pair(tId, it)
                        }
                    }
                    result.forEach { (typeId, parentId) ->
                        cache.getOrPut(parentId) { ConcurrentHashMap.newKeySet<Long>() }.add(typeId)
                    }
                }
            }
        }
    }

    private fun syncQuery(publication: IlPublication, request: InMemoryIlHierarchyReq): Sequence<IlTypeDto> {
        val persistence = publication.db.persistence
        val hierarchy = hierarchies[publication.db] ?: return emptySequence()

        fun getSubclasses(typeId: Long, transitive: Boolean, result: HashSet<Long>) {
            // TODO #1 filter by asm name or location from publication
            val directSubclasses = hierarchy[typeId].orEmpty().toSet()
            result.addAll(directSubclasses)
            if (transitive) {
                directSubclasses.forEach {
                    getSubclasses(it, true, result)
                }
            }
        }

        val subclasses = hashSetOf<Long>()
        if (request.typeId.typeArgs.isNotEmpty()) {
            val nonFilteredSubclasses = hashSetOf<Long>()
            val rootId = persistence.typeIdInterner.findIdOrNew(request.typeId.apply { this.typeArgs.map { null } })
            getSubclasses(rootId, request.transitive, nonFilteredSubclasses)
            val reqType = publication.findIlTypeOrNull(request.typeId)!!
            nonFilteredSubclasses.filterTo(subclasses) {
                val substTarget = publication.findIlTypeOrNull(it.asTypeId(persistence.typeIdInterner))!!
                reqType.substInto(substTarget)
            }
            println("")
        } else {
            getSubclasses(request.typeId.interned(persistence.typeIdInterner), request.transitive, subclasses)
        }
        if (subclasses.isEmpty()) return emptySequence()
        return persistence.read { context ->
            subclasses.flatMap { typeId ->
                context.txn.find(type = "Type", propertyName = "typeId", typeId.compressed).map {
                    it.getRawBlob("bytes")?.getIlTypeDto()
                }.filterNotNull()
            }.asSequence()
        }
    }

    private fun IlType.substInto(target: IlType): Boolean {
        // TODO #1
        return true
    }
}