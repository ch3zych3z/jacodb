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

import kotlinx.coroutines.runBlocking
import org.jacodb.api.net.TestDllServer
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.ilinstances.IlArrayConstant
import org.jacodb.api.net.ilinstances.IlTypeRef
import org.jacodb.api.net.ilinstances.impl.IlTypeImpl
import org.jacodb.api.net.publication.IlPredefinedTypesExt.int32
import org.jacodb.api.net.publication.IlPredefinedTypesExt.string
import org.jacodb.api.net.storage.TypeIdExt.emptyTypeId
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import java.lang.reflect.Type
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class InMemoryIlHierarchyTest {
    companion object {
        val testEntryAttrType = "TACBuilder.Tests.InMemoryIlHierarchy.InMemoryHierarchyTestEntryAttribute"
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
    fun testSimple() {
        val testClasses = publication.allTypes.filter {
            it.declType == TypeId(
                listOf(),
                publication.findAsmNameByLocationOrNull(TestDllServer.testDllPath)!!,
                "TACBuilder.Tests.InMemoryIlHierarchy.Simple"
            )
        }.map { IlTypeImpl.from(it, publication) }

        testRoutine(testClasses)
    }

    @Test
    fun testImplementors() {
        val testClasses = publication.allTypes.filter {
            it.declType == TypeId(
                listOf(),
                publication.findAsmNameByLocationOrNull(TestDllServer.testDllPath)!!,
                "TACBuilder.Tests.InMemoryIlHierarchy.Implementors"
            )
        }.map { IlTypeImpl.from(it, publication) }

        testRoutine(testClasses)
    }

    @Test
    fun testNonGenericChildren() {
        val testClasses = publication.allTypes.filter {
            it.declType == TypeId(
                listOf(),
                publication.findAsmNameByLocationOrNull(TestDllServer.testDllPath)!!,
                "TACBuilder.Tests.InMemoryIlHierarchy.NonGenericChildren"
            )
        }.map { IlTypeImpl.from(it, publication) }

        testRoutine(testClasses)
    }

    @Test
    fun testGenericChildren() {
        val requestTypeIdTemplate = TypeId(
            listOf(emptyTypeId()),
            TypeRequestTest.publication.findAsmNameByLocationOrNull(TestDllServer.testDllPath)!!,
            "TACBuilder.Tests.InMemoryIlHierarchy.SingleParamBase`1"
        )
        val intSubst =
            TypeId(listOf(publication.int32().id), requestTypeIdTemplate.asmName, requestTypeIdTemplate.typeName)
        val intRequest = InMemoryIlHierarchyReq(intSubst)
        runBlocking {
            val response =
                InMemoryIlHierarchy.query(publication, intRequest).toList()

            assertEquals(2, response.size)
            response.forEach {
                assertTrue(it.isGenericType && !it.isGenericDefinition)
                assertEquals(publication.int32(), it.genericArgs.first())
            }
            assertEquals(setOf("SingleParamStruct`1", "SingleParamAny`1"), response.map { it.name }.toSet())


        }
        val strSubst =
            TypeId(listOf(publication.string().id), requestTypeIdTemplate.asmName, requestTypeIdTemplate.typeName)
        val strRequest = InMemoryIlHierarchyReq(strSubst)
        runBlocking {
            val response =
                InMemoryIlHierarchy.query(publication, strRequest).toList()
            assertEquals(2, response.size)
            response.forEach {
                assertTrue(it.isGenericType && !it.isGenericDefinition)
                assertEquals(publication.string(), it.genericArgs.first())
            }
            assertEquals(setOf("SingleParamClass`1", "SingleParamAny`1"), response.map { it.name }.toSet())

        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun testRoutine(testClasses: List<IlTypeImpl>) {
        val testEntries =
            testClasses.filter { it.attributes.any { attr -> attr.type.fullname == testEntryAttrType } }
        testEntries.forEach { entry ->
            val req = InMemoryIlHierarchyReq(entry.id)
            val expectationRefs: List<IlTypeRef> =
                (entry.attributes.first { it.type.fullname == testEntryAttrType }.constructorArgs[0] as IlArrayConstant).values as List<IlTypeRef>
            val expectation = expectationRefs.map { it.referencedType }.sortedBy { it.typeToken }
            runBlocking {
                val actual =
                    InMemoryIlHierarchy.query(publication, req)
                        .sortedBy { it.typeToken }.toList()
                assertContentEquals(expectation, actual)
            }
        }
    }
}