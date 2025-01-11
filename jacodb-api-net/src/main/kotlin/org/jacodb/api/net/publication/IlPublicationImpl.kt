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

package org.jacodb.api.net.publication

import com.jetbrains.rd.framework.impl.RpcTimeouts
import org.jacodb.api.net.ilinstances.impl.IlTypeImpl
import org.jacodb.api.net.*
import org.jacodb.api.net.features.IlFeaturesChain
import org.jacodb.api.net.generated.models.IlTypeDto
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.generated.models.ilModel
import org.jacodb.api.net.generated.models.ilSigModel
import org.jacodb.api.net.ilinstances.IlType
import org.jacodb.api.net.storage.asSymbolId
import org.jacodb.api.net.storage.id
import org.jacodb.api.net.storage.txn
import org.jacodb.api.storage.ers.compressed
import java.util.LinkedList
import java.util.Queue
import kotlin.LazyThreadSafetyMode.PUBLICATION


class IlPublicationImpl(
    override val db: IlDatabase,
    override val features: List<IlPublicationFeature>,
    val settings: IlSettings,
    override val targetAsmLocations: List<String>
) : IlPublication {
    override val featuresChain =
        features.let { it + IlPublicationFeatureImpl() + IlPublicationTypeRequestFeature() }.let { IlFeaturesChain(it) }
    override val allTypes: List<IlTypeDto> get() = db.persistence.allTypes

    override val referencedAsmLocations: Map<String, List<String>> by lazy(PUBLICATION) {
        val worklist: Queue<String> = LinkedList<String>(targetAsmLocations);
        val res: MutableMap<String, List<String>> = mutableMapOf()
        db.persistence.read { ctx ->
            val txn = ctx.txn
            while (worklist.isNotEmpty()) {
                val curLoc = worklist.poll()
                if (res.containsKey(curLoc)) continue
                val refs = txn.find(
                    type = "Assembly",
                    propertyName = "location",
                    value = db.persistence.findIdBySymbol(curLoc).compressed
                ).single()
                    .getLinks("references")
                    .map { db.persistence.findSymbolById(it.getCompressed<Long>("location")!!) }.toList()
                res.put(
                    curLoc, refs
                )
                refs.forEach { r -> if (!res.containsKey(r)) worklist.offer(r) }
            }
        }
        res
    }

    override fun findIlTypeOrNull(typeId: TypeId): IlType? =
        featuresChain.callUntilResolved<IlTypeSearchFeature, ResolvedIlTypeResult> { feature ->
            feature.findType(typeId)
        }?.type

    override fun findAsmNameByLocationOrNull(
        location: String,
    ): String? = db.persistence.read { ctx ->
        ctx.txn.find(
            type = "Assembly",
            propertyName = "location",
            value = db.persistence.findIdBySymbol(location).compressed
        ).single().getCompressed<Long>("name")?.let { nameId -> db.persistence.findSymbolById(nameId) }
    }


    private inner class IlPublicationFeatureImpl : IlTypeSearchFeature {
        override fun findType(typeId: TypeId): ResolvedIlTypeResult? {
            val persistence = db.persistence
            val type = persistence.findTypeSourceOrNull(typeId)
                ?.let { dto -> IlTypeImpl.from(dto, this@IlPublicationImpl) }
            return if (type == null) null else ResolvedIlTypeResult(typeId, type)
        }

        override fun event(result: Any): IlPublicationEvent {
            return IlPublicationEvent(this, result)
        }
    }

    private inner class IlPublicationTypeRequestFeature : IlTypeSearchFeature {
        override fun findType(typeId: TypeId): ResolvedIlTypeResult? {
            var rawResponse = listOf<IlTypeDto>()
            db.server.scheduler.invokeOrQueue {
                rawResponse =
                    db.server.protocol.ilModel.ilSigModel.genericSubstitutions.sync(
                        listOf(typeId),
                        RpcTimeouts.longRunning
                    )

            }
            if (rawResponse.isEmpty()) return null;
            val response = IlTypeImpl.from(rawResponse.single(), this@IlPublicationImpl)
            return ResolvedIlTypeResult(response.id, response)
        }

        override fun event(result: Any): IlPublicationEvent? {
            return IlPublicationEvent(this, result)
        }
    }
}
