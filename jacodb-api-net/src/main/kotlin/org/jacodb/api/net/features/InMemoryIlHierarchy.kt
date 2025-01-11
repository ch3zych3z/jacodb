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

import org.jacodb.api.net.IlDatabase
import org.jacodb.api.net.IlPublication
import org.jacodb.api.net.generated.models.IlTypeDto
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.generated.models.getIlTypeDto
import org.jacodb.api.net.ilinstances.IlType
import org.jacodb.api.net.storage.asTypeId
import org.jacodb.api.net.storage.interned
import org.jacodb.api.net.storage.txn
import org.jacodb.api.net.storage.withEmptyTypeArgs
import org.jacodb.api.storage.ers.compressed
import org.jacodb.api.storage.ers.links
import java.util.concurrent.ConcurrentHashMap

typealias InMemoryIlHierarchyCache = ConcurrentHashMap<Long, MutableSet<Long>>

data class InMemoryIlHierarchyReq(
    val typeId: TypeId,
    val transitive: Boolean = true,
    val includeBytecode: Boolean = false
)

object InMemoryIlHierarchy : IlFeature<InMemoryIlHierarchyReq, IlType> {

    // TODO #2 check in memory hierarchy is built for the whole db, not publication
    private val hierarchies = ConcurrentHashMap<IlDatabase, InMemoryIlHierarchyCache>()

    override suspend fun query(
        publication: IlPublication,
        request: InMemoryIlHierarchyReq
    ): Sequence<IlType> {
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
                        links(type, "baseType").asIterable.singleOrNull()?.let { baseType ->
                            if (baseType.getCompressed<Boolean>("isGenericType")!!) {
                                links(baseType, "genericDefinition").asIterable.singleOrNull()?.let { baseGenDef ->
                                    parents += baseGenDef.getCompressed<Long>("typeId")!!
                                }
                            } else
                                parents += baseType.getCompressed<Long>("typeId")!!
                        }
                        links(type, "implements").asIterable.forEach { interf ->
                            if (interf.getCompressed<Boolean>("isGenericType")!!) {
                                links(interf, "genericDefinition").asIterable.singleOrNull()?.let { baseGenDef ->
                                    parents += baseGenDef.getCompressed<Long>("typeId")!!
                                }
                            } else
                                parents += interf.getCompressed<Long>("typeId")!!
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

    private fun syncQuery(publication: IlPublication, request: InMemoryIlHierarchyReq): Sequence<IlType> {
        val persistence = publication.db.persistence
        val typeIdInterner = persistence.typeIdInterner
        val hierarchy = hierarchies[publication.db] ?: return emptySequence()

        fun Long.optGenericDefn() = asTypeId(typeIdInterner).withEmptyTypeArgs().interned(typeIdInterner)

        fun Long.isSupertypeOfOrNull(mbChild: Long): Long? {
            val sup = publication.findIlTypeOrNull(typeIdInterner.findValue(this))!!
            if (!sup.isGenericType) return mbChild
            // got a feeling that there is a mistake here
            val sub = publication.findIlTypeOrNull(typeIdInterner.findValue(mbChild))!!
            if (!sub.isGenericType) return mbChild
            val supDef = sup.genericDefinition!!
            val matching =
                (sub.interfaces + sub.baseType).filter { it != null && it.genericDefinition?.let { gd -> gd == supDef } ?: false }
            check(sub.isGenericDefinition)
            val paramToArg = sub.genericArgs.associateWith { param ->
                matching.mapNotNull { supArg ->
                    supArg?.genericArgs?.mapIndexedNotNull { index, arg ->
                        if (arg == param) sup.genericArgs[index] else null
                    }
                }.flatten().singleOrNull()
            }

            // TODO replace with proper checks here
            val subTypeId = sub.id
            val requestTypeId = TypeId(
                sub.genericDefinition!!.genericArgs.map { paramToArg[it]?.id ?: it.id },
                subTypeId.asmName,
                subTypeId.typeName
            )
            // check interning works properly on such request
            val response = publication.findIlTypeOrNull(requestTypeId)
            return response?.let { typeIdInterner.findIdOrNew(it.id) }

            /* we want to check this :> other
             * here may be the following situations:
             * 1. both ~ non-generic
             *   + the only possible check is exact inheritance (or transitive)
             * 2. this ~ generic & other ~ non-generic
             *   -
             * 3. this ~ non-generic & other ~ generic
             *   + the only possible check here is generic defn inherit from this (since we cannot define specific constraints for generic substitution)
             * 4. both ~ generic
             *   - T1<P1, P2> :> T2<P3, P4, P5>
             *
             * seems like for situations except when both are generics, exact inheritance is the only think we can check
             */
        }

        fun subClassesOf(requestedType: Long, transitive: Boolean, result: HashSet<Long>) {
            val direct =
                hierarchy[requestedType.optGenericDefn()].orEmpty().mapNotNull { requestedType.isSupertypeOfOrNull(it) }
                    .toSet()
            result.addAll(direct)
            if (!transitive) return
            direct.forEach { subclass -> subClassesOf(subclass, true, result) }
        }

        val subclasses = hashSetOf<Long>()
        subClassesOf(request.typeId.interned(typeIdInterner), request.transitive, subclasses)

        if (subclasses.isEmpty()) return emptySequence()
        return subclasses.mapNotNull { publication.findIlTypeOrNull(it.asTypeId(typeIdInterner)) }.asSequence()
//        persistence.read { context ->
//            subclasses.flatMap { typeId ->
//                context.txn.find(type = "Type", propertyName = "typeId", typeId.compressed).map {
//                    it.getRawBlob("bytes")?.getIlTypeDto()
//                }.filterNotNull()
//            }.asSequence()
//        }
    }
}

