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

@file:Suppress("unused")

package org.jacodb.api.net.models

import com.jetbrains.rd.generator.nova.*
import org.jacodb.api.net.models.IlModel.typeId

object IlMethodBodyModel : Ext(IlRoot) {
    private val instanceIdRef = structdef {
        field("type", typeId)
        field("instanceToken", PredefinedType.int)
    }
    private val IlExprDto = basestruct {
        field("type", typeId)
    }
    private val IlValueDto = basestruct extends IlExprDto {}
    val IlConstDto = basestruct extends IlValueDto {}
    private val IlNumConstDto = basestruct extends IlConstDto {}

    private val IlInt8ConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.int8) }
    private val IlUint8ConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.uint8) }

    private val IlInt16ConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.int16) }
    private val IlUint16ConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.uint16) }

    private val IlInt32ConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.int32) }
    private val IlUint32ConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.uint32) }

    private val IlInt64ConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.int64) }
    private val IlUint64ConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.uint64) }

    private val IlFloatConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.float) }
    private val IlDoubleConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.double) }

    private val IlCharConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.char) }

    val IlArrayConstDto = structdef extends IlConstDto {
        field("values", immutableList(IlConstDto))
    }
    val IlEnumConstDto = structdef extends IlConstDto {
        field("underlyingType", typeId)
        field("underlyingValue", IlConstDto)

    }
    private val IlNullDto = structdef extends IlConstDto {}
    private val IlBoolConstDto = structdef extends IlConstDto { field("value", PredefinedType.bool) }
    private val IlStringConstDto = structdef extends IlConstDto { field("value", PredefinedType.string) }

    private val IlTypeRefDto = structdef extends IlConstDto { field("referencedType", typeId) }
    private val IlMethodRefDto = structdef extends IlConstDto { field("method", instanceIdRef) }
    private val IlFieldRefDto = structdef extends IlConstDto { field("field", instanceIdRef) }

    private val IlUnaryOpDto = structdef extends IlExprDto { field("operand", IlExprDto) }
    private val IlBinaryOpDto = structdef extends IlExprDto {
        field("lhs", IlExprDto)
        field("rhs", IlExprDto)
    }

    private val IlNewExprDto = structdef extends IlExprDto {
    }

    private val IlSizeOfExprDto = structdef extends IlExprDto {
        field("targetType", typeId)
        // TODO use type as targetType
    }

    private val IlFieldAccessDto = structdef extends IlValueDto {
        field("instance", IlExprDto.nullable)
        field("field", instanceIdRef)
    }
    private val IlArrayAccessDto = structdef extends IlValueDto {
        field("array", IlExprDto)
        field("index", IlExprDto)
    }

    private val IlNewArrayExprDto = structdef extends IlExprDto {
        field("size", IlExprDto)
        // type == elemType
    }
    private val IlArrayLengthExprDto = structdef extends IlExprDto { field("array", IlExprDto) }
    private val IlCallDto = structdef extends IlExprDto {
        field("method", instanceIdRef)
        field("args", immutableList(IlExprDto))
    }

    private val IlCastExprDto = basestruct extends IlExprDto {
        field("targetType", typeId)
        field("operand", IlExprDto)
    }

    private val IlConvExprDto = structdef extends IlCastExprDto {}
    private val IlBoxExprDto = structdef extends IlCastExprDto {}
    private val IlUnboxExprDto = structdef extends IlCastExprDto {}
    private val IlCastClassExprDto = structdef extends IlCastExprDto {}
    private val IlIsInstExprDto = structdef extends IlCastExprDto {}

    // TODO("seems improper")
    private val IlRefExprDto = basestruct extends IlValueDto {
        field("value", IlExprDto)
    }
    private val IlDerefExprDto = basestruct extends IlValueDto {
        field("value", IlExprDto)
    }

    private val IlManagedRefExprDto = structdef extends IlRefExprDto {}
    private val IlUnmanagedRefExprDto = structdef extends IlRefExprDto {}
    private val IlManagedDerefExprDto = structdef extends IlDerefExprDto {}
    private val IlUnmanagedDerefExprDto = structdef extends IlDerefExprDto {}

    private val IlStackAllocExprDto = structdef extends IlExprDto {
        field("size", IlExprDto)
    }
    private val IlArgListRefDto = structdef extends IlExprDto {
        field("method", instanceIdRef)
    }
    private val IlCalliDto = structdef extends IlExprDto {
        field("signature", IlModel.IlSignatureDto)
        field("ftn", IlExprDto)
        field("args", immutableList(IlExprDto))
    }
    val IlStmtDto = basestruct {}

    private val IlAssignStmtDto = structdef extends IlStmtDto {
        field("lhs", IlValueDto)
        field("rhs", IlExprDto)
    }

    private val IlCallStmtDto = structdef extends IlStmtDto {
        field("call", IlCallDto)
    }
    private val IlCalliStmtDto = structdef extends IlStmtDto {
        field("calli", IlCalliDto)
    }
    private val IlReturnStmtDto = structdef extends IlStmtDto {
        field("retVal", IlExprDto.nullable)
    }

    private val IlEhStmtDto = basestruct extends IlStmtDto {}
    private val IlThrowStmtDto = structdef extends IlEhStmtDto {
        field("value", IlExprDto)
    }
    private val IlRethrowStmtDto = structdef extends IlEhStmtDto {}
    private val IlEndFinallyStmtDto = structdef extends IlEhStmtDto {}
    private val IlEndFaultStmtDto = structdef extends IlEhStmtDto {}
    private val IlEndFilterStmtDto = structdef extends IlEhStmtDto {
        field("value", IlExprDto)
    }

    private val IlBranchStmtDto = basestruct extends IlStmtDto {
        field("target", PredefinedType.int)
    }

    private val IlGotoStmtDto = structdef extends IlBranchStmtDto {
    }

    private val IlIfStmtDto = structdef extends IlBranchStmtDto {
        field("cond", IlExprDto)
    }

    private val IlVarKind = enum {
        +"local"
        +"temp"
        +"err"
    }
    private val IlArgAccessDto = structdef extends IlValueDto {
        field("index", PredefinedType.int)
    }
    private val IlVarAccessDto = structdef extends IlValueDto {
        field("kind", IlVarKind)
        field("index", PredefinedType.int)
    }
}

