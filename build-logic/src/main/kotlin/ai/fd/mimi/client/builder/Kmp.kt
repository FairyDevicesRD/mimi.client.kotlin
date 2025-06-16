package ai.fd.mimi.client.builder

import org.gradle.api.Project

internal fun Project.configureKmp() {
    kotlin {
        jvm()
        // linuxX64()
        macosArm64()

        configureKotlinCommon()
    }
}
