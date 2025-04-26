plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.kotlin.gradle)
}

gradlePlugin {
    plugins {
        register("mimiClientBuilder") {
            id = "ai.fd.mimi.client.builder"
            implementationClass = "MimiClientBuilderPlugin"
        }
    }
}
