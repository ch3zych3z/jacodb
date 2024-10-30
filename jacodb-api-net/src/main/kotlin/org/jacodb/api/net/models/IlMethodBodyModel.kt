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
import org.jacodb.api.net.models.IlModel.cacheKey

object IlMethodBodyModel : Ext(IlRoot) {
    private val IlExprDto = basestruct {
        field("type", cacheKey)
    }
    private val IlValueDto = basestruct extends IlExprDto {}
    private val IlConstDto = basestruct extends IlValueDto {}
    private val IlNumConstDto = basestruct extends IlConstDto {}

    private val IlByteConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.byte) }
    private val IlIntConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.int) }
    private val IlLongConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.long) }
    private val IlFloatConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.float) }
    private val IlDoubleConstDto = structdef extends IlNumConstDto { field("value", PredefinedType.double) }

    private val IlNullDto = structdef extends IlConstDto {}
    private val IlBoolConstDto = structdef extends IlConstDto { field("value", PredefinedType.bool) }
    private val IlStringConstDto = structdef extends IlConstDto { field("value", PredefinedType.string) }

    // TODO fix inconsistent naming
    private val IlTypeRefDto = structdef extends IlConstDto { field("referencedType", cacheKey) }
    private val IlMethodRefDto = structdef extends IlConstDto { field("method", cacheKey) }
    private val IlFieldRefDto = structdef extends IlConstDto { field("field", cacheKey) }

    private val IlUnaryOpDto = structdef extends IlExprDto { field("operand", IlExprDto) }
    private val IlBinaryOpDto = structdef extends IlExprDto {
        field("lhs", IlExprDto)
        field("rhs", IlExprDto)
    }

    private val IlInitExprDto = structdef extends IlExprDto {
    }
    private val IlNewExprDto = structdef extends IlExprDto {
        field("args", immutableList(IlExprDto))
    }

    private val IlSizeOfExprDto = structdef extends IlExprDto {
        field("targetType", cacheKey)
        // TODO use type as targetType
    }

    private val IlFieldAccessDto = structdef extends IlValueDto {
        field("instance", IlExprDto.nullable)
        field("field", cacheKey)
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
        field("method", cacheKey)
        field("args", immutableList(IlExprDto))
    }

    private val IlCastExprDto = basestruct extends IlExprDto {
        field("targetType", cacheKey) // TODO mb same with ilexpr type
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

    val IlStmtDto = basestruct {}

    private val IlAssignStmtDto = structdef extends IlStmtDto {
        field("lhs", IlValueDto)
        field("rhs", IlExprDto)
    }

    private val IlCallStmtDto = structdef extends IlStmtDto {
        field("call", IlCallDto)
    }

    private val IlReturnStmtDto = structdef extends IlStmtDto {
        field("retVal", IlExprDto.nullable)
    }

    private val IlEhStmtDto = structdef extends IlStmtDto {}

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

