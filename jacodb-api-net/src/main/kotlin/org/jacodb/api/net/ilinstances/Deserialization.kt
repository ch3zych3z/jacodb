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
import org.jacodb.api.net.generated.models.IlAddOpDto
import org.jacodb.api.net.generated.models.IlAndOpDto
import org.jacodb.api.net.generated.models.IlArgAccessDto
import org.jacodb.api.net.generated.models.IlArrayAccessDto
import org.jacodb.api.net.generated.models.IlArrayConstDto
import org.jacodb.api.net.generated.models.IlArrayLengthExprDto
import org.jacodb.api.net.generated.models.IlBinaryOpDto
import org.jacodb.api.net.generated.models.IlBoolConstDto
import org.jacodb.api.net.generated.models.IlBoxExprDto
import org.jacodb.api.net.generated.models.IlCallDto
import org.jacodb.api.net.generated.models.IlCeqOpDto
import org.jacodb.api.net.generated.models.IlCgeOpDto
import org.jacodb.api.net.generated.models.IlCgtOpDto
import org.jacodb.api.net.generated.models.IlCharConstDto
import org.jacodb.api.net.generated.models.IlCleOpDto
import org.jacodb.api.net.generated.models.IlCltOpDto
import org.jacodb.api.net.generated.models.IlCneOpDto
import org.jacodb.api.net.generated.models.IlConstDto
import org.jacodb.api.net.generated.models.IlConvExprDto
import org.jacodb.api.net.generated.models.IlDivOpDto
import org.jacodb.api.net.generated.models.IlDoubleConstDto
import org.jacodb.api.net.generated.models.IlEnumConstDto
import org.jacodb.api.net.generated.models.IlExprDto
import org.jacodb.api.net.generated.models.IlFieldAccessDto
import org.jacodb.api.net.generated.models.IlFieldRefDto
import org.jacodb.api.net.generated.models.IlFloatConstDto
import org.jacodb.api.net.generated.models.IlInt16ConstDto
import org.jacodb.api.net.generated.models.IlInt32ConstDto
import org.jacodb.api.net.generated.models.IlInt64ConstDto
import org.jacodb.api.net.generated.models.IlInt8ConstDto
import org.jacodb.api.net.generated.models.IlIsInstExprDto
import org.jacodb.api.net.generated.models.IlManagedDerefExprDto
import org.jacodb.api.net.generated.models.IlManagedRefExprDto
import org.jacodb.api.net.generated.models.IlMethodRefDto
import org.jacodb.api.net.generated.models.IlMulOpDto
import org.jacodb.api.net.generated.models.IlNegOpDto
import org.jacodb.api.net.generated.models.IlNewArrayExprDto
import org.jacodb.api.net.generated.models.IlNewExprDto
import org.jacodb.api.net.generated.models.IlNotOpDto
import org.jacodb.api.net.generated.models.IlNullDto
import org.jacodb.api.net.generated.models.IlOrOpDto
import org.jacodb.api.net.generated.models.IlRemOpDto
import org.jacodb.api.net.generated.models.IlShlOpDto
import org.jacodb.api.net.generated.models.IlShrOpDto
import org.jacodb.api.net.generated.models.IlSizeOfExprDto
import org.jacodb.api.net.generated.models.IlStackAllocExprDto
import org.jacodb.api.net.generated.models.IlStringConstDto
import org.jacodb.api.net.generated.models.IlSubOpDto
import org.jacodb.api.net.generated.models.IlTypeRefDto
import org.jacodb.api.net.generated.models.IlUint16ConstDto
import org.jacodb.api.net.generated.models.IlUint32ConstDto
import org.jacodb.api.net.generated.models.IlUint64ConstDto
import org.jacodb.api.net.generated.models.IlUint8ConstDto
import org.jacodb.api.net.generated.models.IlUnaryOpDto
import org.jacodb.api.net.generated.models.IlUnboxExprDto
import org.jacodb.api.net.generated.models.IlUnmanagedDerefExprDto
import org.jacodb.api.net.generated.models.IlUnmanagedRefExprDto
import org.jacodb.api.net.generated.models.IlVarAccessDto
import org.jacodb.api.net.generated.models.IlVarKind
import org.jacodb.api.net.generated.models.IlXorOpDto
import org.jacodb.api.net.ilinstances.impl.IlArrayType
import org.jacodb.api.net.ilinstances.impl.IlMethodImpl
import org.jacodb.api.net.publication.IlPredefinedTypesExt.nuint
import org.jacodb.api.net.publication.IlPredefinedTypesExt.uint32


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
        ilMethod.declaringType.publication.findIlTypeOrNull(type.typeName)!!
    return when (this) {
        is IlNegOpDto -> IlNegOp(exprType, operand.deserialize(ilMethod))
        is IlNotOpDto -> IlNotOp(exprType, operand.deserialize(ilMethod))
        else -> throw IllegalArgumentException("Unexpected unaryOp type $this")
    }
}

fun IlBinaryOpDto.deserialize(ilMethod: IlMethod): IlExpr {
    val binOpType = ilMethod.declaringType.publication.findIlTypeOrNull(type.typeName)!!
    val lhsExpr = lhv.deserialize(ilMethod)
    val rhsExpr = rhv.deserialize(ilMethod)
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
            IlFieldAccess(fld, instance?.deserialize(ilMethod) as IlValue?)
        }

        is IlArrayAccessDto -> IlArrayAccess(
            array.deserialize(ilMethod) as IlValue,
            index.deserialize(ilMethod) as IlValue
        )
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
