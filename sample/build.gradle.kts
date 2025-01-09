plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.mimi.builder)
}

dependencies {
    implementation(project(":client"))
    implementation(project(":engine:okhttp"))
    implementation(project(":engine:ktor"))
    implementation(project(":service:asr"))
    implementation(project(":service:nict-asr"))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.logging)
    implementation(libs.logback.classic)
}
