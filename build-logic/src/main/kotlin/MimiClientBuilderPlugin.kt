import ai.fd.mimi.client.builder.configureCommon
import ai.fd.mimi.client.builder.configureKmp
import org.gradle.api.Plugin
import org.gradle.api.Project

class MimiClientBuilderPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureCommon()
            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                configureKmp()
            }
        }
    }
}
