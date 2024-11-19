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

package org.example.ilinstances

import org.jacodb.api.net.devmocs.IlClasspathMock
import org.jacodb.api.net.generated.models.*
import org.jacodb.api.net.ilinstances.*
import kotlin.LazyThreadSafetyMode.PUBLICATION

class IlMethod(private val declType: IlType, private val dto: IlMethodDto, classpath: IlClasspathMock) : IlInstance {
    val returnType: IlType? by lazy { classpath.findType(dto.returnType) }
    val name: String = dto.name
    val parametes: List<IlParameter> by lazy(PUBLICATION) { dto.parameters.map { IlParameter(it, classpath) }.toMutableList() }
    val attributes: List<IlAttribute> by lazy(PUBLICATION) { dto.attrs.map { IlAttribute(it, classpath) } }
    val body: List<IlStmt> by lazy(PUBLICATION) { dto.body.map { IlStmt.deserialize(this, it) } }
    val resolved: Boolean = dto.resolved

    // TODO args next to parameters seems defn improper
//    val args: List<IlArgument> = dto.parameters.map { IlArgument(it) }.toList()
    val locals: MutableList<IlLocalVar> = mutableListOf()
    val temps: MutableList<IlTempVar> = mutableListOf()
    val errs: MutableList<IlErrVar> = mutableListOf()
    val scopes: MutableList<IlEhScope> = mutableListOf()

    override fun toString(): String {
        return "${returnType ?: ""} $name(${parametes.joinToString(", ")})"
    }
}

class IlParameter(private val dto: IlParameterDto, classpath: IlClasspathMock) : IlInstance {
    val paramType: IlType by lazy(PUBLICATION) { classpath.findType(dto.type)!! }
    val attributes: List<IlAttribute> by lazy(PUBLICATION) { dto.attrs.map { IlAttribute(it, classpath) } }
    val index: Int = dto.index
    val name: String = dto.name
    val defaultValue: IlConst get() = dto.defaultValue.deserializeConst()

    override fun toString(): String {
        return paramType.toString()
    }
}

abstract class IlEhScope {
    abstract val tb: IlStmt
    abstract val te: IlStmt
    abstract val hb: IlStmt
    abstract val he: IlStmt

    companion object {
        fun deserialize(src: IlMethod, dto: IlEhScopeDto): IlEhScope {
            return when (dto) {
                is IlFilterScopeDto -> IlFilterScope(
                    src.body[dto.tb],
                    src.body[dto.te - 1],
                    src.body[dto.hb],
                    src.body[dto.he - 1],
                    src.body[dto.fb]
                )

                is IlCatchScopeDto -> IlCatchScope(
                    src.body[dto.tb],
                    src.body[dto.te - 1],
                    src.body[dto.hb],
                    src.body[dto.he - 1]
                )

                is IlFaultScopeDto -> IlFaultScope(
                    src.body[dto.tb],
                    src.body[dto.te - 1],
                    src.body[dto.hb],
                    src.body[dto.he - 1]
                )

                is IlFinallyScopeDto -> IlFinallyScope(
                    src.body[dto.tb],
                    src.body[dto.te - 1],
                    src.body[dto.hb],
                    src.body[dto.he - 1]
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
