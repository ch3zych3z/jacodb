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

package org.jacodb.api.net.ilinstances.virtual

import org.jacodb.api.common.cfg.CommonInst
import org.jacodb.api.common.cfg.ControlFlowGraph
import org.jacodb.api.net.generated.models.IlStmtDto
import org.jacodb.api.net.ilinstances.IlMethod
import org.jacodb.api.net.ilinstances.IlParameter
import org.jacodb.api.net.ilinstances.IlStmt
import org.jacodb.api.net.ilinstances.IlType


class IlMethodVirtual : IlMethod {

    internal class Builder {
        fun build(): IlMethodVirtual {
            TODO()
        }
    }

    companion object {
        fun IlMethod.toVirtual() = Builder().build()
    }

    override val declaringType: IlType
        get() = TODO("Not yet implemented")
    override val returnType: IlType
        get() = TODO("Not yet implemented")
    override val name: String
        get() = TODO("Not yet implemented")
    override val signature: String
        get() = TODO("Not yet implemented")
    override val rawInstList: List<IlStmtDto>
        get() = TODO("Not yet implemented")
    override val instList: List<IlStmt>
        get() = TODO("Not yet implemented")
    override val parameters: List<IlParameter>
        get() = TODO("Not yet implemented")

    override fun flowGraph(): ControlFlowGraph<CommonInst> {
        TODO("Not yet implemented")
    }
}