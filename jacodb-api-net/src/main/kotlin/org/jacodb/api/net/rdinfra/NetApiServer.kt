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
import com.jetbrains.rd.util.spinUntil
import com.jetbrains.rd.util.threading.SingleThreadScheduler
import com.jetbrains.rd.util.threading.SynchronousScheduler
import org.jacodb.api.net.IlDatabase
import org.jacodb.api.net.generated.models.*
import java.io.File
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.system.exitProcess


fun logWithTime(message: String) {
    println("${LocalTime.now()}: $message ")
}

fun IWire.onDisconnect(lifetime: Lifetime = Lifetime.Eternal, callback: () -> Unit) {
    var wasConnected = false
    connected.advise(lifetime) { isConnected ->
        if (!isConnected && wasConnected) {
            callback()
        }
        wasConnected = isConnected
    }
}

open class RdServer(port: Int, private val netExePath: String, val db: IlDatabase) {
    val lifetimeDef = Lifetime.Eternal.createNested()
    val lifetime = lifetimeDef.lifetime
    val scheduler = SynchronousScheduler
    val protocol = createProtocol(lifetime)

    private lateinit var netProcess: Process
    private var unresponded = 0
    private fun createProtocol(processLifetime: Lifetime): Protocol {
//        val scheduler = SingleThreadScheduler(
//            processLifetime,
//            "scheduler"
//        )
        return Protocol(
            "protocol",
            Serializers(),
            Identities(IdKind.Server),
            scheduler,
            SocketWire.Server(
                processLifetime,
                scheduler,
                8083,
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
    init {
        netProcess = spawnDotNetProcess(netExePath)

    }

    private fun spawnDotNetProcess(exePath: String): Process {
        val process = ProcessBuilder("./TACBuilder", "--rd")
            .directory(File(exePath))
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
        return process
    }
}

class NetApiServer(private val exePath: String, private val requestAsmPath: String, private val db: IlDatabase) {
    private val server = RdServer(8083, exePath, db)
//    init {
//        server.run()
//    }

    fun requestTestAsm() {
        requestAsm(requestAsmPath)
    }

    private fun requestAsm(path: String) {
//        var response = server.signalModel.publication.sync(PublicationRequest(path))
//        db.persistence.persist(response.reachableTypes)
    }

    fun close() {
        server.close()
    }
}
