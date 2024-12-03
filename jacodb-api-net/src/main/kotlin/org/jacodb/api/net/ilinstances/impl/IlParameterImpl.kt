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

import org.jacodb.api.net.IlPublication
import org.jacodb.api.net.generated.models.IlParameterDto
import org.jacodb.api.net.ilinstances.IlAttribute
import org.jacodb.api.net.ilinstances.IlConstant
import org.jacodb.api.net.ilinstances.IlParameter
import org.jacodb.api.net.ilinstances.deserializeConst
import kotlin.LazyThreadSafetyMode.PUBLICATION

class IlParameterImpl(private val dto: IlParameterDto, val enclosingMethod: IlMethodImpl) : IlParameter {
    private val publication: IlPublication
        get() = enclosingMethod.declaringType.publication
    val paramType: IlTypeImpl by lazy(PUBLICATION) { publication.findIlTypeOrNull(dto.type.typeName)!! }
    val attributes: List<IlAttribute> by lazy(PUBLICATION) { dto.attrs.map { IlAttribute(it, publication) } }
    val index: Int = dto.index
    val name: String = dto.name
    val defaultValue: IlConstant? get() = dto.defaultValue?.deserializeConst(publication)

    override fun toString(): String {
        return paramType.toString()
    }
}
