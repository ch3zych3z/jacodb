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

@file:Suppress("EXPERIMENTAL_API_USAGE","EXPERIMENTAL_UNSIGNED_LITERALS","PackageDirectoryMismatch","UnusedImport","unused","LocalVariableName","CanBeVal","PropertyName","EnumEntryName","ClassName","ObjectPropertyName","UnnecessaryVariable","SpellCheckingInspection")
package org.jacodb.api.net.generated.models

import com.jetbrains.rd.framework.*
import com.jetbrains.rd.framework.base.*
import com.jetbrains.rd.framework.impl.*

import com.jetbrains.rd.util.lifetime.*
import com.jetbrains.rd.util.reactive.*
import com.jetbrains.rd.util.string.*
import com.jetbrains.rd.util.*
import kotlin.time.Duration
import kotlin.reflect.KClass
import kotlin.jvm.JvmStatic



/**
 * #### Generated from [IlModel.kt:23]
 */
class IlModel private constructor(
) : RdExtBase() {
    //companion
    
    companion object : ISerializersOwner {
        
        override fun registerSerializersCore(serializers: ISerializers)  {
            serializers.register(CacheKey)
            serializers.register(AsmCacheKey)
            serializers.register(IlAsmDto)
            serializers.register(IlTypeDto)
            serializers.register(IlFieldDto)
            serializers.register(IlParameterDto)
            serializers.register(IlAttrDto)
            serializers.register(IlLocalVarDto)
            serializers.register(IlTempVarDto)
            serializers.register(IlErrVarDto)
            serializers.register(IlCatchScopeDto)
            serializers.register(IlFilterScopeDto)
            serializers.register(IlFaultScopeDto)
            serializers.register(IlFinallyScopeDto)
            serializers.register(IlMethodDto)
            serializers.register(IlDto_Unknown)
            serializers.register(IlVarDto_Unknown)
            serializers.register(IlEhScopeDto_Unknown)
        }
        
        
        @JvmStatic
        @JvmName("internalCreateModel")
        @Deprecated("Use create instead", ReplaceWith("create(lifetime, protocol)"))
        internal fun createModel(lifetime: Lifetime, protocol: IProtocol): IlModel  {
            @Suppress("DEPRECATION")
            return create(lifetime, protocol)
        }
        
        @JvmStatic
        @Deprecated("Use protocol.ilModel or revise the extension scope instead", ReplaceWith("protocol.ilModel"))
        fun create(lifetime: Lifetime, protocol: IProtocol): IlModel  {
            IlRoot.register(protocol.serializers)
            
            return IlModel()
        }
        
        
        const val serializationHash = -8431306000670948402L
        
    }
    override val serializersOwner: ISerializersOwner get() = IlModel
    override val serializationHash: Long get() = IlModel.serializationHash
    
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlModel (")
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): IlModel   {
        return IlModel(
        )
    }
    //contexts
}
val IProtocol.ilModel get() = getOrCreateExtension(IlModel::class) { @Suppress("DEPRECATION") IlModel.create(lifetime, this) }



/**
 * #### Generated from [IlModel.kt:30]
 */
