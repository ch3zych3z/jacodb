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

import org.example.ilinstances.*
import org.jacodb.api.net.generated.models.*

sealed interface IlExpr {
}

fun IlExprDto.deserialize(ilMethod: IlMethod): IlExpr = when (this) {
    is IlNullDto -> IlNull()
    is IlBoolConstDto -> IlBoolConst(value)
    is IlStringConstDto -> IlStringConst(value)
    is IlByteConstDto -> IlByteConst(value)
    is IlIntConstDto -> IlIntConst(value)
    is IlLongConstDto -> IlLongConst(value)
    is IlFloatConstDto -> IlFloatConst(value)
    is IlDoubleConstDto -> IlDoubleConst(value)
    is IlTypeRefDto -> IlTypeRef(IlInstance.cache.getType(referencedType))
    is IlMethodRefDto -> IlMethodRef(IlInstance.cache.getMethod(method))
    is IlFieldRefDto -> IlFieldRef(IlInstance.cache.getField(field))
    is IlUnaryOpDto -> IlUnaryOp(operand.deserialize(ilMethod))
    is IlBinaryOpDto -> IlBinaryOp(lhs.deserialize(ilMethod), rhs.deserialize(ilMethod))
    is IlArrayLengthExprDto -> IlArrayLengthExpr(array.deserialize(ilMethod))
    is IlCallDto -> IlCall(IlInstance.cache.getMethod(method), args.map { it.deserialize(ilMethod) })
    is IlInitExprDto -> IlInitExpr(IlInstance.cache.getType(type))
    is IlNewArrayExprDto -> IlNewArrayExpr(IlInstance.cache.getType(type), size.deserialize(ilMethod))
    is IlNewExprDto -> IlNewExpr(IlInstance.cache.getType(type), args.map { it.deserialize(ilMethod) })
    is IlSizeOfExprDto -> IlSizeOfExpr(IlInstance.cache.getType(targetType))
    is IlStackAllocExprDto -> IlStackAllocExpr(IlInstance.cache.getType(type), size.deserialize(ilMethod))
    is IlManagedRefExprDto -> IlManagedRefExpr(IlInstance.cache.getType(type), value.deserialize(ilMethod))
    is IlUnmanagedRefExprDto -> IlUnmanagedRefExpr(IlInstance.cache.getType(type), value.deserialize(ilMethod))
    is IlManagedDerefExprDto -> IlManagedDerefExpr(IlInstance.cache.getType(type), value.deserialize(ilMethod))
    is IlUnmanagedDerefExprDto -> IlUnmanagedDerefExpr(IlInstance.cache.getType(type), value.deserialize(ilMethod))
    is IlConvExprDto -> IlConvExpr(IlInstance.cache.getType(type), operand.deserialize(ilMethod))
    is IlBoxExprDto -> IlBoxExpr(IlInstance.cache.getType(type), operand.deserialize(ilMethod))
    is IlUnboxExprDto -> IlUnboxExpr(IlInstance.cache.getType(type), operand.deserialize(ilMethod))
    is IlCastClassExprDto -> IlCastClassExpr(IlInstance.cache.getType(type), operand.deserialize(ilMethod))
    is IlIsInstExprDto -> IlIsInstExpr(IlInstance.cache.getType(type), operand.deserialize(ilMethod))
    is IlFieldAccessDto -> IlFieldAccess(IlInstance.cache.getField(field), instance?.deserialize(ilMethod))
    is IlArrayAccessDto -> IlArrayAccess(array.deserialize(ilMethod), index.deserialize(ilMethod))
    is IlVarAccessDto -> when (kind) {
        IlVarKind.local -> ilMethod.locals[index]
        IlVarKind.temp -> ilMethod.temps[index]
        IlVarKind.err -> ilMethod.errs[index]
    }

    is IlArgAccessDto -> ilMethod.args[index]
    else -> throw NotImplementedError()
}

class IlUnaryOp(val operand: IlExpr) : IlExpr {
    override fun toString(): String = "unOp $operand"
}

class IlBinaryOp(val lhs: IlExpr, val rhs: IlExpr) : IlExpr {
    override fun toString(): String = "$lhs binOp $rhs"
}

