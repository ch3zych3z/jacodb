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

interface IlValue : IlExpr
interface IlLocal : IlValue
interface IlConst : IlValue
abstract class IlNumericConst : IlConst {
    abstract val value: Number
    override fun toString(): String = "$value"
}

class IlNull : IlConst
data class IlBoolConst(val value: Boolean) : IlConst {
    override fun toString() = "$value"
}

data class IlStringConst(val value: String) : IlConst {
    override fun toString() = "`$value`"
}

data class IlByteConst(override val value: Byte) : IlNumericConst()
data class IlIntConst(override val value: Int) : IlNumericConst()
data class IlLongConst(override val value: Long) : IlNumericConst()
data class IlFloatConst(override val value: Float) : IlNumericConst()
data class IlDoubleConst(override val value: Double) : IlNumericConst()

data class IlArrayConst(val values: List<IlConst>) : IlConst

data class IlTypeRef(val referencedType: IlType) : IlConst
data class IlMethodRef(val referencedMethod: IlMethod) : IlConst
data class IlFieldRef(val referencedField: IlField) : IlConst

