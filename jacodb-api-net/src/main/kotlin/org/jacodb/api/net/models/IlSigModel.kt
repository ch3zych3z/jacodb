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

@file:Suppress("unused")

package org.jacodb.api.net.models

import com.jetbrains.rd.generator.nova.*

object IlSigModel : Ext(IlModel) {
    private val publicationRequest = structdef {
        field("rootAsms", immutableList(PredefinedType.string))
    }
    private val publicationResponse = structdef {
        field("reachableAsms", immutableList(PredefinedType.string))
        field("referencedAsms", immutableList(immutableList(PredefinedType.string)))
        field("reachableTypes", immutableList(IlModel.IlTypeDto))
    }

    init {
        call("publication", publicationRequest, publicationResponse)
        call("callForAsm", publicationRequest, immutableList(IlModel.IlTypeDto))
        signal("asmRequest", publicationRequest).apply { async }
        signal("asmResponse", immutableList(IlModel.IlDto)).apply { async }
    }
}