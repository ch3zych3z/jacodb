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
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.generated.models.getListOf
import org.jacodb.api.net.generated.models.getTypeId
import org.jacodb.api.net.ilinstances.IlType
import org.jacodb.api.net.storage.txn
import org.jacodb.api.storage.ers.links
import java.util.concurrent.ConcurrentHashMap

typealias InMemoryIlHierarchyCache = ConcurrentHashMap<Long, MutableSet<Long>>

data class InMemoryIlHierarchyReq(
    val name: String,
    val transitive: Boolean = true,
    val includeBytecode: Boolean = false
)

object InMemoryIlHierarchy : IlFeature<InMemoryIlHierarchyReq, IlType> {

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
                    context.txn.all("Type").map { type ->
                        val tId: Long = type.getCompressed<Long>("typeId")!!
                        val parents = mutableListOf<Long>()
                        links(type, "baseType").asIterable.singleOrNull()?.let {
                            parents += it.getCompressed<Long>("typeId")!!
                        }
                        links(type, "implements").asIterable.forEach {
                            parents += it.getCompressed<Long>("typeId")!!
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
        val hierarchy = hierarchies[publication.db] ?: return emptySequence()

//        fun getSubclasses(typeId: Long)
        TODO()
    }
}