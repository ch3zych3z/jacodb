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

import org.jacodb.api.net.cfg.IlGraphImpl
import org.jacodb.api.net.features.IlFeaturesChain
import org.jacodb.api.net.generated.models.IlTypeDto
import org.jacodb.api.net.ilinstances.IlField
import org.jacodb.api.net.ilinstances.IlMethod
import org.jacodb.api.net.ilinstances.IlStmt
import org.jacodb.api.net.ilinstances.IlType

interface IlPublication {
    val db: IlDatabase
    val features: List<IlPublicationFeature>
    val featuresChain: IlFeaturesChain
    val allTypes: List<IlTypeDto>
    fun findIlTypeOrNull(name: String): IlType?
    fun findIlTypes(name: String): List<IlType>
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

interface IlTypeSearchExactFeature : IlPublicationFeature {
    // TODO add cache
    fun findExactType(fullname: String, asmName: String? = null): ResolvedIlTypeResult
}

interface IlTypeExtFeature : IlPublicationFeature {
    fun fieldsOf(type: IlType): List<IlField>?
    fun methodsOf(type: IlType): List<IlMethod>?
}

interface IlMethodExtFeature : IlPublicationFeature {

    class IlInstListResult(val method: IlMethod, val instructions: List<IlStmt>) : FeatureCallResult
    class IlFlowGraphResult(val method: IlMethod, val flowGraph: IlGraphImpl) : FeatureCallResult

    fun instList(method: IlMethod): IlInstListResult?
    fun flowGraph(method: IlMethod): IlFlowGraphResult?
}

interface IlInstExtFeature : IlPublicationFeature {
    fun transformInstList(method: IlMethod, instList: List<IlStmt>): List<IlStmt>
}

class IlPublicationEvent(val feature: IlTypeSearchFeature, val result: Any)

interface FeatureCallResult

class ResolvedIlTypeResult(val name: String, val type: IlType?) : FeatureCallResult
class ResolvedIlTypesResult(val name: String, val types: List<IlType>) : FeatureCallResult
class ResolvedMethodsResult(val type: IlType, val methods: List<IlMethod>?) : FeatureCallResult
class ResolvedFieldsResult(val type: IlType, val fields: List<IlField>?) : FeatureCallResult
