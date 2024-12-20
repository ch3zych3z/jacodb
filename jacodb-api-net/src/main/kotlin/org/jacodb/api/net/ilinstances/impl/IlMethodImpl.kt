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

package org.jacodb.api.net.ilinstances.impl

import org.jacodb.api.common.cfg.CommonInst
import org.jacodb.api.common.cfg.ControlFlowGraph
import org.jacodb.api.net.IlMethodExtFeature
import org.jacodb.api.net.IlMethodExtFeature.IlFlowGraphResult
import org.jacodb.api.net.IlMethodExtFeature.IlInstListResult
import org.jacodb.api.net.IlPublication
import org.jacodb.api.net.generated.models.*
import org.jacodb.api.net.ilinstances.*
import kotlin.LazyThreadSafetyMode.PUBLICATION

class IlMethodImpl(override val declaringType: IlTypeImpl, private val dto: IlMethodDto) : IlMethod {
    private val publication: IlPublication
        get() = declaringType.publication

    override fun flowGraph(): ControlFlowGraph<CommonInst> {
        return publication.featuresChain.callUntilResolved<IlMethodExtFeature, IlFlowGraphResult> { it.flowGraph(this) }!!.flowGraph
    }

    override val isConstructed: Boolean = dto.isConstructed

    override val isStatic: Boolean = dto.isStatic
    override val returnType: IlType by lazy { dto.returnType.let { publication.findIlTypeOrNull(dto.returnType)!! } }

    override val name: String = dto.name
    override val parameters: List<IlParameterImpl> by lazy(PUBLICATION) {
        dto.parameters.map { IlParameterImpl(it, this) }.toMutableList()
    }
    override val attributes: List<IlAttributeImpl> by lazy(PUBLICATION) {
        dto.attrs.map {
            IlAttributeImpl(
                it,
                publication
            )
        }
    }
    override val rawInstList: List<IlStmtDto>
        get() = dto.rawInstList

    override val instList: List<IlStmt>
        get() =
            publication.featuresChain.callUntilResolved<IlMethodExtFeature, IlInstListResult> {
                it.instList(
                    this
                )
            }!!.instructions


    val resolved: Boolean = dto.resolved

    // TODO args next to parameters seems defn improper
    val args: List<IlArgument> by lazy(PUBLICATION) {
        dto.parameters.mapIndexed { index, it ->
            if (index == 0 && !dto.isStatic) IlThis(declaringType) else
                IlArgumentImpl(
                    this,
                    it
                )
        }.toList()
    }
    val locals: List<IlLocalVar> by lazy(PUBLICATION) { dto.locals.map { IlLocalVar(it, publication) } }
    val temps: List<IlTempVar> by lazy(PUBLICATION) { dto.temps.map { IlTempVar(it, publication) } }
    val errs: List<IlErrVar> by lazy(PUBLICATION) { dto.errs.map { IlErrVar(it, publication) } }
    override val scopes: List<IlEhScope> by lazy(PUBLICATION) { dto.ehScopes.map { IlEhScope.deserialize(this, it) } }

    override fun toString(): String {
        return "$returnType $name(${parameters.joinToString(", ")})"
    }
}

abstract class IlEhScope {
    abstract val tb: IlStmt
    abstract val te: IlStmt
    abstract val hb: IlStmt
    abstract val he: IlStmt

    val throwers: MutableList<IlStmt> = mutableListOf()
    fun bindThrower(thrower: IlStmt) = throwers.add(thrower)

    companion object {
        fun deserialize(src: IlMethodImpl, dto: IlEhScopeDto): IlEhScope {
            return when (dto) {
                is IlFilterScopeDto -> IlFilterScope(
                    src.instList[dto.tb],
                    src.instList[dto.te],
                    src.instList[dto.hb],
                    src.instList[dto.he],
                    src.instList[dto.fb]
                )

                is IlCatchScopeDto -> IlCatchScope(
                    src.instList[dto.tb],
                    src.instList[dto.te],
                    src.instList[dto.hb],
                    src.instList[dto.he]
                )

                is IlFaultScopeDto -> IlFaultScope(
                    src.instList[dto.tb],
                    src.instList[dto.te],
                    src.instList[dto.hb],
                    src.instList[dto.he]
                )

                is IlFinallyScopeDto -> IlFinallyScope(
                    src.instList[dto.tb],
                    src.instList[dto.te],
                    src.instList[dto.hb],
                    src.instList[dto.he]
                )

                else -> throw NotImplementedError()
            }
        }
    }
}

class IlCatchScope(override val tb: IlStmt, override val te: IlStmt, override val hb: IlStmt, override val he: IlStmt) :
    IlEhScope()

class IlFilterScope(
    override val tb: IlStmt,
    override val te: IlStmt,
    override val hb: IlStmt,
    override val he: IlStmt,
    val fb: IlStmt
) : IlEhScope()

class IlFinallyScope(
    override val tb: IlStmt,
    override val te: IlStmt,
    override val hb: IlStmt,
    override val he: IlStmt
) :
    IlEhScope()

class IlFaultScope(override val tb: IlStmt, override val te: IlStmt, override val hb: IlStmt, override val he: IlStmt) :
    IlEhScope()
