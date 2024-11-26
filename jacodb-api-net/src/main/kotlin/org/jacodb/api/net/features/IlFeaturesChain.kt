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

import org.jacodb.api.net.IlPublicationFeature

class IlFeaturesChain(val features: List<IlPublicationFeature>) {

    inline fun <reified T> run(action : T.() -> Unit)  {
        features.filterIsInstance<T>().forEach(action)
    }

    inline fun <reified T : IlPublicationFeature, R> callUntilResolved(action: (T) -> R?) : R? {
        for (feature in features) {
            if (feature is T) {
                val res = action(feature)
                if (res != null) {
                    val event = feature.event(res)
                    event?.let { features.forEach { it.on(event) }  }
                    return res
                }
            }
        }
        return null
    }
}
