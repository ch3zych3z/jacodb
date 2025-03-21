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

package org.jacodb.api.net.ilinstances.ext

import org.jacodb.api.net.ilinstances.IlType

fun IlType.isAssignableTo(type: IlType): Boolean = type.isAssignableFrom(this)

fun IlType.isAssignableFrom(type: IlType): Boolean = when {
    type == this -> {
        true
    }

    type.isSubclassOf(this) -> {
        true
    }

    this.isInterface -> {
        type.implementInterface(this)
    }

    this.isGenericParameter -> {
        this.genericParameterConstraints.all { constraint -> constraint.isAssignableFrom(type) }
    }

    else -> {
        false
    }
}

fun IlType.implementInterface(iface: IlType): Boolean {
    var t: IlType? = this
    while (t != null) {
        val interfaces = t.interfaces
        if (interfaces.any { i -> i == iface || i.implementInterface(iface) }) return true
        t = t.baseType
    }
    return false
}

fun IlType.isSubclassOf(type: IlType): Boolean {
    var p: IlType? = this;
    if (p == type) return false;
    while (p != null) {
        if (p == type) return true;
        p = p.baseType
    }
    return false;
}
