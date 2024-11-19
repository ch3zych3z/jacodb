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
    private val _callForAsm: RdCall<Request, List<IlTypeDto>>,
    private val _asmRequest: RdSignal<Request>,
    private val _asmResponse: RdSignal<List<IlDto>>
) : RdExtBase() {
    //companion
    
    companion object : ISerializersOwner {
        
        override fun registerSerializersCore(serializers: ISerializers)  {
            serializers.register(Request)
        }
        
        
        
        
        private val __IlTypeDtoListSerializer = AbstractPolymorphic(IlTypeDto).list()
        private val __IlDtoListSerializer = AbstractPolymorphic(IlDto).list()
        
        const val serializationHash = 9130847229297571826L
        
    }
    override val serializersOwner: ISerializersOwner get() = IlSigModel
    override val serializationHash: Long get() = IlSigModel.serializationHash
    
    //fields
    val callForAsm: IRdCall<Request, List<IlTypeDto>> get() = _callForAsm
    val asmRequest: IAsyncSignal<Request> get() = _asmRequest
    val asmResponse: IAsyncSignal<List<IlDto>> get() = _asmResponse
    //methods
    //initializer
    init {
        _asmRequest.async = true
        _asmResponse.async = true
    }
    
    init {
        bindableChildren.add("callForAsm" to _callForAsm)
        bindableChildren.add("asmRequest" to _asmRequest)
        bindableChildren.add("asmResponse" to _asmResponse)
    }
    
    //secondary constructor
    internal constructor(
    ) : this(
        RdCall<Request, List<IlTypeDto>>(Request, __IlTypeDtoListSerializer),
        RdSignal<Request>(Request),
        RdSignal<List<IlDto>>(__IlDtoListSerializer)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("IlSigModel (")
        printer.indent {
            print("callForAsm = "); _callForAsm.print(printer); println()
            print("asmRequest = "); _asmRequest.print(printer); println()
            print("asmResponse = "); _asmResponse.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): IlSigModel   {
        return IlSigModel(
            _callForAsm.deepClonePolymorphic(),
            _asmRequest.deepClonePolymorphic(),
            _asmResponse.deepClonePolymorphic()
        )
    }
    //contexts
}
val IlModel.ilSigModel get() = getOrCreateExtension("ilSigModel", ::IlSigModel)



/**
 * #### Generated from [IlSigModel.kt:24]
 */
data class Request (
    val rootAsm: String
) : IPrintable {
    //companion
    
    companion object : IMarshaller<Request> {
        override val _type: KClass<Request> = Request::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): Request  {
            val rootAsm = buffer.readString()
            return Request(rootAsm)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: Request)  {
            buffer.writeString(value.rootAsm)
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
        
        other as Request
        
        if (rootAsm != other.rootAsm) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + rootAsm.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("Request (")
        printer.indent {
            print("rootAsm = "); rootAsm.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}
