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
        field("id", cacheKey)
        field("declType", cacheKey.nullable)
        field("returnType", cacheKey.nullable)
        field("name", PredefinedType.string)
        field("parameters", immutableList(IlParameterDto))
        field("resolved", PredefinedType.bool)
        field("locals", immutableList(IlLocalVarDto))
        field("temps", immutableList(IlTempVarDto))
        field("errs", immutableList(IlErrVarDto))
        field("ehScopes", immutableList(IlEhScopeDto))
        field("body", immutableList(IlMethodBodyModel.IlStmtDto))
    }
}