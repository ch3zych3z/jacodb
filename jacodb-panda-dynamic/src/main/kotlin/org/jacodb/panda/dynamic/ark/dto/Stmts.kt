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

package org.jacodb.panda.dynamic.ark.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonElement

@Serializable
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("_")
sealed interface StmtDto

@Serializable
@SerialName("UnknownStmt")
data class UnknownStmtDto(
    val stmt: JsonElement,
) : StmtDto

@Serializable
@SerialName("NopStmt")
object NopStmtDto : StmtDto {
    override fun toString(): String = javaClass.simpleName
}

@Serializable
@SerialName("AssignStmt")
data class AssignStmtDto(
    val left: ValueDto, // Local
    val right: ValueDto,
) : StmtDto

@Serializable
@SerialName("ArkInvokeStmt")
data class CallStmtDto(
    val expr: CallExprDto,
) : StmtDto

@Serializable
@SerialName("DeleteStmt")
data class DeleteStmtDto(
    val arg: FieldRefDto,
) : StmtDto

@Serializable
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("_")
sealed interface TerminatingStmtDto : StmtDto

@Serializable
@SerialName("ReturnStmt")
data class ReturnStmtDto(
    val arg: ValueDto?,
) : TerminatingStmtDto

@Serializable
@SerialName("ThrowStmt")
data class ThrowStmtDto(
    val arg: ValueDto,
) : TerminatingStmtDto

@Serializable
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("_")
sealed interface BranchingStmtDto : StmtDto

@Serializable
@SerialName("GotoStmt")
object GotoStmtDto : BranchingStmtDto {
    override fun toString(): String = javaClass.simpleName
}

@Serializable
@SerialName("IfStmt")
data class IfStmtDto(
    val condition: ConditionExprDto,
) : BranchingStmtDto

@Serializable
@SerialName("SwitchStmt")
data class SwitchStmtDto(
    val arg: ValueDto,
    val cases: List<ValueDto>,
) : BranchingStmtDto