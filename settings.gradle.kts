rootProject.name = "mimi.client.kotlin"

includeBuild("build-logic")
include("engine:core")
include("engine:ktor")
include("engine:okhttp")
include("service:asr-core")
include("service:asr")
include("service:nict-asr")
include("service:nict-tts")
include("utils")
include("sample")

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}
