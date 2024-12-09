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
import org.jacodb.impl.storage.ers.BuiltInBindingProvider
import org.jacodb.impl.storage.ers.decorators.unwrap
import org.jacodb.impl.storage.ers.kv.KVErsTransaction
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

class IlDbTypeInternerImpl<Value>(private val kvValueMapName: String = defaultMapName) :
    IlDbInstanceInterner<Value, IlDatabasePersistence, ILDBContext> {
    private val idGen = AtomicLong()
    private val valueCache = ConcurrentHashMap<Value, Long>()
    private val idCache = ConcurrentHashMap<Long, Value>()
    private val newElements = ConcurrentSkipListMap<Value, Long>()
    override fun setup(persistence: IlDatabasePersistence) {
        // TODO
    }

    override fun close() {
        valueCache.clear()
        newElements.clear()
    }

    override fun findValue(id: Long): Value = idCache[id]!!

    override fun findValueOrNull(id: Long): Value? = idCache[id]

    override fun findIdOrNew(value: Value): Long = valueCache.computeIfAbsent(value) { t ->
        idGen.incrementAndGet().also {
            newElements[value] = it
            idCache[it] = value
        }
    }

    override fun flush(ctx: ILDBContext) {
        val entries = newElements.entries.toList()
        if (entries.isEmpty()) return
        val txn = ctx.txn
        val unwrapped = txn.unwrap
        if (unwrapped is KVErsTransaction) {
            val kvTxn = unwrapped.kvTxn
            val symbolsMap = kvTxn.getNamedMap(kvValueMapName, create = true)!!
            val stringBinding = BuiltInBindingProvider.getBinding(String::class.java)
            val longBinding = BuiltInBindingProvider.getBinding(Long::class.java)
            // TODO
//            entries.forEach { (name, id) ->
//                kvTxn.put(symbolsMap, longBinding.getBytesCompressed(id), stringBinding.getBytes(name))
//            }
        } else {
            entries.forEach { (name, id) ->
                txn.newEntity("Symbol").also { symbol ->
                    symbol["name"] = name
                    symbol["id"] = id
                }
            }
        }
        entries.forEach {
            newElements.remove(it.key)
        }
    }
}