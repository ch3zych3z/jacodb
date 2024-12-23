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

import com.jetbrains.rd.framework.Protocol
import com.jetbrains.rd.framework.impl.RpcTimeouts
import com.jetbrains.rd.framework.util.NetUtils
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.completeWith
import org.jacodb.api.net.database.IlDatabaseImpl
import org.jacodb.api.net.features.IlApproximations
import org.jacodb.api.net.features.IlMethodInstructionsFeature
import org.jacodb.api.net.generated.models.PublicationRequest
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.generated.models.ilModel
import org.jacodb.api.net.generated.models.ilSigModel
import org.jacodb.api.net.ilinstances.impl.IlMethodImpl
import org.jacodb.api.net.publication.IlPredefinedAsmsExt.mscorelib
import org.jacodb.api.net.publication.IlPredefinedTypesExt.int32
import org.jacodb.api.net.publication.IlPublicationCache
import org.jacodb.api.net.rdinfra.RdServer
import org.jacodb.api.net.storage.id

suspend fun <T> Protocol.onScheduler(block: () -> T): T {
    val deffered = CompletableDeferred<T>()
    scheduler.invokeOrQueue { deffered.completeWith(runCatching { block() }) }
    return deffered.await()
}

fun main(args: Array<String>) {
    assert(args[0] == "--exe")
    val exePath = args[1]
    assert(args[2] == "--asms")
    val asmPaths = args.drop(3)
    val settings = IlSettings()
    val database = IlDatabaseImpl(settings)
    val freePort = NetUtils.findFreePort(0)
    val server = RdServer(freePort, exePath, database)
    server.protocol.scheduler.queue {
        val res =
            server.protocol.ilModel.ilSigModel.publication.sync(PublicationRequest(asmPaths), RpcTimeouts.longRunning)
        database.persistence.persistAsmHierarchy(res.reachableAsms, res.referencedAsms)
        database.persistence.persistTypes(res.reachableTypes)


        println("got result")
        val publication = database.publication(
            asmPaths,
            listOf(
                IlPublicationCache(settings.publicationCacheSettings),
                IlMethodInstructionsFeature(),
//                IlApproximations
            )
        )
        val allTypes = publication.allTypes
        println("types fetched")
        allTypes.forEach { typeDto ->
            if (typeDto.asmName == publication.mscorelib() && typeDto.fullname.contains("System.Collections.Generic.List`1[T]")) {
                val defn = publication.findIlTypeOrNull(typeDto.id())?.genericDefinition ?: return@forEach
                val subst = server.protocol.ilModel.ilSigModel.genericSubstitutions.sync(
                    listOf(
                        TypeId(
                            listOf(publication.int32().id),
                            defn.asmName,
                            defn.fullname
                        )
                    )
                )
                println(subst)
            }
            val type = publication.findIlTypeOrNull(typeDto.id())
            if (type == null) {
                println("not found ${typeDto.fullname}")
                return@forEach
            }
            type.methods.forEach { m ->
                try {
                    m.instList.forEachIndexed { index, stmt ->
                        check(stmt.location.index == index && stmt.location.method == m)

                    }
                } catch (e: Exception) {
                    println("err instList for ${m.name} with ${e.message} at ${e.stackTrace}")
                }
                try {
                    val graph = m.flowGraph()
                    if (m.name.contains("ThrowRethrow"))
                        println("found")
                } catch (e: Exception) {
                    println("err flowGraph for ${m.name} with ${e.message}")
                }
                try {
                    val scopes = (m as IlMethodImpl).scopes
                    scopes.forEach { scope ->
                        check(scope.tb.location.index <= scope.te.location.index)
                        check(scope.hb.location.index <= scope.he.location.index)
                    }
                } catch (e: Exception) {
                    println("err scopes for ${m.name} with ${e.message}")
                }
            }
        }
        server.close()
    }
}