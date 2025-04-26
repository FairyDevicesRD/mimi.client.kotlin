package ai.fd.mimi.client.builder

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension

internal fun Project.configureCommon() {
    val libraryVersion = libs.findVersion("projectVersion").get().requiredVersion
    group = "ai.fd.mimi.client"
    version = libraryVersion
}

internal fun KotlinBaseExtension.configureKotlinCommon() {
    jvmToolchain(17)
}
