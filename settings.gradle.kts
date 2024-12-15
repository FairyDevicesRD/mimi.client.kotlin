rootProject.name = "mimi.client.kotlin"

includeBuild("build-logic")
include("client")
include("engine:core")
include("engine:ktor")
include("engine:okhttp")
include("service:asr")

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}
