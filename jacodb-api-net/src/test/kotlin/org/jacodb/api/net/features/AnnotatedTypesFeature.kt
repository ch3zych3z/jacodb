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

import org.jacodb.api.net.IlPublication
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.ilinstances.IlType
import org.jacodb.api.net.storage.txn


object AnnotatedTypesFeature : IlFeature<TypeId, IlType> {
    private val observedAnnotationsNames =
        mutableSetOf<String>()
    private val collectedTypes: MutableMap<String, MutableSet<Long>> by lazy {
        observedAnnotationsNames.associateWithTo(mutableMapOf()) { mutableSetOf() }
    }


    fun observeByName(name: String) = observedAnnotationsNames.add(name)

    override suspend fun query(
        publication: IlPublication,
        request: TypeId
    ): Sequence<IlType> =
        collectedTypes.entries.singleOrNull { (annotation, _) ->
            annotation == request.typeName
        }?.value?.map {
            publication.db.persistence.findTypeIdById(it).let { typeId -> publication.findIlTypeOrNull(typeId)!! }
        }?.asSequence() ?: emptySequence<IlType>()


    override fun onSignal(signal: IlSignal) {
        when (signal) {
            is IlSignal.BeforeIndexing -> {
                signal.db.persistence.read { ctx ->
                    ctx.txn.all("Attribute").forEach { attr ->
                        val fullname =
                            attr.getCompressed<Long>("fullname").let { signal.db.persistence.findSymbolById(it!!) }
                        if (collectedTypes.keys.contains(fullname)) {
                            collectedTypes[fullname]!!.addAll(
                                attr.getLinks("target")
                                    .mapNotNull { typeEntity -> typeEntity.getCompressed<Long>("typeId") })
                        }
                    }
                }
            }
        }
    }


}