data class AsmCacheKey (
    val asm: Int
) : IPrintable {
    //companion
    
    companion object : IMarshaller<AsmCacheKey> {
        override val _type: KClass<AsmCacheKey> = AsmCacheKey::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): AsmCacheKey  {
            val asm = buffer.readInt()
            return AsmCacheKey(asm)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: AsmCacheKey)  {
            buffer.writeInt(value.asm)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as AsmCacheKey
        
        if (asm != other.asm) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + asm.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("AsmCacheKey (")
        printer.indent {
            print("asm = "); asm.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:24]
 */
data class CacheKey (
    val asm: Int,
    val mod: Int,
    val inst: Int
) : IPrintable {
    //companion
    
    companion object : IMarshaller<CacheKey> {
        override val _type: KClass<CacheKey> = CacheKey::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): CacheKey  {
            val asm = buffer.readInt()
            val mod = buffer.readInt()
            val inst = buffer.readInt()
            return CacheKey(asm, mod, inst)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: CacheKey)  {
            buffer.writeInt(value.asm)
            buffer.writeInt(value.mod)
            buffer.writeInt(value.inst)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as CacheKey
        
        if (asm != other.asm) return false
        if (mod != other.mod) return false
        if (inst != other.inst) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + asm.hashCode()
        __r = __r*31 + mod.hashCode()
        __r = __r*31 + inst.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("CacheKey (")
        printer.indent {
            print("asm = "); asm.print(printer); println()
            print("mod = "); mod.print(printer); println()
            print("inst = "); inst.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:36]
 */
class IlAsmDto (
    val id: AsmCacheKey,
    val location: String
) : IlDto (
) {
    //companion
    
    companion object : IMarshaller<IlAsmDto> {
        override val _type: KClass<IlAsmDto> = IlAsmDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlAsmDto  {
            val id = AsmCacheKey.read(ctx, buffer)
            val location = buffer.readString()
            return IlAsmDto(id, location)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlAsmDto)  {
            AsmCacheKey.write(ctx, buffer, value.id)
            buffer.writeString(value.location)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlAsmDto
        
        if (id != other.id) return false
        if (location != other.location) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + id.hashCode()
        __r = __r*31 + location.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlAsmDto (")
        printer.indent {
            print("id = "); id.print(printer); println()
            print("location = "); location.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:73]
 */
class IlAttrDto (
    val attrType: CacheKey,
    val ctorArgs: List<IlConstDto>,
    val namedArgsNames: List<String>,
    val namedArgsValues: List<IlConstDto>
) : IlDto (
) {
    //companion
    
    companion object : IMarshaller<IlAttrDto> {
        override val _type: KClass<IlAttrDto> = IlAttrDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlAttrDto  {
            val attrType = CacheKey.read(ctx, buffer)
            val ctorArgs = buffer.readList { ctx.serializers.readPolymorphic<IlConstDto>(ctx, buffer, IlConstDto) }
            val namedArgsNames = buffer.readList { buffer.readString() }
            val namedArgsValues = buffer.readList { ctx.serializers.readPolymorphic<IlConstDto>(ctx, buffer, IlConstDto) }
            return IlAttrDto(attrType, ctorArgs, namedArgsNames, namedArgsValues)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlAttrDto)  {
            CacheKey.write(ctx, buffer, value.attrType)
            buffer.writeList(value.ctorArgs) { v -> ctx.serializers.writePolymorphic(ctx, buffer, v) }
            buffer.writeList(value.namedArgsNames) { v -> buffer.writeString(v) }
            buffer.writeList(value.namedArgsValues) { v -> ctx.serializers.writePolymorphic(ctx, buffer, v) }
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlAttrDto
        
        if (attrType != other.attrType) return false
        if (ctorArgs != other.ctorArgs) return false
        if (namedArgsNames != other.namedArgsNames) return false
        if (namedArgsValues != other.namedArgsValues) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + attrType.hashCode()
        __r = __r*31 + ctorArgs.hashCode()
        __r = __r*31 + namedArgsNames.hashCode()
        __r = __r*31 + namedArgsValues.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlAttrDto (")
        printer.indent {
            print("attrType = "); attrType.print(printer); println()
            print("ctorArgs = "); ctorArgs.print(printer); println()
            print("namedArgsNames = "); namedArgsNames.print(printer); println()
            print("namedArgsValues = "); namedArgsValues.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:93]
 */
class IlCatchScopeDto (
    tb: Int,
    te: Int,
    hb: Int,
    he: Int
) : IlEhScopeDto (
    tb,
    te,
    hb,
    he
) {
    //companion
    
    companion object : IMarshaller<IlCatchScopeDto> {
        override val _type: KClass<IlCatchScopeDto> = IlCatchScopeDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlCatchScopeDto  {
            val tb = buffer.readInt()
            val te = buffer.readInt()
            val hb = buffer.readInt()
            val he = buffer.readInt()
            return IlCatchScopeDto(tb, te, hb, he)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlCatchScopeDto)  {
            buffer.writeInt(value.tb)
            buffer.writeInt(value.te)
            buffer.writeInt(value.hb)
            buffer.writeInt(value.he)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlCatchScopeDto
        
        if (tb != other.tb) return false
        if (te != other.te) return false
        if (hb != other.hb) return false
        if (he != other.he) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + tb.hashCode()
        __r = __r*31 + te.hashCode()
        __r = __r*31 + hb.hashCode()
        __r = __r*31 + he.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlCatchScopeDto (")
        printer.indent {
            print("tb = "); tb.print(printer); println()
            print("te = "); te.print(printer); println()
            print("hb = "); hb.print(printer); println()
            print("he = "); he.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:34]
 */
abstract class IlDto (
) : IPrintable {
    //companion
    
    companion object : IAbstractDeclaration<IlDto> {
        override fun readUnknownInstance(ctx: SerializationCtx, buffer: AbstractBuffer, unknownId: RdId, size: Int): IlDto  {
            val objectStartPosition = buffer.position
            val unknownBytes = ByteArray(objectStartPosition + size - buffer.position)
            buffer.readByteArrayRaw(unknownBytes)
            return IlDto_Unknown(unknownId, unknownBytes)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    //hash code trait
    //pretty print
    //deepClone
    //contexts
}


class IlDto_Unknown (
    override val unknownId: RdId,
    val unknownBytes: ByteArray
) : IlDto (
), IUnknownInstance {
    //companion
    
    companion object : IMarshaller<IlDto_Unknown> {
        override val _type: KClass<IlDto_Unknown> = IlDto_Unknown::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlDto_Unknown  {
            throw NotImplementedError("Unknown instances should not be read via serializer")
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlDto_Unknown)  {
            buffer.writeByteArrayRaw(value.unknownBytes)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlDto_Unknown
        
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlDto_Unknown (")
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:87]
 */
abstract class IlEhScopeDto (
    val tb: Int,
    val te: Int,
    val hb: Int,
    val he: Int
) : IlDto (
) {
    //companion
    
    companion object : IAbstractDeclaration<IlEhScopeDto> {
        override fun readUnknownInstance(ctx: SerializationCtx, buffer: AbstractBuffer, unknownId: RdId, size: Int): IlEhScopeDto  {
            val objectStartPosition = buffer.position
            val tb = buffer.readInt()
            val te = buffer.readInt()
            val hb = buffer.readInt()
            val he = buffer.readInt()
            val unknownBytes = ByteArray(objectStartPosition + size - buffer.position)
            buffer.readByteArrayRaw(unknownBytes)
            return IlEhScopeDto_Unknown(tb, te, hb, he, unknownId, unknownBytes)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    //hash code trait
    //pretty print
    //deepClone
    //contexts
}


class IlEhScopeDto_Unknown (
    tb: Int,
    te: Int,
    hb: Int,
    he: Int,
    override val unknownId: RdId,
    val unknownBytes: ByteArray
) : IlEhScopeDto (
    tb,
    te,
    hb,
    he
), IUnknownInstance {
    //companion
    
    companion object : IMarshaller<IlEhScopeDto_Unknown> {
        override val _type: KClass<IlEhScopeDto_Unknown> = IlEhScopeDto_Unknown::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlEhScopeDto_Unknown  {
            throw NotImplementedError("Unknown instances should not be read via serializer")
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlEhScopeDto_Unknown)  {
            buffer.writeInt(value.tb)
            buffer.writeInt(value.te)
            buffer.writeInt(value.hb)
            buffer.writeInt(value.he)
            buffer.writeByteArrayRaw(value.unknownBytes)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlEhScopeDto_Unknown
        
        if (tb != other.tb) return false
        if (te != other.te) return false
        if (hb != other.hb) return false
        if (he != other.he) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + tb.hashCode()
        __r = __r*31 + te.hashCode()
        __r = __r*31 + hb.hashCode()
        __r = __r*31 + he.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlEhScopeDto_Unknown (")
        printer.indent {
            print("tb = "); tb.print(printer); println()
            print("te = "); te.print(printer); println()
            print("hb = "); hb.print(printer); println()
            print("he = "); he.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:85]
 */
class IlErrVarDto (
    type: CacheKey,
    index: Int
) : IlVarDto (
    type,
    index
) {
    //companion
    
    companion object : IMarshaller<IlErrVarDto> {
        override val _type: KClass<IlErrVarDto> = IlErrVarDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlErrVarDto  {
            val type = CacheKey.read(ctx, buffer)
            val index = buffer.readInt()
            return IlErrVarDto(type, index)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlErrVarDto)  {
            CacheKey.write(ctx, buffer, value.type)
            buffer.writeInt(value.index)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlErrVarDto
        
        if (type != other.type) return false
        if (index != other.index) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + type.hashCode()
        __r = __r*31 + index.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlErrVarDto (")
        printer.indent {
            print("type = "); type.print(printer); println()
            print("index = "); index.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:98]
 */
class IlFaultScopeDto (
    tb: Int,
    te: Int,
    hb: Int,
    he: Int
) : IlEhScopeDto (
    tb,
    te,
    hb,
    he
) {
    //companion
    
    companion object : IMarshaller<IlFaultScopeDto> {
        override val _type: KClass<IlFaultScopeDto> = IlFaultScopeDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlFaultScopeDto  {
            val tb = buffer.readInt()
            val te = buffer.readInt()
            val hb = buffer.readInt()
            val he = buffer.readInt()
            return IlFaultScopeDto(tb, te, hb, he)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlFaultScopeDto)  {
            buffer.writeInt(value.tb)
            buffer.writeInt(value.te)
            buffer.writeInt(value.hb)
            buffer.writeInt(value.he)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlFaultScopeDto
        
        if (tb != other.tb) return false
        if (te != other.te) return false
        if (hb != other.hb) return false
        if (he != other.he) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + tb.hashCode()
        __r = __r*31 + te.hashCode()
        __r = __r*31 + hb.hashCode()
        __r = __r*31 + he.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlFaultScopeDto (")
        printer.indent {
            print("tb = "); tb.print(printer); println()
            print("te = "); te.print(printer); println()
            print("hb = "); hb.print(printer); println()
            print("he = "); he.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:51]
 */
class IlFieldDto (
    val id: CacheKey,
    val declType: CacheKey,
    val fieldType: CacheKey,
    val isStatic: Boolean,
    val name: String,
    val attrs: List<IlAttrDto>
) : IlDto (
) {
    //companion
    
    companion object : IMarshaller<IlFieldDto> {
        override val _type: KClass<IlFieldDto> = IlFieldDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlFieldDto  {
            val id = CacheKey.read(ctx, buffer)
            val declType = CacheKey.read(ctx, buffer)
            val fieldType = CacheKey.read(ctx, buffer)
            val isStatic = buffer.readBool()
            val name = buffer.readString()
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            return IlFieldDto(id, declType, fieldType, isStatic, name, attrs)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlFieldDto)  {
            CacheKey.write(ctx, buffer, value.id)
            CacheKey.write(ctx, buffer, value.declType)
            CacheKey.write(ctx, buffer, value.fieldType)
            buffer.writeBool(value.isStatic)
            buffer.writeString(value.name)
            buffer.writeList(value.attrs) { v -> IlAttrDto.write(ctx, buffer, v) }
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlFieldDto
        
        if (id != other.id) return false
        if (declType != other.declType) return false
        if (fieldType != other.fieldType) return false
        if (isStatic != other.isStatic) return false
        if (name != other.name) return false
        if (attrs != other.attrs) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + id.hashCode()
        __r = __r*31 + declType.hashCode()
        __r = __r*31 + fieldType.hashCode()
        __r = __r*31 + isStatic.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + attrs.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlFieldDto (")
        printer.indent {
            print("id = "); id.print(printer); println()
            print("declType = "); declType.print(printer); println()
            print("fieldType = "); fieldType.print(printer); println()
            print("isStatic = "); isStatic.print(printer); println()
            print("name = "); name.print(printer); println()
            print("attrs = "); attrs.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:95]
 */
class IlFilterScopeDto (
    val fb: Int,
    tb: Int,
    te: Int,
    hb: Int,
    he: Int
) : IlEhScopeDto (
    tb,
    te,
    hb,
    he
) {
    //companion
    
    companion object : IMarshaller<IlFilterScopeDto> {
        override val _type: KClass<IlFilterScopeDto> = IlFilterScopeDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlFilterScopeDto  {
            val tb = buffer.readInt()
            val te = buffer.readInt()
            val hb = buffer.readInt()
            val he = buffer.readInt()
            val fb = buffer.readInt()
            return IlFilterScopeDto(fb, tb, te, hb, he)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlFilterScopeDto)  {
            buffer.writeInt(value.tb)
            buffer.writeInt(value.te)
            buffer.writeInt(value.hb)
            buffer.writeInt(value.he)
            buffer.writeInt(value.fb)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlFilterScopeDto
        
        if (fb != other.fb) return false
        if (tb != other.tb) return false
        if (te != other.te) return false
        if (hb != other.hb) return false
        if (he != other.he) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + fb.hashCode()
        __r = __r*31 + tb.hashCode()
        __r = __r*31 + te.hashCode()
        __r = __r*31 + hb.hashCode()
        __r = __r*31 + he.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlFilterScopeDto (")
        printer.indent {
            print("fb = "); fb.print(printer); println()
            print("tb = "); tb.print(printer); println()
            print("te = "); te.print(printer); println()
            print("hb = "); hb.print(printer); println()
            print("he = "); he.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:99]
 */
class IlFinallyScopeDto (
    tb: Int,
    te: Int,
    hb: Int,
    he: Int
) : IlEhScopeDto (
    tb,
    te,
    hb,
    he
) {
    //companion
    
    companion object : IMarshaller<IlFinallyScopeDto> {
        override val _type: KClass<IlFinallyScopeDto> = IlFinallyScopeDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlFinallyScopeDto  {
            val tb = buffer.readInt()
            val te = buffer.readInt()
            val hb = buffer.readInt()
            val he = buffer.readInt()
            return IlFinallyScopeDto(tb, te, hb, he)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlFinallyScopeDto)  {
            buffer.writeInt(value.tb)
            buffer.writeInt(value.te)
            buffer.writeInt(value.hb)
            buffer.writeInt(value.he)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlFinallyScopeDto
        
        if (tb != other.tb) return false
        if (te != other.te) return false
        if (hb != other.hb) return false
        if (he != other.he) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + tb.hashCode()
        __r = __r*31 + te.hashCode()
        __r = __r*31 + hb.hashCode()
        __r = __r*31 + he.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlFinallyScopeDto (")
        printer.indent {
            print("tb = "); tb.print(printer); println()
            print("te = "); te.print(printer); println()
            print("hb = "); hb.print(printer); println()
            print("he = "); he.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:80]
 */
class IlLocalVarDto (
    val isPinned: Boolean,
    type: CacheKey,
    index: Int
) : IlVarDto (
    type,
    index
) {
    //companion
    
    companion object : IMarshaller<IlLocalVarDto> {
        override val _type: KClass<IlLocalVarDto> = IlLocalVarDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlLocalVarDto  {
            val type = CacheKey.read(ctx, buffer)
            val index = buffer.readInt()
            val isPinned = buffer.readBool()
            return IlLocalVarDto(isPinned, type, index)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlLocalVarDto)  {
            CacheKey.write(ctx, buffer, value.type)
            buffer.writeInt(value.index)
            buffer.writeBool(value.isPinned)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlLocalVarDto
        
        if (isPinned != other.isPinned) return false
        if (type != other.type) return false
        if (index != other.index) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + isPinned.hashCode()
        __r = __r*31 + type.hashCode()
        __r = __r*31 + index.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlLocalVarDto (")
        printer.indent {
            print("isPinned = "); isPinned.print(printer); println()
            print("type = "); type.print(printer); println()
            print("index = "); index.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:102]
 */
class IlMethodDto (
    val id: CacheKey,
    val declType: CacheKey?,
    val returnType: CacheKey?,
    val attrs: List<IlAttrDto>,
    val name: String,
    val parameters: List<IlParameterDto>,
    val resolved: Boolean,
    val locals: List<IlLocalVarDto>,
    val temps: List<IlTempVarDto>,
    val errs: List<IlErrVarDto>,
    val ehScopes: List<IlEhScopeDto>,
    val body: List<IlStmtDto>
) : IlDto (
) {
    //companion
    
    companion object : IMarshaller<IlMethodDto> {
        override val _type: KClass<IlMethodDto> = IlMethodDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlMethodDto  {
            val id = CacheKey.read(ctx, buffer)
            val declType = buffer.readNullable { CacheKey.read(ctx, buffer) }
            val returnType = buffer.readNullable { CacheKey.read(ctx, buffer) }
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            val name = buffer.readString()
            val parameters = buffer.readList { IlParameterDto.read(ctx, buffer) }
            val resolved = buffer.readBool()
            val locals = buffer.readList { IlLocalVarDto.read(ctx, buffer) }
            val temps = buffer.readList { IlTempVarDto.read(ctx, buffer) }
            val errs = buffer.readList { IlErrVarDto.read(ctx, buffer) }
            val ehScopes = buffer.readList { ctx.serializers.readPolymorphic<IlEhScopeDto>(ctx, buffer, IlEhScopeDto) }
            val body = buffer.readList { ctx.serializers.readPolymorphic<IlStmtDto>(ctx, buffer, IlStmtDto) }
            return IlMethodDto(id, declType, returnType, attrs, name, parameters, resolved, locals, temps, errs, ehScopes, body)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlMethodDto)  {
            CacheKey.write(ctx, buffer, value.id)
            buffer.writeNullable(value.declType) { CacheKey.write(ctx, buffer, it) }
            buffer.writeNullable(value.returnType) { CacheKey.write(ctx, buffer, it) }
            buffer.writeList(value.attrs) { v -> IlAttrDto.write(ctx, buffer, v) }
            buffer.writeString(value.name)
            buffer.writeList(value.parameters) { v -> IlParameterDto.write(ctx, buffer, v) }
            buffer.writeBool(value.resolved)
            buffer.writeList(value.locals) { v -> IlLocalVarDto.write(ctx, buffer, v) }
            buffer.writeList(value.temps) { v -> IlTempVarDto.write(ctx, buffer, v) }
            buffer.writeList(value.errs) { v -> IlErrVarDto.write(ctx, buffer, v) }
            buffer.writeList(value.ehScopes) { v -> ctx.serializers.writePolymorphic(ctx, buffer, v) }
            buffer.writeList(value.body) { v -> ctx.serializers.writePolymorphic(ctx, buffer, v) }
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlMethodDto
        
        if (id != other.id) return false
        if (declType != other.declType) return false
        if (returnType != other.returnType) return false
        if (attrs != other.attrs) return false
        if (name != other.name) return false
        if (parameters != other.parameters) return false
        if (resolved != other.resolved) return false
        if (locals != other.locals) return false
        if (temps != other.temps) return false
        if (errs != other.errs) return false
        if (ehScopes != other.ehScopes) return false
        if (body != other.body) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + id.hashCode()
        __r = __r*31 + if (declType != null) declType.hashCode() else 0
        __r = __r*31 + if (returnType != null) returnType.hashCode() else 0
        __r = __r*31 + attrs.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + parameters.hashCode()
        __r = __r*31 + resolved.hashCode()
        __r = __r*31 + locals.hashCode()
        __r = __r*31 + temps.hashCode()
        __r = __r*31 + errs.hashCode()
        __r = __r*31 + ehScopes.hashCode()
        __r = __r*31 + body.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlMethodDto (")
        printer.indent {
            print("id = "); id.print(printer); println()
            print("declType = "); declType.print(printer); println()
            print("returnType = "); returnType.print(printer); println()
            print("attrs = "); attrs.print(printer); println()
            print("name = "); name.print(printer); println()
            print("parameters = "); parameters.print(printer); println()
            print("resolved = "); resolved.print(printer); println()
            print("locals = "); locals.print(printer); println()
            print("temps = "); temps.print(printer); println()
            print("errs = "); errs.print(printer); println()
            print("ehScopes = "); ehScopes.print(printer); println()
            print("body = "); body.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:60]
 */
data class IlParameterDto (
    val index: Int,
    val type: CacheKey,
    val name: String,
    val defaultValue: String?,
    val attrs: List<IlAttrDto>
) : IPrintable {
    //companion
    
    companion object : IMarshaller<IlParameterDto> {
        override val _type: KClass<IlParameterDto> = IlParameterDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlParameterDto  {
            val index = buffer.readInt()
            val type = CacheKey.read(ctx, buffer)
            val name = buffer.readString()
            val defaultValue = buffer.readNullable { buffer.readString() }
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            return IlParameterDto(index, type, name, defaultValue, attrs)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlParameterDto)  {
            buffer.writeInt(value.index)
            CacheKey.write(ctx, buffer, value.type)
            buffer.writeString(value.name)
            buffer.writeNullable(value.defaultValue) { buffer.writeString(it) }
            buffer.writeList(value.attrs) { v -> IlAttrDto.write(ctx, buffer, v) }
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlParameterDto
        
        if (index != other.index) return false
        if (type != other.type) return false
        if (name != other.name) return false
        if (defaultValue != other.defaultValue) return false
        if (attrs != other.attrs) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + index.hashCode()
        __r = __r*31 + type.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + if (defaultValue != null) defaultValue.hashCode() else 0
        __r = __r*31 + attrs.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlParameterDto (")
        printer.indent {
            print("index = "); index.print(printer); println()
            print("type = "); type.print(printer); println()
            print("name = "); name.print(printer); println()
            print("defaultValue = "); defaultValue.print(printer); println()
            print("attrs = "); attrs.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:84]
 */
class IlTempVarDto (
    type: CacheKey,
    index: Int
) : IlVarDto (
    type,
    index
) {
    //companion
    
    companion object : IMarshaller<IlTempVarDto> {
        override val _type: KClass<IlTempVarDto> = IlTempVarDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlTempVarDto  {
            val type = CacheKey.read(ctx, buffer)
            val index = buffer.readInt()
            return IlTempVarDto(type, index)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlTempVarDto)  {
            CacheKey.write(ctx, buffer, value.type)
            buffer.writeInt(value.index)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlTempVarDto
        
        if (type != other.type) return false
        if (index != other.index) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + type.hashCode()
        __r = __r*31 + index.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlTempVarDto (")
        printer.indent {
            print("type = "); type.print(printer); println()
            print("index = "); index.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:41]
 */
class IlTypeDto (
    val id: CacheKey,
    val name: String,
    val genericArgs: List<CacheKey>,
    val isGenericParam: Boolean,
    val isValueType: Boolean,
    val isManaged: Boolean,
    val attrs: List<IlAttrDto>
) : IlDto (
) {
    //companion
    
    companion object : IMarshaller<IlTypeDto> {
        override val _type: KClass<IlTypeDto> = IlTypeDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlTypeDto  {
            val id = CacheKey.read(ctx, buffer)
            val name = buffer.readString()
            val genericArgs = buffer.readList { CacheKey.read(ctx, buffer) }
            val isGenericParam = buffer.readBool()
            val isValueType = buffer.readBool()
            val isManaged = buffer.readBool()
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            return IlTypeDto(id, name, genericArgs, isGenericParam, isValueType, isManaged, attrs)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlTypeDto)  {
            CacheKey.write(ctx, buffer, value.id)
            buffer.writeString(value.name)
            buffer.writeList(value.genericArgs) { v -> CacheKey.write(ctx, buffer, v) }
            buffer.writeBool(value.isGenericParam)
            buffer.writeBool(value.isValueType)
            buffer.writeBool(value.isManaged)
            buffer.writeList(value.attrs) { v -> IlAttrDto.write(ctx, buffer, v) }
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlTypeDto
        
        if (id != other.id) return false
        if (name != other.name) return false
        if (genericArgs != other.genericArgs) return false
        if (isGenericParam != other.isGenericParam) return false
        if (isValueType != other.isValueType) return false
        if (isManaged != other.isManaged) return false
        if (attrs != other.attrs) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + id.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + genericArgs.hashCode()
        __r = __r*31 + isGenericParam.hashCode()
        __r = __r*31 + isValueType.hashCode()
        __r = __r*31 + isManaged.hashCode()
        __r = __r*31 + attrs.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlTypeDto (")
        printer.indent {
            print("id = "); id.print(printer); println()
            print("name = "); name.print(printer); println()
            print("genericArgs = "); genericArgs.print(printer); println()
            print("isGenericParam = "); isGenericParam.print(printer); println()
            print("isValueType = "); isValueType.print(printer); println()
            print("isManaged = "); isManaged.print(printer); println()
            print("attrs = "); attrs.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:68]
 */
abstract class IlVarDto (
    val type: CacheKey,
    val index: Int
) : IlDto (
) {
    //companion
    
    companion object : IAbstractDeclaration<IlVarDto> {
        override fun readUnknownInstance(ctx: SerializationCtx, buffer: AbstractBuffer, unknownId: RdId, size: Int): IlVarDto  {
            val objectStartPosition = buffer.position
            val type = CacheKey.read(ctx, buffer)
            val index = buffer.readInt()
            val unknownBytes = ByteArray(objectStartPosition + size - buffer.position)
            buffer.readByteArrayRaw(unknownBytes)
            return IlVarDto_Unknown(type, index, unknownId, unknownBytes)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    //hash code trait
    //pretty print
    //deepClone
    //contexts
}


class IlVarDto_Unknown (
    type: CacheKey,
    index: Int,
    override val unknownId: RdId,
    val unknownBytes: ByteArray
) : IlVarDto (
    type,
    index
), IUnknownInstance {
    //companion
    
    companion object : IMarshaller<IlVarDto_Unknown> {
        override val _type: KClass<IlVarDto_Unknown> = IlVarDto_Unknown::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlVarDto_Unknown  {
            throw NotImplementedError("Unknown instances should not be read via serializer")
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlVarDto_Unknown)  {
            CacheKey.write(ctx, buffer, value.type)
            buffer.writeInt(value.index)
            buffer.writeByteArrayRaw(value.unknownBytes)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as IlVarDto_Unknown
        
        if (type != other.type) return false
        if (index != other.index) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + type.hashCode()
        __r = __r*31 + index.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlVarDto_Unknown (")
        printer.indent {
            print("type = "); type.print(printer); println()
            print("index = "); index.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}
