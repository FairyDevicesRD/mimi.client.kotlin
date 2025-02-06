plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.allopen) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.mimi.builder) apply false
    alias(libs.plugins.kotlinx.kover)
}

dependencies {
    kover(project(":engine:core"))
    kover(project(":engine:ktor"))
    kover(project(":engine:okhttp"))
    kover(project(":service:asr-core"))
    kover(project(":service:asr"))
    kover(project(":service:nict-asr"))
    kover(project(":service:nict-tts"))
}
