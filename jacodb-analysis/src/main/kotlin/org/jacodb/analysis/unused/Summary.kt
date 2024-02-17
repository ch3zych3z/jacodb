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

package org.jacodb.analysis.unused

import org.jacodb.analysis.ifds.Edge
import org.jacodb.analysis.ifds.Summary
import org.jacodb.analysis.ifds.Vertex
import org.jacodb.api.JcMethod

data class SummaryEdge(
    val edge: Edge<Fact>,
) : Summary {
    override val method: JcMethod
        get() = edge.method
}

data class Vulnerability(
    val message: String,
    val sink: Vertex<Fact>,
    val edge: Edge<Fact>? = null,
) : Summary {
    override val method: JcMethod
        get() = sink.method
}