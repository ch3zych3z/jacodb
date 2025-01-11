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

package org.jacodb.api.net.database

import org.jacodb.api.net.IlDatabase
import org.jacodb.api.net.IlDatabasePersistence
import org.jacodb.api.net.IlSettings
import org.jacodb.api.net.IlPublication
import org.jacodb.api.net.IlPublicationFeature
import org.jacodb.api.net.features.IlApproximations
import org.jacodb.api.net.features.IlFeature
import org.jacodb.api.net.features.IlSignal
import org.jacodb.api.net.features.InMemoryIlHierarchy
import org.jacodb.api.net.storage.IlDatabasePersistenceImpl
import org.jacodb.api.net.publication.IlPublicationImpl
import org.jacodb.api.net.rdinfra.RdServer
import org.jacodb.api.storage.ers.EmptyErsSettings
import org.jacodb.api.storage.ers.EntityRelationshipStorageSPI

class IlDatabaseImpl(val settings: IlSettings) : IlDatabase {
    override var persistence: IlDatabasePersistence
    override lateinit var server: RdServer
    override fun bindTo(server: RdServer) {
        this.server = server
    }

    override fun publication(targetAsms: List<String>, features: List<IlPublicationFeature>): IlPublication {
        featuresRegistry.broadcast(IlSignal.BeforeIndexing(this))
        return IlPublicationImpl(
            this,
            features,
            settings,
            targetAsms
        )
    }

    // TODO 
    val featuresRegistry = listOf(/*IlApproximations,*/ InMemoryIlHierarchy)
    fun List<IlFeature<*, *>>.broadcast(signal: IlSignal) {
        featuresRegistry.forEach { f -> f.onSignal(signal) }
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    init {
        val ers = EntityRelationshipStorageSPI.getProvider(settings.ersSpiId)
            .newStorage(persistenceLocation = null, settings = EmptyErsSettings)
        persistence = IlDatabasePersistenceImpl(ers)
    }
}
