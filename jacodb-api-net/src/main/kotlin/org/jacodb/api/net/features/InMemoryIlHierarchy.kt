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
import org.jacodb.impl.features.InMemoryHierarchy
import java.util.concurrent.ConcurrentHashMap

typealias InMemoryIlHierarchyCache = ConcurrentHashMap<Long, ConcurrentHashMap<Long, MutableSet<Long>>>

data class InMemoryIlHierarchyReq(val name: String)

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
                    val result = mutableListOf<Triple<Long?, Long?, Long?>>()
                    context.txn.all("Type").map { type ->
                        val asmName: Long? = type.getCompressed("assembly")
                        val fullName: Long? = type.getCompressed("fullname")
                        val parents = mutableListOf<TypeId>()
                        type.getCompressed<ByteArray>("baseType")?.let {
                            parents.add(it.getTypeId())
                        }
                        type.getCompressed<ByteArray>("interfaces")?.let {
                            it.getListOf<TypeId>().forEach { t -> parents.add(t) }
                        }
                    }
                }
            }
        }
    }

    private fun syncQuery(publication: IlPublication, request: InMemoryIlHierarchyReq): Sequence<IlType> {
        TODO()
    }
}