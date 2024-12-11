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

package org.jacodb.api.net.ilinstances.virtual

import org.jacodb.api.net.IlPublication
import org.jacodb.api.net.features.eliminateApproximation
import org.jacodb.api.net.ilinstances.IlAttribute
import org.jacodb.api.net.ilinstances.IlField
import org.jacodb.api.net.ilinstances.IlType
import org.jacodb.api.net.publication.IlPredefinedTypesExt.void

class IlFieldVirtual(
    override val declaringType: IlType,
    override val fieldType: IlType,
    override val name: String,
    override val isStatic: Boolean,
    override val attributes: List<IlAttribute>
) : IlField {
    private class Builder(publication: IlPublication) {
        var name: String = "_virtual_"
            private set

        fun name(value: String) = apply {
            name = value
        }

        lateinit var declaringType: IlType
        fun declaringType(type: IlType) = apply {
            declaringType = type
        }

        var fieldType: IlType = publication.void()
            private set

        fun fieldType(value: IlType) = apply {
            fieldType = value
        }

        var isStatic: Boolean = false
            private set

        fun isStatic(value: Boolean) = apply {
            isStatic = value
        }

        var attributes: List<IlAttribute> = emptyList()
            private set

        fun attributes(value: List<IlAttribute>) = apply {
            attributes = value
        }

        fun build(): IlFieldVirtual {
            return IlFieldVirtual(declaringType, fieldType, name, isStatic, attributes)
        }
    }

    companion object {
        fun IlField.toVirtualOf(type: IlType) =
            Builder(this.fieldType.publication).declaringType(type).fieldType(fieldType.eliminateApproximation())
                .name(this.name).isStatic(isStatic).attributes(attributes).build()
    }
}