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

import org.jacodb.api.net.ILDBContext
import org.jacodb.api.net.IlDatabasePersistence
import org.jacodb.api.net.generated.models.IlTypeDto
import org.jacodb.api.net.generated.models.TypeId
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListMap
import java.util.concurrent.atomic.AtomicLong

interface IlDbInstanceInterner<Value, Persistence, Context> {
    fun setup(persistence: Persistence)
    fun close()
    fun findValue(id: Long): Value
    fun findValueOrNull(id: Long): Value?
    fun findIdOrNew(instance: Value): Long

    fun flush(ctx: Context)
}

private const val defaultMapName = "org.jacodb.impl.Values"

// TODO #3 maybe introduce separate defn for this type (not impl)
class IlDbTypeIdInternerImpl(
    private val kvValueMapName: String = defaultMapName,
    private val symbolInterner: IlDbSymbolInterner
) :
    IlDbInstanceInterner<TypeId, IlDatabasePersistence, ILDBContext> {
    private val idGen = AtomicLong()
    private val valueCache = ConcurrentHashMap<TypeIdCompressed, Long>()
    private val idCache = ConcurrentHashMap<Long, TypeIdCompressed>()
    private val newElements = ConcurrentSkipListMap<TypeIdCompressed, Long>()
    override fun setup(persistence: IlDatabasePersistence) {
        // TODO
    }

    override fun close() {
        valueCache.clear()
        newElements.clear()
    }

    override fun findValue(id: Long): TypeId = idCache[id]!!.decompressed()

    override fun findValueOrNull(id: Long): TypeId? = idCache[id]?.decompressed()

    private data class TypeIdCompressed(val asmName: Long, val typeName: Long, val typeArgs: List<Long?>) :
        Comparable<TypeIdCompressed> {
        private fun compLongs(pair: Pair<Long?, Long?>): Int =
            when {
                pair.second == null -> -1
                pair.first == null -> 1
                else -> (pair.second!! - pair.first!!).toInt()
            }


        override fun compareTo(other: TypeIdCompressed): Int {
            if (asmName != other.asmName) return (asmName - other.asmName).toInt()
            if (typeName != other.typeName) return (typeName - other.typeName).toInt()
            if (typeArgs.size != other.typeArgs.size) return typeArgs.size - other.typeArgs.size
            return typeArgs.zip(other.typeArgs).firstOrNull { pair -> compLongs(pair) != 0 }
                ?.let { pair -> compLongs(pair) } ?: 0
        }

        override fun equals(other: Any?): Boolean {
            if (other == null || other !is TypeIdCompressed) return false;
            return other.asmName == asmName && other.typeName == typeName && other.typeArgs.zip(typeArgs)
                .firstOrNull { it.first != it.second } != (null ?: true);
        }

        override fun hashCode(): Int {
            var result = asmName.hashCode()
            result = 31 * result + typeName.hashCode()
            result = 31 * result + typeArgs.hashCode()
            return result
        }

    }

    private fun TypeId.compressed(): TypeIdCompressed = TypeIdCompressed(
        symbolInterner.findSymbolIdOrNew(asmName),
        symbolInterner.findSymbolIdOrNew(typeName),
        typeArgs.map { (it as TypeId).interned(this@IlDbTypeIdInternerImpl) }
    )

    private fun TypeIdCompressed.decompressed(): TypeId = TypeId(
        asmName = symbolInterner.findSymbol(asmName),
        typeName = symbolInterner.findSymbol(typeName),
        typeArgs = typeArgs.map { findValue(it!!) }
    )

    override fun findIdOrNew(value: TypeId): Long =
        valueCache.computeIfAbsent(value.compressed()) { typeCompressed ->
            idGen.incrementAndGet().also {
                newElements[typeCompressed] = it
                idCache[it] = typeCompressed
            }
        }

    fun createIdWithPseudonym(value: TypeId, pseudonym: TypeId): Long {
        val id = findIdOrNew(value)
        return valueCache.computeIfAbsent(pseudonym.compressed()) { t ->
            id.also {
                newElements[t] = it
            }
        }
    }

    override fun flush(ctx: ILDBContext) {
//        val entries = newElements.entries.toList()
//        if (entries.isEmpty()) return
//        val txn = ctx.txn
//        val unwrapped = txn.unwrap
//        if (unwrapped is KVErsTransaction) {
//            val kvTxn = unwrapped.kvTxn
//            val symbolsMap = kvTxn.getNamedMap(kvValueMapName, create = true)!!
//            val stringBinding = BuiltInBindingProvider.getBinding(String::class.java)
//            val longBinding = BuiltInBindingProvider.getBinding(Long::class.java)
//            // TODO
////            entries.forEach { (typeName, id) ->
////                kvTxn.put(symbolsMap, longBinding.getBytesCompressed(id), stringBinding.getBytes(typeName))
////            }
//        } else {
//            entries.forEach { (typeName, id) ->
//                txn.newEntity("Symbol").also { symbol ->
//                    symbol["typeName"] = typeName
//                    symbol["id"] = id
//                }
//            }
//        }
//        entries.forEach {
//            newElements.remove(it.key)
//        }
    }
}

fun TypeId.interned(interner: IlDbInstanceInterner<TypeId, IlDatabasePersistence, ILDBContext>): Long =
    interner.findIdOrNew(this)

fun Long.asTypeId(interner: IlDbInstanceInterner<TypeId, IlDatabasePersistence, ILDBContext>): TypeId =
    interner.findValue(this)

fun IlTypeDto.id() = TypeId(asmName = asmName, typeName = fullname, typeArgs = genericArgs)