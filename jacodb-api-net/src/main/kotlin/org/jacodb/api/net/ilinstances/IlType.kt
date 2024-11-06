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

import org.jacodb.api.net.generated.models.AsmCacheKey
import org.jacodb.api.net.generated.models.IlTypeDto
import org.jacodb.api.net.ilinstances.IlAttribute

class IlType(private val dto: IlTypeDto) : IlInstance {
    lateinit var declAsm: IlAsm
    val name: String = dto.name
    val attributes: MutableList<IlAttribute> = mutableListOf()
    val fields: MutableList<IlField> = mutableListOf()
    val methods: MutableList<IlMethod> = mutableListOf()
    override fun attach() {
        declAsm = IlInstance.cache.getAsm(AsmCacheKey(dto.id.asm))
        declAsm.types.add(this)
        dto.attrs.forEach { attributes.add(IlAttribute(it)) }
    }

    override fun toString(): String {
        return name
    }
}