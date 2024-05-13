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

package parser

import org.jacodb.panda.dynamic.parser.IRParser
import org.jacodb.panda.dynamic.parser.TSParser
import org.jacodb.panda.dynamic.parser.dumpDot
import java.io.File

fun loadIr(filePath: String): IRParser {
    val sampleFilePath = object {}::class.java.getResource(filePath)?.path
        ?: error("Resource not found: $filePath")
    return IRParser(sampleFilePath)
}

fun loadIrWithTs(filePath: String, tsPath: String): IRParser {
    val sampleFilePath = object {}::class.java.getResource(filePath)?.path
        ?: error("Resource not found: $filePath")
    val sampleTSPath = object {}::class.java.getResource(tsPath)?.toURI()
        ?: error("Resource not found: $tsPath")
    val tsParser = TSParser(sampleTSPath)
    val tsFunctions = tsParser.collectFunctions()
    return IRParser(sampleFilePath, tsFunctions)
}

object DumpIrToDot {
    @JvmStatic
    fun main(args: Array<String>) {
        val name = "PhiTest"
        val parser = loadIrWithTs("/samples/$name.json", "/samples/$name.ts")
        val program = parser.getProgram()
        val project = parser.getProject()
        println(program)
        println(project)

        val path = "dump"
        val dotFile = File("$path.dot")
        program.dumpDot(dotFile)
        println("Generated DOT file: ${dotFile.absolutePath}")
        for (format in listOf("pdf")) {
            val formatFile = File("$path.$format")
            val p = Runtime.getRuntime().exec("dot -T$format $dotFile -o $formatFile")
            p.waitFor()
            print(p.inputStream.bufferedReader().readText())
            print(p.errorStream.bufferedReader().readText())
            println("Generated ${format.uppercase()} file: ${formatFile.absolutePath}")
        }
    }
}
