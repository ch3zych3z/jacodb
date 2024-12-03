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
import org.jacodb.api.net.core.IlExprVisitor
import org.jacodb.api.net.generated.models.*
import org.jacodb.api.net.ilinstances.impl.IlFieldImpl
import org.jacodb.api.net.ilinstances.impl.IlMethodImpl
import org.jacodb.api.net.ilinstances.impl.IlTypeImpl

sealed interface IlExpr {
    fun <T> accept(visitor: IlExprVisitor<T>): T
}

fun IlConstDto.deserializeConst(publication: IlPublication): IlConstant = when (this) {
    is IlNullDto -> IlNull()
    is IlBoolConstDto -> IlBoolConstant(value)
    is IlStringConstDto -> IlStringConstant(value)
    is IlCharConstDto -> IlCharConstant(value)
    is IlInt8ConstDto -> IlInt8Constant(value)
    is IlUint8ConstDto -> IlUInt8Constant(value)
    is IlInt16ConstDto -> IlInt16Constant(value)
    is IlUint16ConstDto -> IlUInt16Constant(value)
    is IlInt32ConstDto -> IlInt32Constant(value)
    is IlUint32ConstDto -> IlUInt32Constant(value)
    is IlInt64ConstDto -> IlInt64Constant(value)
    is IlUint64ConstDto -> IlUInt64Constant(value)
    is IlFloatConstDto -> IlFloatConstant(value)
    is IlDoubleConstDto -> IlDoubleConstant(value)
    is IlTypeRefDto -> IlTypeRef(publication.findIlTypeOrNull(type.typeName)!!)
    is IlMethodRefDto -> IlMethodRef(publication.findIlTypeOrNull(this.method.type.typeName)!!.methods.first { method -> method.signature == this.method.name })
    is IlFieldRefDto -> IlFieldRef(publication.findIlTypeOrNull(this.field.type.typeName)!!.fields.first { field -> field.name == this.field.name })
    is IlArrayConstDto -> IlArrayConstant(values.map { it.deserializeConst(publication) })
    is IlEnumConstDto -> IlEnumConstant(publication.findIlTypeOrNull(this.underlyingType.typeName)!!, underlyingValue.deserializeConst(publication))
    else -> throw NotImplementedError()
}

fun IlExprDto.deserialize(ilMethod: IlMethodImpl): IlExpr {
    val publication = ilMethod.declaringType.publication
    return when (this) {
        is IlUnaryOpDto -> IlUnaryOp(operand.deserialize(ilMethod))
        is IlBinaryOpDto -> IlBinaryOp(lhs.deserialize(ilMethod), rhs.deserialize(ilMethod))
        is IlArrayLengthExprDto -> IlArrayLengthExpr(array.deserialize(ilMethod))
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

        is IlNewArrayExprDto -> IlNewArrayExpr(
            publication.findIlTypeOrNull(type.typeName)!!,
            size.deserialize(ilMethod)
        )

        is IlNewExprDto -> IlNewExpr(
            publication.findIlTypeOrNull(type.typeName)!!
        )

        is IlSizeOfExprDto -> IlSizeOfExpr(publication.findIlTypeOrNull(targetType.typeName)!!)
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

        is IlConvExprDto -> IlConvExpr(publication.findIlTypeOrNull(type.typeName)!!, operand.deserialize(ilMethod))
        is IlBoxExprDto -> IlBoxExpr(publication.findIlTypeOrNull(type.typeName)!!, operand.deserialize(ilMethod))
        is IlUnboxExprDto -> IlUnboxExpr(publication.findIlTypeOrNull(type.typeName)!!, operand.deserialize(ilMethod))
        is IlCastClassExprDto -> IlCastClassExpr(
            publication.findIlTypeOrNull(type.typeName)!!,
            operand.deserialize(ilMethod)
        )

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
        is IlVarAccessDto -> when (kind) {
            IlVarKind.local -> ilMethod.locals[index]
            IlVarKind.temp -> ilMethod.temps[index]
            IlVarKind.err -> ilMethod.errs[index]
        }

        is IlArgAccessDto -> ilMethod.args[index]
        else -> throw NotImplementedError()
    }
}

class IlUnaryOp(val operand: IlExpr) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlUnaryOp(this)
    }

    override fun toString(): String = "unOp $operand"
}

class IlBinaryOp(val lhs: IlExpr, val rhs: IlExpr) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlBinaryOp(this)
    }

    override fun toString(): String = "$lhs binOp $rhs"
}

class IlInitExpr(val type: IlTypeImpl) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlInitExpr(this)
    }

    override fun toString(): String = "init $type"
}

class IlNewArrayExpr(val elementType: IlTypeImpl, val size: IlExpr) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlNewArrayExpr(this)
    }

    override fun toString(): String {
        return "new $elementType[$size]"
    }
}

class IlNewExpr(val type: IlTypeImpl) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlNewExpr(this)
    }

    override fun toString(): String {
        return "new $type"
    }
}

