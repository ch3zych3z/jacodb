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

import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.jacodb.api.net.TestDllServer
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.ilinstances.ext.getOverridingMethods
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

class OverridingMethodsTest {
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
    fun inherited() {
        val parent = AssignableFromTests.Companion.publication.findIlTypeOrNull(
            TypeId(
                listOf(),
                AssignableFromTests.Companion.testAsm, "TACBuilder.Tests.InMemoryIlHierarchy.Simple+Base"
            )
        )
        assertNotNull(parent)
        val child = AssignableFromTests.Companion.publication.findIlTypeOrNull(
            TypeId(
                listOf(),
                AssignableFromTests.Companion.testAsm, "TACBuilder.Tests.InMemoryIlHierarchy.Simple+DirectChild"
            )
        )
        assertNotNull(child)
        val parentMethod = parent.methods.single { it.name == "VirtualMethod"}
        val overridings = parentMethod.getOverridingMethods(publication)
        val childMethod = child.methods.single { it.name == "VirtualMethod"}
        assertEquals(2, overridings.size)
        assertContains(overridings, childMethod)
    }
}