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

package org.jacodb.api.net.features

import org.jacodb.api.net.core.IlExprVisitor
import org.jacodb.api.net.core.IlStmtVisitor
import org.jacodb.api.net.ilinstances.*
import org.jacodb.api.net.ilinstances.impl.IlArrayType

object IlApproximationsInstSubstitutor : IlExprVisitor<IlExpr>, IlStmtVisitor<IlStmt> {
    override fun visitIlUnaryOp(expr: IlUnaryOp): IlExpr {
        return IlUnaryOp(expr.type, expr.operand.accept(this))
    }

    override fun visitIlBinaryOp(expr: IlBinaryOp): IlExpr {
        return IlBinaryOp(expr.type, expr.lhs.accept(this), expr.rhs.accept(this), expr.isChecked, expr.isUnsigned)
    }

    override fun visitIlArrayLength(expr: IlArrayLengthExpr): IlExpr {
        return IlArrayLengthExpr(expr.type, expr.array.accept(this))
    }

    override fun visitIlCall(expr: IlCall): IlExpr {
        TODO("Not yet implemented")
    }

    override fun visitIlNewArrayExpr(expr: IlNewArrayExpr): IlExpr {
        return IlNewArrayExpr(expr.type.eliminateApproximation() as IlArrayType, expr.size.accept(this))
    }

    override fun visitIlNewExpr(expr: IlNewExpr): IlExpr {
        return IlNewExpr(expr.type.eliminateApproximation())
    }

    override fun visitIlSizeOfExpr(expr: IlSizeOfExpr): IlExpr {
        return IlSizeOfExpr(expr.type, expr.observedType.eliminateApproximation())
    }

    override fun visitIlStackAllocExpr(expr: IlStackAllocExpr): IlExpr {
        return IlStackAllocExpr(expr.type.eliminateApproximation(), expr.size.accept(this))
    }

    override fun visitIlManagedRefExpr(expr: IlManagedRefExpr): IlExpr {
        return IlManagedRefExpr(expr.type.eliminateApproximation(), expr.value.accept(this))
    }

    override fun visitIlUnmanagedRefExpr(expr: IlUnmanagedRefExpr): IlExpr {
        return IlUnmanagedRefExpr(expr.type.eliminateApproximation(), expr.value.accept(this))
    }

    override fun visitIlManagedDerefExpr(expr: IlManagedDerefExpr): IlExpr {
        return IlManagedDerefExpr(expr.type.eliminateApproximation(), expr.value.accept(this))
    }

    override fun visitIlUnmanagedDerefExpr(expr: IlUnmanagedDerefExpr): IlExpr {
        return IlUnmanagedDerefExpr(expr.type.eliminateApproximation(), expr.value.accept(this))
    }

    override fun visitIlConvExpr(expr: IlConvCastExpr): IlExpr {
        return IlConvCastExpr(expr.expectedType.eliminateApproximation(), expr.operand.accept(this))
    }

    override fun visitIlBoxExpr(expr: IlBoxExpr): IlExpr {
        return IlBoxExpr(expr.expectedType.eliminateApproximation(), expr.operand.accept(this))
    }

    override fun visitIlUnboxExpr(expr: IlUnboxExpr): IlExpr {
        return IlUnboxExpr(expr.expectedType.eliminateApproximation(), expr.operand.accept(this))
    }

    override fun visitIlIsInstExpr(expr: IlIsInstExpr): IlExpr {
        return IlIsInstExpr(expr.expectedType.eliminateApproximation(), expr.operand.accept(this))
    }

    override fun visitIlFieldAccess(expr: IlFieldAccess): IlExpr {
        // TODO eliminate from field
        return IlFieldAccess(expr.field, expr.instance?.accept(this) as IlValue?)
    }

    override fun visitIlArrayAccess(expr: IlArrayAccess): IlExpr {
        return IlArrayAccess(expr.array.accept(this) as IlValue, expr.index.accept(this) as IlValue)
    }

    override fun visitIlLocalVar(expr: IlLocalVar): IlExpr {
        return IlLocalVar(expr.type.eliminateApproximation(), expr.index)
    }