class IlSizeOfExpr(val observedType: IlTypeImpl) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlSizeOfExpr(this)
    }

    override fun toString(): String {
        return "sizeOf($observedType)"
    }
}

class IlArrayLengthExpr(val array: IlExpr) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlArrayLength(this)
    }

    override fun toString(): String {
        return "len($array)"
    }
}

class IlFieldAccess(val field: IlFieldImpl, val receiver: IlExpr?) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlFieldAccess(this)
    }

    override fun toString(): String = "${receiver?.toString() ?: ""}$field"
}

class IlArrayAccess(val array: IlExpr, val index: IlExpr) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlArrayAccess(this)
    }

    override fun toString(): String {
        return "$array[$index]"
    }
}

class IlCall(val method: IlMethodImpl, val args: List<IlExpr>) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlCall(this)
    }

    override fun toString(): String {
        return "${method.returnType?.toString() ?: ""} ${method.name}(${args.joinToString(", ")})"
    }
}

class IlCallIndirect(val signature: IlSignature, val ftn: IlExpr, val args: List<IlExpr>) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }

}

sealed class IlCastExpr(val expectedType: IlTypeImpl, val operand: IlExpr) : IlExpr {

}

class IlConvExpr(expectedType: IlTypeImpl, operand: IlExpr) : IlCastExpr(expectedType, operand) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlConvExpr(this)
    }

    override fun toString(): String {
        return "$operand conv $expectedType"

    }
}

class IlBoxExpr(expectedType: IlTypeImpl, operand: IlExpr) : IlCastExpr(expectedType, operand) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlBoxExpr(this)
    }

    override fun toString(): String {
        return "$operand box $expectedType"
    }
}

class IlUnboxExpr(expectedType: IlTypeImpl, operand: IlExpr) : IlCastExpr(expectedType, operand) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlUnboxExpr(this)
    }

    override fun toString(): String {
        return "$operand unbox $expectedType"
    }
}

class IlCastClassExpr(expectedType: IlTypeImpl, operand: IlExpr) : IlCastExpr(expectedType, operand) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlCastClassExpr(this)
    }

    override fun toString(): String {
        return "$operand castclass $expectedType"
    }
}

class IlIsInstExpr(expectedType: IlTypeImpl, operand: IlExpr) : IlCastExpr(expectedType, operand) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlIsInstExpr(this)
    }

    override fun toString(): String {
        return "$operand isinst $expectedType"
    }

}

interface IlRefExpr : IlValue
interface IlDerefExpr : IlValue

class IlManagedRefExpr(val type: IlTypeImpl, val value: IlExpr) : IlRefExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlManagedRefExpr(this)
    }

    override fun toString(): String {
        return "m& $value"
    }
}

class IlUnmanagedRefExpr(val type: IlTypeImpl, val value: IlExpr) : IlRefExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlUnmanagedRefExpr(this)
    }

    override fun toString(): String {
        return "um& $value"
    }
}

class IlManagedDerefExpr(val type: IlTypeImpl, val value: IlExpr) : IlDerefExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlManagedDerefExpr(this)
    }

    override fun toString(): String {
        return "m* $value"
    }
}

class IlUnmanagedDerefExpr(val type: IlTypeImpl, val value: IlExpr) : IlDerefExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlUnmanagedDerefExpr(this)
    }

    override fun toString(): String {
        return "u* $value"
    }
}

class IlStackAllocExpr(val type: IlTypeImpl, val size: IlExpr) : IlExpr {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlStackAllocExpr(this)
    }

    override fun toString(): String {
        return "stackalloc $type $size"
    }
}

class IlArgument(private val dto: IlParameterDto) : IlLocal {
    lateinit var paramType: IlTypeImpl

    val name: String = dto.name
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlArg(this)
    }

    override fun toString(): String {
        return name
    }
}

class IlLocalVar(val type: IlTypeImpl, val index: Int) : IlLocal {
    constructor(dto: IlVarDto, publication: IlPublication) :
            this(publication.findIlTypeOrNull(dto.type.typeName)!!, dto.index)

    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlLocalVar(this)
    }

    override fun toString(): String {
        return "local$index"
    }
}

class IlTempVar(val type: IlTypeImpl, val index: Int) : IlLocal {
    constructor(dto: IlVarDto, publication: IlPublication) :
            this(publication.findIlTypeOrNull(dto.type.typeName)!!, dto.index)

    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitIlTempVar(this)
    }

    override fun toString(): String {
        return "temp$index"
    }
}

class IlErrVar(val type: IlTypeImpl, val index: Int) : IlLocal {
    constructor(dto: IlVarDto, publication: IlPublication) :
            this(publication.findIlTypeOrNull(dto.type.typeName)!!, dto.index)

    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        return visitor.visitErrVar(this)
    }

    override fun toString(): String {
        return "err$index"
    }
}
