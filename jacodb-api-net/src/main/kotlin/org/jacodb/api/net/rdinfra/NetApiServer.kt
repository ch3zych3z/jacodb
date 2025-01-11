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

package org.jacodb.api.net.rdinfra

import com.jetbrains.rd.framework.*
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.threading.SynchronousScheduler
import org.jacodb.api.net.IlDatabase
import java.io.File
import kotlin.concurrent.thread

open class RdServer(private val port: Int, private val netExePath: String, val db: IlDatabase) {
    val lifetimeDef = Lifetime.Eternal.createNested()
    val lifetime = lifetimeDef.lifetime
    val scheduler = SynchronousScheduler
    val protocol = createProtocol(lifetime)

    private var netProcess: Process = spawnDotNetProcess(netExePath)
    private fun createProtocol(processLifetime: Lifetime): Protocol {
        db.bindTo(this)
        return Protocol(
            "protocol",
            Serializers(),
            Identities(IdKind.Server),
            scheduler,
            SocketWire.Server(
                processLifetime,
                scheduler,
                port,
                "socket"
            ),
            processLifetime
        )
    }

    fun close() {
        thread {
            lifetimeDef.terminate()
            if (netProcess.isAlive)
                netProcess.destroy()
        }
    }


    private fun spawnDotNetProcess(exePath: String): Process {
        val process = ProcessBuilder("./TACBuilder", "-m", "rd", "-p", port.toString())
            .directory(File(exePath))
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
        return process
    }
}