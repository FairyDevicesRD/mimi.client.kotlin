rootProject.name = "mimi.client.kotlin"

includeBuild("build-logic")
include("client")
include("engine:core")
include("engine:ktor")
include("engine:okhttp")
include("service:asr-core")
include("service:asr")
include("sample")

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}
