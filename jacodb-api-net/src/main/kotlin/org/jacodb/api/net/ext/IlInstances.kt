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

package org.jacodb.api.net.ext

import org.jacodb.api.net.core.IlStmtVisitor
import org.jacodb.api.net.ilinstances.IlCall
import org.jacodb.api.net.ilinstances.IlStmt

object CallVisitor : IlStmtVisitor.Default<IlCall?> {
    override fun visitDefault(stmt: IlStmt): IlCall? {
        return stmt.operands.filterIsInstance<IlCall>().firstOrNull()
    }
}

val IlStmt.call : IlCall?
    get() = accept(CallVisitor)