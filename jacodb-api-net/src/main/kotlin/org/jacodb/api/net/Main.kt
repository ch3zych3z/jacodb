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

package org.jacodb.api.net

import com.jetbrains.rd.framework.*
import com.jetbrains.rd.util.catch
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.isAlive
import com.jetbrains.rd.util.reactive.IScheduler
import org.example.ilinstances.IlInstance
import org.jacodb.api.net.generated.models.Request
import org.jacodb.api.net.generated.models.ilModel
import org.jacodb.api.net.generated.models.ilSigModel
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit

fun pumpCurrentThread(lifetime: Lifetime, initializationAction: (IScheduler) -> Unit) {
    val actions = ConcurrentLinkedQueue<() -> Unit>()
    val currentThread = Thread.currentThread()
    val scheduler = object : IScheduler {
        override val isActive: Boolean
            get() = currentThread == Thread.currentThread()

        override fun flush() {
            while (true) {
                val action = actions.poll() ?: return
                if (lifetime.isAlive)
                    catch { action() }
            }
        }

        override fun queue(action: () -> Unit) {
            if (lifetime.isAlive)
                actions.add(action)
        }
    }

    initializationAction(scheduler)

    j@ while (lifetime.isAlive) {
        val action = actions.poll()
        if (action == null) {
            Thread.yield()
            continue@j
        }
        catch { action() }
    }
}

fun main() {
    val socketLifetimeDef = Lifetime.Eternal.createNested()
    val lifetime = socketLifetimeDef.lifetime

    val serializers = Serializers()

    pumpCurrentThread(lifetime) { scheduler ->
        val protocol = Protocol(
            "Server",
            serializers,
            Identities(IdKind.Server),
            scheduler,
            SocketWire.Server(lifetime, scheduler, 8083, "Server"),
            lifetime
        )

        val ilModel = protocol.ilModel;
        val sigModel = ilModel.ilSigModel;
        var (process, alive) = spawnDotNetProcess("/home/andrew/Documents/dotnet-tac/TACBuilder/bin/Debug/net8.0/linux-x64/")

        sigModel.asmResponse.advise(lifetime) { response ->
            response.forEach { dto ->
                IlInstance.cache.put(dto)
            }
            response.forEach { dto -> IlInstance.cache.get(dto).attach() }
            println("response deserialized")
            if (process.isAlive)
                process.destroy()
            println("process destroyed ${process.isAlive}")
        }

        assert(alive)
        sigModel.asmRequest.fire(Request("/home/andrew/Documents/dotnet-tac/TACBuilder.Tests/bin/Release/net8.0/linux-x64/publish/TACBuilder.Tests.dll"))
    }
}

fun spawnDotNetProcess(exePath: String): Pair<Process, Boolean> {
    val process = ProcessBuilder("./TACBuilder", "--rd")
        .directory(File(exePath))
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()
    val alive = process.waitFor(10, TimeUnit.SECONDS)
    println(".NET process spawned")
    return Pair(process, alive)
}