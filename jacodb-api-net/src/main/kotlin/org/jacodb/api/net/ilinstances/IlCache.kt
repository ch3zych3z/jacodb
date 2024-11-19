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

package org.example.ilinstances


import org.jacodb.api.net.generated.models.*

class CacheKey
class IlAsm
class AsmCacheKey

class IlCache {
//    private val assemblies: MutableMap<AsmCacheKey, IlAsm> = mutableMapOf()
//    private val types: MutableMap<CacheKey, IlType> = mutableMapOf()
//    private val fields: MutableMap<CacheKey, IlField> = mutableMapOf()
//    private val methods: MutableMap<CacheKey, IlMethod> = mutableMapOf()
    fun put(dto: IlDto) {
        throw NotImplementedError()
//        when (dto) {
//            is IlAsmDto -> putAsm(dto)
//            is IlTypeDto -> putType(dto)
//            is IlFieldDto -> putField(dto)
//            is IlMethodDto -> putMethod(dto)
//        }
    }

    fun get(dto: IlDto): IlInstance = throw NotImplementedError()
//        when (dto) {
//            is IlAsmDto -> getAsm(dto.id)
//            is IlTypeDto -> getType(dto.id)
//            is IlFieldDto -> getField(dto.id)
//            is IlMethodDto -> getMethod(dto.id)
//            else -> throw UnsupportedOperationException("unknown dto $dto")
//        }

    fun getAsm(key: AsmCacheKey): IlAsm = throw NotImplementedError()//assemblies[key]!!

    private fun putType(type: IlTypeDto) {
        throw NotImplementedError()
//        types[type.id] = IlType(type)
    }

    fun getType(key: CacheKey): IlType = throw NotImplementedError()//types[key]!!

    private fun putField(field: IlFieldDto) {
        throw NotImplementedError()
//        fields[field.id] = IlField(field)
    }

    fun getField(key: CacheKey): IlField = throw NotImplementedError()//fields[key]!!
    private fun putMethod(method: IlMethodDto) {
        throw NotImplementedError()
//        methods[method.id] = IlMethod(method)
    }

    fun getMethod(key: CacheKey): IlMethod = throw NotImplementedError()//methods[key]!!

    fun printEntriesHierarchy() {
//        assemblies.values.forEach {
//            println(it.location)
//            it.types.forEach {
//                println("${it.name} {")
//                it.fields.forEach {
//                    println("${it.declType.name} ${it.name}")
//                }
//                println()
//                it.methods.forEach {
//                    println("${it.returnType?.name ?: "void" } ${it.name} (${it.parametes.joinToString(", ") { p -> "${p.paramType.name} : ${p.name}" }})")
//                }
//                println("}")
//            }
//        }
    }
}