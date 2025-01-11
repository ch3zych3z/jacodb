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
import com.jetbrains.rd.util.threading.SynchronousScheduler
import org.jacodb.api.net.database.IlDatabaseImpl
import org.jacodb.api.net.features.IlMethodInstructionsFeature
import org.jacodb.api.net.generated.models.PublicationRequest
import org.jacodb.api.net.generated.models.ilModel
import org.jacodb.api.net.generated.models.ilSigModel
import org.jacodb.api.net.publication.IlPublicationCache
import org.jacodb.api.net.rdinfra.RdServer

fun main(args: Array<String>) {
    assert(args[0] == "--exe")
    val exePath = args[1]
    assert(args[2] == "--asms")
    val asmPaths = args.drop(3)
    val settings = IlSettings()
    val database = IlDatabaseImpl(settings)
    val freePort = NetUtils.findFreePort(0)
    val server = RdServer(freePort, exePath, database)
    server.protocol.scheduler.invokeOrQueue {
        val res =
            server.protocol.ilModel.ilSigModel.publication.sync(PublicationRequest(asmPaths), RpcTimeouts.longRunning)
        database.persistence.persistAsmHierarchy(res.reachableAsms, res.referencedAsms)
        database.persistence.persistTypes(res.reachableTypes)
    }
    println("got result")
    val publication = database.publication(
        asmPaths,
        listOf(
            IlPublicationCache(settings.publicationCacheSettings),
            IlMethodInstructionsFeature(),
        )
    )
    val allTypes = publication.allTypes
    println("types fetched")
    server.close()

}