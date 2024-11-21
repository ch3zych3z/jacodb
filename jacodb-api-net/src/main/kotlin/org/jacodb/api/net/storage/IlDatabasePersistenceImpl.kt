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
import org.jacodb.api.net.generated.models.IlDto
import org.jacodb.api.net.generated.models.IlTypeDto
import org.jacodb.api.storage.ers.Entity
import org.jacodb.api.storage.ers.EntityRelationshipStorage
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class IlDatabasePersistenceImpl(override val ers: EntityRelationshipStorage) : IlDatabasePersistence {
    private val lock = ReentrantLock()

    override val interner: IlDbSymbolInterner = IlDbSymbolInterner()

    override fun findSymbolById(id: Int): String {
        TODO("Not yet implemented")
    }

    override fun findIdBySymbol(symbol: String): Int {
        TODO("Not yet implemented")
    }

    override fun <T> read(action: (ILDBContext) -> T): T {
        return if (ers.isInRam) { // RAM storage doesn't support explicit readonly transactions
            ers.transactionalOptimistic(attempts = 10) { txn ->
                action(toILDBContext(txn))
            }
        } else {
            ers.transactional(readonly = true) { txn ->
                action(toILDBContext(txn))
            }
        }
    }

    override fun <T> write(action: (ILDBContext) -> T): T = lock.withLock {
        ers.transactional { txn ->
            action(toILDBContext(txn))
        }
    }

    private fun findTypeSources(ctx: ILDBContext, fullName: String) : Sequence<IlTypeDto> {
        val txn = ctx.txn
        return txn.find(type = "type", propertyName = "bytecode", value = fullName).map { it.toDto() }
    }

    override fun findTypeSourceByNameOrNull(fullName: String): IlTypeDto? = read { ctx ->
        findTypeSources(ctx, fullName).firstOrNull()
    }

    override fun persist(types: List<IlTypeDto>) {
        if (types.isEmpty()) return
        write { ctx ->
            val txn = ctx.txn
            types.forEach { type ->
                val entity = txn.newEntity("type")
                entity["name"] = type.name
                // TODO fix when dto updates
                entity["assembly"] = type.name
                entity.setRawBlob("bytecode", ByteArray(0))
            }
            txn.commit()
        }
    }
}

fun Entity.toDto() : IlTypeDto = TODO("cast")
