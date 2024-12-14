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
import org.jacodb.api.net.IlPublication
import org.jacodb.api.net.core.IlExprVisitor
import org.jacodb.api.net.generated.models.IlParameterDto
import org.jacodb.api.net.generated.models.IlVarDto
import org.jacodb.api.net.ilinstances.impl.IlArrayType

sealed interface IlExpr : CommonExpr {
    val type: IlType
    override val typeName: String
        get() = type.fullname

    fun <T> accept(visitor: IlExprVisitor<T>): T
}

open class IlUnaryOp(override val type: IlType, val operand: IlExpr) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlUnaryOp(this)
    }

    override fun toString(): String = "unOp $operand"
}

class IlNegOp(type: IlType, operand: IlExpr) : IlUnaryOp(type, operand)
class IlNotOp(type: IlType, operand: IlExpr) : IlUnaryOp(type, operand)


open class IlBinaryOp(
    override val type: IlType,
    val lhs: IlExpr,
    val rhs: IlExpr,
    val isChecked: Boolean = false,
    val isUnsigned: Boolean = false
) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlBinaryOp(this)
    }

    override fun toString(): String = "$lhs binOp $rhs"
}

class IlAddOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlSubOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlMulOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlDivOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlRemOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlAndOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlOrOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlXorOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlShlOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlShrOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlCeqOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlCneOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlCgtOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlCgeOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlCltOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlCleOp(type: IlType, lhs: IlExpr, rhs: IlExpr, isChecked: Boolean, isUnsigned: Boolean) :
    IlBinaryOp(type, lhs, rhs, isChecked, isUnsigned)

class IlNewArrayExpr(override val type: IlArrayType, val size: IlExpr) : IlExpr {
    //TODO critical serialize array type into newarr
    val elementType: IlType get() = type.elementType
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlNewArrayExpr(this)
    }

    override fun toString(): String {
        return "new $elementType[$size]"
    }
}

class IlNewExpr(override val type: IlType) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlNewExpr(this)
    }

    override fun toString(): String {
        return "new $type"
    }
}

class IlSizeOfExpr(override val type: IlType, val observedType: IlType) : IlExpr {

    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlSizeOfExpr(this)
    }

    override fun toString(): String {
        return "sizeOf($observedType)"
    }
}

class IlArrayLengthExpr(override val type: IlType, val array: IlExpr) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlArrayLength(this)
    }

    override fun toString(): String {
        return "len($array)"
    }
}

class IlFieldAccess(val field: IlField, override val instance: IlValue?) : IlValue, CommonFieldRef {
    override val type: IlType get() = this.field.fieldType
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlFieldAccess(this)
    }

    override fun toString(): String = "${instance?.toString() ?: ""}$field"
}

class IlArrayAccess(override val array: IlValue, override val index: IlValue) : IlValue, CommonArrayAccess {
    override val type: IlType
        get() = (array.type as IlArrayType).elementType

    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlArrayAccess(this)
    }

    override fun toString(): String {
        return "$array[$index]"
    }
}

class IlCall(val method: IlMethod, override val args: List<IlValue>) : IlExpr, CommonCallExpr {
    override val type: IlType get() = method.returnType
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlCall(this)
    }

    override fun toString(): String {
        return "${method.returnType.toString()} ${method.name}(${args.joinToString(", ")})"
    }
}

class IlCallIndirect(val signature: IlSignature, val ftn: IlExpr, val args: List<IlExpr>) : IlExpr {
    override val type: IlType
        get() = signature.returnType

    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }

}

sealed class IlCastExpr(val expectedType: IlType, val operand: IlExpr) : IlExpr {

}

// TODO hz
class IlConvCastExpr(override val type: IlType, operand: IlExpr) : IlCastExpr(type, operand) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlConvExpr(this)
    }

    override fun toString(): String {
        return "$operand conv $expectedType"

    }
}

class IlBoxExpr(override val type: IlType, operand: IlExpr) : IlCastExpr(type, operand) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlBoxExpr(this)
    }

    override fun toString(): String {
        return "$operand box $expectedType"
    }
}

class IlUnboxExpr(override val type: IlType, operand: IlExpr) :
    IlCastExpr(type, operand) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlUnboxExpr(this)
    }

    override fun toString(): String {
        return "$operand unbox $expectedType"
    }
}

// TODO maybe we need value of`type` to be nullable here
class IlIsInstExpr(override val type: IlType, operand: IlExpr) :
    IlCastExpr(type, operand) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlIsInstExpr(this)
    }

    override fun toString(): String {
        return "$operand isinst $expectedType"
    }

}

interface IlRefExpr : IlValue
interface IlDerefExpr : IlValue

class IlManagedRefExpr(override val type: IlType, val value: IlExpr) : IlRefExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlManagedRefExpr(this)
    }

    override fun toString(): String {
        return "m& $value"
    }
}

class IlUnmanagedRefExpr(override val type: IlType, val value: IlExpr) : IlRefExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlUnmanagedRefExpr(this)
    }

    override fun toString(): String {
        return "um& $value"
    }
}

class IlManagedDerefExpr(override val type: IlType, val value: IlExpr) : IlDerefExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlManagedDerefExpr(this)
    }

    override fun toString(): String {
        return "m* $value"
    }
}

class IlUnmanagedDerefExpr(override val type: IlType, val value: IlExpr) : IlDerefExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlUnmanagedDerefExpr(this)
    }

    override fun toString(): String {
        return "u* $value"
    }
}

class IlStackAllocExpr(override val type: IlType, val size: IlExpr) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlStackAllocExpr(this)
    }

    override fun toString(): String {
        return "stackalloc $type $size"
    }
}

abstract class IlArgument(override val type: IlType, val name: String, val index: Int) : IlLocal, CommonArgument {
    override fun toString(): String {
        return name
    }
}

class IlThis(type: IlType) : IlArgument(type, "this", 0), CommonThis {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlArg(this)
    }

}

class IlArgumentImpl(type: IlType, name: String, index: Int) : IlArgument(type, name, index) {
    constructor(
        method: IlMethod,
        dto: IlParameterDto
    ) : this(method.declaringType.publication.findIlTypeOrNull(dto.type.typeName)!!, dto.name, dto.index)

    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlArg(this)
    }
}

class IlLocalVar(override val type: IlType, val index: Int) : IlLocal {
    constructor(dto: IlVarDto, publication: IlPublication) :
            this(publication.findIlTypeOrNull(dto.type.typeName)!!, dto.index)

    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlLocalVar(this)
    }

    override fun toString(): String {
        return "local$index"
    }
}

class IlTempVar(override val type: IlType, val index: Int) : IlLocal {
    constructor(dto: IlVarDto, publication: IlPublication) :
            this(publication.findIlTypeOrNull(dto.type.typeName)!!, dto.index)

    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlTempVar(this)
    }

    override fun toString(): String {
        return "temp$index"
    }
}

class IlErrVar(override val type: IlType, val index: Int) : IlLocal {
    constructor(dto: IlVarDto, publication: IlPublication) :
            this(publication.findIlTypeOrNull(dto.type.typeName)!!, dto.index)

    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitErrVar(this)
    }

    override fun toString(): String {
        return "err$index"
    }
}
