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

import org.jacodb.api.net.ilinstances.IlField
import org.example.ilinstances.IlMethod
import org.example.ilinstances.IlType
import org.jacodb.api.net.IlInstExtFeature
import org.jacodb.api.net.IlTypeExtFeature
import org.jacodb.api.net.ilinstances.IlStmt
import org.jacodb.api.net.storage.asSymbol
import org.jacodb.api.net.storage.txn

object IlApproximations : IlFeature, IlTypeExtFeature, IlInstExtFeature {
    private val originalToApproximation : MutableMap<OriginalTypeName, ApproximatedTypeName> = mutableMapOf()
    private val approximationToOriginal : MutableMap<ApproximatedTypeName, OriginalTypeName> = mutableMapOf()

    fun findApproximationByOriginalOrNull(original: String): ApproximatedTypeName? =
        originalToApproximation[original.toOriginalTypeName()]

    fun findOriginalByApproximationOrNull(approximation: String): OriginalTypeName? =
        approximationToOriginal[approximation.toApproximatedTypeName()]

    override fun fieldsOf(type: IlType): List<IlField>? {
        val approximationTypeName = findApproximationByOriginalOrNull(type.name)?.name ?: return null
        val approximationType = type.typeLoader.findIlTypeOrNull(approximationTypeName)
        return approximationType?.fields
    }

    override fun methodsOf(type: IlType): List<IlMethod>? {
        val approximationTypeName = findApproximationByOriginalOrNull(type.name)?.name ?: return null
        val approximationType = type.typeLoader.findIlTypeOrNull(approximationTypeName)
        return approximationType?.methods
    }


    override fun transformInstList(instList: List<IlStmt>): List<IlStmt> {
        TODO("Not yet implemented")
    }

    override fun onSignal(signal: IlSignal) {
        when (signal) {
            is IlSignal.BeforeIndexing ->
                signal.db.persistence.read { ctx ->
                    val persistence = signal.db.persistence
                    val approxSymbolId = persistence.findIdBySymbol(APPROXIMATION_ATTRIBUTE)
                    val txn = ctx.txn
                    // find approx with name = ....Approximation
                    // get approximation type name
                    // filter targeting types
                    // get namedArgs
                    // find first named arg target class for approximation value
                    txn.find(type = "Attribute", propertyName = "nameId", value = approxSymbolId).map { attr ->
                        // TODO maybe introduce attribute target symbol for types, methods, params and filer types here
                        val originalTypePropertyId = persistence.findIdBySymbol(ORIGINAL_TYPE_PROPERTY)
                        val approxTypeId = attr.getLink("target").get<Int>("nameId")
                        val namedArg = attr.getLink("namedArgs")
                        assert(namedArg.get<Int>("nameId") == originalTypePropertyId)
                        val originalTypeId = namedArg.get<Int>(ORIGINAL_TYPE_PROPERTY)
                        originalTypeId to approxTypeId
                    }.forEach { (originalId, approxId) ->
                        val originalTn = originalId!!.asSymbol(persistence.interner).toOriginalTypeName()
                        val approxTn = approxId!!.asSymbol(persistence.interner).toApproximatedTypeName()
                        originalToApproximation[originalTn] = approxTn
                        approximationToOriginal[approxTn] = originalTn
                    }
                }
        }
    }
}

const val APPROXIMATION_ATTRIBUTE = "Approximation"
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

fun IlType.eliminateApproximation() : IlType {
    val originalName = IlApproximations.findOriginalByApproximationOrNull(this.name)?.name ?: return this
    return typeLoader.findIlTypeOrNull(originalName)!!
}
