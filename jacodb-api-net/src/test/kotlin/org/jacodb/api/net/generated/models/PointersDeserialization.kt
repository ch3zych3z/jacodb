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

import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.jacodb.api.net.TestDllServer
import org.jacodb.api.net.ilinstances.impl.IlPointerType
import org.jacodb.api.net.ilinstances.impl.IlPrimitiveType
import org.jacodb.api.net.publication.IlPredefinedAsmExt.mscorelib
import org.jacodb.api.net.publication.IlPredefinedTypesExt.byte
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

class PointersDeserialization {
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
    fun asteriskByteIsUnmanagedPointer() {
        val astByte = publication.findIlTypeOrNull(TypeId(listOf(), publication.mscorelib(), "*System.Byte"))
        assertNotNull(astByte)
        assertIs<IlPointerType>(astByte)
        assertTrue(!astByte.isManaged)
    }

    @Test
    fun apersandByteIsManagedPointer() {
        val ampByte = publication.findIlTypeOrNull(TypeId(listOf(), publication.mscorelib(), "&System.Byte"))
        assertNotNull(ampByte)
        assertIs<IlPointerType>(ampByte)
        assertTrue(ampByte.isManaged)
    }

    @Test
    fun pureByteInPublication() {
        var byte = publication.findIlTypeOrNull(publication.byte().id)
        assertNotNull(byte)
        assertIs<IlPrimitiveType>(byte)
    }

}