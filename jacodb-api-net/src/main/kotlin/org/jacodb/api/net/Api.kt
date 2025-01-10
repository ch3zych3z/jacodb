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

package org.jacodb.api.net

import org.jacodb.api.net.generated.models.IlAsmDto
import org.jacodb.api.net.generated.models.IlTypeDto
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.storage.IlDbInstanceInterner
import org.jacodb.api.net.storage.IlDbSymbolInterner
import org.jacodb.api.net.storage.IlDbTypeIdInternerImpl
import org.jacodb.api.storage.ers.EntityRelationshipStorage
import java.io.Closeable
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

interface IlDatabase : Closeable {
    val persistence: IlDatabasePersistence

    fun publication(targtetAsms: List<String>, features: List<IlPublicationFeature>): IlPublication
}


// TODO #3 check inheritance from ErsPersistence
interface IlDatabasePersistence {
    val ers: EntityRelationshipStorage
    val allTypes: List<IlTypeDto>
    val symbolInterner: IlDbSymbolInterner

    // TODO Impl should not appear in interface
    val typeIdInterner: IlDbInstanceInterner<TypeId, IlDatabasePersistence, ILDBContext>
    fun findSymbolById(id: Long): String
    fun findIdBySymbol(symbol: String): Long

    // TODO maybe make generics
    fun findTypeIdById(id: Long): TypeId
    fun findIdByTypeId(typeId: TypeId): Long

    fun <T> read(action: (ILDBContext) -> T): T
    fun <T> write(action: (ILDBContext) -> T): T

    fun findTypeSourceOrNull(typeId: TypeId): IlTypeDto?
    fun persistTypes(types: List<IlTypeDto>)
    fun persistAsmHierarchy(asms: List<IlAsmDto>, referenced: List<List<IlAsmDto>>)
}

interface ContextProperty<T>

@Suppress("UNCHECKED_CAST")
class ILDBContext private constructor() {
    private val contextObjects = ConcurrentHashMap<ContextProperty<*>, Any>()

    fun <T> getContextObject(property: ContextProperty<T>): T =
        contextObjects[property] as? T ?: throw NullPointerException("Property $property not found in context objects")

    fun setContextObject(property: ContextProperty<*>, value: Any) =
        apply { contextObjects[property] = value }

    companion object {
        fun <T : Any> of(property: ContextProperty<T>, contextObject: T): ILDBContext =
            ILDBContext().also { it.setContextObject(property, contextObject) }
    }
}
