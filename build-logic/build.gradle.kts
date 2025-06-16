plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(17)

    compilerOptions {
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
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
