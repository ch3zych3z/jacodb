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

package org.jacodb.api.net.features

import org.jacodb.api.net.TestDllServer
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.publication.IlPredefinedTypesExt.int32
import org.jacodb.api.net.publication.IlPredefinedTypesExt.nuint
import org.jacodb.api.net.publication.IlPredefinedTypesExt.string
import org.jacodb.api.net.storage.TypeIdExt.emptyTypeId
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TypeRequestTest {
    companion object {
        private val env = TestDllServer.freshEnv()
        val server get() = env.first
        val publication get() = env.second

        @JvmStatic
        @AfterAll
        fun dispatch() {
            server.close()
        }
    }

    @Test
    fun genericBaseRequests() {
        val request = TypeId(
            listOf(emptyTypeId()),
            publication.findAsmNameByLocationOrNull(TestDllServer.testDllPath)!!,
            "TACBuilder.Tests.InMemoryIlHierarchy.SingleParamBase`1"
        )
        val requestGenericDefn = publication.findIlTypeOrNull(
            request
        )
        val substs = listOf(publication.int32(), publication.string(), publication.nuint())
        substs.forEach {
            val response = publication.findIlTypeOrNull(
                TypeId(listOf(it.id), request.asmName, request.typeName)
            )
            assertNotNull(response, "value expected")
            assertEquals(it, response.genericArgs[0])
            assertEquals(response.genericDefinition, requestGenericDefn)
        }

    }
}