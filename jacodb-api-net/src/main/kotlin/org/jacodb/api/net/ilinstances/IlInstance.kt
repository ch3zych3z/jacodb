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

package org.jacodb.api.net.ilinstances

import org.jacodb.api.common.CommonMethod
import org.jacodb.api.common.CommonMethodParameter
import org.jacodb.api.common.CommonTypeName
import org.jacodb.api.net.IlPublication
import org.jacodb.api.net.generated.models.IlStmtDto

interface IlInstance

interface IlType : IlInstance, CommonTypeName {
    val publication: IlPublication
    val declaringType: IlType?
    val genericArgs: List<IlType>
    override val typeName: String
    val fullname: String
    val asmName: String
    val name: String

    val attributes: List<IlAttribute>

    val fields: List<IlField>
    val methods: List<IlMethod>
}

interface IlField : IlInstance {
    val fieldType: IlType
    val name: String
    val isStatic: Boolean
    val attributes: List<IlAttribute>
}

interface IlMethod : IlInstance, CommonMethod {
    val declaringType: IlType
    override val returnType: IlType
    override val name: String
    val signature: String
    val rawInstList: List<IlStmtDto>
    val instList: List<IlStmt>
    override val parameters: List<IlParameter>
}

interface IlParameter : IlInstance, CommonMethodParameter {
    override val type: IlType
    val attributes: List<IlAttribute>
    val name: String
}

interface IlAttribute : IlInstance {
    val type: IlType
}
