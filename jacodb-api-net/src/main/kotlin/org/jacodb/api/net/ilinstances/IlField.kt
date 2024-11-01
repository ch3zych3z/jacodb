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

import org.jacodb.api.net.generated.models.IlFieldDto

class IlField(private val dto: IlFieldDto) : IlInstance {
    lateinit var declType: IlType
    lateinit var fieldType: IlType
    val isStatic: Boolean = dto.isStatic
    val name: String = dto.name

    override fun attach() {
        declType = IlInstance.cache.getType(dto.declType)
        declType.fields.add(this)
        fieldType = IlInstance.cache.getType(dto.fieldType)

    }

    override fun toString(): String {
        return if (isStatic) "$declType.$name" else "$.$name"
    }
}