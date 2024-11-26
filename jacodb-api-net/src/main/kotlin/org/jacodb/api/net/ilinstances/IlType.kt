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

package org.example.ilinstances

import org.jacodb.api.net.IlTypeExtFeature
import org.jacodb.api.net.IlPublication
import org.jacodb.api.net.features.IlFeaturesChain
import org.jacodb.api.net.generated.models.*
import kotlin.LazyThreadSafetyMode.*
import org.jacodb.api.net.ilinstances.IlAttribute
import org.jacodb.api.net.ilinstances.IlField
import java.util.*

// typeLoader should be public in particular to resolve refs inside TAC
open class IlType(private val dto: IlTypeDto, val publication: IlPublication) : IlInstance {
    val declType: IlType? by lazy(PUBLICATION) { dto.declType?.let { publication.findIlTypeOrNull(it.typeName) } }
    val namespace: String = dto.namespaceName
    val name: String = dto.name
    val attributes: List<IlAttribute> by lazy(PUBLICATION) { dto.attrs.map { IlAttribute(it, publication) } }
    val genericArgs: List<IlType> by lazy(PUBLICATION) { dto.genericArgs.map { publication.findIlTypeOrNull(it.typeName)!! } }
    val fields: List<IlField> by lazy(PUBLICATION) { dto.fields.map { IlField(this, it, publication) }
        .joinFeatureFields(this, publication.featuresChain) }
    val methods: List<IlMethod> by lazy(PUBLICATION) { dto.methods.map { IlMethod(this, it, publication) }
        .joinFeatureMethods(this, publication.featuresChain) }


    override fun toString(): String {
        return name
    }
}

class IlPointerType(private val dto: IlPointerTypeDto, typeLoader: IlPublication): IlType(dto, typeLoader)
open class IlValueType(private val dto: IlValueTypeDto, typeLoader: IlPublication) : IlType(dto, typeLoader)
class IlEnumType(private val dto: IlEnumTypeDto, typeLoader: IlPublication) : IlType(dto, typeLoader)
class IlPrimitiveType(private val dto: IlPrimitiveTypeDto, typeLoader: IlPublication) : IlType(dto, typeLoader)
class IlStructType(private val dto: IlStructTypeDto, typeLoader: IlPublication) : IlType(dto, typeLoader)

open class IlReferenceType(private val dto: IlReferenceTypeDto, typeLoader: IlPublication) : IlType(dto, typeLoader)
class IlArrayType(private val dto: IlArrayTypeDto, typeLoader: IlPublication) : IlType(dto, typeLoader)
class IlClassType(private val dto: IlClassTypeDto, typeLoader: IlPublication) : IlType(dto, typeLoader)


private fun List<IlField>.joinFeatureFields(type: IlType, featuresChain: IlFeaturesChain) : List<IlField> {
    val additional = TreeSet<IlField> { a, b -> a.name.compareTo(b.name)}
    featuresChain.run<IlTypeExtFeature> {
        fieldsOf(type)?.let { additional.addAll(it)  }
    }
    return appendOrReplace(additional, IlField::name)
}

private fun List<IlMethod>.joinFeatureMethods(type: IlType, featuresChain: IlFeaturesChain ) : List<IlMethod> {
    val additional = TreeSet<IlMethod> { a, b -> a.name.compareTo(b.name)}
    featuresChain.run<IlTypeExtFeature> {
        methodsOf(type)?.let { additional.addAll(it)  }
    }
    return appendOrReplace(additional, IlMethod::name)
}


private fun <T> List<T>.appendOrReplace(additional: Set<T>, getKey: (T) -> String) : List<T> {
    if (additional.isEmpty()) return this
    val additionalMap = additional.associateBy(getKey).toMutableMap()
    return map {
        val uniqueName = getKey(it)
        additionalMap[uniqueName]?.let {
            additionalMap.remove(uniqueName)
        } ?: it
    } + additionalMap.values
}
