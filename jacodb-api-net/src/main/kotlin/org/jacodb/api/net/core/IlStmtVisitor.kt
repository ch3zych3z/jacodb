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

package org.jacodb.api.net.core

import org.jacodb.api.net.ilinstances.*

interface IlStmtVisitor<out T> {
    fun visitIlAssignStmt(stmt: IlAssignStmt): T
    fun visitIlCallStmt(stmt: IlCallStmt): T
    fun visitIlReturnStmt(stmt: IlReturnStmt): T
    fun visitIlEndFinallyStmt(stmt: IlEndFinallyStmt): T
    fun visitIlEndFaultStmt(stmt: IlEndFaultStmt): T
    fun visitIlRethrowStmt(stmt: IlRethrowStmt): T
    fun visitIlEndFilterStmt(stmt: IlEndFilterStmt): T
    fun visitIlThrowStmt(stmt: IlThrowStmt): T
    fun visitIlGotoStmt(stmt: IlGotoStmt): T
    fun visitIlIfStmt(stmt: IlIfStmt): T
    
    interface Default<out T> : IlStmtVisitor<T> {
        fun visitDefault(stmt: IlStmt) : T

        override fun visitIlAssignStmt(stmt: IlAssignStmt): T = visitDefault(stmt)
        override fun visitIlCallStmt(stmt: IlCallStmt): T = visitDefault(stmt)
        override fun visitIlReturnStmt(stmt: IlReturnStmt): T = visitDefault(stmt)
        override fun visitIlEndFinallyStmt(stmt: IlEndFinallyStmt): T = visitDefault(stmt)
        override fun visitIlEndFaultStmt(stmt: IlEndFaultStmt): T = visitDefault(stmt)
        override fun visitIlRethrowStmt(stmt: IlRethrowStmt): T = visitDefault(stmt)
        override fun visitIlEndFilterStmt(stmt: IlEndFilterStmt): T = visitDefault(stmt)
        override fun visitIlThrowStmt(stmt: IlThrowStmt): T = visitDefault(stmt)
        override fun visitIlGotoStmt(stmt: IlGotoStmt): T = visitDefault(stmt)
        override fun visitIlIfStmt(stmt: IlIfStmt): T = visitDefault(stmt)
    }
}
