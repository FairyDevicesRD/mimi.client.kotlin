package ai.fd.mimi.client.builder

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmExtension

internal fun Project.configureKotlinJvm() {
    extensions.configure<KotlinJvmExtension> {
        configureKotlinCommon()
    }
}
