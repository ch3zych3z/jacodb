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
            serializers.register(TypeId)
            serializers.register(IlPointerTypeDto)
            serializers.register(IlPrimitiveTypeDto)
            serializers.register(IlEnumTypeDto)
            serializers.register(IlStructTypeDto)
            serializers.register(IlClassTypeDto)
            serializers.register(IlArrayTypeDto)
            serializers.register(IlAttrDto)
            serializers.register(IlFieldDto)
            serializers.register(IlParameterDto)
            serializers.register(IlLocalVarDto)
            serializers.register(IlTempVarDto)
            serializers.register(IlErrVarDto)
            serializers.register(IlCatchScopeDto)
            serializers.register(IlFilterScopeDto)
            serializers.register(IlFaultScopeDto)
            serializers.register(IlFinallyScopeDto)
            serializers.register(IlMethodDto)
            serializers.register(IlSignatureDto)
            serializers.register(TypeIdBase_Unknown)
            serializers.register(IlDto_Unknown)
            serializers.register(IlTypeDto_Unknown)
            serializers.register(IlValueTypeDto_Unknown)
            serializers.register(IlReferenceTypeDto_Unknown)
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
        
        
        const val serializationHash = 8637739640480870871L
        
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
 * #### Generated from [IlModel.kt:69]
 */
