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
import org.jacodb.api.net.IlMethodExtFeature
import org.jacodb.api.net.IlMethodExtFeature.IlInstListResult
import org.jacodb.api.net.IlPublication
import org.jacodb.api.net.generated.models.IlStmtDto
import org.jacodb.api.net.ilinstances.*
import org.jacodb.api.net.ilinstances.impl.IlEhScope
import org.jacodb.api.net.publication.IlPredefinedTypesExt.void


class IlMethodVirtual(
    override val declaringType: IlType,
    override val isStatic: Boolean,
    override val returnType: IlType,
    override val name: String,
    override val attributes: List<IlAttribute>,
    override val parameters: List<IlParameter>,
    override val rawInstList: List<IlStmtDto>
) : IlMethod {
    // TODO
    override val scopes: List<IlEhScope>
        get() = listOf()

    private class Builder(publication: IlPublication) {
        lateinit var declaringType: IlType
            private set

        fun declaringType(value: IlType) = apply {
            declaringType = value
        }

        var isStatic: Boolean = false
            private set

        fun isStatic(value: Boolean) = apply { isStatic = value }
        var returnType: IlType = publication.void()
            private set

        fun returnType(value: IlType) = apply {
            returnType = value
        }

        var name: String = "_virtual_"
            private set

        fun name(value: String) = apply {
            name = value
        }

        var attributes: List<IlAttribute> = emptyList()
            private set

        fun attributes(value: List<IlAttribute>) = apply {
            attributes = value
        }

        var parameters: List<IlParameter> = emptyList()
            private set

        fun parameters(value: List<IlParameter>) = apply {
            parameters = value.map { IlParameterVirtual(it.type, it.attributes, it.name) }
        }

        var rawInstList: List<IlStmtDto> = emptyList()
            private set

        fun rawInstList(value: List<IlStmtDto>) = apply {
            rawInstList = value
        }


        fun build(): IlMethodVirtual =
            IlMethodVirtual(declaringType, isStatic, returnType, name, attributes, parameters, rawInstList)
    }

    val publication: IlPublication get() = declaringType.publication

    override val instList: List<IlStmt>
        get() = publication.featuresChain.callUntilResolved<IlMethodExtFeature, IlInstListResult> {
            it.instList(
                this
            )
        }!!.instructions

    companion object {
        fun IlMethod.toVirtualOf(type: IlType) =
            Builder(type.publication).declaringType(type).isStatic(isStatic).returnType(returnType).name(name)
                .attributes(attributes)
                .parameters(parameters).rawInstList(rawInstList).build()
    }

    override fun flowGraph(): ControlFlowGraph<CommonInst> {
        TODO("Not yet implemented")
    }
}