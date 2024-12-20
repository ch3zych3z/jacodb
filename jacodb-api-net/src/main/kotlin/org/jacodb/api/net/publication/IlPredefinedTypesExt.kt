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

package org.jacodb.api.net.publication

import org.jacodb.api.net.IlPublication
import org.jacodb.api.net.generated.models.TypeId
import org.jacodb.api.net.ilinstances.IlType
import org.jacodb.api.net.publication.IlPredefinedAsmsExt.mscorelib


object IlPredefinedAsmsExt {
    fun IlPublication.mscorelib(): String? {
        return referencedAsmLocations.keys.single {
            it.contains("System.Private.CoreLib")
        }.let {
            findAsmNameByLocationOrNull(it)
        }
    }
}

object IlPredefinedTypesExt {
    fun IlPublication.void(): IlType =
        findIlTypeOrNull(TypeId(asmName = mscorelib()!!, typeName = "System.Void", typeArgs = emptyList()))!!

    fun IlPublication.int32(): IlType =
        findIlTypeOrNull(TypeId(asmName = mscorelib()!!, typeName = "System.Int32", typeArgs = emptyList()))!!

    fun IlPublication.uint32(): IlType =
        findIlTypeOrNull(TypeId(asmName = mscorelib()!!, typeName = "System.UInt32", typeArgs = emptyList()))!!

    fun IlPublication.nint(): IlType =
        findIlTypeOrNull(TypeId(asmName = mscorelib()!!, typeName = "System.IntPtr", typeArgs = emptyList()))!!

    fun IlPublication.nuint(): IlType =
        findIlTypeOrNull(TypeId(asmName = mscorelib()!!, typeName = "System.UIntPtr", typeArgs = emptyList()))!!

}