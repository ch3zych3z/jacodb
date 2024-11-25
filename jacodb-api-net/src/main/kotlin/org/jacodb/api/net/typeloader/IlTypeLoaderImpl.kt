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

package org.jacodb.api.net.typeloader

import org.example.ilinstances.IlType
import org.jacodb.api.net.*
import org.jacodb.api.net.features.IlFeaturesChain
import org.jacodb.api.net.generated.models.IlTypeDto

class IlTypeLoaderImpl(
    override val db: IlDatabase,
    features: List<IlTypeLoaderFeature>,
) : IlTypeLoader {
    override val featuresChain = IlFeaturesChain(features + IlTypeLoaderFeatureImpl())
    override val allTypes: List<IlTypeDto> get() = db.persistence.allTypes

    override fun findIlTypeOrNull(name: String): IlType? =
        featuresChain.callUntilResolved<IlTypeSearchFeature, ResolvedIlTypeResult> { feature ->
            feature.findType(name)
        }?.type

    private inner class IlTypeLoaderFeatureImpl() : IlTypeSearchFeature {
        override fun findType(name: String): ResolvedIlTypeResult {
            val persistence = db.persistence
            val type = persistence.findTypeSourceByNameOrNull(name)?.let { dto -> IlType(dto, this@IlTypeLoaderImpl) }
            return ResolvedIlTypeResult(name, type)
            // maybe return null for unknown classes to continue search in chain?
        }
    }
}
