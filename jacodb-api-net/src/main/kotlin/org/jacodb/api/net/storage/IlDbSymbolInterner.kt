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
import org.jacodb.api.storage.kv.forEach
import org.jacodb.impl.storage.ers.BuiltInBindingProvider
import org.jacodb.impl.storage.ers.decorators.unwrap
import org.jacodb.impl.storage.ers.kv.KVErsTransaction
import java.io.Closeable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListMap
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.max


interface IlDbSymbolInterner {
    fun findSymbol(id: Long): String
    fun findSymbolOrNull(id: Long): String?
    fun findSymbolIdOrNew(symbol: String): Long

    fun flush(ctx: ILDBContext)
}

private const val symbolsMapName = "org.jacodb.impl.Symbols"

class ILDBSymbolsInternerImpl : IlDbSymbolInterner, Closeable {

    private val symbolsIdGen = AtomicLong()
    private val symbolsCache = ConcurrentHashMap<String, Long>()
    private val idCache = ConcurrentHashMap<Long, String>()
    private val newElements = ConcurrentSkipListMap<String, Long>()

    fun setup(persistence: IlDatabasePersistence) = persistence.read { context ->
        val txn = context.txn
        var maxId = -1L
        val unwrapped = txn.unwrap
        if (unwrapped is KVErsTransaction) {
            val kvTxn = unwrapped.kvTxn
            val stringBinding = BuiltInBindingProvider.getBinding(String::class.java)
            val longBinding = BuiltInBindingProvider.getBinding(Long::class.java)
            kvTxn.navigateTo(symbolsMapName).forEach { idBytes, nameBytes ->
                val id = longBinding.getObjectCompressed(idBytes)
                val name = stringBinding.getObject(nameBytes)
                symbolsCache[name] = id
                idCache[id] = name
                maxId = max(maxId, id)
            }
        } else {
            val symbols = txn.all("Symbol").toList()
            symbols.forEach { symbol ->
                val name: String? = symbol.getBlob("name")
                val id: Long? = symbol.getCompressedBlob("id")
                if (name != null && id != null) {
                    symbolsCache[name] = id
                    idCache[id] = name
                    maxId = max(maxId, id)
                }
            }
        }
        symbolsIdGen.set(maxId)
    }


    override fun findSymbolIdOrNew(symbol: String): Long {
        return symbolsCache.computeIfAbsent(symbol) {
            symbolsIdGen.incrementAndGet().also {
                newElements[symbol] = it
                idCache[it] = symbol
            }
        }
    }

    override fun findSymbol(id: Long): String = idCache[id]!!

    override fun findSymbolOrNull(id: Long): String? = idCache[id]

    override fun flush(ctx: ILDBContext) {
        val entries = newElements.entries.toList()
        if (entries.isNotEmpty()) {
            val txn = ctx.txn
            val unwrapped = txn.unwrap
            if (unwrapped is KVErsTransaction) {
                val kvTxn = unwrapped.kvTxn
                val symbolsMap = kvTxn.getNamedMap(symbolsMapName, create = true)!!
                val stringBinding = BuiltInBindingProvider.getBinding(String::class.java)
                val longBinding = BuiltInBindingProvider.getBinding(Long::class.java)
                entries.forEach { (name, id) ->
                    kvTxn.put(symbolsMap, longBinding.getBytesCompressed(id), stringBinding.getBytes(name))
                }
            } else {
                entries.forEach { (name, id) ->
                    txn.newEntity("Symbol").also { symbol ->
                        symbol["name"] = name
                        symbol["id"] = id
                    }
                }
            }
        }

        entries.forEach {
            newElements.remove(it.key)
        }
    }

    override fun close() {
        symbolsCache.clear()
        newElements.clear()
    }
}



fun String.asSymbolId(interner: IlDbSymbolInterner) : Long = interner.findSymbolIdOrNew(this)
fun Long.asSymbol(interner: IlDbSymbolInterner) : String = interner.findSymbol(this)
