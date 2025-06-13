package ai.fd.mimi.client.builder

import java.util.Properties
import org.gradle.api.Project
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configurePublish() {
    extensions.findByType<KotlinMultiplatformExtension>()?.apply {
        withSourcesJar(true)
    }
    val libraryVersion = libs.findVersion("projectVersion").get().requiredVersion
    afterEvaluate {
        publishing {
            publications {
                configurePublication {
                    groupId = "ai.fd.mimi.client"
                    version = libraryVersion

                    pom {
                        name = "Mimi client library"
                        description = "Mimi ${project.artifactIdAwareName} module"
                        licenses {
                            license {
                                name = "MIT License"
                                url = "https://opensource.org/licenses/MIT"
                            }
                        }
                        developers {
                            developer {
                                id = "Fairydevices"
                                name = "Fairy Devices"
                            }
                        }
                    }
                }
            }
            repositories {
                maven {
                    val properties = Properties()
                    properties.load(project.rootProject.file("github.properties").inputStream())
                    name = "GithubPackages"
                    val u = properties.getProperty("url") ?: System.getenv("URL")
                    url = uri(u ?: "undefine")
                    credentials {
                        username = properties.getProperty("username") ?: System.getenv("USERNAME")
                        password = properties.getProperty("token") ?: System.getenv("TOKEN")
                    }
                }
            }
        }
    }
}

private val Project.artifactIdAwareName: String
    get() {
        val names = mutableListOf<String>()
        var currentProject: Project? = this
        while (currentProject?.parent != null) {
            names.add(currentProject.name)
            currentProject = currentProject.parent
        }
        return names.reversed().joinToString("-")
    }

context(Project)
private fun PublicationContainer.configurePublication(
    configureAction: MavenPublication.() -> Unit
) {
    if (pluginManager.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
        // In KMP environment, we need to use the automatically created publication.
        withType<MavenPublication> {
            artifactId = if (name == "kotlinMultiplatform") {
                artifactIdAwareName
            } else {
                "$artifactIdAwareName-$name"
            }
            configureAction()
        }
    } else if (pluginManager.hasPlugin("org.jetbrains.kotlin.jvm")) {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = project.artifactIdAwareName
            configureAction()
        }
    }
}
