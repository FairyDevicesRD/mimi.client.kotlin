plugins {
    `kotlin-dsl`
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
