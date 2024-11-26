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
import com.jetbrains.rd.util.threading.SynchronousScheduler
import org.jacodb.api.net.IlDatabase
import org.jacodb.api.net.generated.models.*
import java.io.File
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess


fun logWithTime(message: String) {
    println("${LocalTime.now()}: $message ")
}


open class RdServer(port: Int, private val netExePath: String, val db: IlDatabase) {
    private val lifetimeDef = Lifetime.Eternal.createNested()
    protected val lifetime = lifetimeDef.lifetime
    val scheduler = SynchronousScheduler
    val protocol = Protocol(
        "Server", Serializers(), Identities(IdKind.Server), scheduler,
        SocketWire.Server(lifetime, scheduler, port, "Server"), lifetime
    )
    val instanceModel: IlModel
        get() = protocol.ilModel
    val signalModel: IlSigModel
        get() = instanceModel.ilSigModel
    private lateinit var netProcess: Process
    private var unresponded = 0
    fun request(action: () -> Unit) {
        unresponded += 1
        queue { action() }
        spinUntil { unresponded == 0 }
    }

    private fun queue(action: () -> Unit) {
        scheduler.queue {
            try {
                action()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private fun before() {
        logWithTime("Starting server")
        netProcess = spawnDotNetProcess(netExePath)
        if (netProcess.isAlive) logWithTime("dotnet process spawned") else exitProcess(1)
    }

    private fun start() {
        logWithTime("registering callbacks")
        queue {
            signalModel.asmResponse.advise(lifetime) { response ->
                // TODO response here is a list of IlType children (or IlType itself
                logWithTime("response deserialized")
                db.persistence.persist(response.map { it as IlTypeDto })
                unresponded -= 1
            }

        }
        logWithTime("after queue")
    }

    fun close() {
        logWithTime("Spinning started")
        spinUntil { unresponded == 0 }
        logWithTime("Spinning finished")

        lifetimeDef.terminate()
        if (netProcess.isAlive)
            netProcess.destroy()
    }

    fun run() {
        logWithTime("Test run")
        try {
            before()
            start()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        logWithTime("left run")
    }

    private fun spawnDotNetProcess(exePath: String): Process {
        val process = ProcessBuilder("./TACBuilder", "--rd")
            .directory(File(exePath))
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
        process.waitFor(1, TimeUnit.SECONDS)
        return process
    }
}

class NetApiServer(private val exePath: String, private val requestAsmPath: String, db: IlDatabase) {
    private val server = RdServer(8083, exePath, db)

    init {
        server.run()
    }

    fun requestTestAsm() {
        requestAsm(requestAsmPath)
    }

    private fun requestAsm(path: String) {
        logWithTime("requesting asm $path")
        server.request { server.signalModel.asmRequest.fire(Request(path)) }
        logWithTime("request sent")
    }

    fun close() {
        server.close()
    }
}
