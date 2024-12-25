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

    override val symbolInterner: IlDbSymbolInterner =
        IlDbSymbolsInternerImpl().apply { setup(this@IlDatabasePersistenceImpl) }

    override val typeIdInterner: IlDbTypeIdInternerImpl =
        IlDbTypeIdInternerImpl(symbolInterner = symbolInterner).apply { setup(this@IlDatabasePersistenceImpl) }

    override fun findSymbolById(id: Long): String = id.asSymbol(symbolInterner)

    override fun findIdBySymbol(symbol: String): Long = symbol.asSymbolId(symbolInterner)
    override fun findTypeIdById(id: Long): TypeId = id.asTypeId(typeIdInterner)
    override fun findIdByTypeId(typeId: TypeId): Long = typeId.interned(typeIdInterner)

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

    override fun findTypeSourceOrNull(typeId: TypeId): IlTypeDto? = read { ctx ->
        val seq = findTypeSources(ctx, typeId).toList()
        seq.firstOrNull()
    }

    private fun findTypeSources(ctx: ILDBContext, typeId: TypeId): Sequence<IlTypeDto> {
        val txn = ctx.txn
        val id = typeId.interned(typeIdInterner)
        return txn.find(type = "Type", propertyName = "typeId", value = id.compressed).map { it.toDto() }
    }

    override fun persistTypes(types: List<IlTypeDto>) {
        if (types.isEmpty()) return
        val typeEntities = hashMapOf<Long, Entity>()
        write { ctx ->
            val txn = ctx.txn
            types.forEach { type ->
                val entity = txn.newEntity("Type")
// TODO #2 links to generic args?
                val tIdx =
                    if (type.isGenericDefinition) typeIdInterner.createIdWithPseudonym(type.id(), type.id().apply {
                        this.typeArgs.map { null }
                    }) else type.id()
                        .interned(typeIdInterner)
                entity["typeId"] = tIdx.compressed
                entity["fullname"] = type.fullname.asSymbolId(symbolInterner).compressed
                entity["assembly"] = type.asmName.asSymbolId(symbolInterner).compressed
                entity["isGenericType"] = type.isGenericType.compressed
                entity["isGenericDefinition"] = type.isGenericDefinition.compressed
                entity.setRawBlob("bytes", type.getBytes())
                type.attrs.forEach { it.save(txn, entity) }
                typeEntities[tIdx] = entity
            }
            types.forEach { type ->
                type.baseType?.let { baseId ->
                    links(
                        typeEntities[type.id().interned(typeIdInterner)]!!,
                        "baseType"
                    ) += typeEntities[baseId.interned(typeIdInterner)]!!
                }
                val typeInterfaces = links(typeEntities[type.id().interned(typeIdInterner)]!!, "implements")
                type.interfaces.forEach { interfaceId ->
                    typeInterfaces += typeEntities[interfaceId.interned(typeIdInterner)]!!
                }
                val genericArgs = links(typeEntities[type.id().interned(typeIdInterner)]!!, "genericArguments")
                type.genericArgs.forEach { arg ->
                    genericArgs += typeEntities[arg.interned(typeIdInterner)]!!
                }
                type.genericDefinition?.let {
                    links(
                        typeEntities[type.id().interned(typeIdInterner)]!!,
                        "genericDefinition"
                    ) += typeEntities[it.interned(typeIdInterner)]!!
                }
            }
        }
    }

    override fun persistAsmHierarchy(asms: List<IlAsmDto>, referenced: List<List<IlAsmDto>>) {
        val asmToEntity: MutableMap<IlAsmDto, Entity> = mutableMapOf()

        write { ctx ->
            asms.forEach { dto ->
                val txn = ctx.txn
                val entity = txn.newEntity("Assembly")
                entity["name"] = dto.name.asSymbolId(symbolInterner).compressed
                entity["location"] = dto.location.asSymbolId(symbolInterner).compressed
                asmToEntity[dto] = entity
            }
            asms.zip(referenced).forEach { (cur, refs) ->
                val refLinks = links(asmToEntity[cur]!!, "references")
                refs.forEach { ref ->
                    refLinks += asmToEntity[ref]!!
                }
            }
        }
    }

    // TODO target groups: type, method, field, parameter
    // TODO bindings to TypeDTO
    // TODO: support only types now
    private fun IlAttrDto.save(txn: Transaction, target: Entity): Entity {
        return txn.newEntity("Attribute").also { attr ->
            links(attr, "target") += target
            attr["typeId"] = attrType.interned(typeIdInterner).compressed
            attr["fullname"] = attrType.typeName.asSymbolId(symbolInterner).compressed
            attr["assembly"] = attrType.asmName.asSymbolId(symbolInterner)
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
