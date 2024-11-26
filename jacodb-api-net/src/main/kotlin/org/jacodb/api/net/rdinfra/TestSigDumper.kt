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

package org.jacodb.api.net.rdinfra

import org.jacodb.api.net.generated.models.IlTypeDto
import java.io.File
import kotlin.io.path.createTempFile

object TestSigDumper {
    fun dump(types: List<IlTypeDto>) {
        val tmpPath = createTempFile()
        val tmpFile = tmpPath.toFile()
        println(tmpPath)
        println(tmpFile.absolutePath)
        val writer = tmpFile.printWriter()
        val sep = System.lineSeparator()
        types.sortedBy { it.name }.forEach { t ->
            writer.write(t.name + sep)
            t.fields.sortedBy { it.name }.forEach { f -> writer.write("${f.fieldType.typeName} ${f.name}${sep}") }
            t.methods.sortedBy { it.name }
                .forEach { m -> writer.write("${m.returnType!!.typeName} ${m.name} ${m.parameters.size}$sep") }
        }
        writer.close()
    }
}
