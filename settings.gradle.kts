rootProject.name = "mimi.client.kotlin"

includeBuild("build-logic")
include("engine:core")
include("engine:ktor")
include("engine:okhttp")
include("service:token")
include("service:asr-core")
include("service:asr")
include("service:nict-asr")
include("service:nict-tts")
include("utils")
include("sample")
include("sample-thinklet")

pluginManagement {
    repositories {
        google {
            @Suppress("UnstableApiUsage")
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        google()
    }
}
