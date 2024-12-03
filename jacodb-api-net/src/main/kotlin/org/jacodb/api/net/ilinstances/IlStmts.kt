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

import org.jacodb.api.net.core.IlStmtVisitor
import org.jacodb.api.net.generated.models.*
import org.jacodb.api.net.ilinstances.impl.IlMethodImpl

interface IlStmt {
    fun <T> accept(visitor: IlStmtVisitor<T>): T

    companion object {
        fun deserialize(src: IlMethodImpl, dto: IlStmtDto): IlStmt {
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
                is IlGotoStmtDto -> IlGotoStmt(dto, src)
                is IlIfStmtDto -> IlIfStmt(dto, src)
                else -> throw Exception("unexpected dto $dto")
            }
        }
    }
}

class IlAssignStmt(val lhs: IlExpr, val rhs: IlExpr) : IlStmt {

    constructor(dto: IlAssignStmtDto, src: IlMethodImpl) : this(dto.lhs.deserialize(src), dto.rhs.deserialize(src))

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlAssignStmt(this)
    }

    override fun toString(): String {
        return "$lhs = $rhs"
    }
}

class IlCallStmt(val call: IlCall) : IlStmt {
    constructor(dto: IlCallStmtDto, src: IlMethodImpl) : this(dto.call.deserialize(src) as IlCall)

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlCallStmt(this)
    }

    override fun toString(): String {
        return call.toString()
    }
}

class IlCalliStmt(dto: IlCalliStmtDto, src: IlMethodImpl) : IlStmt {
    val calli = dto.calli.deserialize(src)
    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

class IlReturnStmt(val value: IlExpr?) : IlStmt {

    constructor(dto: IlReturnStmtDto, src: IlMethodImpl) : this(dto.retVal?.deserialize(src))

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlReturnStmt(this)
    }

    override fun toString(): String {
        return "return ${value ?: ""}"
    }
}

interface IlEhStmt : IlStmt

class IlThrowStmt(val value: IlExpr) : IlEhStmt {
    constructor(dto: IlThrowStmtDto, src: IlMethodImpl) : this(dto.value.deserialize(src))

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlThrowStmt(this)
    }

    override fun toString(): String {
        return "throw $value"
    }
}

class IlRethrowStmt : IlEhStmt {
    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlRethrowStmt(this)
    }

    override fun toString(): String {
        return "rethrow"
    }
}

class IlEndFinallyStmt : IlEhStmt {
    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlEndFinallyStmt(this)
    }

    override fun toString(): String {
        return "endfinally"
    }
}

class IlEndFaultStmt : IlEhStmt {
    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlEndFaultStmt(this)
    }

    override fun toString(): String {
        return "endfault"
    }
}

class IlEndFilterStmt(val value: IlExpr) : IlEhStmt {
    constructor(dto: IlEndFilterStmtDto, src: IlMethodImpl) : this(dto.value.deserialize(src))

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlEndFilterStmt(this)
    }

    override fun toString(): String {
        return "endfilter $value"
    }
}

interface IlBranchStmt : IlStmt

// TODO CRITICAL
class IlGotoStmt(val target: Int /*IlStmt*/) : IlBranchStmt {

    constructor(
        dto: IlBranchStmtDto,
        src: IlMethodImpl
    ) : this(dto.target) //IlStmt.deserialize(src, src.rawInstList[min(dto.target, src.rawInstList.size - 1)]))

//    override fun updateTarget(dto: IlBranchStmtDto, src: IlMethod) {
//        // TODO NestedFinally
//        val targetIndex = when {
//            dto.target < src.rawInstList.size -> dto.target
//            else -> src.rawInstList.indices.last
//        }
//        target = src.rawInstList[targetIndex]
//    }

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlGotoStmt(this)
    }

//    private fun hasTargetSet() = ::target.isInitialized

    override fun toString(): String {
        return "goto $target"
//        return when {
//            !hasTargetSet() -> "goto ?"
//            hasTargetSet() && target != this -> "goto $target"
//            else -> "goto self"
//        }
    }

}

// TODO CRITICAL
class IlIfStmt(val target: Int /*IlStmt*/, val condition: IlExpr) : IlBranchStmt {

    constructor(dto: IlIfStmtDto, src: IlMethodImpl) : this(
        dto.target,
//        IlStmt.deserialize(src, src.rawInstList[min(dto.target, src.rawInstList.size - 1)]),
        dto.cond.deserialize(src)
    )

//    override fun updateTarget(dto: IlBranchStmtDto, src: IlMethod) {
//        // TOOD forcedFault
//        val targetIndex = when {
//            dto.target < src.rawInstList.size -> dto.target
//            else -> src.rawInstList.indices.last
//        }
//        target = src.rawInstList[targetIndex]
//    }

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlIfStmt(this)
    }

    override fun toString(): String {
        return "if $condition goto $target"
    }
}
