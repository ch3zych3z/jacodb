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

import org.jacodb.api.net.ilinstances.impl.IlTypeImpl
import org.jacodb.api.net.*
import org.jacodb.api.net.features.IlFeaturesChain
import org.jacodb.api.net.generated.models.IlTypeDto
import org.jacodb.api.net.ilinstances.IlType
import org.jacodb.api.net.storage.txn

class IlPublicationImpl(
    override val db: IlDatabase, override val features: List<IlPublicationFeature>, val settings: IlSettings
) : IlPublication {
    override val featuresChain = features.let { it + IlPublicationFeatureImpl() }.let { IlFeaturesChain(it) }
    override val allTypes: List<IlTypeDto> get() = db.persistence.allTypes
    override fun findIlTypeOrNull(name: String): IlType? =
        featuresChain.callUntilResolved<IlTypeSearchFeature, ResolvedIlTypeResult> { feature ->
            feature.findType(name)
        }?.type

    override fun findIlTypes(name: String): List<IlType> =
        featuresChain.callUntilResolved<IlTypeSearchAllFeature, ResolvedIlTypesResult> { feature ->
            feature.findTypes(name)
        }?.types ?: emptyList()

    private inner class IlPublicationFeatureImpl() : IlTypeSearchFeature, IlTypeSearchAllFeature,
        IlTypeSearchExactFeature {
        override fun findType(name: String): ResolvedIlTypeResult {
            val persistence = db.persistence
            val type = persistence.findTypeSourceByNameOrNull(name)
                ?.let { dto -> IlTypeImpl.from(dto, this@IlPublicationImpl) }
            return ResolvedIlTypeResult(name, type)
            // maybe return null for unknown classes to continue search in chain?
        }

        override fun findTypes(name: String): ResolvedIlTypesResult {
            val persistence = db.persistence
            val types = persistence.findTypeSourcesByName(name)
            return ResolvedIlTypesResult(name, types.map { IlTypeImpl.from(it, this@IlPublicationImpl) })
        }

        override fun event(result: Any): IlPublicationEvent {
            return IlPublicationEvent(this, result)
        }

        override fun findExactType(fullName: String, asmName: String?): ResolvedIlTypeResult {
            val persistence = db.persistence
            val lookup = persistence.findTypeSourcesByName(fullName)
            if (lookup.size == 1)
                return ResolvedIlTypeResult(fullName, IlTypeImpl.from(lookup.first(), this@IlPublicationImpl))
            if (asmName == null || lookup.size == 0) return ResolvedIlTypeResult(fullName, null)
            val exactLookup = lookup.filter {
                it.asmName == asmName
            }
            // TODO use asm hierarchy here
            if (exactLookup.size != 1) return ResolvedIlTypeResult(fullName, null)
            return ResolvedIlTypeResult(fullName, IlTypeImpl.from(exactLookup.first(), this@IlPublicationImpl))

        }
    }
}
