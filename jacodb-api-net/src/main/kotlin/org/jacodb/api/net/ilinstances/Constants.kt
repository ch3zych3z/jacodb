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

import org.jacodb.api.net.core.IlExprVisitor

class IlNull : IlConstant {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlNullConst(this)
}

data class IlBoolConstant(val value: Boolean) : IlConstant {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlBoolConst(this)

    override fun toString() = "$value"
}

data class IlStringConstant(val value: String) : IlConstant {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlStringConst(this)

    override fun toString() = "`$value`"
}

data class IlCharConstant(override val value: Char) : IlNumericConstant<Char>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlCharConst(this)
}

data class IlInt8Constant(override val value: Byte) : IlNumericConstant<Byte>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlInt8Const(this)
}

data class IlUInt8Constant(override val value: UByte) : IlNumericConstant<UByte>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlUInt8Const(this)
}

data class IlInt16Constant(override val value: Short) : IlNumericConstant<Short>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlInt16Const(this)
}

data class IlUInt16Constant(override val value: UShort) : IlNumericConstant<UShort>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlUInt16Const(this)
}

data class IlInt32Constant(override val value: Int) : IlNumericConstant<Int>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlInt32Const(this)
}

data class IlUInt32Constant(override val value: UInt) : IlNumericConstant<UInt>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlUInt32Const(this)
}

data class IlInt64Constant(override val value: Long) : IlNumericConstant<Long>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlInt64Const(this)
}

data class IlUInt64Constant(override val value: ULong) : IlNumericConstant<ULong>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlUInt64Const(this)
}

data class IlFloatConstant(override val value: Float) : IlNumericConstant<Float>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlFloatConst(this)
}

data class IlDoubleConstant(override val value: Double) : IlNumericConstant<Double>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlDoubleConst(this)
}

data class IlArrayConstant(val values: List<IlConstant>) : IlConstant {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlArrayConst(this)
}

data class IlEnumConstant(val enumType: IlType, val underlyingConst: IlConstant) : IlConstant {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlEnumConst(this)
}

data class IlTypeRef(val referencedType: IlType) : IlConstant {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlTypeRefConst(this)
}

data class IlMethodRef(val referencedMethod: IlMethod) : IlConstant {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlMethodRefConst(this)
}

data class IlFieldRef(val referencedField: IlField) : IlConstant {
    override fun <T> accept(visitor: IlExprVisitor<T>): T = visitor.visitIlFieldRefConst(this)
}
