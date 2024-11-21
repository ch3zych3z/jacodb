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

import org.example.ilinstances.IlMethod
import org.jacodb.api.net.generated.models.*

interface IlStmt {
    companion object {
        fun deserialize(src: IlMethod, dto: IlStmtDto): IlStmt {
            return when (dto) {
                is IlAssignStmtDto -> IlAssignStmt(dto, src)
                is IlCallStmtDto -> IlCallStmt(dto, src)
                is IlCalliStmtDto -> IlCalliStmt(dto, src)
                is IlReturnStmtDto -> IlReturnStmt(dto, src)
                is IlEndFinallyStmtDto -> IlEndFinallyStmt()
                is IlEndFaultStmtDto -> IlEndFaultStmt()
                is IlRethrowStmtDto -> IlRethrowStmt()
                is IlEndFilterStmtDto -> IlEndFilterStmt(dto, src)
                is IlThrowStmtDto -> IlThrowStmt(dto, src)
                is IlGotoStmtDto -> IlGotoStmt()
                is IlIfStmtDto -> IlIfStmt(dto, src)
                else -> throw Exception("unexpected dto $dto")
            }
        }
    }
}

class IlAssignStmt(dto: IlAssignStmtDto, src: IlMethod) : IlStmt {
    val lhs = dto.lhs.deserialize(src)
    val rhs = dto.rhs.deserialize(src)
    override fun toString(): String {
        return "$lhs = $rhs"
    }
}

class IlCallStmt(dto: IlCallStmtDto, src: IlMethod) : IlStmt {
    val call = dto.call.deserialize(src)
    override fun toString(): String {
        return call.toString()
    }
}
class IlCalliStmt(dto: IlCalliStmtDto, src: IlMethod): IlStmt {
    val calli = dto.calli.deserialize(src)
}
class IlReturnStmt(dto: IlReturnStmtDto, src: IlMethod) : IlStmt {
    val value: IlExpr? = dto.retVal?.deserialize(src)
    override fun toString(): String {
        return "return ${value ?: ""}"
    }
}

interface IlEhStmt : IlStmt
class IlThrowStmt(dto: IlThrowStmtDto, src: IlMethod) : IlEhStmt {
    val value = dto.value.deserialize(src)
    override fun toString(): String {
        return "throw $value"
    }
}

class IlRethrowStmt : IlEhStmt {
    override fun toString(): String {
        return "rethrow"
    }
}

class IlEndFinallyStmt : IlEhStmt {
    override fun toString(): String {
        return "endfinally"
    }
}

class IlEndFaultStmt : IlEhStmt {
    override fun toString(): String {
        return "endfault"
    }
}

class IlEndFilterStmt(dto: IlEndFilterStmtDto, src: IlMethod) : IlEhStmt {
    val value = dto.value.deserialize(src)
    override fun toString(): String {
        return "endfilter $value"
    }
}

interface IlBranchStmt : IlStmt {
    fun updateTarget(dto: IlBranchStmtDto, src: IlMethod): Unit
}

class IlGotoStmt : IlBranchStmt {
    lateinit var target: IlStmt

    override fun updateTarget(dto: IlBranchStmtDto, src: IlMethod) {
        // TODO NestedFinally
        val targetIndex = when {
            dto.target < src.body.size -> dto.target
            else -> src.body.indices.last
        }
        target = src.body[targetIndex]
    }

    private fun hasTargetSet() = ::target.isInitialized

    override fun toString(): String {
        return when {
            !hasTargetSet() -> "goto ?"
            hasTargetSet() && target != this -> "goto $target"
            else -> "goto self"
        }
    }

}

class IlIfStmt(dto: IlIfStmtDto, src: IlMethod) : IlBranchStmt {
    lateinit var target: IlStmt
    val condition = dto.cond.deserialize(src)
    override fun updateTarget(dto: IlBranchStmtDto, src: IlMethod) {
        // TOOD forcedFault
        val targetIndex = when {
            dto.target < src.body.size -> dto.target
            else -> src.body.indices.last
        }
        target = src.body[targetIndex]
    }

    override fun toString(): String {
        return "if $condition goto $target"
    }
}