    override fun visitIlTempVar(expr: IlTempVar): IlExpr {
        return IlTempVar(expr.type.eliminateApproximation(), expr.index)
    }

    override fun visitErrVar(expr: IlErrVar): IlExpr {
        return IlErrVar(expr.type.eliminateApproximation(), expr.index)
    }

    override fun visitIlArg(expr: IlArgument): IlExpr {
        TODO("Not yet implemented")

    }

    override fun visitIlNullConst(const: IlNull): IlExpr = const
    override fun visitIlBoolConst(const: IlBoolConstant): IlExpr = const
    override fun visitIlStringConst(const: IlStringConstant): IlExpr = const
    override fun visitIlCharConst(const: IlCharConstant): IlExpr = const

    override fun visitIlInt8Const(const: IlInt8Constant): IlExpr = const
    override fun visitIlInt16Const(const: IlInt16Constant): IlExpr = const
    override fun visitIlInt32Const(const: IlInt32Constant): IlExpr = const
    override fun visitIlInt64Const(const: IlInt64Constant): IlExpr = const
    override fun visitIlUInt8Const(const: IlUInt8Constant): IlExpr = const
    override fun visitIlUInt16Const(const: IlUInt16Constant): IlExpr = const
    override fun visitIlUInt32Const(const: IlUInt32Constant): IlExpr = const
    override fun visitIlUInt64Const(const: IlUInt64Constant): IlExpr = const
    override fun visitIlFloatConst(const: IlFloatConstant): IlExpr = const
    override fun visitIlDoubleConst(const: IlDoubleConstant): IlExpr = const
    override fun visitIlEnumConst(const: IlEnumConstant): IlExpr =
        IlEnumConstant(
            const.type.eliminateApproximation(),
            const.enumType.eliminateApproximation(),
            const.underlyingConst.accept(this) as IlConstant
        )

    override fun visitIlTypeRefConst(const: IlTypeRef): IlExpr {
        return IlTypeRef(const.type, const.referencedType.eliminateApproximation())
    }

    override fun visitIlMethodRefConst(const: IlMethodRef): IlExpr = const

    override fun visitIlFieldRefConst(const: IlFieldRef): IlExpr {
        // instance info??
        TODO()
    }

    override fun visitIlArrayConst(const: IlArrayConstant): IlExpr {
        val newValues = const.values.map { it.accept(this) as IlConstant }
        return IlArrayConstant(const.type, newValues)
    }

    override fun visitIlAssignStmt(stmt: IlAssignStmt): IlStmt {
        return IlAssignStmt(stmt.lhv.accept(this) as IlValue, stmt.rhv.accept(this))
    }

    override fun visitIlCallStmt(stmt: IlCallStmt): IlStmt {
        return IlCallStmt(stmt.call.accept(this) as IlCall)
    }

    override fun visitIlReturnStmt(stmt: IlReturnStmt): IlStmt {
        return IlReturnStmt(stmt.value?.accept(this))
    }

    override fun visitIlEndFinallyStmt(stmt: IlEndFinallyStmt): IlStmt {
        return stmt
    }

    override fun visitIlEndFaultStmt(stmt: IlEndFaultStmt): IlStmt {
        return stmt
    }

    override fun visitIlRethrowStmt(stmt: IlRethrowStmt): IlStmt {
        return stmt
    }

    override fun visitIlEndFilterStmt(stmt: IlEndFilterStmt): IlStmt {
        return IlEndFilterStmt(stmt.value.accept(this))
    }

    override fun visitIlThrowStmt(stmt: IlThrowStmt): IlStmt {
        return IlThrowStmt(stmt.value.accept(this))
    }

    // TODO CRITICAL
    override fun visitIlGotoStmt(stmt: IlGotoStmt): IlStmt {
        return stmt //IlGotoStmt(stmt.target.accept(this))
    }

    override fun visitIlIfStmt(stmt: IlIfStmt): IlStmt {
        return stmt // IlIfStmt(stmt.target.accept(this), stmt.condition.accept(this))
    }
}
