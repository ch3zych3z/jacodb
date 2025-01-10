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

package org.jacodb.api.net.features

import com.jetbrains.rd.framework.impl.RpcTimeouts
import com.jetbrains.rd.framework.util.NetUtils
import kotlinx.coroutines.runBlocking
import org.jacodb.api.net.IlSettings
import org.jacodb.api.net.database.IlDatabaseImpl
import org.jacodb.api.net.generated.models.PublicationRequest
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.generated.models.ilModel
import org.jacodb.api.net.generated.models.ilSigModel
import org.jacodb.api.net.ilinstances.IlArrayConstant
import org.jacodb.api.net.ilinstances.IlTypeRef
import org.jacodb.api.net.ilinstances.impl.IlTypeImpl
import org.jacodb.api.net.publication.IlPublicationCache
import org.jacodb.api.net.rdinfra.RdServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals


class InMemoryIlHierarchyTest {
    companion object {
        val testEntryAttrType = "TACBuilder.Tests.InMemoryIlHierarchy.InMemoryHierarchyTestEntryAttribute"
        val testDllPath =
            "/Users/petrukhinandrew/RiderProjects/dotnet-tac/TACBuilder.Tests/bin/Release/net8.0/osx-arm64/publish/TACBuilder.Tests.dll"
        private val server = setupTestDllDb()
        val publication = server.db.publication(
            listOf(testDllPath),
            listOf(
                IlPublicationCache(IlSettings().publicationCacheSettings),
                IlMethodInstructionsFeature(),
            )
        )

        private fun setupTestDllDb(): RdServer {
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
            return server
        }

        @JvmStatic
        @AfterAll
        fun dispatch() {
            server.close()
        }
    }

    @Test
    fun testSimple() {
        val testClasses = publication.allTypes.filter {
            it.declType == TypeId(
                listOf(),
                publication.findAsmNameByLocationOrNull(testDllPath)!!,
                "TACBuilder.Tests.InMemoryIlHierarchy.Simple"
            )
        }.map { IlTypeImpl.from(it, publication) }

        testRoutine(testClasses)
    }

    @Test
    fun testImplementors() {
        val testClasses = publication.allTypes.filter {
            it.declType == TypeId(
                listOf(),
                publication.findAsmNameByLocationOrNull(testDllPath)!!,
                "TACBuilder.Tests.InMemoryIlHierarchy.Implementors"
            )
        }.map { IlTypeImpl.from(it, publication) }

        testRoutine(testClasses)
    }

    @Test
    fun testNonGenericChildren() {
        val testClasses = publication.allTypes.filter {
            it.declType == TypeId(
                listOf(),
                publication.findAsmNameByLocationOrNull(testDllPath)!!,
                "TACBuilder.Tests.InMemoryIlHierarchy.NonGenericChildren"
            )
        }.map { IlTypeImpl.from(it, publication) }

        testRoutine(testClasses)
    }

    @Test
    fun testGenericChildren() {
        val testClasses = publication.allTypes.filter {
            it.declType == TypeId(
                listOf(),
                publication.findAsmNameByLocationOrNull(testDllPath)!!,
                "TACBuilder.Tests.InMemoryIlHierarchy.GenericChildren"
            )
        }.map { IlTypeImpl.from(it, publication) }

        testRoutine(testClasses)
    }

    @Suppress("UNCHECKED_CAST")
    private fun testRoutine(testClasses: List<IlTypeImpl>) {
        val testEntries =
            testClasses.filter { it.attributes.any { attr -> attr.type.fullname == testEntryAttrType } }
        testEntries.forEach { entry ->
            val req = InMemoryIlHierarchyReq(entry.id)
            val expectationRefs: List<IlTypeRef> =
                (entry.attributes.first { it.type.fullname == testEntryAttrType }.constructorArgs[0] as IlArrayConstant).values as List<IlTypeRef>
            val expectation = expectationRefs.map { it.referencedType }
            runBlocking {
                val actual =
                    InMemoryIlHierarchy.query(publication, req).map { IlTypeImpl.from(it, publication) }.toList()
                assertContentEquals(expectation, actual)
            }
        }
    }
}