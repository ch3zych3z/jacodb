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

import org.example.ilinstances.IlMethod
import org.example.ilinstances.IlType
import org.jacodb.api.net.core.IlExprVisitor

class IlNull : IlConst {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlBoolConst(val value: Boolean) : IlConst {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }

    override fun toString() = "$value"
}

data class IlStringConst(val value: String) : IlConst {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }

    override fun toString() = "`$value`"
}

data class IlCharConst(override val value: Char) : IlNumericConst<Char>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlInt8Const(override val value: Byte) : IlNumericConst<Byte>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlUint8Const(override val value: UByte) : IlNumericConst<UByte>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlInt16Const(override val value: Short) : IlNumericConst<Short>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlUint16Const(override val value: UShort) : IlNumericConst<UShort>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlInt32Const(override val value: Int) : IlNumericConst<Int>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlUint32Const(override val value: UInt) : IlNumericConst<UInt>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlInt64Const(override val value: Long) : IlNumericConst<Long>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlUint64Const(override val value: ULong) : IlNumericConst<ULong>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlFloatConst(override val value: Float) : IlNumericConst<Float>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlDoubleConst(override val value: Double) : IlNumericConst<Double>(value) {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlArrayConst(val values: List<IlConst>) : IlConst {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlEnumConst(val enumType: IlType, val underlyingConst: IlConst) : IlConst {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlTypeRef(val referencedType: IlType) : IlConst {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlMethodRef(val referencedMethod: IlMethod) : IlConst {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}

data class IlFieldRef(val referencedField: IlField) : IlConst {
    override fun <T> accept(visitor: IlExprVisitor<T>): T {
        TODO("Not yet implemented")
    }
}
