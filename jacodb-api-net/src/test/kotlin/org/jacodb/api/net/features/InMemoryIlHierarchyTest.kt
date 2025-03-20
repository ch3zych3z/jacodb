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

import kotlin.collections.filter
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import kotlinx.coroutines.runBlocking
import org.jacodb.api.net.TestDllServer
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.ilinstances.IlArrayConstant
import org.jacodb.api.net.ilinstances.IlAttribute
import org.jacodb.api.net.ilinstances.IlType
import org.jacodb.api.net.ilinstances.IlTypeRef
import org.jacodb.api.net.ilinstances.impl.IlAttributeImpl
import org.jacodb.api.net.publication.IlPredefinedTypeExt.int32
import org.jacodb.api.net.publication.IlPredefinedTypeExt.string
import org.jacodb.api.net.storage.TypeIdExt.emptyTypeId
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test


class InMemoryIlHierarchyTest {
    companion object {
        val testEntryAttrType = "TACBuilder.Tests.InMemoryIlHierarchy.InMemoryHierarchyTestEntryAttribute"
        val env = TestDllServer.freshEnv()
        val server get() = env.first
        val publication get() = env.second
        val annotatedClasses
            get() = runBlocking {
                publication.query(
                    AnnotatedTypesFeature,
                    TypeId(
                        listOf(),
                        publication.findAsmNameByLocationOrNull(TestDllServer.testDllPath)!!,
                        testEntryAttrType
                    )
                )
            }
        @JvmStatic
        @AfterAll
        fun dispatch() {
            server.close()
        }
    }

    @Test
    fun testSimple() {
        val testClasses =
            annotatedClasses.filter { it.declaringType?.fullname == "TACBuilder.Tests.InMemoryIlHierarchy.Simple" }
                .toList()
        testRoutine(testClasses)
    }

    @Test
    fun testImplementors() {
        val testClasses =
            annotatedClasses.filter { it.declaringType?.fullname == "TACBuilder.Tests.InMemoryIlHierarchy.Implementors" }
                .toList()
        testRoutine(testClasses)
    }

    @Test
    fun testNonGenericChildren() {
        val testClasses =
            annotatedClasses.filter { it.declaringType?.fullname == "TACBuilder.Tests.InMemoryIlHierarchy.NonGenericChildren" }
                .toList()
        testRoutine(testClasses)
    }

    @OptIn(DelicateCoroutinesApi::class)
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
            val response = InMemoryIlHierarchy.query(publication, intRequest).toList()
            response.forEach {
                assertTrue(it.isGenericType && !it.isGenericDefinition)
                assertEquals(publication.int32(), it.genericArgs.first())
            }
            assertEquals(setOf("SingleParamStruct`1", "SingleParamAny`1"), response.map { it.name }.toSet())
        }

        val strSubst =
            TypeId(listOf(publication.string().id), requestTypeIdTemplate.asmName, requestTypeIdTemplate.typeName)
        val strRequest = InMemoryIlHierarchyReq(strSubst)

        val response = GlobalScope.future { InMemoryIlHierarchy.query(publication, strRequest).toList() }.join()

        assertEquals(2, response.size)
        response.forEach {
            assertTrue(it.isGenericType && !it.isGenericDefinition)
            assertEquals(publication.string(), it.genericArgs.first())
        }
        assertEquals(setOf("SingleParamClass`1", "SingleParamAny`1"), response.map { it.name }.toSet())

    }

    @Test
    fun testDefaultCtorSingleArg() {
        val exactSubst = TypeId(
            listOf(),
            TypeRequestTest.publication.findAsmNameByLocationOrNull(TestDllServer.testDllPath)!!,
            "TACBuilder.Tests.InMemoryIlHierarchy.DefaultCtorTypeParam"
        )

        val requestTypeIdTemplate = TypeId(
            listOf(emptyTypeId()),
            TypeRequestTest.publication.findAsmNameByLocationOrNull(TestDllServer.testDllPath)!!,
            "TACBuilder.Tests.InMemoryIlHierarchy.DefaultCtorTestBase`1"
        )

        val exactRequest = InMemoryIlHierarchyReq(
            TypeId(
                listOf(exactSubst),
                requestTypeIdTemplate.asmName,
                requestTypeIdTemplate.typeName
            )
        )
        runBlocking {
            val response =
                InMemoryIlHierarchy.query(publication, exactRequest).toList()

            response.forEach {
                assertTrue(it.isGenericType && !it.isGenericDefinition)
                assertEquals(exactSubst, it.genericArgs.first().id)
            }
            assertEquals(setOf("DefaultCtorClass`1"), response.map { it.name }.toSet())


        }
        val strSubst =
            TypeId(listOf(publication.string().id), requestTypeIdTemplate.asmName, requestTypeIdTemplate.typeName)
        val strRequest = InMemoryIlHierarchyReq(strSubst)
        runBlocking {
            val response =
                InMemoryIlHierarchy.query(publication, strRequest).toList()
            assertTrue(response.isEmpty())
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun testRoutine(testClasses: List<IlType>) {
        assertNotEquals(0, testClasses.size, "no test classes found")
        val testEntries =
            testClasses.filter { it.attributes.any { attr -> attr.type.fullname == testEntryAttrType } }
        testEntries.forEach { entry ->
            val req = InMemoryIlHierarchyReq(entry.id)
            val expectationRefs: List<IlTypeRef> =
                ((entry.attributes.first<IlAttribute> { it.type.fullname == testEntryAttrType } as IlAttributeImpl).constructorArgs[0] as IlArrayConstant).values as List<IlTypeRef>
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