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

package org.jacodb.panda.dynamic.parser

import org.jacodb.panda.dynamic.parser.ByteCodeParser.Companion.toByteBuffer

class IrBcConnector(
    private val bcParser: ByteCodeParser
) {

    private fun mapOffset(numArgs: ULong, bc: String): Int {
        val initOffset = numArgs.toInt() * 2
        return Integer.decode(bc) - initOffset
    }

    fun getLdName(currentFuncName: String, bc: String): String {
        val code = bcParser.getMethodCodeByName(currentFuncName)
        return code.getResolvedValue(mapOffset(code.numArgs, bc)).toByteArray().toString()
    }

    fun getCallArgFuncName(currentFuncName: String, bc: String): String {
        val code = bcParser.getMethodCodeByName(currentFuncName)
        return code.getAccValueByOffset(mapOffset(code.numArgs, bc)).toByteArray().toString()
    }
}