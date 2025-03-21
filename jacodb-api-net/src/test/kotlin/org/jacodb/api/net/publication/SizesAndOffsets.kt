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

import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import org.jacodb.api.net.TestDllServer
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.publication.IlPredefinedTypeExt.byte
import org.jacodb.api.net.publication.IlPredefinedTypeExt.double
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

class SizesAndOffsets {
    companion object {
        val env = TestDllServer.freshEnv()
        val server get() = env.first
        val publication get() = env.second

        @JvmStatic
        @AfterAll
        fun dispatch() {
            server.close()
        }
    }

    @Test
    fun primitiveSizePresent() {
        var byte = publication.findIlTypeOrNull(publication.byte().id)
        assertNotNull(byte)
        assertEquals(1, byte.size)
        var double = publication.findIlTypeOrNull(publication.double().id)
        assertNotNull(double)
        assertEquals(8, double.size)
    }

    @Test
    fun fieldHaveDifferentOffsets() {
        val asm = publication.findAsmNameByLocationOrNull(publication.targetAsmLocations[0])!!
        val point = publication.findIlTypeOrNull(TypeId(listOf(), asm, "IntegrationTests.Point"))
        assertNotNull(point)
        val fields = point.fields.filter { it.name in listOf("_x", "_y")}
        assertEquals(2, fields.size)
        assertNotEquals(fields.first().offset, fields.last().offset)
    }
}