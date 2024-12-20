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

package org.jacodb.api.net.features

import org.jacodb.api.net.IlInstExtFeature
import org.jacodb.api.net.IlPublication
import org.jacodb.api.net.IlTypeExtFeature
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.generated.models.unsafeString
import org.jacodb.api.net.ilinstances.IlField
import org.jacodb.api.net.ilinstances.IlMethod
import org.jacodb.api.net.ilinstances.IlStmt
import org.jacodb.api.net.ilinstances.IlType
import org.jacodb.api.net.ilinstances.virtual.IlFieldVirtual.Companion.toVirtualOf
import org.jacodb.api.net.ilinstances.virtual.IlMethodVirtual.Companion.toVirtualOf
import org.jacodb.api.net.storage.asSymbolId
import org.jacodb.api.net.storage.asTypeId
import org.jacodb.api.net.storage.txn
import org.jacodb.api.storage.ers.compressed
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

object IlApproximations : IlFeature<Any?, Any?>, IlTypeExtFeature, IlInstExtFeature {
    private val originalToApproximation: ConcurrentMap<OriginalTypeId, ApproximatedTypeId> = ConcurrentHashMap()
    private val approximationToOriginal: ConcurrentMap<ApproximatedTypeId, OriginalTypeId> = ConcurrentHashMap()

    fun findApproximationByOriginalOrNull(original: TypeId): ApproximatedTypeId? =
        originalToApproximation[original.toOriginalTypeId()]

    fun findOriginalByApproximationOrNull(approximation: TypeId): OriginalTypeId? =
        approximationToOriginal[approximation.toApproximatedTypeId()]

    override fun fieldsOf(type: IlType): List<IlField>? {
        val approximationTypeId = findApproximationByOriginalOrNull(type.id)?.typeId ?: return null
        val approximationType = type.publication.findIlTypeOrNull(approximationTypeId)
        return approximationType?.fields?.map { it.toVirtualOf(type) }
    }

    override fun methodsOf(type: IlType): List<IlMethod>? {
        val approximationTypeId = findApproximationByOriginalOrNull(type.id)?.typeId ?: return null
        val approximationType = type.publication.findIlTypeOrNull(approximationTypeId)
        return approximationType?.methods?.map { it.toVirtualOf(type) }
    }

    override suspend fun query(
        publication: IlPublication,
        request: Any?
    ): Sequence<Any?> {
        return emptySequence()
    }

    override fun onSignal(signal: IlSignal) {
        if (signal !is IlSignal.BeforeIndexing) return
        signal.db.persistence.read<Unit> { ctx ->
            val persistence = signal.db.persistence
            val approxTypeId = persistence.findIdBySymbol(APPROXIMATION_ATTRIBUTE)
            val txn = ctx.txn
            txn.find(type = "Attribute", propertyName = "typeId", value = approxTypeId.compressed).map { attr ->
                val approxTypeId = attr.getLink("target").get<Long>("typeId")
                val originalTypeId =
                    attr.getRawBlob(ORIGINAL_TYPE_PROPERTY)!!.unsafeString().asSymbolId(persistence.symbolInterner)
                originalTypeId to approxTypeId
            }.forEach { (originalId, approxId) ->
                val originalTn = originalId.asTypeId(persistence.typeIdInterner).toOriginalTypeId()
                val approxTn = approxId!!.asTypeId(persistence.typeIdInterner).toApproximatedTypeId()
                originalToApproximation[originalTn] = approxTn
                approximationToOriginal[approxTn] = originalTn
            }
        }
    }


    override fun transformInstList(
        method: IlMethod,
        instList: List<IlStmt>
    ): List<IlStmt> {
        return instList.map { it.accept(IlApproximationsInstSubstitutor) }
    }
}

const val APPROXIMATION_ATTRIBUTE = "TACBuilder.Tests.ApproximationAttribute"
const val ORIGINAL_TYPE_PROPERTY = "OriginalType"


@JvmInline
value class OriginalTypeId(val typeId: TypeId) {
    override fun toString(): String = typeId.typeName
}

fun TypeId.toOriginalTypeId() = OriginalTypeId(this)

@JvmInline
value class ApproximatedTypeId(val typeId: TypeId) {
    override fun toString(): String = typeId.typeName
}

fun TypeId.toApproximatedTypeId() = ApproximatedTypeId(this)

fun IlType.eliminateApproximation(): IlType {
    val original = IlApproximations.findOriginalByApproximationOrNull(this.id)?.typeId ?: return this
    return publication.findIlTypeOrNull(original)!!
}
