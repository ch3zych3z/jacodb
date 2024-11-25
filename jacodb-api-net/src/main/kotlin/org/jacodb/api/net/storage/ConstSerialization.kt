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

package org.jacodb.api.net.storage

import org.jacodb.api.net.generated.models.*


enum class ConstKind { NULL, BOOL, STRING, CHAR,
    INT8, INT16, INT32, INT64, UINT8, UINT16, UINT32, UINT64, FLOAT, DOUBLE,
    TYPE_REF, METHOD_REF, ENUM }

fun IlConstDto.serialize() : List<Pair<String, ConstKind>> {
    val flattenValues = mutableListOf<Pair<String, ConstKind>>()
    when (this) {
        is IlArrayConstDto -> this.values
    }
    TODO()
}

fun IlConstDto.serializePrimitive() : Pair<String, ConstKind> =
    when (this) {
        is IlNullDto -> "null" to ConstKind.NULL
        is IlBoolConstDto -> value.toString() to ConstKind.BOOL
        is IlStringConstDto -> value to ConstKind.STRING
        is IlCharConstDto -> value.toString() to ConstKind.CHAR
        is IlInt8ConstDto -> value.toString() to ConstKind.INT8
        is IlInt16ConstDto -> value.toString() to ConstKind.INT16
        is IlInt32ConstDto -> value.toString() to ConstKind.INT32
        is IlInt64ConstDto -> value.toString() to ConstKind.INT64
        is IlUint8ConstDto -> value.toString() to ConstKind.UINT8
        is IlUint16ConstDto -> value.toString() to ConstKind.UINT16
        is IlUint32ConstDto -> value.toString() to ConstKind.UINT32
        is IlUint64ConstDto -> value.toString() to ConstKind.UINT64
        is IlFloatConstDto -> value.toString() to ConstKind.FLOAT
        is IlDoubleConstDto -> value.toString() to ConstKind.DOUBLE
        is IlTypeRefDto -> referencedType.typeName to ConstKind.TYPE_REF // ??
        is IlMethodRefDto -> type.typeName to ConstKind.METHOD_REF // ??
        is IlEnumConstDto -> underlyingValue.serializePrimitive().first to ConstKind.ENUM
        else -> throw RuntimeException("Unexpected const $this")
    }
