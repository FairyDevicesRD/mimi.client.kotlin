package ai.fd.mimi.client.builder

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.kotlin(block: KotlinMultiplatformExtension.() -> Unit) =
    extensions.getByType(KotlinMultiplatformExtension::class.java).block()

internal fun Project.publishing(block: PublishingExtension.() -> Unit) =
    extensions.getByType(PublishingExtension::class.java).block()

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")
