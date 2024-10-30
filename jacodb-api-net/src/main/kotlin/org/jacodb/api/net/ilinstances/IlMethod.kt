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

import org.jacodb.api.net.generated.models.IlBranchStmtDto
import org.jacodb.api.net.generated.models.IlMethodDto
import org.jacodb.api.net.generated.models.IlParameterDto
import org.jacodb.api.net.ilinstances.*

class IlMethod(private val dto: IlMethodDto) : IlInstance {
    var declType: IlType? = null
    var returnType: IlType? = null
    val name: String = dto.name
    val parametes: MutableList<IlParameter> = dto.parameters.map { IlParameter(it) }.toMutableList()
    val args: List<IlArgument> = dto.parameters.map { IlArgument(it) }.toList()
    val resolved: Boolean = dto.resolved
    val locals: MutableList<IlLocalVar> = mutableListOf()
    val temps: MutableList<IlTempVar> = mutableListOf()
    val errs: MutableList<IlErrVar> = mutableListOf()

    val body: MutableList<IlStmt> = mutableListOf()

    override fun attach() {
        if (dto.declType != null) {
            declType = IlInstance.cache.getType(dto.declType)
            (declType as IlType).methods.add(this)
        }
        if (dto.returnType != null)
            returnType = IlInstance.cache.getType(dto.returnType)
        parametes.forEach { it.attach() }
        dto.locals.map { IlLocalVar(it) }.sortedBy { it.index }.forEach { locals.add(it) }
        dto.temps.map { IlTempVar(it) }.sortedBy { it.index }.forEach { temps.add(it) }
        dto.errs.map { IlErrVar(it) }.sortedBy { it.index }.forEach { errs.add(it) }
        dto.body.forEach { body.add(IlStmt.deserialize(this, it)) }
        dto.body.zip(body).filter { (_, inst) -> inst is IlBranchStmt }
            .forEach { (dto, inst) -> inst as IlBranchStmt; dto as IlBranchStmtDto; inst.updateTarget(dto, this) }
    }

    override fun toString(): String {
        return "${returnType ?: ""} $name(${parametes.joinToString(", ")})"
    }
}

class IlParameter(private val dto: IlParameterDto) : IlInstance {
    lateinit var paramType: IlType
    val name: String = dto.name
    override fun attach() {
        paramType = IlInstance.cache.getType(dto.type)
    }

    override fun toString(): String {
        return paramType.toString()
    }
}