class IlInitExpr(val type: IlType) : IlExpr {
    override fun toString(): String = "init $type"
}

class IlNewArrayExpr(val elementType: IlType, val size: IlExpr) : IlExpr {
    override fun toString(): String {
        return "new $elementType[$size]"
    }
}

class IlNewExpr(val type: IlType, val args: List<IlExpr>) : IlExpr {
    override fun toString(): String {
        return "new $type(${args.joinToString(", ")})"
    }
}

class IlSizeOfExpr(val observedType: IlType) : IlExpr {
    override fun toString(): String {
        return "sizeOf($observedType)"
    }
}

class IlArrayLengthExpr(val array: IlExpr) : IlExpr {
    override fun toString(): String {
        return "len($array)"
    }
}

class IlFieldAccess(val field: IlField, val receiver: IlExpr?) : IlExpr {
    override fun toString(): String = "${receiver?.toString() ?: ""}$field"
}

class IlArrayAccess(val array: IlExpr, val index: IlExpr) : IlExpr {
    override fun toString(): String {
        return "$array[$index]"
    }
}

class IlCall(val method: IlMethod, val args: List<IlExpr>) : IlExpr {
    override fun toString(): String {
        return "${method.returnType?.toString() ?: ""} ${method.name}(${args.joinToString(", ")})"
    }
}

sealed class IlCastExpr(val expectedType: IlType, val operand: IlExpr) : IlExpr {

}

class IlConvExpr(expectedType: IlType, operand: IlExpr) : IlCastExpr(expectedType, operand) {
    override fun toString(): String {
        return "$operand conv $expectedType"

    }
}

class IlBoxExpr(expectedType: IlType, operand: IlExpr) : IlCastExpr(expectedType, operand) {
    override fun toString(): String {
        return "$operand box $expectedType"
    }
}

class IlUnboxExpr(expectedType: IlType, operand: IlExpr) : IlCastExpr(expectedType, operand) {
    override fun toString(): String {
        return "$operand unbox $expectedType"
    }
}

class IlCastClassExpr(expectedType: IlType, operand: IlExpr) : IlCastExpr(expectedType, operand) {
    override fun toString(): String {
        return "$operand castclass $expectedType"
    }
}

class IlIsInstExpr(expectedType: IlType, operand: IlExpr) : IlCastExpr(expectedType, operand) {
    override fun toString(): String {
        return "$operand isinst $expectedType"
    }

}

interface IlRefExpr : IlValue
interface IlDerefExpr : IlValue

class IlManagedRefExpr(val type: IlType, val value: IlExpr) : IlRefExpr {
    override fun toString(): String {
        return "m& $value"
    }
}

class IlUnmanagedRefExpr(val type: IlType, val value: IlExpr) : IlRefExpr {
    override fun toString(): String {
        return "um& $value"
    }
}
class IlManagedDerefExpr(val type: IlType, val value: IlExpr) : IlDerefExpr {
    override fun toString(): String {
        return "m* $value"
    }
}
class IlUnmanagedDerefExpr(val type: IlType, val value: IlExpr) : IlDerefExpr {
    override fun toString(): String {
        return "u* $value"
    }
}

class IlStackAllocExpr(val type: IlType, val size: IlExpr) : IlExpr {
    override fun toString(): String {
        return "stackalloc $type $size"
    }
}

class IlArgument(private val dto: IlParameterDto) : IlLocal {
    lateinit var paramType: IlType

    val name: String = dto.name
    override fun toString(): String {
        return name
    }
}

class IlLocalVar(dto: IlVarDto) : IlLocal {
    val type: IlType = IlInstance.cache.getType(dto.type)
    val index: Int = dto.index

    override fun toString(): String {
        return "local$index"
    }
}

class IlTempVar(dto: IlVarDto) : IlLocal {
    val type: IlType = IlInstance.cache.getType(dto.type)
    val index: Int = dto.index

    override fun toString(): String {
        return "temp$index"
    }
}

class IlErrVar(dto: IlVarDto) : IlLocal {
    val type: IlType = IlInstance.cache.getType(dto.type)
    val index: Int = dto.index

    override fun toString(): String {
        return "err$index"
    }
}