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

package org.jacodb.api.net.generated.models

fun getRandomString(length: Int): String {
    val allowedChar = ('a'..'z') + ('A'..'Z')
    return (0..<length).map { allowedChar.random() }.joinToString("")
}

fun randomPrimitive(namesLength: Int) = IlPrimitiveTypeDto(
    asmName = getRandomString(namesLength),
    namespaceName = getRandomString(namesLength),
    name = getRandomString(namesLength),
    declType = TypeId(
        asmName = getRandomString(namesLength),
        typeName = getRandomString(namesLength),
        typeArgs = emptyList()
    ),
    genericArgs = emptyList(),
    isGenericParam = false,
    isValueType = true,
    isManaged = false,
    attrs = emptyList(),
    fields = emptyList(),
    methods = emptyList(),
    moduleToken = 0,
    typeToken = 1,
    fullname = getRandomString(namesLength),
    baseType = null,
    interfaces = emptyList(),
    isGenericDefinition = false
)
