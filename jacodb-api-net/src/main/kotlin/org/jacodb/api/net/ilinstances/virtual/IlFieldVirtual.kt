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

import org.jacodb.api.net.ilinstances.IlAttribute
import org.jacodb.api.net.ilinstances.IlField
import org.jacodb.api.net.ilinstances.IlType

class IlFieldVirtual : IlField {
    private class Builder {
        fun build(): IlFieldVirtual {
            TODO()
        }
    }

    companion object {
        fun IlField.toVirtual() = Builder().build()
    }
    override val fieldType: IlType
        get() = TODO("Not yet implemented")
    override val name: String
        get() = TODO("Not yet implemented")
    override val isStatic: Boolean
        get() = TODO("Not yet implemented")
    override val attributes: List<IlAttribute>
        get() = TODO("Not yet implemented")
}