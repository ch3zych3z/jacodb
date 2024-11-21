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

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DtoByteConversionIdentity {
    @Test
    fun checkPrimitiveType() {
        val primitiveType = IlPrimitiveTypeDto(
            asmName = "lol",
            namespaceName = "kek",
            name = "",
            declType = TypeId("type", "id"),
            genericArgs = listOf(),
            isGenericParam = false,
            isValueType = true,
            isManaged = false,
            attrs = listOf(),
            fields = listOf(),
            methods = listOf()
        )
        val bytes = primitiveType.getBytes()
        val sameType = bytes.getIlTypeDto()
        assertIs<IlPrimitiveTypeDto>(sameType)
        assertEquals(primitiveType.asmName, sameType.asmName)
        assertEquals(primitiveType.namespaceName, sameType.namespaceName)
    }

    @Test
    fun checkEnumType() {
        val initialEnum = IlEnumTypeDto(
            underlyingType = TypeId("lol", "kek"),
            names = listOf("A", "B", "C"),
            values = listOf(1,2,3).map{ IlInt32ConstDto(it, type = TypeId("int", "int")) }.toList(),
            asmName = "asm",
            namespaceName = "namespace",
            name = "name",
            declType = null,
            genericArgs = listOf(),
            isGenericParam = false,
            isValueType = true,
            isManaged = false,
            attrs = listOf(),
            fields = listOf(),
            methods = listOf()
        )

        val bytes = initialEnum.getBytes()

        val resIlType = bytes.getIlTypeDto()
        assertIs<IlEnumTypeDto>(resIlType)
        assertEquals(initialEnum, resIlType)
    }
}