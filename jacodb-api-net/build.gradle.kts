import com.jetbrains.rd.generator.gradle.RdGenExtension
import com.jetbrains.rd.generator.gradle.RdGenTask

plugins {
    java
    application
    id(Plugins.RdGen)
}

val rdgenModelsCompileClasspath by configurations.creating {
    extendsFrom(configurations.compileClasspath.get())
}

kotlin {
    sourceSets.create("rdgenModels").apply {
        kotlin.srcDir("src/main/rdgen")
    }
}

dependencies {
    api(project(":jacodb-api-common"))
    api(project(":jacodb-api-storage"))

    api(Libs.kotlinx_coroutines_core)
    api(Libs.kotlinx_coroutines_jdk8)
    api(Libs.jooq)

    implementation(Libs.rd_framework)
    implementation(Libs.rd_core)
    implementation(Libs.rd_gen)

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.mockito:mockito-core:5.1.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
}

val sourcesBaseDir = projectDir.resolve("src/main/kotlin")
val csSourcesBaseDir = projectDir.resolve("src/main/resources")
val generatedPackage = "org.jacodb.api.net.generated"
val generatedSourceDir = sourcesBaseDir.resolve(generatedPackage.replace('.', '/'))

val generatedModelsPackage = "$generatedPackage.models"
val generatedModelsSourceDir = sourcesBaseDir.resolve(generatedModelsPackage.replace('.', '/'))
val csModelsDir = csSourcesBaseDir.resolve("cs-models")

val generateModels = tasks.register<RdGenTask>("generateProtocolModels") {
    dependsOn.addAll(listOf("compileKotlin"))
    val rdParams = extensions.getByName("params") as RdGenExtension
    val sourcesDir = projectDir.resolve("src/main/kotlin").resolve("org/jacodb/api/net/models")

    group = "rdgen"
    rdParams.verbose = true
    rdParams.sources(sourcesDir)
    rdParams.hashFolder = layout.buildDirectory.file("rdgen/hashes").get().asFile.absolutePath
    // where to search roots
    rdParams.packages = "org.jacodb.api.net.models"

    rdParams.generator {
        language = "kotlin"
        transform = "asis"
        root = "org.jacodb.api.net.models.IlRoot"

        directory = generatedModelsSourceDir.absolutePath
        namespace = generatedModelsPackage
    }

    rdParams.generator {
        language = "csharp"
        transform = "reversed"
        root = "org.jacodb.api.net.models.IlRoot"

        directory = csModelsDir.absolutePath
        namespace = generatedModelsPackage
    }
}