plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlin.android)
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
    }
    buildFeatures {
        buildConfig = true
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
}
