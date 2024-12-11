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

package org.jacodb.api.net.cfg

import org.jacodb.api.common.cfg.BytecodeGraph
import org.jacodb.api.net.ilinstances.IlBranchStmt
import org.jacodb.api.net.ilinstances.IlMethod
import org.jacodb.api.net.ilinstances.IlStmt
import org.jacodb.api.net.ilinstances.IlTerminatingStmt
import java.util.Collections.singleton

interface IlGraph : BytecodeGraph<IlStmt> {
    val method: IlMethod
}

class IlGraphImpl(override val method: IlMethod, override val instructions: List<IlStmt>) : IlGraph {

    private val predecessorMap = hashMapOf<IlStmt, Set<IlStmt>>()
    private val successorMap = hashMapOf<IlStmt, Set<IlStmt>>()

//    private val throwPredecessors = hashMapOf<IlCat, Set<IlStmt>>()
//    private val throwSuccessors = hashMapOf<IlStmt, Set<JcCatchInst>>()

    init {
        instructions.forEachIndexed { index, inst ->
            val successors: Set<IlStmt> = when (inst) {
                is IlTerminatingStmt -> emptySet()
                is IlBranchStmt -> inst.sucessors.map { instructions[it] }.toSet()
                else -> if (index + 1 < instructions.size) setOf(instructions[index + 1]) else emptySet()
            }
            successorMap[inst] = successors
            successors.forEach { succ -> predecessorMap.add(succ, inst) }
        }
    }

    override val entries: List<IlStmt> = if (instructions.isEmpty()) emptyList() else listOf(instructions.first())
    override val exits: List<IlStmt> by lazy {
        instructions.filterIsInstance<IlTerminatingStmt>()
    }


    override fun throwers(node: IlStmt): Set<IlStmt> {
        TODO("Not yet implemented")
    }

    override fun catchers(node: IlStmt): Set<IlStmt> {
        TODO("Not yet implemented")
    }

    override fun successors(node: IlStmt): Set<IlStmt> = successorMap[node] ?: emptySet()

    override fun predecessors(node: IlStmt): Set<IlStmt> = predecessorMap[node] ?: emptySet()
    private fun <KEY, VALUE> MutableMap<KEY, Set<VALUE>>.add(key: KEY, value: VALUE) {
        val current = this[key]
        if (current == null) {
            this[key] = singleton(value)
        } else {
            this[key] = current + value
        }
    }
}
