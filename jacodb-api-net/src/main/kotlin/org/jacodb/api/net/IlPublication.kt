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

package org.jacodb.api.net

import org.jacodb.api.net.ilinstances.impl.IlFieldImpl
import org.jacodb.api.net.ilinstances.impl.IlMethodImpl
import org.jacodb.api.net.ilinstances.impl.IlTypeImpl
import org.jacodb.api.net.features.IlFeaturesChain
import org.jacodb.api.net.generated.models.IlTypeDto
import org.jacodb.api.net.ilinstances.IlStmt

interface IlPublication {
    val db: IlDatabase
    val features: List<IlPublicationFeature>
    val featuresChain: IlFeaturesChain
    val allTypes: List<IlTypeDto>
    fun findIlTypeOrNull(name: String): IlTypeImpl?
    fun findIlTypes(name: String): List<IlTypeImpl>
}

interface IlPublicationFeature {
    fun on(event: IlPublicationEvent) {}

    fun event(result: Any): IlPublicationEvent? = null
}

interface IlTypeSearchFeature : IlPublicationFeature {
    fun findType(name: String): ResolvedIlTypeResult?
}

interface IlTypeSearchAllFeature : IlPublicationFeature {
    fun findTypes(name: String): ResolvedIlTypesResult
}

interface IlTypeExtFeature : IlPublicationFeature {
    fun fieldsOf(type: IlTypeImpl): List<IlFieldImpl>?
    fun methodsOf(type: IlTypeImpl): List<IlMethodImpl>?
}

interface IlMethodExtFeature : IlPublicationFeature {
    fun instList(method: IlMethodImpl): ResolvedInstructionsResult?
}

interface IlInstExtFeature : IlPublicationFeature {
    fun transformInstList(method: IlMethodImpl, instList: List<IlStmt>): List<IlStmt>
}

class IlPublicationEvent(val feature: IlTypeSearchFeature, val result: Any)

interface FeatureCallResult

class ResolvedIlTypeResult(val name: String, val type: IlTypeImpl?) : FeatureCallResult
class ResolvedIlTypesResult(val name: String, val types: List<IlTypeImpl>) : FeatureCallResult

class ResolvedMethodsResult(val type: IlTypeImpl, val methods: List<IlMethodImpl>?) : FeatureCallResult
class ResolvedFieldsResult(val type: IlTypeImpl, val fields: List<IlFieldImpl>?) : FeatureCallResult

class ResolvedInstructionsResult(val method: IlMethodImpl, val instructions: List<IlStmt>) : FeatureCallResult
