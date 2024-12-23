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

package org.jacodb.api.net.ilinstances.impl

import org.jacodb.api.net.ilinstances.IlType
import org.jacodb.api.net.IlTypeExtFeature
import org.jacodb.api.net.IlPublication
import org.jacodb.api.net.features.IlFeaturesChain
import org.jacodb.api.net.generated.models.*
import kotlin.LazyThreadSafetyMode.*
import org.jacodb.api.net.ilinstances.IlField
import org.jacodb.api.net.ilinstances.IlMethod
import java.util.*

sealed class IlTypeImpl(private val dto: IlTypeDto, override val publication: IlPublication) : IlType {

    companion object Resolver {
        fun from(dto: IlTypeDto, pub: IlPublication): IlTypeImpl {
            return when (dto) {
                is IlPointerTypeDto -> IlPointerType(dto, pub)
                is IlEnumTypeDto -> IlEnumType(dto, pub)
                is IlPrimitiveTypeDto -> IlPrimitiveType(dto, pub)
                is IlStructTypeDto -> IlStructType(dto, pub)
                is IlArrayTypeDto -> IlArrayType(dto, pub)
                is IlClassTypeDto -> IlClassType(dto, pub)
                is IlReferenceTypeDto -> IlReferenceType(dto, pub)
                is IlValueTypeDto -> IlValueType(dto, pub)
                else -> throw IllegalArgumentException()
            }
        }
    }

    override val isConstructed: Boolean = dto.isConstructed
    override val declaringType: IlType? by lazy(PUBLICATION) { dto.declType?.let { publication.findIlTypeOrNull(it) } }

    override val isGenericType: Boolean = dto.isGenericType
    override val genericDefinition: IlType? by lazy(PUBLICATION) {
        dto.genericDefinition?.let { publication.findIlTypeOrNull(it) }
    }
    override val isGenericDefinition: Boolean
        get() = dto.isGenericDefinition
    override val isGenericParameter: Boolean
        get() = dto.isGenericParam

    override val genericArgs: List<IlType> by lazy(PUBLICATION) { dto.genericArgs.map { publication.findIlTypeOrNull(it)!! } }

    override val hasDefaultCtorConstraint: Boolean = dto.hasDefaultCtorConstraint
    override val hasNotNullValueTypeConstraint: Boolean = dto.hasNotNullValueTypeConstraint
    override val hasRefTypeConstraint: Boolean = dto.hasRefTypeConstraint
    override val isCovariant: Boolean = dto.isCovariant
    override val isContravariant: Boolean = dto.isContravariant

    override val baseType: IlType? by lazy(PUBLICATION) {
        dto.baseType?.let { publication.findIlTypeOrNull(it) }
    }
    override val interfaces: List<IlType> by lazy(PUBLICATION) {
        dto.interfaces.mapNotNull { publication.findIlTypeOrNull(it) }
    }
    override val moduleToken: Int = dto.moduleToken
    override val typeToken: Int = dto.typeToken
    override val asmName = dto.asmName
    val namespace: String = dto.namespaceName
    override val name: String = dto.name
    override val fullname = dto.fullname
    override val typeName = fullname
    override val attributes: List<IlAttributeImpl> by lazy(PUBLICATION) {
        dto.attrs.map {
            IlAttributeImpl(
                it,
                publication
            )
        }
    }

    override val fields: List<IlField> by lazy(PUBLICATION) {
        val fields = dto.fields.map { IlFieldImpl(this, it, publication) }
        fields.joinFeatureFields(this, publication.featuresChain)
    }
    override val methods: List<IlMethod> by lazy(PUBLICATION) {
        val methods = dto.methods.map { IlMethodImpl(this, it) }
        methods.joinFeatureMethods(this, publication.featuresChain)
    }

    override fun toString(): String {
        return name
    }
}

class IlPointerType(private val dto: IlPointerTypeDto, publication: IlPublication) : IlTypeImpl(dto, publication)
open class IlValueType(private val dto: IlValueTypeDto, publication: IlPublication) : IlTypeImpl(dto, publication)
class IlEnumType(private val dto: IlEnumTypeDto, publication: IlPublication) : IlTypeImpl(dto, publication)
class IlPrimitiveType(private val dto: IlPrimitiveTypeDto, publication: IlPublication) : IlTypeImpl(dto, publication)
class IlStructType(private val dto: IlStructTypeDto, publication: IlPublication) : IlTypeImpl(dto, publication)

open class IlReferenceType(private val dto: IlReferenceTypeDto, publication: IlPublication) :
    IlTypeImpl(dto, publication)

class IlArrayType(private val dto: IlArrayTypeDto, publication: IlPublication) : IlTypeImpl(dto, publication) {
    val elementType: IlType by lazy { publication.findIlTypeOrNull(dto.elementType)!! }
}

class IlClassType(private val dto: IlClassTypeDto, publication: IlPublication) : IlTypeImpl(dto, publication)


private fun List<IlFieldImpl>.joinFeatureFields(type: IlTypeImpl, featuresChain: IlFeaturesChain): List<IlField> {
    val additional = TreeSet<IlField> { a, b -> a.name.compareTo(b.name) }
    featuresChain.run<IlTypeExtFeature> {
        fieldsOf(type)?.let { additional.addAll(it) }
    }
    return appendOrReplace(additional, IlField::name)
}

private fun List<IlMethodImpl>.joinFeatureMethods(
    type: IlTypeImpl,
    featuresChain: IlFeaturesChain
): List<IlMethod> {
    val additional = TreeSet<IlMethod> { a, b -> a.name.compareTo(b.name) }
    featuresChain.run<IlTypeExtFeature> {
        methodsOf(type)?.let { additional.addAll(it) }
    }
    return appendOrReplace(additional, IlMethod::name)
}


private fun <T> List<T>.appendOrReplace(additional: Set<T>, getKey: (T) -> String): List<T> {
    if (additional.isEmpty()) return this
    val additionalMap = additional.associateBy(getKey).toMutableMap()
    return map {
        val uniqueName = getKey(it)
        additionalMap[uniqueName]?.let {
            additionalMap.remove(uniqueName)
        } ?: it
    } + additionalMap.values
}
