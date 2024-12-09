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

import org.jacodb.api.net.IlPublication
import org.jacodb.api.net.IlTypeSearchExactFeature
import org.jacodb.api.net.ResolvedIlTypeResult
import org.jacodb.api.net.core.IlExprVisitor
import org.jacodb.api.net.generated.models.*
import org.jacodb.api.net.ilinstances.impl.IlArrayType
import org.jacodb.api.net.ilinstances.impl.IlMethodImpl
import org.jacodb.api.net.publication.IlPredefinedTypesExt.nuint
import org.jacodb.api.net.publication.IlPredefinedTypesExt.uint32

sealed interface IlExpr {
    val type: IlType
    fun <T> accept(visitor: IlExprVisitor<T>): T
}

fun IlConstDto.deserializeConst(publication: IlPublication): IlConstant {
    val constType = publication.findIlTypeOrNull(type.typeName)!!
    return when (this) {
        is IlNullDto -> IlNull(constType)
        is IlBoolConstDto -> IlBoolConstant(constType, value)
        is IlStringConstDto -> IlStringConstant(constType, value)
        is IlCharConstDto -> IlCharConstant(constType, value)
        is IlInt8ConstDto -> IlInt8Constant(constType, value)
        is IlUint8ConstDto -> IlUInt8Constant(constType, value)
        is IlInt16ConstDto -> IlInt16Constant(constType, value)
        is IlUint16ConstDto -> IlUInt16Constant(constType, value)
        is IlInt32ConstDto -> IlInt32Constant(constType, value)
        is IlUint32ConstDto -> IlUInt32Constant(constType, value)
        is IlInt64ConstDto -> IlInt64Constant(constType, value)
        is IlUint64ConstDto -> IlUInt64Constant(constType, value)
        is IlFloatConstDto -> IlFloatConstant(constType, value)
        is IlDoubleConstDto -> IlDoubleConstant(constType, value)
        is IlTypeRefDto -> IlTypeRef(constType, publication.findIlTypeOrNull(referencedType.typeName)!!)
        is IlMethodRefDto -> IlMethodRef(
            constType,
            publication.findIlTypeOrNull(this.method.type.typeName)!!.methods.first { method -> method.signature == this.method.name })

        is IlFieldRefDto -> IlFieldRef(
            constType,
            publication.findIlTypeOrNull(this.field.type.typeName)!!.fields.first { field -> field.name == this.field.name })

        is IlArrayConstDto -> IlArrayConstant(constType as IlArrayType, values.map { it.deserializeConst(publication) })
        is IlEnumConstDto -> IlEnumConstant(
            constType,
            publication.findIlTypeOrNull(this.underlyingType.typeName)!!,
            underlyingValue.deserializeConst(publication)
        )

        else -> throw NotImplementedError()
    }
}

fun IlUnaryOpDto.deserialize(ilMethod: IlMethod): IlExpr {
    val exprType =
        ilMethod.declaringType.publication.featuresChain.callUntilResolved<IlTypeSearchExactFeature, ResolvedIlTypeResult> { t ->
            t.findExactType(
                type.typeName,
                type.asmName
            )
        }!!.type!!
    return when (this) {
        is IlNegOpDto -> IlNegOp(exprType, operand.deserialize(ilMethod))
        is IlNotOpDto -> IlNotOp(exprType, operand.deserialize(ilMethod))
        else -> throw IllegalArgumentException("Unexpected unaryOp type $this")
    }
}

fun IlBinaryOpDto.deserialize(ilMethod: IlMethod): IlExpr {
    val binOpType = ilMethod.declaringType.publication.findIlTypeOrNull(type.typeName)!!
    val lhsExpr = lhs.deserialize(ilMethod)
    val rhsExpr = rhs.deserialize(ilMethod)
    return when (this) {
        is IlAddOpDto -> IlAddOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        is IlSubOpDto -> IlSubOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        is IlMulOpDto -> IlMulOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        is IlDivOpDto -> IlDivOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        is IlRemOpDto -> IlRemOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        is IlAndOpDto -> IlAndOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        is IlOrOpDto -> IlOrOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        is IlXorOpDto -> IlXorOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        is IlShlOpDto -> IlShlOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        is IlShrOpDto -> IlShrOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        is IlCeqOpDto -> IlCeqOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        is IlCneOpDto -> IlCneOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        is IlCgtOpDto -> IlCgtOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        is IlCgeOpDto -> IlCgeOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        is IlCltOpDto -> IlCltOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        is IlCleOpDto -> IlCleOp(binOpType, lhsExpr, rhsExpr, isChecked, isUnsigned)
        else -> throw IllegalArgumentException("Unexpected binOp type $this")
    }
}

