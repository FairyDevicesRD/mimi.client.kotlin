package ai.fd.mimi.client.builder

import org.gradle.api.Project

internal fun Project.configureCommon() {
    val libraryVersion = libs.findVersion("projectVersion").get().requiredVersion
    group = "ai.fd.mimi.client"
    version = libraryVersion
}
