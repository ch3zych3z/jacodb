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

const val folder = "Il"

object IlRoot : Root()


object IlModel : Ext(IlRoot) {

    val cacheKey = structdef {
        field("asm", PredefinedType.int)
        field("mod", PredefinedType.int)
        field("inst", PredefinedType.int)
    }

    private val asmCacheKey = structdef {
        field("asm", PredefinedType.int)
    }

    val IlDto = basestruct {}

    private val IlAsmDto = structdef extends IlDto {
        field("id", asmCacheKey)
        field("location", PredefinedType.string)
    }

    private val IlTypeDto = structdef extends IlDto {
        field("id", cacheKey)
        field("name", PredefinedType.string)
        field("genericArgs", immutableList(cacheKey))
        field("isGenericParam", PredefinedType.bool)
        field("isValueType", PredefinedType.bool)
        field("isManaged", PredefinedType.bool)
    }

    private val IlFieldDto = structdef extends IlDto {
        field("id", cacheKey)
        field("declType", cacheKey)
        field("fieldType", cacheKey)
        field("isStatic", PredefinedType.bool)
        field("name", PredefinedType.string)
    }

    private val IlParameterDto = structdef {
        field("index", PredefinedType.int)
        field("type", cacheKey)
        field("name", PredefinedType.string)
        field("defaultValue", PredefinedType.string.nullable)
    }
    private val IlVarDto = basestruct extends IlDto {
        field("type", cacheKey)
        field("index", PredefinedType.int)
    }

    private val IlLocalVarDto = structdef extends IlVarDto {
    }
    private val IlTempVarDto = structdef extends IlVarDto {
    }
    private val IlErrVarDto = structdef extends IlVarDto {}
    private val IlMethodDto = structdef extends IlDto {
        field("id", cacheKey)
        field("declType", cacheKey.nullable)
        field("returnType", cacheKey.nullable)
        field("name", PredefinedType.string)
        field("parameters", immutableList(IlParameterDto))
        field("resolved", PredefinedType.bool)
        field("locals", immutableList(IlLocalVarDto))
        field("temps", immutableList(IlTempVarDto))
        field("errs", immutableList(IlErrVarDto))
        field("body", immutableList(IlMethodBodyModel.IlStmtDto))
    }


}

object IlMethodBodyModel : Ext(IlRoot) {
    private val IlExprDto = basestruct {
        field("type", IlModel.cacheKey)
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


    private val IlTypeRefDto = structdef extends IlConstDto { field("referencedType", IlModel.cacheKey) }
    private val IlMethodRefDto = structdef extends IlConstDto { field("method", IlModel.cacheKey) }
    private val IlFieldRefDto = structdef extends IlConstDto { field("field", IlModel.cacheKey) }

    private val IlUnaryOpDto = structdef extends IlExprDto { field("operand", IlExprDto) }
    private val IlBinaryOpDto = structdef extends IlExprDto {
        field("lhs", IlExprDto)
        field("rhs", IlExprDto)
    }

    private val IlInitExprDto = structdef extends IlExprDto {
        field("targetType", IlModel.cacheKey)
    }
    private val IlNewExprDto = structdef extends IlExprDto {
        field("targetType", IlModel.cacheKey)
        field("args", immutableList(IlExprDto))
    }

    private val IlSizeOfExprDto = structdef extends IlExprDto {
        field("targetType", IlModel.cacheKey)
    }

    private val IlFieldAccessDto = structdef extends IlValueDto {
        field("instance", IlExprDto.nullable)
        field("field", IlModel.cacheKey)
    }
    private val IlArrayAccessDto = structdef extends IlValueDto {
        field("array", IlExprDto)
        field("index", IlExprDto)
    }

    private val IlNewArrayExprDto = structdef extends IlExprDto {
        field("elementType", IlModel.cacheKey)
        field("size", IlExprDto)
    }
    private val IlArrayLengthExprDto = structdef extends IlExprDto { field("array", IlExprDto) }
    private val IlCallDto = structdef extends IlExprDto {
        field("method", IlModel.cacheKey)
        field("args", immutableList(IlModel.cacheKey))
    }

    private val IlCastExprDto = basestruct extends IlExprDto {}
    private val IlConvExprDto = structdef extends IlExprDto {}
    private val IlBoxExprDto = structdef extends IlExprDto {}
    private val IlUnboxExprDto = structdef extends IlExprDto {}
    private val IlCastClassExprDto = structdef extends IlExprDto {}
    private val IlIsInstExprDto = structdef extends IlExprDto {}

    private val IlRefExprDto = basestruct extends IlValueDto {}
    private val IlDerefExprDto = basestruct extends IlValueDto {}

    private val IlManagedRefExprDto = basestruct extends IlRefExprDto {}
    private val IlUnmanagedRefExprDto = basestruct extends IlRefExprDto {}
    private val IlManagedDerefExprDto = basestruct extends IlDerefExprDto {}
    private val IlUnmanagedDerefExprDto = basestruct extends IlDerefExprDto {}

    private val IlStackAllocExprDto = structdef extends IlExprDto {}

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

    private val IlBranchStmtDto = basestruct extends IlStmtDto {}

    private val IlGotoStmtDto = structdef extends IlBranchStmtDto {
        field("target", PredefinedType.int)
    }

    private val IlIfStmtDto = structdef extends IlBranchStmtDto {
        field("target", PredefinedType.int)
        field("cond", IlExprDto)
    }
    private val IlVarKind = enum {
        const("local", PredefinedType.int, 0)
        const("temp", PredefinedType.int, 1)
        const("err", PredefinedType.int, 2)
    }
    private val IlArgAccessDto = structdef extends IlExprDto {
        field("index", PredefinedType.int)
    }
    private val IlVarAccessDto = structdef extends IlExprDto {
        field("kind", IlVarKind)
        field("index", PredefinedType.int)
    }
}

object IlSigModel : Ext(IlModel) {
    private val request = structdef {
        field("rootAsm", PredefinedType.string)
    }

    init {
        signal("asmRequest", request).apply { async }
        signal("asmResponse", immutableList(IlModel.IlDto)).apply { async }
    }
}