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

package org.jacodb.impl.features.classpaths

import kotlinx.metadata.InconsistentKotlinMetadataException
import kotlinx.metadata.KmConstructor
import kotlinx.metadata.KmFunction
import kotlinx.metadata.KmProperty
import kotlinx.metadata.KmTypeParameter
import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata
import org.jacodb.api.JcClassExtFeature
import org.jacodb.api.JcClassOrInterface
import org.jacodb.api.ext.annotation
import org.jacodb.impl.bytecode.logger

object KotlinMetadata : JcClassExtFeature {

    const val METADATA_KEY = "kotlinClassMetadata"

    override fun extensionValuesOf(clazz: JcClassOrInterface): Map<String, Any>? {
        val kMetadata = clazz.kMetadata
        if (kMetadata != null) {
            return mapOf(METADATA_KEY to KotlinMetadataHolder(kMetadata))
        }

        return null
    }

    /**
     * Returns [KotlinClassMetadata] instance for the class if it was generated by Kotlin.
     * See docs for [Metadata] for more info on how it is represented in bytecode and what it contains.
     */
    @Suppress("UNCHECKED_CAST")
    private val JcClassOrInterface.kMetadata: KotlinClassMetadata?
        get() {
            val kmParameters = annotation("kotlin.Metadata")?.values ?: return null
            val kmHeader = KotlinClassHeader(
                kmParameters["k"] as? Int,
                (kmParameters["mv"] as? List<Int>)?.toIntArray(),
                (kmParameters["d1"] as? List<String>)?.toTypedArray(),
                (kmParameters["d2"] as? List<String>)?.toTypedArray(),
                kmParameters["xs"] as? String,
                kmParameters["pn"] as? String,
                kmParameters["xi"] as? Int,
            )
            return try {
                KotlinClassMetadata.read(kmHeader)
            } catch (e: InconsistentKotlinMetadataException) {
                logger.warn {
                    "Can't parse Kotlin metadata annotation found on class $name, the class may be damaged"
                }
                null
            }
        }

}

class KotlinMetadataHolder(val meta: KotlinClassMetadata) {

    val functions: List<KmFunction> = when (meta) {
        is KotlinClassMetadata.Class -> meta.toKmClass().functions
        is KotlinClassMetadata.FileFacade -> meta.toKmPackage().functions
        is KotlinClassMetadata.MultiFileClassPart -> meta.toKmPackage().functions
        else -> listOf()
    }

    val constructors: List<KmConstructor> =
        (meta as? KotlinClassMetadata.Class)?.toKmClass()?.constructors ?: emptyList()

    val properties: List<KmProperty> = when (meta) {
        is KotlinClassMetadata.Class -> meta.toKmClass().properties
        is KotlinClassMetadata.FileFacade -> meta.toKmPackage().properties
        is KotlinClassMetadata.MultiFileClassPart -> meta.toKmPackage().properties
        else -> listOf()
    }

    val kmTypeParameters: List<KmTypeParameter>? =
        (meta as? KotlinClassMetadata.Class)?.toKmClass()?.typeParameters


}