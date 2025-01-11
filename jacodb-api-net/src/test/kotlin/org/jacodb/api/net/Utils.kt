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

import com.jetbrains.rd.framework.impl.RpcTimeouts
import com.jetbrains.rd.framework.util.NetUtils
import org.jacodb.api.net.database.IlDatabaseImpl
import org.jacodb.api.net.features.IlMethodInstructionsFeature
import org.jacodb.api.net.generated.models.PublicationRequest
import org.jacodb.api.net.generated.models.ilModel
import org.jacodb.api.net.generated.models.ilSigModel
import org.jacodb.api.net.publication.IlPublicationCache
import org.jacodb.api.net.rdinfra.RdServer


object TestDllServer {
    val testDllPath =
        "/Users/petrukhinandrew/RiderProjects/dotnet-tac/TACBuilder.Tests/bin/Release/net8.0/osx-arm64/publish/TACBuilder.Tests.dll"

    fun freshEnv(): Pair<RdServer, IlPublication> {
        val exePath = "/Users/petrukhinandrew/RiderProjects/dotnet-tac/TACBuilder/bin/Debug/net8.0/osx-arm64/"
        val asmPaths =
            listOf(testDllPath)

        val settings = IlSettings()
        val database = IlDatabaseImpl(settings)
        val freePort = NetUtils.findFreePort(0)
        val server = RdServer(freePort, exePath, database)
        server.protocol.scheduler.queue {
            val res =
                server.protocol.ilModel.ilSigModel.publication.sync(
                    PublicationRequest(asmPaths),
                    RpcTimeouts.longRunning
                )
            database.persistence.persistAsmHierarchy(res.reachableAsms, res.referencedAsms)
            database.persistence.persistTypes(res.reachableTypes)
        }
        return Pair(
            server, server.db.publication(
                listOf(testDllPath),
                listOf(
                    IlPublicationCache(IlSettings().publicationCacheSettings),
                    IlMethodInstructionsFeature(),
                )
            )
        )
    }
}