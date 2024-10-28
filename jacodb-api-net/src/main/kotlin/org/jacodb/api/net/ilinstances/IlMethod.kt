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

import org.jacodb.api.net.generated.models.IlMethodDto
import org.jacodb.api.net.generated.models.IlParameterDto

class IlMethod(private val dto: IlMethodDto) : IlInstance {
    var declType: IlType? = null
    var returnType: IlType? = null
    val name: String = dto.name
    val parametes: MutableList<IlParameter> = dto.parameters.map { IlParameter(it) }.toMutableList()

    override fun attach() {
        if (dto.declType != null) {
            declType = IlInstance.cache.getType(dto.declType)
            (declType as IlType).methods.add(this)
        }
        if (dto.returnType != null)
            returnType = IlInstance.cache.getType(dto.returnType)
        parametes.forEach { it.attach() }
    }
}

class IlParameter(private val dto: IlParameterDto) : IlInstance {
    lateinit var paramType: IlType
    val name: String = dto.name
    override fun attach() {
        paramType = IlInstance.cache.getType(dto.type)
    }

}