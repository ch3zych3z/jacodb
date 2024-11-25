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


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import org.mockito.kotlin.mock

class DtoSerializationExtKtTest {
    private val namesSize = 10
    //region Generated with Explyt. Tests for DtoSerializationExtKt.getBytes(): ByteArray

    @Test
    fun validateSerializationOfIlPointerTypeDto() {
        val pointerType = IlPointerTypeDto(
            targetType = TypeId("targetAsm", "targetType"),
            asmName = "asmName",
            namespaceName = "namespaceName",
            name = "pointerTypeName",
            declType = null,
            genericArgs = listOf(),
            isGenericParam = false,
            isValueType = true,
            isManaged = false,
            attrs = listOf(),
            fields = listOf(),
            methods = listOf()
        )
        val bytes = pointerType.getBytes()
        val deserializedType = bytes.getIlTypeDto()
        assertIs<IlPointerTypeDto>(deserializedType)
        assertEquals(pointerType.asmName, deserializedType.asmName)
        assertEquals(pointerType.namespaceName, deserializedType.namespaceName)
    }

    @Test
    fun validateSerializationOfIlStructTypeDto() {
        // Create mock fields
        val field1 = IlFieldDto(
            fieldType = TypeId(asmName = "System. String", typeName = "String"), // Proper TypeId instantiation
            isStatic = false,
            name = "field1",
            attrs = listOf() // Add any attributes if needed
        )

        val field2 = IlFieldDto(
            fieldType = TypeId(asmName = "System. Int32", typeName = "Int"), // Proper TypeId instantiation
            isStatic = true,
            name = "field2",
            attrs = listOf()
        )

        // Create mock methods
        val method1 = IlMethodDto(
            returnType = TypeId(asmName = "System. Void", typeName = "Void"), // Proper TypeId instantiation
            attrs = listOf(),
            name = "method1",
            parameters = listOf(
                IlParameterDto(
                    name = "param1",
                    type = TypeId(asmName = "System. String", typeName = "String"),
                    index = 0,
                    defaultValue = null,
                    attrs = listOf()
                )
            ), // Proper TypeId instantiation
            resolved = true,
            locals = listOf(),
            temps = listOf(),
            errs = listOf(),
            ehScopes = listOf(),
            body = listOf(
                IlAssignStmtDto(
                    lhs = IlVarAccessDto(
                        kind = IlVarKind.local,
                        type = TypeId("", ""),
                        index = 1
                    ),
                    rhs = IlArrayConstDto(
                        values = listOf('1', '2', '3').map {
                            IlCharConstDto(
                                it.toChar(),
                                type = TypeId("char", "char")
                            )
                        },
                        type = TypeId("charArr", "charArr")
                    )
                )
            ) // Add statements if needed
        )

        val method2 = IlMethodDto(
            returnType = TypeId(asmName = "System. Int32", typeName = "Int"), // Proper TypeId instantiation
            attrs = listOf(),
            name = "method2",
            parameters = listOf(
                IlParameterDto(
                    name = "param1",
                    type = TypeId(asmName = "System. Int32", typeName = "Int"),
                    index = 0,
                    defaultValue = null,
                    attrs = listOf()
                ), // Proper TypeId instantiation
                IlParameterDto(
                    name = "param2",
                    type = TypeId(asmName = "System. Int32", typeName = "Int"),
                    index = 1,
                    defaultValue = null,
                    attrs = listOf()
                ) // Proper TypeId instantiation
            ),
            resolved = false,
            locals = listOf(),
            temps = listOf(),
            errs = listOf(),
            ehScopes = listOf(),
            body = listOf()
        )

        // Create the struct type with the mock fields and methods
        val structType = IlStructTypeDto(
            asmName = "asmName",
            namespaceName = "namespaceName",
            name = "structTypeName",
            declType = null,
            genericArgs = listOf(),
            isGenericParam = false,
            isValueType = true,
            isManaged = false,
            attrs = listOf(),
            fields = listOf(field1, field2), // Added fields
            methods = listOf(method1, method2) // Added methods
        )

        // Serialize and deserialize the struct type
        val bytes = structType.getBytes()
        val deserializedType = bytes.getIlTypeDto()

        // Assertions to validate the deserialization
        assertIs<IlStructTypeDto>(deserializedType)
        assertEquals(structType.asmName, deserializedType.asmName)
        assertEquals(structType.namespaceName, deserializedType.namespaceName)

        // Validate fields
        assertEquals(2, deserializedType.fields.size)
        assertEquals("field1", deserializedType.fields[0].name)
        assertEquals("field2", deserializedType.fields[1].name)

        // Validate methods
        assertEquals(2, deserializedType.methods.size)
        assertEquals("method1", deserializedType.methods[0].name)
        assertEquals("method2", deserializedType.methods[1].name)
    }

    @Test
    fun validateSerializationOfIlArrayTypeDto() {
        val arrayType = IlArrayTypeDto(
            elementType = TypeId("elementAsm", "elementType"),
            asmName = "asmName",
            namespaceName = "namespaceName",
            name = "arrayTypeName",
            declType = null,
            genericArgs = listOf(),
            isGenericParam = false,
            isValueType = true,
            isManaged = false,
            attrs = listOf(),
            fields = listOf(),
            methods = listOf()
        )
        val bytes = arrayType.getBytes()
        val deserializedType = bytes.getIlTypeDto()
        assertIs<IlArrayTypeDto>(deserializedType)
        assertEquals(arrayType.asmName, deserializedType.asmName)
        assertEquals(arrayType.namespaceName, deserializedType.namespaceName)
    }

    //endregion

}
