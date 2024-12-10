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

import org.jacodb.api.common.cfg.*
import org.jacodb.api.jvm.JcMethod
import org.jacodb.api.jvm.cfg.JcExpr
import org.jacodb.api.jvm.cfg.JcInst
import org.jacodb.api.jvm.cfg.JcInstLocation
import org.jacodb.api.net.core.IlStmtVisitor
import org.jacodb.api.net.generated.models.*

interface IlStmtLocation : CommonInstLocation {
    override val method: IlMethod
    val index: Int
}

interface IlStmt : CommonInst {
    override val location: IlStmtLocation
    val operands: List<IlExpr>
        get() = emptyList()

    override val method: IlMethod
        get() = location.method

    fun <T> accept(visitor: IlStmtVisitor<T>): T

    companion object {
        fun deserialize(location: IlStmtLocation, src: IlMethod, dto: IlStmtDto): IlStmt {
            return when (dto) {
                is IlAssignStmtDto -> IlAssignStmt(location, dto, src)
                is IlCallStmtDto -> IlCallStmt(location, dto, src)
                is IlCalliStmtDto -> IlCalliStmt(location, dto, src)
                is IlReturnStmtDto -> IlReturnStmt(location, dto, src)
                is IlEndFinallyStmtDto -> IlEndFinallyStmt(location)
                is IlEndFaultStmtDto -> IlEndFaultStmt(location)
                is IlRethrowStmtDto -> IlRethrowStmt(location)
                is IlEndFilterStmtDto -> IlEndFilterStmt(location, dto, src)
                is IlThrowStmtDto -> IlThrowStmt(location, dto, src)
                is IlGotoStmtDto -> IlGotoStmt(location, dto, src)
                is IlIfStmtDto -> IlIfStmt(location, dto, src)
                else -> throw Exception("unexpected dto $dto")
            }
        }
    }
}

abstract class AbstractIlStmt(override val location: IlStmtLocation) : IlStmt {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractIlStmt

        return location == other.location
    }

    override fun hashCode(): Int {
        return location.hashCode()
    }
}

class IlAssignStmt(
    location: IlStmtLocation,
    override val lhv: IlValue,
    override val rhv: IlExpr
) : AbstractIlStmt(location), CommonAssignInst {

    constructor(location: IlStmtLocation, dto: IlAssignStmtDto, src: IlMethod) : this(
        location,
        dto.lhs.deserialize(src) as IlValue,
        dto.rhs.deserialize(src)
    )

    override val operands: List<IlExpr>
        get() = listOf(lhv, rhv)

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlAssignStmt(this)
    }

    override fun toString(): String {
        return "$lhv = $rhv"
    }
}

class IlCallStmt(
    location: IlStmtLocation,
    val call: IlCall
) : AbstractIlStmt(location) {
    constructor(location: IlStmtLocation, dto: IlCallStmtDto, src: IlMethod) : this(
        location,
        dto.call.deserialize(src) as IlCall
    )

    override val operands: List<IlExpr>
        get() = listOf(call)

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlCallStmt(this)
    }

    override fun toString(): String = call.toString()
}

class IlCalliStmt(
    location: IlStmtLocation,
    dto: IlCalliStmtDto,
    src: IlMethod
) : AbstractIlStmt(location) {
    val calli = dto.calli.deserialize(src)

    override val operands: List<IlExpr>
        get() = listOf(calli)

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

class IlReturnStmt(
    location: IlStmtLocation,
    override val returnValue: IlValue?
) : AbstractIlStmt(location), CommonReturnInst {

    constructor(
        location: IlStmtLocation,
        dto: IlReturnStmtDto,
        src: IlMethod
    ) : this(
        location,
        dto.retVal?.deserialize(src) as IlValue
    )

    override val operands: List<IlExpr>
        get() = listOfNotNull(returnValue)

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlReturnStmt(this)
    }

    override fun toString(): String = "return ${returnValue ?: ""}"
}

interface IlEhStmt : IlStmt

class IlThrowStmt(
    location: IlStmtLocation,
    val value: IlExpr
) : AbstractIlStmt(location), IlEhStmt {
    constructor(location: IlStmtLocation, dto: IlThrowStmtDto, src: IlMethod) : this(
        location,
        dto.value.deserialize(src)
    )

    override val operands: List<IlExpr>
        get() = listOf(value)

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlThrowStmt(this)
    }

    override fun toString(): String = "throw $value"
}

class IlRethrowStmt(
    location: IlStmtLocation
) : AbstractIlStmt(location), IlEhStmt {

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlRethrowStmt(this)
    }

    override fun toString(): String = "rethrow"
}

class IlEndFinallyStmt(
    location: IlStmtLocation
) : AbstractIlStmt(location), IlEhStmt {

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlEndFinallyStmt(this)
    }

    override fun toString(): String = "endfinally"
}

class IlEndFaultStmt(
    location: IlStmtLocation
) : AbstractIlStmt(location), IlEhStmt {

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlEndFaultStmt(this)
    }

    override fun toString(): String = "endfault"
}

class IlEndFilterStmt(
    location: IlStmtLocation,
    val value: IlExpr
) : AbstractIlStmt(location), IlEhStmt {

    override val operands: List<IlExpr>
        get() = listOf(value)

    constructor(location: IlStmtLocation, dto: IlEndFilterStmtDto, src: IlMethod) : this(
        location,
        dto.value.deserialize(src)
    )

    override fun <T> accept(visitor: IlStmtVisitor<T>): T {
        return visitor.visitIlEndFilterStmt(this)
    }

    override fun toString(): String {
        return "endfilter $value"
    }
}

interface IlBranchStmt : IlStmt

// TODO CRITICAL
class IlGotoStmt(
    location: IlStmtLocation,
    val target: Int /*IlStmt*/
) : AbstractIlStmt(location), IlBranchStmt, CommonGotoInst {

    constructor(
        location: IlStmtLocation,
        dto: IlBranchStmtDto,
        src: IlMethod
    ) : this(
        location,
        dto.target
    )


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

    override fun toString(): String = "goto $target"

}

// TODO CRITICAL
class IlIfStmt(
    location: IlStmtLocation,
    val target: Int /*IlStmt*/,
    val condition: IlExpr
) : AbstractIlStmt(location), IlBranchStmt, CommonIfInst {

    constructor(location: IlStmtLocation, dto: IlIfStmtDto, src: IlMethod) : this(
        location,
        dto.target,
        dto.cond.deserialize(src)
    )

    override val operands: List<IlExpr>
        get() = listOf(condition)

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
