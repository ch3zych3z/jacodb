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

import org.jacodb.api.net.ilinstances.IlField
import org.example.ilinstances.IlMethod
import org.example.ilinstances.IlType
import org.jacodb.api.net.features.IlFeaturesChain
import org.jacodb.api.net.generated.models.IlTypeDto
import org.jacodb.api.net.ilinstances.IlStmt

interface IlTypeLoader {
    val db: IlDatabase
    val featuresChain: IlFeaturesChain
    val allTypes: List<IlTypeDto>
    fun findIlTypeOrNull(name: String): IlType?
}

interface IlTypeLoaderFeature {
    fun on(event: IlTypeLoaderEvent) { }
    fun event(result: Any) : IlTypeLoaderEvent? = null
}

interface IlTypeSearchFeature : IlTypeLoaderFeature {
    fun findType(name: String): ResolvedIlTypeResult?
}

interface IlTypeExtFeature : IlTypeLoaderFeature {
    fun fieldsOf(type: IlType): List<IlField>?
    fun methodsOf(type: IlType): List<IlMethod>?
}

interface IlMethodExtFeature : IlTypeLoaderFeature {
    fun instList(method: IlMethod): ResolvedInstructionsResult?
}

interface IlInstExtFeature : IlTypeLoaderFeature {
    fun transformInstList(instList: List<IlStmt>): List<IlStmt>
}

interface IlTypeLoaderEvent {
    val feature: IlTypeSearchFeature
    val result: Any
}

interface FeatureCallResult

class ResolvedIlTypeResult(val name: String, val type: IlType?) : FeatureCallResult

class ResolvedMethodsResult(val type: IlType, val methods: List<IlMethod>?) : FeatureCallResult
class ResolvedFieldsResult(val type: IlType, val fields: List<IlField>?) : FeatureCallResult

class ResolvedInstructionsResult(val method: IlMethod, val instructions: List<IlStmt>?) : FeatureCallResult
