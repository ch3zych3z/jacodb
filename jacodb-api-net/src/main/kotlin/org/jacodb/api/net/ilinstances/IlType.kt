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

package org.example.ilinstances

import org.jacodb.api.net.devmocs.IlClasspathMock
import org.jacodb.api.net.generated.models.*
import kotlin.LazyThreadSafetyMode.*
import org.jacodb.api.net.ilinstances.IlAttribute
import org.jacodb.api.net.ilinstances.IlField

// classpath should be public in particular to resolve refs inside TAC
open class IlType(private val dto: IlTypeDto, val classpath: IlClasspathMock) : IlInstance {
    val declType: IlType? by lazy(PUBLICATION) { classpath.findType(dto.declType) }
    val namespace: String = dto.namespaceName
    val name: String = dto.name
    val attributes: List<IlAttribute> by lazy(PUBLICATION) { dto.attrs.map { IlAttribute(it, classpath) } }
    val genericArgs: List<IlType> by lazy(PUBLICATION) { dto.genericArgs.map { classpath.findType(it)!! } }
    val fields: List<IlField> by lazy(PUBLICATION) { dto.fields.map { IlField(this, it, classpath) } }
    val methods: List<IlMethod> by lazy(PUBLICATION) { dto.methods.map { IlMethod(this, it, classpath) } }


    override fun toString(): String {
        return name
    }
}

class IlPointerType(private val dto: IlPointerTypeDto, classpath: IlClasspathMock): IlType(dto, classpath)
open class IlValueType(private val dto: IlValueTypeDto, classpath: IlClasspathMock) : IlType(dto, classpath)
class IlEnumType(private val dto: IlEnumTypeDto, classpath: IlClasspathMock) : IlType(dto, classpath)
class IlPrimitiveType(private val dto: IlPrimitiveTypeDto, classpath: IlClasspathMock) : IlType(dto, classpath)
class IlStructType(private val dto: IlStructTypeDto, classpath: IlClasspathMock) : IlType(dto, classpath)

open class IlReferenceType(private val dto: IlReferenceTypeDto, classpath: IlClasspathMock) : IlType(dto, classpath)
class IlArrayType(private val dto: IlArrayTypeDto, classpath: IlClasspathMock) : IlType(dto, classpath)
class IlClassType(private val dto: IlClassTypeDto, classpath: IlClasspathMock) : IlType(dto, classpath)
class IlManagedReference(private val dto: IlManagedReferenceDto, classpath: IlClasspathMock) : IlType(dto, classpath)


