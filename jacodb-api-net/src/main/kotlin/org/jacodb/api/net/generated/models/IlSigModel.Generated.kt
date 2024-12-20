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
 * #### Generated from [IlSigModel.kt:23]
 */
class IlSigModel private constructor(
    private val _publication: RdCall<PublicationRequest, PublicationResponse>,
    private val _callForAsm: RdCall<PublicationRequest, List<IlTypeDto>>,
    private val _asmRequest: RdSignal<PublicationRequest>,
    private val _asmResponse: RdSignal<List<IlDto>>
) : RdExtBase() {
    //companion
    
    companion object : ISerializersOwner {
        
        override fun registerSerializersCore(serializers: ISerializers)  {
            serializers.register(PublicationRequest)
            serializers.register(IlAsmDto)
            serializers.register(PublicationResponse)
        }
        
        
        
        
        private val __IlTypeDtoListSerializer = AbstractPolymorphic(IlTypeDto).list()
        private val __IlDtoListSerializer = AbstractPolymorphic(IlDto).list()
        
        const val serializationHash = 7950792047261741369L
        
    }
    override val serializersOwner: ISerializersOwner get() = IlSigModel
    override val serializationHash: Long get() = IlSigModel.serializationHash
    
    //fields
    val publication: IRdCall<PublicationRequest, PublicationResponse> get() = _publication
    val callForAsm: IRdCall<PublicationRequest, List<IlTypeDto>> get() = _callForAsm
    val asmRequest: IAsyncSignal<PublicationRequest> get() = _asmRequest
    val asmResponse: IAsyncSignal<List<IlDto>> get() = _asmResponse
    //methods
    //initializer
    init {
        _asmRequest.async = true
        _asmResponse.async = true
    }
    
    init {
        bindableChildren.add("publication" to _publication)
        bindableChildren.add("callForAsm" to _callForAsm)
        bindableChildren.add("asmRequest" to _asmRequest)
        bindableChildren.add("asmResponse" to _asmResponse)
    }
    
    //secondary constructor
    internal constructor(
    ) : this(
        RdCall<PublicationRequest, PublicationResponse>(PublicationRequest, PublicationResponse),
        RdCall<PublicationRequest, List<IlTypeDto>>(PublicationRequest, __IlTypeDtoListSerializer),
        RdSignal<PublicationRequest>(PublicationRequest),
        RdSignal<List<IlDto>>(__IlDtoListSerializer)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlSigModel (")
        printer.indent {
            print("publication = "); _publication.print(printer); println()
            print("callForAsm = "); _callForAsm.print(printer); println()
            print("asmRequest = "); _asmRequest.print(printer); println()
            print("asmResponse = "); _asmResponse.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): IlSigModel   {
        return IlSigModel(
            _publication.deepClonePolymorphic(),
            _callForAsm.deepClonePolymorphic(),
            _asmRequest.deepClonePolymorphic(),
            _asmResponse.deepClonePolymorphic()
        )
    }
    //contexts
}
val IlModel.ilSigModel get() = getOrCreateExtension("ilSigModel", ::IlSigModel)



/**
 * #### Generated from [IlSigModel.kt:27]
 */
data class IlAsmDto (
    val name: String,
    val location: String
) : IPrintable {
    //companion
    
    companion object : IMarshaller<IlAsmDto> {
        override val _type: KClass<IlAsmDto> = IlAsmDto::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): IlAsmDto  {
            val name = buffer.readString()
            val location = buffer.readString()
            return IlAsmDto(name, location)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: IlAsmDto)  {
            buffer.writeString(value.name)
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
        
        if (name != other.name) return false
        if (location != other.location) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + name.hashCode()
        __r = __r*31 + location.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlAsmDto (")
        printer.indent {
            print("name = "); name.print(printer); println()
            print("location = "); location.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlSigModel.kt:24]
 */
data class PublicationRequest (
    val rootAsms: List<String>
) : IPrintable {
    //companion
    
    companion object : IMarshaller<PublicationRequest> {
        override val _type: KClass<PublicationRequest> = PublicationRequest::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): PublicationRequest  {
            val rootAsms = buffer.readList { buffer.readString() }
            return PublicationRequest(rootAsms)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: PublicationRequest)  {
            buffer.writeList(value.rootAsms) { v -> buffer.writeString(v) }
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
        
        other as PublicationRequest
        
        if (rootAsms != other.rootAsms) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + rootAsms.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("PublicationRequest (")
        printer.indent {
            print("rootAsms = "); rootAsms.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [IlSigModel.kt:31]
 */
data class PublicationResponse (
    val reachableAsms: List<IlAsmDto>,
    val referencedAsms: List<List<IlAsmDto>>,
    val reachableTypes: List<IlTypeDto>
) : IPrintable {
    //companion
    
    companion object : IMarshaller<PublicationResponse> {
        override val _type: KClass<PublicationResponse> = PublicationResponse::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): PublicationResponse  {
            val reachableAsms = buffer.readList { IlAsmDto.read(ctx, buffer) }
            val referencedAsms = buffer.readList { buffer.readList { IlAsmDto.read(ctx, buffer) } }
            val reachableTypes = buffer.readList { ctx.serializers.readPolymorphic<IlTypeDto>(ctx, buffer, IlTypeDto) }
            return PublicationResponse(reachableAsms, referencedAsms, reachableTypes)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: PublicationResponse)  {
            buffer.writeList(value.reachableAsms) { v -> IlAsmDto.write(ctx, buffer, v) }
            buffer.writeList(value.referencedAsms) { v -> buffer.writeList(v) { v -> IlAsmDto.write(ctx, buffer, v) } }
            buffer.writeList(value.reachableTypes) { v -> ctx.serializers.writePolymorphic(ctx, buffer, v) }
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
        
        other as PublicationResponse
        
        if (reachableAsms != other.reachableAsms) return false
        if (referencedAsms != other.referencedAsms) return false
        if (reachableTypes != other.reachableTypes) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + reachableAsms.hashCode()
        __r = __r*31 + referencedAsms.hashCode()
        __r = __r*31 + reachableTypes.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("PublicationResponse (")
        printer.indent {
            print("reachableAsms = "); reachableAsms.print(printer); println()
            print("referencedAsms = "); referencedAsms.print(printer); println()
            print("reachableTypes = "); reachableTypes.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}
