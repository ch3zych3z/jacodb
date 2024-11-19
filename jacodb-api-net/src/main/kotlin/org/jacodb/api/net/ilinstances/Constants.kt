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

import org.example.ilinstances.IlField
import org.example.ilinstances.IlMethod
import org.example.ilinstances.IlType

class IlNull : IlConst
data class IlBoolConst(val value: Boolean) : IlConst {
    override fun toString() = "$value"
}

data class IlStringConst(val value: String) : IlConst {
    override fun toString() = "`$value`"
}

data class IlCharConst(override val value: Char) : IlNumericConst<Char>(value)
data class IlInt8Const(override val value: Byte) : IlNumericConst<Byte>(value)
data class IlUint8Const(override val value: UByte) : IlNumericConst<UByte>(value)
data class IlInt16Const(override val value: Short) : IlNumericConst<Short>(value)
data class IlUint16Const(override val value: UShort) : IlNumericConst<UShort>(value)
data class IlInt32Const(override val value: Int) : IlNumericConst<Int>(value)
data class IlUint32Const(override val value: UInt) : IlNumericConst<UInt>(value)
data class IlInt64Const(override val value: Long) : IlNumericConst<Long>(value)
data class IlUint64Const(override val value: ULong) : IlNumericConst<ULong>(value)
data class IlFloatConst(override val value: Float) : IlNumericConst<Float>(value)
data class IlDoubleConst(override val value: Double) : IlNumericConst<Double>(value)

data class IlArrayConst(val values: List<IlConst>) : IlConst
data class IlEnumConst(val enumType: IlType, val underlyingConst: IlConst) : IlConst
data class IlTypeRef(val referencedType: IlType) : IlConst
data class IlMethodRef(val referencedMethod: IlMethod) : IlConst
data class IlFieldRef(val referencedField: IlField) : IlConst

