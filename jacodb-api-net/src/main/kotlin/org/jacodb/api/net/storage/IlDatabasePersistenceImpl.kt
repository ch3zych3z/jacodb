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

import com.jetbrains.rd.util.putLong
import org.jacodb.api.net.ILDBContext
import org.jacodb.api.net.IlDatabasePersistence
import org.jacodb.api.net.features.APPROXIMATION_ATTRIBUTE
import org.jacodb.api.net.features.ORIGINAL_TYPE_PROPERTY
import org.jacodb.api.net.generated.models.*
import org.jacodb.api.storage.ers.Entity
import org.jacodb.api.storage.ers.EntityRelationshipStorage
import org.jacodb.api.storage.ers.Transaction
import org.jacodb.api.storage.ers.compressed
import org.jacodb.api.storage.ers.links
import org.jacodb.api.storage.ers.nonSearchable
import java.nio.ByteBuffer
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class IlDatabasePersistenceImpl(override val ers: EntityRelationshipStorage) : IlDatabasePersistence {
    private val lock = ReentrantLock()
    override val allTypes: List<IlTypeDto>
        get() = read { ctx ->
            val txn = ctx.txn
            val types = txn.all("Type").map { it.toDto() }.toList()
            types
        }

    override val interner: IlDbSymbolInterner =
        ILDBSymbolsInternerImpl().apply { setup(this@IlDatabasePersistenceImpl) }

    override fun findSymbolById(id: Long): String = id.asSymbol(interner)

    override fun findIdBySymbol(symbol: String): Long = symbol.asSymbolId(interner)

    override fun <T> read(action: (ILDBContext) -> T): T {
        return if (ers.isInRam) // RAM storage doesn't support explicit readonly transactions
            ers.transactionalOptimistic(attempts = 10) { txn ->
                action(toILDBContext(txn))
            } else ers.transactional(readonly = true) { txn ->
            action(toILDBContext(txn))
        }
    }

    override fun <T> write(action: (ILDBContext) -> T): T = lock.withLock {
        ers.transactional { txn ->
            action(toILDBContext(txn))
        }
    }

    private fun findTypeSources(ctx: ILDBContext, fullName: String): Sequence<IlTypeDto> {
        val txn = ctx.txn
        val id = interner.findSymbolIdOrNew(fullName)
        return txn.find(type = "Type", propertyName = "fullname", value = id).map { it.toDto() }
    }

    override fun findTypeSourceByNameOrNull(fullName: String): IlTypeDto? = read { ctx ->
        val seq = findTypeSources(ctx, fullName).toList()
        seq.firstOrNull()
    }

    override fun findTypeSourcesByName(fullName: String): List<IlTypeDto> = read { ctx ->
        findTypeSources(ctx, fullName).toList()
    }

    override fun persistTypes(types: List<IlTypeDto>) {
        if (types.isEmpty()) return
        write { ctx ->
            val txn = ctx.txn
            types.forEach { type ->
                val entity = txn.newEntity("Type")
                entity["fullname"] = type.fullname.asSymbolId(interner)
                entity["assembly"] = type.asmName.asSymbolId(interner)
                entity.setRawBlob("bytes", type.getBytes())
                type.attrs.forEach { it.save(txn, entity) }
            }
        }
    }

    override fun persistAsmHierarchy(asms: List<String>, referenced: List<List<String>>) {
        fun Long.toByteArray(): ByteArray {
            val buffer = ByteBuffer.allocate(Long.SIZE_BYTES)
            buffer.putLong(this)
            return buffer.array()
        }
        asms.zip(referenced).forEach { pair ->
            write { ctx ->
                val txn = ctx.txn
                val entity = txn.newEntity("Assembly")
                entity["name"] = pair.first.asSymbolId(interner)
                val buf = ByteArray(asms.size * Long.SIZE_BYTES)
                pair.second.map { it.asSymbolId(interner) }.forEachIndexed { index, long ->
                    buf.putLong(long, index * Long.SIZE_BYTES)
                }
                entity["refs"] = buf.compressed.nonSearchable
            }
        }
    }

    // TODO target groups: type, method, field, parameter
    // TODO bindings to TypeDTO
    // TODO: support only types now
    private fun IlAttrDto.save(txn: Transaction, target: Entity): Entity {
        return txn.newEntity("Attribute").also { attr ->
            links(attr, "target") += target
            attr["fullname"] = attrType.typeName.asSymbolId(interner).compressed
            attr["assembly"] = attrType.asmName.asSymbolId(interner)

            attr.setRawBlob("values", ctorArgs.getBytes())

            namedArgsNames.zip(namedArgsValues).forEach { (name, value) ->
                attr.setRawBlob(name, value.getBytes())
            }
            if (attrType.typeName == APPROXIMATION_ATTRIBUTE) {
                println(attr.getRawBlob(ORIGINAL_TYPE_PROPERTY)?.unsafeString())
            }
        }
    }
}

fun Entity.toDto(): IlTypeDto =
    this.getRawBlob(name = "bytes")?.getIlTypeDto() ?: throw RuntimeException("Bytes for entity $this were not found")
