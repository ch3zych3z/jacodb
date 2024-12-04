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
import org.jacodb.api.net.IlTypeExtFeature
import org.jacodb.api.net.generated.models.unsafeString
import org.jacodb.api.net.ilinstances.IlField
import org.jacodb.api.net.ilinstances.IlMethod
import org.jacodb.api.net.ilinstances.IlStmt
import org.jacodb.api.net.ilinstances.IlType
import org.jacodb.api.net.ilinstances.virtual.IlFieldVirtual.Companion.toVirtualOf
import org.jacodb.api.net.ilinstances.virtual.IlMethodVirtual.Companion.toVirtualOf
import org.jacodb.api.net.storage.asSymbol
import org.jacodb.api.net.storage.asSymbolId
import org.jacodb.api.net.storage.txn
import org.jacodb.api.storage.ers.compressed
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

object IlApproximations : IlFeature, IlTypeExtFeature, IlInstExtFeature {
    private val originalToApproximation: ConcurrentMap<OriginalTypeName, ApproximatedTypeName> = ConcurrentHashMap()
    private val approximationToOriginal: ConcurrentMap<ApproximatedTypeName, OriginalTypeName> = ConcurrentHashMap()

    fun findApproximationByOriginalOrNull(original: String): ApproximatedTypeName? =
        originalToApproximation[original.toOriginalTypeName()]

    fun findOriginalByApproximationOrNull(approximation: String): OriginalTypeName? =
        approximationToOriginal[approximation.toApproximatedTypeName()]

    override fun fieldsOf(type: IlType): List<IlField>? {
        val approximationTypeName = findApproximationByOriginalOrNull(type.fullname)?.name ?: return null
        val approximationType = type.publication.findIlTypeOrNull(approximationTypeName)
        return approximationType?.fields?.map { it.toVirtualOf(type) }
    }

    override fun methodsOf(type: IlType): List<IlMethod>? {
        val approximationTypeName = findApproximationByOriginalOrNull(type.name)?.name ?: return null
        val approximationType = type.publication.findIlTypeOrNull(approximationTypeName)
        return approximationType?.methods?.map { it.toVirtualOf(type) }
    }

    override fun onSignal(signal: IlSignal) {
        if (signal !is IlSignal.BeforeIndexing) return
        signal.db.persistence.read<Unit> { ctx ->
            val persistence = signal.db.persistence
            val approxSymbolId = persistence.findIdBySymbol(APPROXIMATION_ATTRIBUTE)
            val txn = ctx.txn
            // find approx with name = ....Approximation
            // get approximation type name
            // filter targeting types
            // get namedArgs
            // find first named arg target class for approximation value
            txn.find(type = "Attribute", propertyName = "fullname", value = approxSymbolId.compressed).map { attr ->
                val approxTypeId = attr.getLink("target").get<Long>("fullname")
                val originalTypeId =
                    attr.getRawBlob(ORIGINAL_TYPE_PROPERTY)!!.unsafeString().asSymbolId(persistence.interner)
                originalTypeId to approxTypeId
            }.forEach { (originalId, approxId) ->
                val originalTn = originalId.asSymbol(persistence.interner).toOriginalTypeName()
                val approxTn = approxId!!.asSymbol(persistence.interner).toApproximatedTypeName()
                originalToApproximation[originalTn] = approxTn
                approximationToOriginal[approxTn] = originalTn
            }
        }
    }


    override fun transformInstList(
        method: IlMethod,
        instList: List<IlStmt>
    ): List<IlStmt> {
        // TODO
        return instList
    }
}

const val APPROXIMATION_ATTRIBUTE = "TACBuilder.Tests.ApproximationAttribute"
const val ORIGINAL_TYPE_PROPERTY = "OriginalType"


@JvmInline
value class OriginalTypeName(val name: String) {
    override fun toString(): String = name
}

fun String.toOriginalTypeName() = OriginalTypeName(this)

@JvmInline
value class ApproximatedTypeName(val name: String) {
    override fun toString(): String = name
}

fun String.toApproximatedTypeName() = ApproximatedTypeName(this)

fun IlType.eliminateApproximation(): IlType {
    val originalName = IlApproximations.findOriginalByApproximationOrNull(this.name)?.name ?: return this
    return publication.findIlTypeOrNull(originalName)!!
}