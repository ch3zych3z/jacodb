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

import com.jetbrains.rd.framework.impl.RpcTimeouts
import com.jetbrains.rd.framework.util.NetUtils
import org.jacodb.api.net.IlSettings
import org.jacodb.api.net.TestDllServer
import org.jacodb.api.net.database.IlDatabaseImpl
import org.jacodb.api.net.features.IlMethodInstructionsFeature
import org.jacodb.api.net.generated.models.PublicationRequest
import org.jacodb.api.net.generated.models.ilModel
import org.jacodb.api.net.generated.models.ilSigModel
import org.jacodb.api.net.ilinstances.impl.IlMethodImpl
import org.jacodb.api.net.rdinfra.RdServer
import org.jacodb.api.net.storage.id
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class InvariantsFulfillment {

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
    fun `no fail on deserialization`() {
        publication.allTypes.forEach { typeDto ->
            val type = publication.findIlTypeOrNull(typeDto.id())
            assertNotNull(type, "type not found: ${typeDto.fullname}")

            type.methods.forEach { m ->
                m.instList.forEachIndexed { index, stmt ->
                    assertEquals(index, stmt.location.index)
                    assertEquals(m, stmt.location.method)
                }
                assertDoesNotThrow { m.flowGraph() }
                val scopes = (m as IlMethodImpl).scopes

                scopes.forEach { scope ->
                    assertTrue(scope.tb.location.index <= scope.te.location.index)
                    assertTrue(scope.hb.location.index <= scope.he.location.index)
                }

            }
        }
    }

}