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

package org.jacodb.api.net.publication

import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.jacodb.api.net.TestDllServer
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.ilinstances.ext.isAssignableFrom
import org.jacodb.api.net.ilinstances.ext.isAssignableTo
import org.jacodb.api.net.publication.IlPredefinedTypeExt.int32
import org.jacodb.api.net.publication.IlPredefinedTypeExt.string
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class AssignableFromTests {

    companion object {
        val env = TestDllServer.freshEnv()
        val server get() = env.first
        val publication get() = env.second
        val testAsm = publication.findAsmNameByLocationOrNull(publication.targetAsmLocations[0])!!

        @JvmStatic
        @AfterAll
        fun dispatch() {
            server.close()
        }
    }

    @Test
    fun sameType() {
        val type = publication.findIlTypeOrNull(TypeId(listOf(), testAsm, "TACBuilder.Tests.InMemoryIlHierarchy.Simple+Base"))
        assertNotNull(type)
        assertTrue(type.isAssignableFrom(type))
        assertTrue(type.isAssignableTo(type))
    }

    @Test
    fun childAndParent() {
        val parent = publication.findIlTypeOrNull(TypeId(listOf(), testAsm, "TACBuilder.Tests.InMemoryIlHierarchy.Simple+Base"))
        assertNotNull(parent)
        val child = publication.findIlTypeOrNull(TypeId(listOf(), testAsm, "TACBuilder.Tests.InMemoryIlHierarchy.Simple+DirectChild"))
        assertNotNull(child)
        assertTrue(parent.isAssignableFrom(child))
        assertTrue(child.isAssignableTo(parent))
        assertFalse(child.isAssignableFrom(parent))
        assertFalse(parent.isAssignableTo(child))
    }

    @Test
    fun longHierarchy() {
        val parent = publication.findIlTypeOrNull(TypeId(listOf(), testAsm, "TACBuilder.Tests.InMemoryIlHierarchy.Simple+Base"))
        assertNotNull(parent)
        val child = publication.findIlTypeOrNull(TypeId(listOf(), testAsm, "TACBuilder.Tests.InMemoryIlHierarchy.Simple+IndirectChild"))
        assertNotNull(child)
        assertTrue(parent.isAssignableFrom(child))
        assertTrue(child.isAssignableTo(parent))
        assertFalse(child.isAssignableFrom(parent))
        assertFalse(parent.isAssignableTo(child))
    }

    @Test
    fun genericParameter() {
        val param = publication.findIlTypeOrNull(TypeId(listOf(), testAsm, "TACBuilder.Tests.InMemoryIlHierarchy.SingleParamStruct`1+T"))
        assertNotNull(param)
        assertTrue(publication.int32().isAssignableTo(param))
        assertFalse(param.isAssignableFrom(publication.string()))
    }
}