fun IlExprDto.deserialize(ilMethod: IlMethod): IlExpr {
    val publication = ilMethod.declaringType.publication
    return when (this) {
        is IlUnaryOpDto -> this.deserialize(ilMethod)
        is IlBinaryOpDto -> this.deserialize(ilMethod)
        is IlArrayLengthExprDto -> IlArrayLengthExpr(publication.nuint(), array.deserialize(ilMethod))
        is IlCallDto -> {
            val declType = publication.findIlTypeOrNull(this.method.type.typeName)
            if (declType == null)
                throw IllegalArgumentException("method decltype not resolved")
            val methods = declType.methods
            if (methods.isEmpty())
                throw IllegalArgumentException("methods expected in type ${declType.name}")
            val callMethod = methods.filter { m -> m.signature == method.name }
            if (callMethod.size != 1)
                throw IllegalArgumentException("unexpected number of methods found for given name")
            IlCall(
                callMethod.first(),
                args.map { it.deserialize(ilMethod) })
        }

        is IlNewArrayExprDto -> {
            var arrType = publication.findIlTypeOrNull(type.typeName)!! as IlArrayType
            IlNewArrayExpr(arrType, size.deserialize(ilMethod))
        }

        is IlNewExprDto -> IlNewExpr(
            publication.findIlTypeOrNull(type.typeName)!!
        )

        is IlSizeOfExprDto -> IlSizeOfExpr(publication.uint32(), publication.findIlTypeOrNull(targetType.typeName)!!)
        is IlStackAllocExprDto -> IlStackAllocExpr(
            publication.findIlTypeOrNull(type.typeName)!!,
            size.deserialize(ilMethod)
        )

        is IlManagedRefExprDto -> IlManagedRefExpr(
            publication.findIlTypeOrNull(type.typeName)!!,
            value.deserialize(ilMethod)
        )

        is IlUnmanagedRefExprDto -> IlUnmanagedRefExpr(
            publication.findIlTypeOrNull(type.typeName)!!,
            value.deserialize(ilMethod)
        )

        is IlManagedDerefExprDto -> IlManagedDerefExpr(
            publication.findIlTypeOrNull(type.typeName)!!,
            value.deserialize(ilMethod)
        )

        is IlUnmanagedDerefExprDto -> IlUnmanagedDerefExpr(
            publication.findIlTypeOrNull(type.typeName)!!,
            value.deserialize(ilMethod)
        )

        is IlConvExprDto -> IlConvCastExpr(publication.findIlTypeOrNull(type.typeName)!!, operand.deserialize(ilMethod))
        is IlBoxExprDto -> IlBoxExpr(publication.findIlTypeOrNull(type.typeName)!!, operand.deserialize(ilMethod))
        is IlUnboxExprDto -> IlUnboxExpr(publication.findIlTypeOrNull(type.typeName)!!, operand.deserialize(ilMethod))

        is IlIsInstExprDto -> IlIsInstExpr(publication.findIlTypeOrNull(type.typeName)!!, operand.deserialize(ilMethod))
        is IlConstDto -> this.deserializeConst(publication)
        is IlFieldAccessDto -> {
            val fieldType = publication.findIlTypeOrNull(this.field.type.typeName)
            if (fieldType == null) throw IllegalArgumentException("field decltype not found")
            val fields = fieldType.fields
            val fld = fields.firstOrNull { it.name == this.field.name }
            if (fld == null) throw IllegalArgumentException("not such field in given class")
            IlFieldAccess(fld, instance?.deserialize(ilMethod))
        }

        is IlArrayAccessDto -> IlArrayAccess(array.deserialize(ilMethod), index.deserialize(ilMethod))
        // TODO remove force casts
        is IlVarAccessDto -> when (kind) {
            IlVarKind.local -> (ilMethod as IlMethodImpl).locals[index]
            IlVarKind.temp -> (ilMethod as IlMethodImpl).temps[index]
            IlVarKind.err -> (ilMethod as IlMethodImpl).errs[index]
        }

        is IlArgAccessDto -> (ilMethod as IlMethodImpl).args[index]
        else -> throw NotImplementedError()
    }
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

class IlFieldAccess(val field: IlField, val receiver: IlExpr?) : IlExpr {
    override val type: IlType get() = this.field.fieldType
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlFieldAccess(this)
    }

    override fun toString(): String = "${receiver?.toString() ?: ""}$field"
}

class IlArrayAccess(val array: IlExpr, val index: IlExpr) : IlExpr {
    override val type: IlType
        get() = (array.type as IlArrayType).elementType

    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlArrayAccess(this)
    }

    override fun toString(): String {
        return "$array[$index]"
    }
}

class IlCall(val method: IlMethod, val args: List<IlExpr>) : IlExpr {
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

class IlArgument(private val method: IlMethod, private val dto: IlParameterDto) : IlLocal {
    override val type: IlType by lazy { method.declaringType.publication.findIlTypeOrNull(dto.type.typeName)!! }
    val name: String = dto.name
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlArg(this)
    }

    override fun toString(): String {
        return name
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
