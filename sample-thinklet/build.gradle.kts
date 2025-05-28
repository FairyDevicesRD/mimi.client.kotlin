import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlin.android)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        load(rootProject.file("local.properties").inputStream())
    }
}

android {
    namespace = "ai.fd.mimi.sample.thinklet"
    compileSdk = 35

    defaultConfig {
        applicationId = "ai.fd.mimi.sample.thinklet"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1"

        buildConfigField("String", "MIMI_APPLICATION_ID", "\"${localProperties.getProperty("MIMI_APPLICATION_ID")}\"")
        buildConfigField("String", "MIMI_CLIENT_ID", "\"${localProperties.getProperty("MIMI_CLIENT_ID")}\"")
        buildConfigField("String", "MIMI_CLIENT_SECRET", "\"${localProperties.getProperty("MIMI_CLIENT_SECRET")}\"")
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":engine:okhttp"))
    implementation(project(":engine:ktor"))
    implementation(project(":service:token"))
    implementation(project(":service:asr"))
    implementation(project(":service:nict-asr"))
    implementation(project(":service:nict-tts"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.thinklet.sdk.audio)
}