class IlArrayTypeDto (
    val elementType: TypeId,
    asmName: String,
    moduleToken: Int,
    typeToken: Int,
    namespaceName: String,
    name: String,
    fullname: String,
    declType: TypeId?,
    baseType: TypeId?,
    interfaces: List<TypeId>,
    genericArgs: List<TypeId>,
    isGenericParam: Boolean,
    isGenericDefinition: Boolean,
    isValueType: Boolean,
    isManaged: Boolean,
    attrs: List<IlAttrDto>,
    fields: List<IlFieldDto>,
    methods: List<IlMethodDto>
) : IlReferenceTypeDto (
    asmName,
    moduleToken,
    typeToken,
    namespaceName,
    name,
    fullname,
    declType,
    baseType,
    interfaces,
    genericArgs,
    isGenericParam,
    isGenericDefinition,
    isValueType,
    isManaged,
    attrs,
    fields,
    methods
) {
    //companion
    
    companion object : IMarshaller<IlArrayTypeDto> {
        override val _type: KClass<IlArrayTypeDto> = IlArrayTypeDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlArrayTypeDto  {
            val asmName = buffer.readString()
            val moduleToken = buffer.readInt()
            val typeToken = buffer.readInt()
            val namespaceName = buffer.readString()
            val name = buffer.readString()
            val fullname = buffer.readString()
            val declType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val baseType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val interfaces = buffer.readList { TypeId.read(ctx, buffer) }
            val genericArgs = buffer.readList { TypeId.read(ctx, buffer) }
            val isGenericParam = buffer.readBool()
            val isGenericDefinition = buffer.readBool()
            val isValueType = buffer.readBool()
            val isManaged = buffer.readBool()
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            val fields = buffer.readList { IlFieldDto.read(ctx, buffer) }
            val methods = buffer.readList { IlMethodDto.read(ctx, buffer) }
            val elementType = TypeId.read(ctx, buffer)
            return IlArrayTypeDto(elementType, asmName, moduleToken, typeToken, namespaceName, name, fullname, declType, baseType, interfaces, genericArgs, isGenericParam, isGenericDefinition, isValueType, isManaged, attrs, fields, methods)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlArrayTypeDto)  {
            buffer.writeString(value.asmName)
            buffer.writeInt(value.moduleToken)
            buffer.writeInt(value.typeToken)
            buffer.writeString(value.namespaceName)
            buffer.writeString(value.name)
            buffer.writeString(value.fullname)
            buffer.writeNullable(value.declType) { TypeId.write(ctx, buffer, it) }
            buffer.writeNullable(value.baseType) { TypeId.write(ctx, buffer, it) }
            buffer.writeList(value.interfaces) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeList(value.genericArgs) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeBool(value.isGenericParam)
            buffer.writeBool(value.isGenericDefinition)
            buffer.writeBool(value.isValueType)
            buffer.writeBool(value.isManaged)
            buffer.writeList(value.attrs) { v -> IlAttrDto.write(ctx, buffer, v) }
            buffer.writeList(value.fields) { v -> IlFieldDto.write(ctx, buffer, v) }
            buffer.writeList(value.methods) { v -> IlMethodDto.write(ctx, buffer, v) }
            TypeId.write(ctx, buffer, value.elementType)
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
        
        other as IlArrayTypeDto
        
        if (elementType != other.elementType) return false
        if (asmName != other.asmName) return false
        if (moduleToken != other.moduleToken) return false
        if (typeToken != other.typeToken) return false
        if (namespaceName != other.namespaceName) return false
        if (name != other.name) return false
        if (fullname != other.fullname) return false
        if (declType != other.declType) return false
        if (baseType != other.baseType) return false
        if (interfaces != other.interfaces) return false
        if (genericArgs != other.genericArgs) return false
        if (isGenericParam != other.isGenericParam) return false
        if (isGenericDefinition != other.isGenericDefinition) return false
        if (isValueType != other.isValueType) return false
        if (isManaged != other.isManaged) return false
        if (attrs != other.attrs) return false
        if (fields != other.fields) return false
        if (methods != other.methods) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + elementType.hashCode()
        __r = __r*31 + asmName.hashCode()
        __r = __r*31 + moduleToken.hashCode()
        __r = __r*31 + typeToken.hashCode()
        __r = __r*31 + namespaceName.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + fullname.hashCode()
        __r = __r*31 + if (declType != null) declType.hashCode() else 0
        __r = __r*31 + if (baseType != null) baseType.hashCode() else 0
        __r = __r*31 + interfaces.hashCode()
        __r = __r*31 + genericArgs.hashCode()
        __r = __r*31 + isGenericParam.hashCode()
        __r = __r*31 + isGenericDefinition.hashCode()
        __r = __r*31 + isValueType.hashCode()
        __r = __r*31 + isManaged.hashCode()
        __r = __r*31 + attrs.hashCode()
        __r = __r*31 + fields.hashCode()
        __r = __r*31 + methods.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlArrayTypeDto (")
        printer.indent {
            print("elementType = "); elementType.print(printer); println()
            print("asmName = "); asmName.print(printer); println()
            print("moduleToken = "); moduleToken.print(printer); println()
            print("typeToken = "); typeToken.print(printer); println()
            print("namespaceName = "); namespaceName.print(printer); println()
            print("name = "); name.print(printer); println()
            print("fullname = "); fullname.print(printer); println()
            print("declType = "); declType.print(printer); println()
            print("baseType = "); baseType.print(printer); println()
            print("interfaces = "); interfaces.print(printer); println()
            print("genericArgs = "); genericArgs.print(printer); println()
            print("isGenericParam = "); isGenericParam.print(printer); println()
            print("isGenericDefinition = "); isGenericDefinition.print(printer); println()
            print("isValueType = "); isValueType.print(printer); println()
            print("isManaged = "); isManaged.print(printer); println()
            print("attrs = "); attrs.print(printer); println()
            print("fields = "); fields.print(printer); println()
            print("methods = "); methods.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:72]
 */
class IlAttrDto (
    val attrType: TypeId,
    val ctorArgs: List<IlConstDto>,
    val namedArgsNames: List<String>,
    val namedArgsValues: List<IlConstDto>,
    val genericArgs: List<TypeId>
) : IlDto (
) {
    //companion
    
    companion object : IMarshaller<IlAttrDto> {
        override val _type: KClass<IlAttrDto> = IlAttrDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlAttrDto  {
            val attrType = TypeId.read(ctx, buffer)
            val ctorArgs = buffer.readList { ctx.serializers.readPolymorphic<IlConstDto>(ctx, buffer, IlConstDto) }
            val namedArgsNames = buffer.readList { buffer.readString() }
            val namedArgsValues = buffer.readList { ctx.serializers.readPolymorphic<IlConstDto>(ctx, buffer, IlConstDto) }
            val genericArgs = buffer.readList { TypeId.read(ctx, buffer) }
            return IlAttrDto(attrType, ctorArgs, namedArgsNames, namedArgsValues, genericArgs)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlAttrDto)  {
            TypeId.write(ctx, buffer, value.attrType)
            buffer.writeList(value.ctorArgs) { v -> ctx.serializers.writePolymorphic(ctx, buffer, v) }
            buffer.writeList(value.namedArgsNames) { v -> buffer.writeString(v) }
            buffer.writeList(value.namedArgsValues) { v -> ctx.serializers.writePolymorphic(ctx, buffer, v) }
            buffer.writeList(value.genericArgs) { v -> TypeId.write(ctx, buffer, v) }
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
        if (genericArgs != other.genericArgs) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + attrType.hashCode()
        __r = __r*31 + ctorArgs.hashCode()
        __r = __r*31 + namedArgsNames.hashCode()
        __r = __r*31 + namedArgsValues.hashCode()
        __r = __r*31 + genericArgs.hashCode()
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
            print("genericArgs = "); genericArgs.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:113]
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
 * #### Generated from [IlModel.kt:68]
 */
class IlClassTypeDto (
    asmName: String,
    moduleToken: Int,
    typeToken: Int,
    namespaceName: String,
    name: String,
    fullname: String,
    declType: TypeId?,
    baseType: TypeId?,
    interfaces: List<TypeId>,
    genericArgs: List<TypeId>,
    isGenericParam: Boolean,
    isGenericDefinition: Boolean,
    isValueType: Boolean,
    isManaged: Boolean,
    attrs: List<IlAttrDto>,
    fields: List<IlFieldDto>,
    methods: List<IlMethodDto>
) : IlReferenceTypeDto (
    asmName,
    moduleToken,
    typeToken,
    namespaceName,
    name,
    fullname,
    declType,
    baseType,
    interfaces,
    genericArgs,
    isGenericParam,
    isGenericDefinition,
    isValueType,
    isManaged,
    attrs,
    fields,
    methods
) {
    //companion
    
    companion object : IMarshaller<IlClassTypeDto> {
        override val _type: KClass<IlClassTypeDto> = IlClassTypeDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlClassTypeDto  {
            val asmName = buffer.readString()
            val moduleToken = buffer.readInt()
            val typeToken = buffer.readInt()
            val namespaceName = buffer.readString()
            val name = buffer.readString()
            val fullname = buffer.readString()
            val declType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val baseType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val interfaces = buffer.readList { TypeId.read(ctx, buffer) }
            val genericArgs = buffer.readList { TypeId.read(ctx, buffer) }
            val isGenericParam = buffer.readBool()
            val isGenericDefinition = buffer.readBool()
            val isValueType = buffer.readBool()
            val isManaged = buffer.readBool()
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            val fields = buffer.readList { IlFieldDto.read(ctx, buffer) }
            val methods = buffer.readList { IlMethodDto.read(ctx, buffer) }
            return IlClassTypeDto(asmName, moduleToken, typeToken, namespaceName, name, fullname, declType, baseType, interfaces, genericArgs, isGenericParam, isGenericDefinition, isValueType, isManaged, attrs, fields, methods)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlClassTypeDto)  {
            buffer.writeString(value.asmName)
            buffer.writeInt(value.moduleToken)
            buffer.writeInt(value.typeToken)
            buffer.writeString(value.namespaceName)
            buffer.writeString(value.name)
            buffer.writeString(value.fullname)
            buffer.writeNullable(value.declType) { TypeId.write(ctx, buffer, it) }
            buffer.writeNullable(value.baseType) { TypeId.write(ctx, buffer, it) }
            buffer.writeList(value.interfaces) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeList(value.genericArgs) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeBool(value.isGenericParam)
            buffer.writeBool(value.isGenericDefinition)
            buffer.writeBool(value.isValueType)
            buffer.writeBool(value.isManaged)
            buffer.writeList(value.attrs) { v -> IlAttrDto.write(ctx, buffer, v) }
            buffer.writeList(value.fields) { v -> IlFieldDto.write(ctx, buffer, v) }
            buffer.writeList(value.methods) { v -> IlMethodDto.write(ctx, buffer, v) }
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
        
        other as IlClassTypeDto
        
        if (asmName != other.asmName) return false
        if (moduleToken != other.moduleToken) return false
        if (typeToken != other.typeToken) return false
        if (namespaceName != other.namespaceName) return false
        if (name != other.name) return false
        if (fullname != other.fullname) return false
        if (declType != other.declType) return false
        if (baseType != other.baseType) return false
        if (interfaces != other.interfaces) return false
        if (genericArgs != other.genericArgs) return false
        if (isGenericParam != other.isGenericParam) return false
        if (isGenericDefinition != other.isGenericDefinition) return false
        if (isValueType != other.isValueType) return false
        if (isManaged != other.isManaged) return false
        if (attrs != other.attrs) return false
        if (fields != other.fields) return false
        if (methods != other.methods) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + asmName.hashCode()
        __r = __r*31 + moduleToken.hashCode()
        __r = __r*31 + typeToken.hashCode()
        __r = __r*31 + namespaceName.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + fullname.hashCode()
        __r = __r*31 + if (declType != null) declType.hashCode() else 0
        __r = __r*31 + if (baseType != null) baseType.hashCode() else 0
        __r = __r*31 + interfaces.hashCode()
        __r = __r*31 + genericArgs.hashCode()
        __r = __r*31 + isGenericParam.hashCode()
        __r = __r*31 + isGenericDefinition.hashCode()
        __r = __r*31 + isValueType.hashCode()
        __r = __r*31 + isManaged.hashCode()
        __r = __r*31 + attrs.hashCode()
        __r = __r*31 + fields.hashCode()
        __r = __r*31 + methods.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlClassTypeDto (")
        printer.indent {
            print("asmName = "); asmName.print(printer); println()
            print("moduleToken = "); moduleToken.print(printer); println()
            print("typeToken = "); typeToken.print(printer); println()
            print("namespaceName = "); namespaceName.print(printer); println()
            print("name = "); name.print(printer); println()
            print("fullname = "); fullname.print(printer); println()
            print("declType = "); declType.print(printer); println()
            print("baseType = "); baseType.print(printer); println()
            print("interfaces = "); interfaces.print(printer); println()
            print("genericArgs = "); genericArgs.print(printer); println()
            print("isGenericParam = "); isGenericParam.print(printer); println()
            print("isGenericDefinition = "); isGenericDefinition.print(printer); println()
            print("isValueType = "); isValueType.print(printer); println()
            print("isManaged = "); isManaged.print(printer); println()
            print("attrs = "); attrs.print(printer); println()
            print("fields = "); fields.print(printer); println()
            print("methods = "); methods.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:31]
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
 * #### Generated from [IlModel.kt:107]
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
 * #### Generated from [IlModel.kt:58]
 */
class IlEnumTypeDto (
    val underlyingType: TypeId,
    val names: List<String>,
    val values: List<IlConstDto>,
    asmName: String,
    moduleToken: Int,
    typeToken: Int,
    namespaceName: String,
    name: String,
    fullname: String,
    declType: TypeId?,
    baseType: TypeId?,
    interfaces: List<TypeId>,
    genericArgs: List<TypeId>,
    isGenericParam: Boolean,
    isGenericDefinition: Boolean,
    isValueType: Boolean,
    isManaged: Boolean,
    attrs: List<IlAttrDto>,
    fields: List<IlFieldDto>,
    methods: List<IlMethodDto>
) : IlValueTypeDto (
    asmName,
    moduleToken,
    typeToken,
    namespaceName,
    name,
    fullname,
    declType,
    baseType,
    interfaces,
    genericArgs,
    isGenericParam,
    isGenericDefinition,
    isValueType,
    isManaged,
    attrs,
    fields,
    methods
) {
    //companion
    
    companion object : IMarshaller<IlEnumTypeDto> {
        override val _type: KClass<IlEnumTypeDto> = IlEnumTypeDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlEnumTypeDto  {
            val asmName = buffer.readString()
            val moduleToken = buffer.readInt()
            val typeToken = buffer.readInt()
            val namespaceName = buffer.readString()
            val name = buffer.readString()
            val fullname = buffer.readString()
            val declType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val baseType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val interfaces = buffer.readList { TypeId.read(ctx, buffer) }
            val genericArgs = buffer.readList { TypeId.read(ctx, buffer) }
            val isGenericParam = buffer.readBool()
            val isGenericDefinition = buffer.readBool()
            val isValueType = buffer.readBool()
            val isManaged = buffer.readBool()
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            val fields = buffer.readList { IlFieldDto.read(ctx, buffer) }
            val methods = buffer.readList { IlMethodDto.read(ctx, buffer) }
            val underlyingType = TypeId.read(ctx, buffer)
            val names = buffer.readList { buffer.readString() }
            val values = buffer.readList { ctx.serializers.readPolymorphic<IlConstDto>(ctx, buffer, IlConstDto) }
            return IlEnumTypeDto(underlyingType, names, values, asmName, moduleToken, typeToken, namespaceName, name, fullname, declType, baseType, interfaces, genericArgs, isGenericParam, isGenericDefinition, isValueType, isManaged, attrs, fields, methods)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlEnumTypeDto)  {
            buffer.writeString(value.asmName)
            buffer.writeInt(value.moduleToken)
            buffer.writeInt(value.typeToken)
            buffer.writeString(value.namespaceName)
            buffer.writeString(value.name)
            buffer.writeString(value.fullname)
            buffer.writeNullable(value.declType) { TypeId.write(ctx, buffer, it) }
            buffer.writeNullable(value.baseType) { TypeId.write(ctx, buffer, it) }
            buffer.writeList(value.interfaces) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeList(value.genericArgs) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeBool(value.isGenericParam)
            buffer.writeBool(value.isGenericDefinition)
            buffer.writeBool(value.isValueType)
            buffer.writeBool(value.isManaged)
            buffer.writeList(value.attrs) { v -> IlAttrDto.write(ctx, buffer, v) }
            buffer.writeList(value.fields) { v -> IlFieldDto.write(ctx, buffer, v) }
            buffer.writeList(value.methods) { v -> IlMethodDto.write(ctx, buffer, v) }
            TypeId.write(ctx, buffer, value.underlyingType)
            buffer.writeList(value.names) { v -> buffer.writeString(v) }
            buffer.writeList(value.values) { v -> ctx.serializers.writePolymorphic(ctx, buffer, v) }
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
        
        other as IlEnumTypeDto
        
        if (underlyingType != other.underlyingType) return false
        if (names != other.names) return false
        if (values != other.values) return false
        if (asmName != other.asmName) return false
        if (moduleToken != other.moduleToken) return false
        if (typeToken != other.typeToken) return false
        if (namespaceName != other.namespaceName) return false
        if (name != other.name) return false
        if (fullname != other.fullname) return false
        if (declType != other.declType) return false
        if (baseType != other.baseType) return false
        if (interfaces != other.interfaces) return false
        if (genericArgs != other.genericArgs) return false
        if (isGenericParam != other.isGenericParam) return false
        if (isGenericDefinition != other.isGenericDefinition) return false
        if (isValueType != other.isValueType) return false
        if (isManaged != other.isManaged) return false
        if (attrs != other.attrs) return false
        if (fields != other.fields) return false
        if (methods != other.methods) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + underlyingType.hashCode()
        __r = __r*31 + names.hashCode()
        __r = __r*31 + values.hashCode()
        __r = __r*31 + asmName.hashCode()
        __r = __r*31 + moduleToken.hashCode()
        __r = __r*31 + typeToken.hashCode()
        __r = __r*31 + namespaceName.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + fullname.hashCode()
        __r = __r*31 + if (declType != null) declType.hashCode() else 0
        __r = __r*31 + if (baseType != null) baseType.hashCode() else 0
        __r = __r*31 + interfaces.hashCode()
        __r = __r*31 + genericArgs.hashCode()
        __r = __r*31 + isGenericParam.hashCode()
        __r = __r*31 + isGenericDefinition.hashCode()
        __r = __r*31 + isValueType.hashCode()
        __r = __r*31 + isManaged.hashCode()
        __r = __r*31 + attrs.hashCode()
        __r = __r*31 + fields.hashCode()
        __r = __r*31 + methods.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlEnumTypeDto (")
        printer.indent {
            print("underlyingType = "); underlyingType.print(printer); println()
            print("names = "); names.print(printer); println()
            print("values = "); values.print(printer); println()
            print("asmName = "); asmName.print(printer); println()
            print("moduleToken = "); moduleToken.print(printer); println()
            print("typeToken = "); typeToken.print(printer); println()
            print("namespaceName = "); namespaceName.print(printer); println()
            print("name = "); name.print(printer); println()
            print("fullname = "); fullname.print(printer); println()
            print("declType = "); declType.print(printer); println()
            print("baseType = "); baseType.print(printer); println()
            print("interfaces = "); interfaces.print(printer); println()
            print("genericArgs = "); genericArgs.print(printer); println()
            print("isGenericParam = "); isGenericParam.print(printer); println()
            print("isGenericDefinition = "); isGenericDefinition.print(printer); println()
            print("isValueType = "); isValueType.print(printer); println()
            print("isManaged = "); isManaged.print(printer); println()
            print("attrs = "); attrs.print(printer); println()
            print("fields = "); fields.print(printer); println()
            print("methods = "); methods.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:105]
 */
class IlErrVarDto (
    type: TypeId,
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
            val type = TypeId.read(ctx, buffer)
            val index = buffer.readInt()
            return IlErrVarDto(type, index)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlErrVarDto)  {
            TypeId.write(ctx, buffer, value.type)
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
 * #### Generated from [IlModel.kt:118]
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
 * #### Generated from [IlModel.kt:80]
 */
class IlFieldDto (
    val fieldType: TypeId,
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
            val fieldType = TypeId.read(ctx, buffer)
            val isStatic = buffer.readBool()
            val name = buffer.readString()
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            return IlFieldDto(fieldType, isStatic, name, attrs)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlFieldDto)  {
            TypeId.write(ctx, buffer, value.fieldType)
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
        
        if (fieldType != other.fieldType) return false
        if (isStatic != other.isStatic) return false
        if (name != other.name) return false
        if (attrs != other.attrs) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
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
 * #### Generated from [IlModel.kt:115]
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
 * #### Generated from [IlModel.kt:119]
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
 * #### Generated from [IlModel.kt:100]
 */
class IlLocalVarDto (
    val isPinned: Boolean,
    type: TypeId,
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
            val type = TypeId.read(ctx, buffer)
            val index = buffer.readInt()
            val isPinned = buffer.readBool()
            return IlLocalVarDto(isPinned, type, index)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlLocalVarDto)  {
            TypeId.write(ctx, buffer, value.type)
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
 * #### Generated from [IlModel.kt:122]
 */
class IlMethodDto (
    val returnType: TypeId,
    val attrs: List<IlAttrDto>,
    val isStatic: Boolean,
    val name: String,
    val parameters: List<IlParameterDto>,
    val resolved: Boolean,
    val locals: List<IlLocalVarDto>,
    val temps: List<IlTempVarDto>,
    val errs: List<IlErrVarDto>,
    val ehScopes: List<IlEhScopeDto>,
    val rawInstList: List<IlStmtDto>
) : IlDto (
) {
    //companion
    
    companion object : IMarshaller<IlMethodDto> {
        override val _type: KClass<IlMethodDto> = IlMethodDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlMethodDto  {
            val returnType = TypeId.read(ctx, buffer)
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            val isStatic = buffer.readBool()
            val name = buffer.readString()
            val parameters = buffer.readList { IlParameterDto.read(ctx, buffer) }
            val resolved = buffer.readBool()
            val locals = buffer.readList { IlLocalVarDto.read(ctx, buffer) }
            val temps = buffer.readList { IlTempVarDto.read(ctx, buffer) }
            val errs = buffer.readList { IlErrVarDto.read(ctx, buffer) }
            val ehScopes = buffer.readList { ctx.serializers.readPolymorphic<IlEhScopeDto>(ctx, buffer, IlEhScopeDto) }
            val rawInstList = buffer.readList { ctx.serializers.readPolymorphic<IlStmtDto>(ctx, buffer, IlStmtDto) }
            return IlMethodDto(returnType, attrs, isStatic, name, parameters, resolved, locals, temps, errs, ehScopes, rawInstList)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlMethodDto)  {
            TypeId.write(ctx, buffer, value.returnType)
            buffer.writeList(value.attrs) { v -> IlAttrDto.write(ctx, buffer, v) }
            buffer.writeBool(value.isStatic)
            buffer.writeString(value.name)
            buffer.writeList(value.parameters) { v -> IlParameterDto.write(ctx, buffer, v) }
            buffer.writeBool(value.resolved)
            buffer.writeList(value.locals) { v -> IlLocalVarDto.write(ctx, buffer, v) }
            buffer.writeList(value.temps) { v -> IlTempVarDto.write(ctx, buffer, v) }
            buffer.writeList(value.errs) { v -> IlErrVarDto.write(ctx, buffer, v) }
            buffer.writeList(value.ehScopes) { v -> ctx.serializers.writePolymorphic(ctx, buffer, v) }
            buffer.writeList(value.rawInstList) { v -> ctx.serializers.writePolymorphic(ctx, buffer, v) }
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
        
        if (returnType != other.returnType) return false
        if (attrs != other.attrs) return false
        if (isStatic != other.isStatic) return false
        if (name != other.name) return false
        if (parameters != other.parameters) return false
        if (resolved != other.resolved) return false
        if (locals != other.locals) return false
        if (temps != other.temps) return false
        if (errs != other.errs) return false
        if (ehScopes != other.ehScopes) return false
        if (rawInstList != other.rawInstList) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + returnType.hashCode()
        __r = __r*31 + attrs.hashCode()
        __r = __r*31 + isStatic.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + parameters.hashCode()
        __r = __r*31 + resolved.hashCode()
        __r = __r*31 + locals.hashCode()
        __r = __r*31 + temps.hashCode()
        __r = __r*31 + errs.hashCode()
        __r = __r*31 + ehScopes.hashCode()
        __r = __r*31 + rawInstList.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlMethodDto (")
        printer.indent {
            print("returnType = "); returnType.print(printer); println()
            print("attrs = "); attrs.print(printer); println()
            print("isStatic = "); isStatic.print(printer); println()
            print("name = "); name.print(printer); println()
            print("parameters = "); parameters.print(printer); println()
            print("resolved = "); resolved.print(printer); println()
            print("locals = "); locals.print(printer); println()
            print("temps = "); temps.print(printer); println()
            print("errs = "); errs.print(printer); println()
            print("ehScopes = "); ehScopes.print(printer); println()
            print("rawInstList = "); rawInstList.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:87]
 */
data class IlParameterDto (
    val index: Int,
    val type: TypeId,
    val name: String,
    val defaultValue: IlConstDto?,
    val attrs: List<IlAttrDto>
) : IPrintable {
    //companion
    
    companion object : IMarshaller<IlParameterDto> {
        override val _type: KClass<IlParameterDto> = IlParameterDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlParameterDto  {
            val index = buffer.readInt()
            val type = TypeId.read(ctx, buffer)
            val name = buffer.readString()
            val defaultValue = buffer.readNullable { ctx.serializers.readPolymorphic<IlConstDto>(ctx, buffer, IlConstDto) }
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            return IlParameterDto(index, type, name, defaultValue, attrs)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlParameterDto)  {
            buffer.writeInt(value.index)
            TypeId.write(ctx, buffer, value.type)
            buffer.writeString(value.name)
            buffer.writeNullable(value.defaultValue) { ctx.serializers.writePolymorphic(ctx, buffer, it) }
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
 * #### Generated from [IlModel.kt:53]
 */
class IlPointerTypeDto (
    val targetType: TypeId,
    asmName: String,
    moduleToken: Int,
    typeToken: Int,
    namespaceName: String,
    name: String,
    fullname: String,
    declType: TypeId?,
    baseType: TypeId?,
    interfaces: List<TypeId>,
    genericArgs: List<TypeId>,
    isGenericParam: Boolean,
    isGenericDefinition: Boolean,
    isValueType: Boolean,
    isManaged: Boolean,
    attrs: List<IlAttrDto>,
    fields: List<IlFieldDto>,
    methods: List<IlMethodDto>
) : IlTypeDto (
    asmName,
    moduleToken,
    typeToken,
    namespaceName,
    name,
    fullname,
    declType,
    baseType,
    interfaces,
    genericArgs,
    isGenericParam,
    isGenericDefinition,
    isValueType,
    isManaged,
    attrs,
    fields,
    methods
) {
    //companion
    
    companion object : IMarshaller<IlPointerTypeDto> {
        override val _type: KClass<IlPointerTypeDto> = IlPointerTypeDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlPointerTypeDto  {
            val asmName = buffer.readString()
            val moduleToken = buffer.readInt()
            val typeToken = buffer.readInt()
            val namespaceName = buffer.readString()
            val name = buffer.readString()
            val fullname = buffer.readString()
            val declType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val baseType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val interfaces = buffer.readList { TypeId.read(ctx, buffer) }
            val genericArgs = buffer.readList { TypeId.read(ctx, buffer) }
            val isGenericParam = buffer.readBool()
            val isGenericDefinition = buffer.readBool()
            val isValueType = buffer.readBool()
            val isManaged = buffer.readBool()
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            val fields = buffer.readList { IlFieldDto.read(ctx, buffer) }
            val methods = buffer.readList { IlMethodDto.read(ctx, buffer) }
            val targetType = TypeId.read(ctx, buffer)
            return IlPointerTypeDto(targetType, asmName, moduleToken, typeToken, namespaceName, name, fullname, declType, baseType, interfaces, genericArgs, isGenericParam, isGenericDefinition, isValueType, isManaged, attrs, fields, methods)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlPointerTypeDto)  {
            buffer.writeString(value.asmName)
            buffer.writeInt(value.moduleToken)
            buffer.writeInt(value.typeToken)
            buffer.writeString(value.namespaceName)
            buffer.writeString(value.name)
            buffer.writeString(value.fullname)
            buffer.writeNullable(value.declType) { TypeId.write(ctx, buffer, it) }
            buffer.writeNullable(value.baseType) { TypeId.write(ctx, buffer, it) }
            buffer.writeList(value.interfaces) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeList(value.genericArgs) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeBool(value.isGenericParam)
            buffer.writeBool(value.isGenericDefinition)
            buffer.writeBool(value.isValueType)
            buffer.writeBool(value.isManaged)
            buffer.writeList(value.attrs) { v -> IlAttrDto.write(ctx, buffer, v) }
            buffer.writeList(value.fields) { v -> IlFieldDto.write(ctx, buffer, v) }
            buffer.writeList(value.methods) { v -> IlMethodDto.write(ctx, buffer, v) }
            TypeId.write(ctx, buffer, value.targetType)
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
        
        other as IlPointerTypeDto
        
        if (targetType != other.targetType) return false
        if (asmName != other.asmName) return false
        if (moduleToken != other.moduleToken) return false
        if (typeToken != other.typeToken) return false
        if (namespaceName != other.namespaceName) return false
        if (name != other.name) return false
        if (fullname != other.fullname) return false
        if (declType != other.declType) return false
        if (baseType != other.baseType) return false
        if (interfaces != other.interfaces) return false
        if (genericArgs != other.genericArgs) return false
        if (isGenericParam != other.isGenericParam) return false
        if (isGenericDefinition != other.isGenericDefinition) return false
        if (isValueType != other.isValueType) return false
        if (isManaged != other.isManaged) return false
        if (attrs != other.attrs) return false
        if (fields != other.fields) return false
        if (methods != other.methods) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + targetType.hashCode()
        __r = __r*31 + asmName.hashCode()
        __r = __r*31 + moduleToken.hashCode()
        __r = __r*31 + typeToken.hashCode()
        __r = __r*31 + namespaceName.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + fullname.hashCode()
        __r = __r*31 + if (declType != null) declType.hashCode() else 0
        __r = __r*31 + if (baseType != null) baseType.hashCode() else 0
        __r = __r*31 + interfaces.hashCode()
        __r = __r*31 + genericArgs.hashCode()
        __r = __r*31 + isGenericParam.hashCode()
        __r = __r*31 + isGenericDefinition.hashCode()
        __r = __r*31 + isValueType.hashCode()
        __r = __r*31 + isManaged.hashCode()
        __r = __r*31 + attrs.hashCode()
        __r = __r*31 + fields.hashCode()
        __r = __r*31 + methods.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlPointerTypeDto (")
        printer.indent {
            print("targetType = "); targetType.print(printer); println()
            print("asmName = "); asmName.print(printer); println()
            print("moduleToken = "); moduleToken.print(printer); println()
            print("typeToken = "); typeToken.print(printer); println()
            print("namespaceName = "); namespaceName.print(printer); println()
            print("name = "); name.print(printer); println()
            print("fullname = "); fullname.print(printer); println()
            print("declType = "); declType.print(printer); println()
            print("baseType = "); baseType.print(printer); println()
            print("interfaces = "); interfaces.print(printer); println()
            print("genericArgs = "); genericArgs.print(printer); println()
            print("isGenericParam = "); isGenericParam.print(printer); println()
            print("isGenericDefinition = "); isGenericDefinition.print(printer); println()
            print("isValueType = "); isValueType.print(printer); println()
            print("isManaged = "); isManaged.print(printer); println()
            print("attrs = "); attrs.print(printer); println()
            print("fields = "); fields.print(printer); println()
            print("methods = "); methods.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:57]
 */
class IlPrimitiveTypeDto (
    asmName: String,
    moduleToken: Int,
    typeToken: Int,
    namespaceName: String,
    name: String,
    fullname: String,
    declType: TypeId?,
    baseType: TypeId?,
    interfaces: List<TypeId>,
    genericArgs: List<TypeId>,
    isGenericParam: Boolean,
    isGenericDefinition: Boolean,
    isValueType: Boolean,
    isManaged: Boolean,
    attrs: List<IlAttrDto>,
    fields: List<IlFieldDto>,
    methods: List<IlMethodDto>
) : IlValueTypeDto (
    asmName,
    moduleToken,
    typeToken,
    namespaceName,
    name,
    fullname,
    declType,
    baseType,
    interfaces,
    genericArgs,
    isGenericParam,
    isGenericDefinition,
    isValueType,
    isManaged,
    attrs,
    fields,
    methods
) {
    //companion
    
    companion object : IMarshaller<IlPrimitiveTypeDto> {
        override val _type: KClass<IlPrimitiveTypeDto> = IlPrimitiveTypeDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlPrimitiveTypeDto  {
            val asmName = buffer.readString()
            val moduleToken = buffer.readInt()
            val typeToken = buffer.readInt()
            val namespaceName = buffer.readString()
            val name = buffer.readString()
            val fullname = buffer.readString()
            val declType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val baseType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val interfaces = buffer.readList { TypeId.read(ctx, buffer) }
            val genericArgs = buffer.readList { TypeId.read(ctx, buffer) }
            val isGenericParam = buffer.readBool()
            val isGenericDefinition = buffer.readBool()
            val isValueType = buffer.readBool()
            val isManaged = buffer.readBool()
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            val fields = buffer.readList { IlFieldDto.read(ctx, buffer) }
            val methods = buffer.readList { IlMethodDto.read(ctx, buffer) }
            return IlPrimitiveTypeDto(asmName, moduleToken, typeToken, namespaceName, name, fullname, declType, baseType, interfaces, genericArgs, isGenericParam, isGenericDefinition, isValueType, isManaged, attrs, fields, methods)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlPrimitiveTypeDto)  {
            buffer.writeString(value.asmName)
            buffer.writeInt(value.moduleToken)
            buffer.writeInt(value.typeToken)
            buffer.writeString(value.namespaceName)
            buffer.writeString(value.name)
            buffer.writeString(value.fullname)
            buffer.writeNullable(value.declType) { TypeId.write(ctx, buffer, it) }
            buffer.writeNullable(value.baseType) { TypeId.write(ctx, buffer, it) }
            buffer.writeList(value.interfaces) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeList(value.genericArgs) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeBool(value.isGenericParam)
            buffer.writeBool(value.isGenericDefinition)
            buffer.writeBool(value.isValueType)
            buffer.writeBool(value.isManaged)
            buffer.writeList(value.attrs) { v -> IlAttrDto.write(ctx, buffer, v) }
            buffer.writeList(value.fields) { v -> IlFieldDto.write(ctx, buffer, v) }
            buffer.writeList(value.methods) { v -> IlMethodDto.write(ctx, buffer, v) }
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
        
        other as IlPrimitiveTypeDto
        
        if (asmName != other.asmName) return false
        if (moduleToken != other.moduleToken) return false
        if (typeToken != other.typeToken) return false
        if (namespaceName != other.namespaceName) return false
        if (name != other.name) return false
        if (fullname != other.fullname) return false
        if (declType != other.declType) return false
        if (baseType != other.baseType) return false
        if (interfaces != other.interfaces) return false
        if (genericArgs != other.genericArgs) return false
        if (isGenericParam != other.isGenericParam) return false
        if (isGenericDefinition != other.isGenericDefinition) return false
        if (isValueType != other.isValueType) return false
        if (isManaged != other.isManaged) return false
        if (attrs != other.attrs) return false
        if (fields != other.fields) return false
        if (methods != other.methods) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + asmName.hashCode()
        __r = __r*31 + moduleToken.hashCode()
        __r = __r*31 + typeToken.hashCode()
        __r = __r*31 + namespaceName.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + fullname.hashCode()
        __r = __r*31 + if (declType != null) declType.hashCode() else 0
        __r = __r*31 + if (baseType != null) baseType.hashCode() else 0
        __r = __r*31 + interfaces.hashCode()
        __r = __r*31 + genericArgs.hashCode()
        __r = __r*31 + isGenericParam.hashCode()
        __r = __r*31 + isGenericDefinition.hashCode()
        __r = __r*31 + isValueType.hashCode()
        __r = __r*31 + isManaged.hashCode()
        __r = __r*31 + attrs.hashCode()
        __r = __r*31 + fields.hashCode()
        __r = __r*31 + methods.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlPrimitiveTypeDto (")
        printer.indent {
            print("asmName = "); asmName.print(printer); println()
            print("moduleToken = "); moduleToken.print(printer); println()
            print("typeToken = "); typeToken.print(printer); println()
            print("namespaceName = "); namespaceName.print(printer); println()
            print("name = "); name.print(printer); println()
            print("fullname = "); fullname.print(printer); println()
            print("declType = "); declType.print(printer); println()
            print("baseType = "); baseType.print(printer); println()
            print("interfaces = "); interfaces.print(printer); println()
            print("genericArgs = "); genericArgs.print(printer); println()
            print("isGenericParam = "); isGenericParam.print(printer); println()
            print("isGenericDefinition = "); isGenericDefinition.print(printer); println()
            print("isValueType = "); isValueType.print(printer); println()
            print("isManaged = "); isManaged.print(printer); println()
            print("attrs = "); attrs.print(printer); println()
            print("fields = "); fields.print(printer); println()
            print("methods = "); methods.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:66]
 */
abstract class IlReferenceTypeDto (
    asmName: String,
    moduleToken: Int,
    typeToken: Int,
    namespaceName: String,
    name: String,
    fullname: String,
    declType: TypeId?,
    baseType: TypeId?,
    interfaces: List<TypeId>,
    genericArgs: List<TypeId>,
    isGenericParam: Boolean,
    isGenericDefinition: Boolean,
    isValueType: Boolean,
    isManaged: Boolean,
    attrs: List<IlAttrDto>,
    fields: List<IlFieldDto>,
    methods: List<IlMethodDto>
) : IlTypeDto (
    asmName,
    moduleToken,
    typeToken,
    namespaceName,
    name,
    fullname,
    declType,
    baseType,
    interfaces,
    genericArgs,
    isGenericParam,
    isGenericDefinition,
    isValueType,
    isManaged,
    attrs,
    fields,
    methods
) {
    //companion
    
    companion object : IAbstractDeclaration<IlReferenceTypeDto> {
        override fun readUnknownInstance(ctx: SerializationCtx, buffer: AbstractBuffer, unknownId: RdId, size: Int): IlReferenceTypeDto  {
            val objectStartPosition = buffer.position
            val asmName = buffer.readString()
            val moduleToken = buffer.readInt()
            val typeToken = buffer.readInt()
            val namespaceName = buffer.readString()
            val name = buffer.readString()
            val fullname = buffer.readString()
            val declType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val baseType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val interfaces = buffer.readList { TypeId.read(ctx, buffer) }
            val genericArgs = buffer.readList { TypeId.read(ctx, buffer) }
            val isGenericParam = buffer.readBool()
            val isGenericDefinition = buffer.readBool()
            val isValueType = buffer.readBool()
            val isManaged = buffer.readBool()
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            val fields = buffer.readList { IlFieldDto.read(ctx, buffer) }
            val methods = buffer.readList { IlMethodDto.read(ctx, buffer) }
            val unknownBytes = ByteArray(objectStartPosition + size - buffer.position)
            buffer.readByteArrayRaw(unknownBytes)
            return IlReferenceTypeDto_Unknown(asmName, moduleToken, typeToken, namespaceName, name, fullname, declType, baseType, interfaces, genericArgs, isGenericParam, isGenericDefinition, isValueType, isManaged, attrs, fields, methods, unknownId, unknownBytes)
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


class IlReferenceTypeDto_Unknown (
    asmName: String,
    moduleToken: Int,
    typeToken: Int,
    namespaceName: String,
    name: String,
    fullname: String,
    declType: TypeId?,
    baseType: TypeId?,
    interfaces: List<TypeId>,
    genericArgs: List<TypeId>,
    isGenericParam: Boolean,
    isGenericDefinition: Boolean,
    isValueType: Boolean,
    isManaged: Boolean,
    attrs: List<IlAttrDto>,
    fields: List<IlFieldDto>,
    methods: List<IlMethodDto>,
    override val unknownId: RdId,
    val unknownBytes: ByteArray
) : IlReferenceTypeDto (
    asmName,
    moduleToken,
    typeToken,
    namespaceName,
    name,
    fullname,
    declType,
    baseType,
    interfaces,
    genericArgs,
    isGenericParam,
    isGenericDefinition,
    isValueType,
    isManaged,
    attrs,
    fields,
    methods
), IUnknownInstance {
    //companion
    
    companion object : IMarshaller<IlReferenceTypeDto_Unknown> {
        override val _type: KClass<IlReferenceTypeDto_Unknown> = IlReferenceTypeDto_Unknown::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlReferenceTypeDto_Unknown  {
            throw NotImplementedError("Unknown instances should not be read via serializer")
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlReferenceTypeDto_Unknown)  {
            buffer.writeString(value.asmName)
            buffer.writeInt(value.moduleToken)
            buffer.writeInt(value.typeToken)
            buffer.writeString(value.namespaceName)
            buffer.writeString(value.name)
            buffer.writeString(value.fullname)
            buffer.writeNullable(value.declType) { TypeId.write(ctx, buffer, it) }
            buffer.writeNullable(value.baseType) { TypeId.write(ctx, buffer, it) }
            buffer.writeList(value.interfaces) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeList(value.genericArgs) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeBool(value.isGenericParam)
            buffer.writeBool(value.isGenericDefinition)
            buffer.writeBool(value.isValueType)
            buffer.writeBool(value.isManaged)
            buffer.writeList(value.attrs) { v -> IlAttrDto.write(ctx, buffer, v) }
            buffer.writeList(value.fields) { v -> IlFieldDto.write(ctx, buffer, v) }
            buffer.writeList(value.methods) { v -> IlMethodDto.write(ctx, buffer, v) }
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
        
        other as IlReferenceTypeDto_Unknown
        
        if (asmName != other.asmName) return false
        if (moduleToken != other.moduleToken) return false
        if (typeToken != other.typeToken) return false
        if (namespaceName != other.namespaceName) return false
        if (name != other.name) return false
        if (fullname != other.fullname) return false
        if (declType != other.declType) return false
        if (baseType != other.baseType) return false
        if (interfaces != other.interfaces) return false
        if (genericArgs != other.genericArgs) return false
        if (isGenericParam != other.isGenericParam) return false
        if (isGenericDefinition != other.isGenericDefinition) return false
        if (isValueType != other.isValueType) return false
        if (isManaged != other.isManaged) return false
        if (attrs != other.attrs) return false
        if (fields != other.fields) return false
        if (methods != other.methods) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + asmName.hashCode()
        __r = __r*31 + moduleToken.hashCode()
        __r = __r*31 + typeToken.hashCode()
        __r = __r*31 + namespaceName.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + fullname.hashCode()
        __r = __r*31 + if (declType != null) declType.hashCode() else 0
        __r = __r*31 + if (baseType != null) baseType.hashCode() else 0
        __r = __r*31 + interfaces.hashCode()
        __r = __r*31 + genericArgs.hashCode()
        __r = __r*31 + isGenericParam.hashCode()
        __r = __r*31 + isGenericDefinition.hashCode()
        __r = __r*31 + isValueType.hashCode()
        __r = __r*31 + isManaged.hashCode()
        __r = __r*31 + attrs.hashCode()
        __r = __r*31 + fields.hashCode()
        __r = __r*31 + methods.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlReferenceTypeDto_Unknown (")
        printer.indent {
            print("asmName = "); asmName.print(printer); println()
            print("moduleToken = "); moduleToken.print(printer); println()
            print("typeToken = "); typeToken.print(printer); println()
            print("namespaceName = "); namespaceName.print(printer); println()
            print("name = "); name.print(printer); println()
            print("fullname = "); fullname.print(printer); println()
            print("declType = "); declType.print(printer); println()
            print("baseType = "); baseType.print(printer); println()
            print("interfaces = "); interfaces.print(printer); println()
            print("genericArgs = "); genericArgs.print(printer); println()
            print("isGenericParam = "); isGenericParam.print(printer); println()
            print("isGenericDefinition = "); isGenericDefinition.print(printer); println()
            print("isValueType = "); isValueType.print(printer); println()
            print("isManaged = "); isManaged.print(printer); println()
            print("attrs = "); attrs.print(printer); println()
            print("fields = "); fields.print(printer); println()
            print("methods = "); methods.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:136]
 */
class IlSignatureDto (
    val returnType: TypeId,
    val isInstance: Boolean,
    val genericParamCount: Int,
    val parametersTypes: List<TypeId>
) : IlDto (
) {
    //companion
    
    companion object : IMarshaller<IlSignatureDto> {
        override val _type: KClass<IlSignatureDto> = IlSignatureDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlSignatureDto  {
            val returnType = TypeId.read(ctx, buffer)
            val isInstance = buffer.readBool()
            val genericParamCount = buffer.readInt()
            val parametersTypes = buffer.readList { TypeId.read(ctx, buffer) }
            return IlSignatureDto(returnType, isInstance, genericParamCount, parametersTypes)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlSignatureDto)  {
            TypeId.write(ctx, buffer, value.returnType)
            buffer.writeBool(value.isInstance)
            buffer.writeInt(value.genericParamCount)
            buffer.writeList(value.parametersTypes) { v -> TypeId.write(ctx, buffer, v) }
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
        
        other as IlSignatureDto
        
        if (returnType != other.returnType) return false
        if (isInstance != other.isInstance) return false
        if (genericParamCount != other.genericParamCount) return false
        if (parametersTypes != other.parametersTypes) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + returnType.hashCode()
        __r = __r*31 + isInstance.hashCode()
        __r = __r*31 + genericParamCount.hashCode()
        __r = __r*31 + parametersTypes.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlSignatureDto (")
        printer.indent {
            print("returnType = "); returnType.print(printer); println()
            print("isInstance = "); isInstance.print(printer); println()
            print("genericParamCount = "); genericParamCount.print(printer); println()
            print("parametersTypes = "); parametersTypes.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:64]
 */
class IlStructTypeDto (
    asmName: String,
    moduleToken: Int,
    typeToken: Int,
    namespaceName: String,
    name: String,
    fullname: String,
    declType: TypeId?,
    baseType: TypeId?,
    interfaces: List<TypeId>,
    genericArgs: List<TypeId>,
    isGenericParam: Boolean,
    isGenericDefinition: Boolean,
    isValueType: Boolean,
    isManaged: Boolean,
    attrs: List<IlAttrDto>,
    fields: List<IlFieldDto>,
    methods: List<IlMethodDto>
) : IlValueTypeDto (
    asmName,
    moduleToken,
    typeToken,
    namespaceName,
    name,
    fullname,
    declType,
    baseType,
    interfaces,
    genericArgs,
    isGenericParam,
    isGenericDefinition,
    isValueType,
    isManaged,
    attrs,
    fields,
    methods
) {
    //companion
    
    companion object : IMarshaller<IlStructTypeDto> {
        override val _type: KClass<IlStructTypeDto> = IlStructTypeDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlStructTypeDto  {
            val asmName = buffer.readString()
            val moduleToken = buffer.readInt()
            val typeToken = buffer.readInt()
            val namespaceName = buffer.readString()
            val name = buffer.readString()
            val fullname = buffer.readString()
            val declType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val baseType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val interfaces = buffer.readList { TypeId.read(ctx, buffer) }
            val genericArgs = buffer.readList { TypeId.read(ctx, buffer) }
            val isGenericParam = buffer.readBool()
            val isGenericDefinition = buffer.readBool()
            val isValueType = buffer.readBool()
            val isManaged = buffer.readBool()
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            val fields = buffer.readList { IlFieldDto.read(ctx, buffer) }
            val methods = buffer.readList { IlMethodDto.read(ctx, buffer) }
            return IlStructTypeDto(asmName, moduleToken, typeToken, namespaceName, name, fullname, declType, baseType, interfaces, genericArgs, isGenericParam, isGenericDefinition, isValueType, isManaged, attrs, fields, methods)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlStructTypeDto)  {
            buffer.writeString(value.asmName)
            buffer.writeInt(value.moduleToken)
            buffer.writeInt(value.typeToken)
            buffer.writeString(value.namespaceName)
            buffer.writeString(value.name)
            buffer.writeString(value.fullname)
            buffer.writeNullable(value.declType) { TypeId.write(ctx, buffer, it) }
            buffer.writeNullable(value.baseType) { TypeId.write(ctx, buffer, it) }
            buffer.writeList(value.interfaces) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeList(value.genericArgs) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeBool(value.isGenericParam)
            buffer.writeBool(value.isGenericDefinition)
            buffer.writeBool(value.isValueType)
            buffer.writeBool(value.isManaged)
            buffer.writeList(value.attrs) { v -> IlAttrDto.write(ctx, buffer, v) }
            buffer.writeList(value.fields) { v -> IlFieldDto.write(ctx, buffer, v) }
            buffer.writeList(value.methods) { v -> IlMethodDto.write(ctx, buffer, v) }
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
        
        other as IlStructTypeDto
        
        if (asmName != other.asmName) return false
        if (moduleToken != other.moduleToken) return false
        if (typeToken != other.typeToken) return false
        if (namespaceName != other.namespaceName) return false
        if (name != other.name) return false
        if (fullname != other.fullname) return false
        if (declType != other.declType) return false
        if (baseType != other.baseType) return false
        if (interfaces != other.interfaces) return false
        if (genericArgs != other.genericArgs) return false
        if (isGenericParam != other.isGenericParam) return false
        if (isGenericDefinition != other.isGenericDefinition) return false
        if (isValueType != other.isValueType) return false
        if (isManaged != other.isManaged) return false
        if (attrs != other.attrs) return false
        if (fields != other.fields) return false
        if (methods != other.methods) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + asmName.hashCode()
        __r = __r*31 + moduleToken.hashCode()
        __r = __r*31 + typeToken.hashCode()
        __r = __r*31 + namespaceName.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + fullname.hashCode()
        __r = __r*31 + if (declType != null) declType.hashCode() else 0
        __r = __r*31 + if (baseType != null) baseType.hashCode() else 0
        __r = __r*31 + interfaces.hashCode()
        __r = __r*31 + genericArgs.hashCode()
        __r = __r*31 + isGenericParam.hashCode()
        __r = __r*31 + isGenericDefinition.hashCode()
        __r = __r*31 + isValueType.hashCode()
        __r = __r*31 + isManaged.hashCode()
        __r = __r*31 + attrs.hashCode()
        __r = __r*31 + fields.hashCode()
        __r = __r*31 + methods.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlStructTypeDto (")
        printer.indent {
            print("asmName = "); asmName.print(printer); println()
            print("moduleToken = "); moduleToken.print(printer); println()
            print("typeToken = "); typeToken.print(printer); println()
            print("namespaceName = "); namespaceName.print(printer); println()
            print("name = "); name.print(printer); println()
            print("fullname = "); fullname.print(printer); println()
            print("declType = "); declType.print(printer); println()
            print("baseType = "); baseType.print(printer); println()
            print("interfaces = "); interfaces.print(printer); println()
            print("genericArgs = "); genericArgs.print(printer); println()
            print("isGenericParam = "); isGenericParam.print(printer); println()
            print("isGenericDefinition = "); isGenericDefinition.print(printer); println()
            print("isValueType = "); isValueType.print(printer); println()
            print("isManaged = "); isManaged.print(printer); println()
            print("attrs = "); attrs.print(printer); println()
            print("fields = "); fields.print(printer); println()
            print("methods = "); methods.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:104]
 */
class IlTempVarDto (
    type: TypeId,
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
            val type = TypeId.read(ctx, buffer)
            val index = buffer.readInt()
            return IlTempVarDto(type, index)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlTempVarDto)  {
            TypeId.write(ctx, buffer, value.type)
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
 * #### Generated from [IlModel.kt:33]
 */
abstract class IlTypeDto (
    val asmName: String,
    val moduleToken: Int,
    val typeToken: Int,
    val namespaceName: String,
    val name: String,
    val fullname: String,
    val declType: TypeId?,
    val baseType: TypeId?,
    val interfaces: List<TypeId>,
    val genericArgs: List<TypeId>,
    val isGenericParam: Boolean,
    val isGenericDefinition: Boolean,
    val isValueType: Boolean,
    val isManaged: Boolean,
    val attrs: List<IlAttrDto>,
    val fields: List<IlFieldDto>,
    val methods: List<IlMethodDto>
) : IlDto (
) {
    //companion
    
    companion object : IAbstractDeclaration<IlTypeDto> {
        override fun readUnknownInstance(ctx: SerializationCtx, buffer: AbstractBuffer, unknownId: RdId, size: Int): IlTypeDto  {
            val objectStartPosition = buffer.position
            val asmName = buffer.readString()
            val moduleToken = buffer.readInt()
            val typeToken = buffer.readInt()
            val namespaceName = buffer.readString()
            val name = buffer.readString()
            val fullname = buffer.readString()
            val declType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val baseType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val interfaces = buffer.readList { TypeId.read(ctx, buffer) }
            val genericArgs = buffer.readList { TypeId.read(ctx, buffer) }
            val isGenericParam = buffer.readBool()
            val isGenericDefinition = buffer.readBool()
            val isValueType = buffer.readBool()
            val isManaged = buffer.readBool()
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            val fields = buffer.readList { IlFieldDto.read(ctx, buffer) }
            val methods = buffer.readList { IlMethodDto.read(ctx, buffer) }
            val unknownBytes = ByteArray(objectStartPosition + size - buffer.position)
            buffer.readByteArrayRaw(unknownBytes)
            return IlTypeDto_Unknown(asmName, moduleToken, typeToken, namespaceName, name, fullname, declType, baseType, interfaces, genericArgs, isGenericParam, isGenericDefinition, isValueType, isManaged, attrs, fields, methods, unknownId, unknownBytes)
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


class IlTypeDto_Unknown (
    asmName: String,
    moduleToken: Int,
    typeToken: Int,
    namespaceName: String,
    name: String,
    fullname: String,
    declType: TypeId?,
    baseType: TypeId?,
    interfaces: List<TypeId>,
    genericArgs: List<TypeId>,
    isGenericParam: Boolean,
    isGenericDefinition: Boolean,
    isValueType: Boolean,
    isManaged: Boolean,
    attrs: List<IlAttrDto>,
    fields: List<IlFieldDto>,
    methods: List<IlMethodDto>,
    override val unknownId: RdId,
    val unknownBytes: ByteArray
) : IlTypeDto (
    asmName,
    moduleToken,
    typeToken,
    namespaceName,
    name,
    fullname,
    declType,
    baseType,
    interfaces,
    genericArgs,
    isGenericParam,
    isGenericDefinition,
    isValueType,
    isManaged,
    attrs,
    fields,
    methods
), IUnknownInstance {
    //companion
    
    companion object : IMarshaller<IlTypeDto_Unknown> {
        override val _type: KClass<IlTypeDto_Unknown> = IlTypeDto_Unknown::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlTypeDto_Unknown  {
            throw NotImplementedError("Unknown instances should not be read via serializer")
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlTypeDto_Unknown)  {
            buffer.writeString(value.asmName)
            buffer.writeInt(value.moduleToken)
            buffer.writeInt(value.typeToken)
            buffer.writeString(value.namespaceName)
            buffer.writeString(value.name)
            buffer.writeString(value.fullname)
            buffer.writeNullable(value.declType) { TypeId.write(ctx, buffer, it) }
            buffer.writeNullable(value.baseType) { TypeId.write(ctx, buffer, it) }
            buffer.writeList(value.interfaces) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeList(value.genericArgs) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeBool(value.isGenericParam)
            buffer.writeBool(value.isGenericDefinition)
            buffer.writeBool(value.isValueType)
            buffer.writeBool(value.isManaged)
            buffer.writeList(value.attrs) { v -> IlAttrDto.write(ctx, buffer, v) }
            buffer.writeList(value.fields) { v -> IlFieldDto.write(ctx, buffer, v) }
            buffer.writeList(value.methods) { v -> IlMethodDto.write(ctx, buffer, v) }
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
        
        other as IlTypeDto_Unknown
        
        if (asmName != other.asmName) return false
        if (moduleToken != other.moduleToken) return false
        if (typeToken != other.typeToken) return false
        if (namespaceName != other.namespaceName) return false
        if (name != other.name) return false
        if (fullname != other.fullname) return false
        if (declType != other.declType) return false
        if (baseType != other.baseType) return false
        if (interfaces != other.interfaces) return false
        if (genericArgs != other.genericArgs) return false
        if (isGenericParam != other.isGenericParam) return false
        if (isGenericDefinition != other.isGenericDefinition) return false
        if (isValueType != other.isValueType) return false
        if (isManaged != other.isManaged) return false
        if (attrs != other.attrs) return false
        if (fields != other.fields) return false
        if (methods != other.methods) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + asmName.hashCode()
        __r = __r*31 + moduleToken.hashCode()
        __r = __r*31 + typeToken.hashCode()
        __r = __r*31 + namespaceName.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + fullname.hashCode()
        __r = __r*31 + if (declType != null) declType.hashCode() else 0
        __r = __r*31 + if (baseType != null) baseType.hashCode() else 0
        __r = __r*31 + interfaces.hashCode()
        __r = __r*31 + genericArgs.hashCode()
        __r = __r*31 + isGenericParam.hashCode()
        __r = __r*31 + isGenericDefinition.hashCode()
        __r = __r*31 + isValueType.hashCode()
        __r = __r*31 + isManaged.hashCode()
        __r = __r*31 + attrs.hashCode()
        __r = __r*31 + fields.hashCode()
        __r = __r*31 + methods.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlTypeDto_Unknown (")
        printer.indent {
            print("asmName = "); asmName.print(printer); println()
            print("moduleToken = "); moduleToken.print(printer); println()
            print("typeToken = "); typeToken.print(printer); println()
            print("namespaceName = "); namespaceName.print(printer); println()
            print("name = "); name.print(printer); println()
            print("fullname = "); fullname.print(printer); println()
            print("declType = "); declType.print(printer); println()
            print("baseType = "); baseType.print(printer); println()
            print("interfaces = "); interfaces.print(printer); println()
            print("genericArgs = "); genericArgs.print(printer); println()
            print("isGenericParam = "); isGenericParam.print(printer); println()
            print("isGenericDefinition = "); isGenericDefinition.print(printer); println()
            print("isValueType = "); isValueType.print(printer); println()
            print("isManaged = "); isManaged.print(printer); println()
            print("attrs = "); attrs.print(printer); println()
            print("fields = "); fields.print(printer); println()
            print("methods = "); methods.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:56]
 */
abstract class IlValueTypeDto (
    asmName: String,
    moduleToken: Int,
    typeToken: Int,
    namespaceName: String,
    name: String,
    fullname: String,
    declType: TypeId?,
    baseType: TypeId?,
    interfaces: List<TypeId>,
    genericArgs: List<TypeId>,
    isGenericParam: Boolean,
    isGenericDefinition: Boolean,
    isValueType: Boolean,
    isManaged: Boolean,
    attrs: List<IlAttrDto>,
    fields: List<IlFieldDto>,
    methods: List<IlMethodDto>
) : IlTypeDto (
    asmName,
    moduleToken,
    typeToken,
    namespaceName,
    name,
    fullname,
    declType,
    baseType,
    interfaces,
    genericArgs,
    isGenericParam,
    isGenericDefinition,
    isValueType,
    isManaged,
    attrs,
    fields,
    methods
) {
    //companion
    
    companion object : IAbstractDeclaration<IlValueTypeDto> {
        override fun readUnknownInstance(ctx: SerializationCtx, buffer: AbstractBuffer, unknownId: RdId, size: Int): IlValueTypeDto  {
            val objectStartPosition = buffer.position
            val asmName = buffer.readString()
            val moduleToken = buffer.readInt()
            val typeToken = buffer.readInt()
            val namespaceName = buffer.readString()
            val name = buffer.readString()
            val fullname = buffer.readString()
            val declType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val baseType = buffer.readNullable { TypeId.read(ctx, buffer) }
            val interfaces = buffer.readList { TypeId.read(ctx, buffer) }
            val genericArgs = buffer.readList { TypeId.read(ctx, buffer) }
            val isGenericParam = buffer.readBool()
            val isGenericDefinition = buffer.readBool()
            val isValueType = buffer.readBool()
            val isManaged = buffer.readBool()
            val attrs = buffer.readList { IlAttrDto.read(ctx, buffer) }
            val fields = buffer.readList { IlFieldDto.read(ctx, buffer) }
            val methods = buffer.readList { IlMethodDto.read(ctx, buffer) }
            val unknownBytes = ByteArray(objectStartPosition + size - buffer.position)
            buffer.readByteArrayRaw(unknownBytes)
            return IlValueTypeDto_Unknown(asmName, moduleToken, typeToken, namespaceName, name, fullname, declType, baseType, interfaces, genericArgs, isGenericParam, isGenericDefinition, isValueType, isManaged, attrs, fields, methods, unknownId, unknownBytes)
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


class IlValueTypeDto_Unknown (
    asmName: String,
    moduleToken: Int,
    typeToken: Int,
    namespaceName: String,
    name: String,
    fullname: String,
    declType: TypeId?,
    baseType: TypeId?,
    interfaces: List<TypeId>,
    genericArgs: List<TypeId>,
    isGenericParam: Boolean,
    isGenericDefinition: Boolean,
    isValueType: Boolean,
    isManaged: Boolean,
    attrs: List<IlAttrDto>,
    fields: List<IlFieldDto>,
    methods: List<IlMethodDto>,
    override val unknownId: RdId,
    val unknownBytes: ByteArray
) : IlValueTypeDto (
    asmName,
    moduleToken,
    typeToken,
    namespaceName,
    name,
    fullname,
    declType,
    baseType,
    interfaces,
    genericArgs,
    isGenericParam,
    isGenericDefinition,
    isValueType,
    isManaged,
    attrs,
    fields,
    methods
), IUnknownInstance {
    //companion
    
    companion object : IMarshaller<IlValueTypeDto_Unknown> {
        override val _type: KClass<IlValueTypeDto_Unknown> = IlValueTypeDto_Unknown::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlValueTypeDto_Unknown  {
            throw NotImplementedError("Unknown instances should not be read via serializer")
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlValueTypeDto_Unknown)  {
            buffer.writeString(value.asmName)
            buffer.writeInt(value.moduleToken)
            buffer.writeInt(value.typeToken)
            buffer.writeString(value.namespaceName)
            buffer.writeString(value.name)
            buffer.writeString(value.fullname)
            buffer.writeNullable(value.declType) { TypeId.write(ctx, buffer, it) }
            buffer.writeNullable(value.baseType) { TypeId.write(ctx, buffer, it) }
            buffer.writeList(value.interfaces) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeList(value.genericArgs) { v -> TypeId.write(ctx, buffer, v) }
            buffer.writeBool(value.isGenericParam)
            buffer.writeBool(value.isGenericDefinition)
            buffer.writeBool(value.isValueType)
            buffer.writeBool(value.isManaged)
            buffer.writeList(value.attrs) { v -> IlAttrDto.write(ctx, buffer, v) }
            buffer.writeList(value.fields) { v -> IlFieldDto.write(ctx, buffer, v) }
            buffer.writeList(value.methods) { v -> IlMethodDto.write(ctx, buffer, v) }
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
        
        other as IlValueTypeDto_Unknown
        
        if (asmName != other.asmName) return false
        if (moduleToken != other.moduleToken) return false
        if (typeToken != other.typeToken) return false
        if (namespaceName != other.namespaceName) return false
        if (name != other.name) return false
        if (fullname != other.fullname) return false
        if (declType != other.declType) return false
        if (baseType != other.baseType) return false
        if (interfaces != other.interfaces) return false
        if (genericArgs != other.genericArgs) return false
        if (isGenericParam != other.isGenericParam) return false
        if (isGenericDefinition != other.isGenericDefinition) return false
        if (isValueType != other.isValueType) return false
        if (isManaged != other.isManaged) return false
        if (attrs != other.attrs) return false
        if (fields != other.fields) return false
        if (methods != other.methods) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + asmName.hashCode()
        __r = __r*31 + moduleToken.hashCode()
        __r = __r*31 + typeToken.hashCode()
        __r = __r*31 + namespaceName.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + fullname.hashCode()
        __r = __r*31 + if (declType != null) declType.hashCode() else 0
        __r = __r*31 + if (baseType != null) baseType.hashCode() else 0
        __r = __r*31 + interfaces.hashCode()
        __r = __r*31 + genericArgs.hashCode()
        __r = __r*31 + isGenericParam.hashCode()
        __r = __r*31 + isGenericDefinition.hashCode()
        __r = __r*31 + isValueType.hashCode()
        __r = __r*31 + isManaged.hashCode()
        __r = __r*31 + attrs.hashCode()
        __r = __r*31 + fields.hashCode()
        __r = __r*31 + methods.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlValueTypeDto_Unknown (")
        printer.indent {
            print("asmName = "); asmName.print(printer); println()
            print("moduleToken = "); moduleToken.print(printer); println()
            print("typeToken = "); typeToken.print(printer); println()
            print("namespaceName = "); namespaceName.print(printer); println()
            print("name = "); name.print(printer); println()
            print("fullname = "); fullname.print(printer); println()
            print("declType = "); declType.print(printer); println()
            print("baseType = "); baseType.print(printer); println()
            print("interfaces = "); interfaces.print(printer); println()
            print("genericArgs = "); genericArgs.print(printer); println()
            print("isGenericParam = "); isGenericParam.print(printer); println()
            print("isGenericDefinition = "); isGenericDefinition.print(printer); println()
            print("isValueType = "); isValueType.print(printer); println()
            print("isManaged = "); isManaged.print(printer); println()
            print("attrs = "); attrs.print(printer); println()
            print("fields = "); fields.print(printer); println()
            print("methods = "); methods.print(printer); println()
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
abstract class IlVarDto (
    val type: TypeId,
    val index: Int
) : IlDto (
) {
    //companion
    
    companion object : IAbstractDeclaration<IlVarDto> {
        override fun readUnknownInstance(ctx: SerializationCtx, buffer: AbstractBuffer, unknownId: RdId, size: Int): IlVarDto  {
            val objectStartPosition = buffer.position
            val type = TypeId.read(ctx, buffer)
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
    type: TypeId,
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
            TypeId.write(ctx, buffer, value.type)
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


/**
 * #### Generated from [IlModel.kt:28]
 */
class TypeId (
    val typeArgs: List<TypeIdBase>,
    asmName: String,
    typeName: String
) : TypeIdBase (
    asmName,
    typeName
) {
    //companion
    
    companion object : IMarshaller<TypeId> {
        override val _type: KClass<TypeId> = TypeId::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): TypeId  {
            val asmName = buffer.readString()
            val typeName = buffer.readString()
            val typeArgs = buffer.readList { ctx.serializers.readPolymorphic<TypeIdBase>(ctx, buffer, TypeIdBase) }
            return TypeId(typeArgs, asmName, typeName)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: TypeId)  {
            buffer.writeString(value.asmName)
            buffer.writeString(value.typeName)
            buffer.writeList(value.typeArgs) { v -> ctx.serializers.writePolymorphic(ctx, buffer, v) }
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
        
        other as TypeId
        
        if (typeArgs != other.typeArgs) return false
        if (asmName != other.asmName) return false
        if (typeName != other.typeName) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + typeArgs.hashCode()
        __r = __r*31 + asmName.hashCode()
        __r = __r*31 + typeName.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("TypeId (")
        printer.indent {
            print("typeArgs = "); typeArgs.print(printer); println()
            print("asmName = "); asmName.print(printer); println()
            print("typeName = "); typeName.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlModel.kt:24]
 */
abstract class TypeIdBase (
    val asmName: String,
    val typeName: String
) : IPrintable {
    //companion
    
    companion object : IAbstractDeclaration<TypeIdBase> {
        override fun readUnknownInstance(ctx: SerializationCtx, buffer: AbstractBuffer, unknownId: RdId, size: Int): TypeIdBase  {
            val objectStartPosition = buffer.position
            val asmName = buffer.readString()
            val typeName = buffer.readString()
            val unknownBytes = ByteArray(objectStartPosition + size - buffer.position)
            buffer.readByteArrayRaw(unknownBytes)
            return TypeIdBase_Unknown(asmName, typeName, unknownId, unknownBytes)
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


class TypeIdBase_Unknown (
    asmName: String,
    typeName: String,
    override val unknownId: RdId,
    val unknownBytes: ByteArray
) : TypeIdBase (
    asmName,
    typeName
), IUnknownInstance {
    //companion
    
    companion object : IMarshaller<TypeIdBase_Unknown> {
        override val _type: KClass<TypeIdBase_Unknown> = TypeIdBase_Unknown::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): TypeIdBase_Unknown  {
            throw NotImplementedError("Unknown instances should not be read via serializer")
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: TypeIdBase_Unknown)  {
            buffer.writeString(value.asmName)
            buffer.writeString(value.typeName)
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
        
        other as TypeIdBase_Unknown
        
        if (asmName != other.asmName) return false
        if (typeName != other.typeName) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + asmName.hashCode()
        __r = __r*31 + typeName.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("TypeIdBase_Unknown (")
        printer.indent {
            print("asmName = "); asmName.print(printer); println()
            print("typeName = "); typeName.print(printer); println()
        }
        printer.print(")")
    }
    
    override fun toString() = PrettyPrinter().singleLine().also { print(it) }.toString()
    //deepClone
    //contexts
}
