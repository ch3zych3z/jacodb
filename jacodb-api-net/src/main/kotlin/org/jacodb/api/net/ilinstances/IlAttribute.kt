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

package org.jacodb.api.net.ilinstances

import org.example.ilinstances.IlType
import org.jacodb.api.net.IlTypeLoader
import org.jacodb.api.net.generated.models.IlAttrDto

class IlAttribute(private val dto: IlAttrDto, private val typeLoader: IlTypeLoader) {
    val type: IlType by lazy(LazyThreadSafetyMode.PUBLICATION) { typeLoader.findIlTypeOrNull(dto.attrType.typeName)!! }
    val constructorArgs: List<IlConst> by lazy { dto.ctorArgs.map { it.deserializeConst() }.toList() }
    val namedArgs: Map<String, IlConst> by lazy(LazyThreadSafetyMode.PUBLICATION)
    { dto.namedArgsNames.zip(dto.namedArgsValues).associate { (k, v) -> k to v.deserializeConst() } }
}
