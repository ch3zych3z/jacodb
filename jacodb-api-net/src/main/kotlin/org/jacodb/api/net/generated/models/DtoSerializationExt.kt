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

package org.jacodb.api.net.generated.models

import com.jetbrains.rd.framework.SerializationCtx
import com.jetbrains.rd.framework.Serializers
import com.jetbrains.rd.framework.createAbstractBuffer
import org.jacodb.api.net.ilinstances.IlInt32Const

enum class IlTypeByteId(val id: Byte) {
    POINTER(0),
    PRIMITIVE(1),
    STRUCT(2),
    ENUM(3),
    ARRAY(4),
    CLASS(5);
}

private fun dtoSerializationCtx(): SerializationCtx {
    val serializers = Serializers()
    serializers.registerSerializersOwnerOnce(IlMethodBodyModel)
    serializers.registerSerializersOwnerOnce(IlModel)
    return SerializationCtx(serializers, emptyMap())
}

private val ctx = dtoSerializationCtx()

fun ByteArray.getIlTypeDto(): IlTypeDto {
    val typeId = get(0)
    val buf = createAbstractBuffer(this.drop(1).toByteArray())
    return when (typeId) {
        IlTypeByteId.POINTER.id -> IlPointerTypeDto.read(ctx, buf)
        IlTypeByteId.PRIMITIVE.id -> IlPrimitiveTypeDto.read(ctx, buf)
        IlTypeByteId.STRUCT.id -> IlStructTypeDto.read(ctx, buf)
        IlTypeByteId.ENUM.id -> IlEnumTypeDto.read(ctx, buf)
        IlTypeByteId.ARRAY.id -> IlArrayTypeDto.read(ctx, buf)
        IlTypeByteId.CLASS.id -> IlClassTypeDto.read(ctx, buf)
        else -> throw DtoDeserializationException("Unexpected bytearray")
    }
}

fun IlTypeDto.getBytes() : ByteArray =
    when (this) {
        is IlPointerTypeDto -> this.getBytes()
        is IlPrimitiveTypeDto -> this.getBytes()
        is IlStructTypeDto -> this.getBytes()
        is IlEnumTypeDto -> this.getBytes()
        is IlArrayTypeDto -> this.getBytes()
        is IlClassTypeDto -> this.getBytes()
        else -> throw DtoSerializationException("Unexpected IlType ${this.name}")
    }

fun IlPointerTypeDto.getBytes(): ByteArray {
    val buf = createAbstractBuffer()
    buf.writeByte(IlTypeByteId.POINTER.id)
    IlPointerTypeDto.write(ctx, buf, this)
    return buf.getArray()
}

fun IlPrimitiveTypeDto.getBytes(): ByteArray {
    val buf = createAbstractBuffer()
    buf.writeByte(IlTypeByteId.PRIMITIVE.id)
    IlPrimitiveTypeDto.write(ctx, buf, this)
    return buf.getArray()
}

fun IlStructTypeDto.getBytes(): ByteArray {
    val buf = createAbstractBuffer()
    buf.writeByte(IlTypeByteId.STRUCT.id)
    IlStructTypeDto.write(ctx, buf, this)
    return buf.getArray()
}

fun IlEnumTypeDto.getBytes(): ByteArray {
    val buf = createAbstractBuffer()
    buf.writeByte(IlTypeByteId.ENUM.id)
    IlEnumTypeDto.write(ctx, buf, this)
    return buf.getArray()
}

fun IlArrayTypeDto.getBytes(): ByteArray {
    val buf = createAbstractBuffer()
    buf.writeByte(IlTypeByteId.ARRAY.id)
    IlArrayTypeDto.write(ctx, buf, this)
    return buf.getArray()
}

fun IlClassTypeDto.getBytes(): ByteArray {
    val buf = createAbstractBuffer()
    buf.writeByte(IlTypeByteId.CLASS.id)
    IlClassTypeDto.write(ctx, buf, this)
    return buf.getArray()
}

open class DtoConversionException(msg: String) : RuntimeException(msg)
class DtoSerializationException(msg: String) : DtoConversionException(msg)
class DtoDeserializationException(msg: String) : DtoConversionException(msg)
