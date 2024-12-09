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

object IlModel : Ext(IlRoot) {
    val typeId = structdef {
        field("asmName", PredefinedType.string)
        field("typeName", PredefinedType.string)
    }

    val IlDto = basestruct {}

    val IlTypeDto = basestruct extends IlDto {
        field("asmName", PredefinedType.string)
        field("namespaceName", PredefinedType.string)
        field("name", PredefinedType.string)
        field("fullname", PredefinedType.string)
        field("declType", typeId.nullable)
        field("baseType", typeId.nullable)
        field("interfaces", immutableList(typeId))
        field("genericArgs", immutableList(typeId))
        field("isGenericParam", PredefinedType.bool)
        field("isGenericDefinition", PredefinedType.bool)
        field("isValueType", PredefinedType.bool)
        field("isManaged", PredefinedType.bool)
        field("attrs", immutableList(IlAttrDto))
        field("fields", immutableList(IlFieldDto))
        field("methods", immutableList(IlMethodDto))
    }

    val IlPointerTypeDto = structdef extends IlTypeDto {
        field("targetType", typeId)
    }
    val IlValueTypeDto = basestruct extends IlTypeDto {}
    val IlPrimitiveTypeDto = structdef extends IlValueTypeDto {}
    val IlEnumTypeDto = structdef extends IlValueTypeDto {
        // TODO name to value mapping
        field("underlyingType", typeId)
        field("names", immutableList(PredefinedType.string))
        field("values", immutableList(IlMethodBodyModel.IlConstDto))
    }
    val IlStructTypeDto = structdef extends IlValueTypeDto {}

    val IlReferenceTypeDto = basestruct extends IlTypeDto {}

    val IlClassTypeDto = structdef extends IlReferenceTypeDto {}
    val IlArrayTypeDto = structdef extends IlReferenceTypeDto {
        field("elementType", typeId)
    }
    private val IlAttrDto = structdef extends IlDto {
        field("attrType", typeId)
        field("ctorArgs", immutableList(IlMethodBodyModel.IlConstDto))
        field("namedArgsNames", immutableList(PredefinedType.string))
        field("namedArgsValues", immutableList(IlMethodBodyModel.IlConstDto))
        field("genericArgs", immutableList(typeId))
    }

    private val IlFieldDto = structdef extends IlDto {
        field("fieldType", typeId)
        field("isStatic", PredefinedType.bool)
        field("name", PredefinedType.string)
        field("attrs", immutableList(IlAttrDto))
    }

    private val IlParameterDto = structdef {
        field("index", PredefinedType.int)
        field("type", typeId)
        field("name", PredefinedType.string)
        field("defaultValue", IlMethodBodyModel.IlConstDto.nullable)
        field("attrs", immutableList(IlAttrDto))
    }

    private val IlVarDto = basestruct extends IlDto {
        field("type", typeId)
        field("index", PredefinedType.int)
    }

    private val IlLocalVarDto = structdef extends IlVarDto {
        field("isPinned", PredefinedType.bool)
    }

    private val IlTempVarDto = structdef extends IlVarDto { }
    private val IlErrVarDto = structdef extends IlVarDto { }

    private val IlEhScopeDto = basestruct extends IlDto {
        field("tb", PredefinedType.int)
        field("te", PredefinedType.int)
        field("hb", PredefinedType.int)
        field("he", PredefinedType.int)
    }
    private val IlCatchScopeDto = structdef extends IlEhScopeDto {
    }
    private val IlFilterScopeDto = structdef extends IlEhScopeDto {
        field("fb", PredefinedType.int)
    }
    private val IlFaultScopeDto = structdef extends IlEhScopeDto { }
    private val IlFinallyScopeDto = structdef extends IlEhScopeDto { }


    private val IlMethodDto = structdef extends IlDto {
        field("returnType", typeId)
        field("attrs", immutableList(IlAttrDto))
        field("isStatic", PredefinedType.bool)
        field("name", PredefinedType.string)
        field("parameters", immutableList(IlParameterDto))
        field("resolved", PredefinedType.bool)
        field("locals", immutableList(IlLocalVarDto))
        field("temps", immutableList(IlTempVarDto))
        field("errs", immutableList(IlErrVarDto))
        field("ehScopes", immutableList(IlEhScopeDto))
        field("rawInstList", immutableList(IlMethodBodyModel.IlStmtDto))
    }

    val IlSignatureDto = structdef extends IlDto {
        field("returnType", typeId)
        field("isInstance", PredefinedType.bool)
        field("genericParamCount", PredefinedType.int)
        field("parametersTypes", immutableList(typeId))
    }
}