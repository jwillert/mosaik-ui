package dev.jwillert.mosaik.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Reports which registry components are already installed in the project, by checking whether
 * each component's files exist in the configured package directory.
 */
abstract class MosaikStatusTask : DefaultTask() {

    @get:Optional
    @get:Input
    abstract val packageName: Property<String>

    @get:Internal
    abstract val sourceRoot: DirectoryProperty

    @TaskAction
    fun status() {
        val pkg = packageName.orNull
            ?: throw GradleException("Set mosaikUi { packageName } before checking component status.")

        val registry = BundledRegistry.load()
        val packageDir = File(sourceRoot.get().asFile, pkg.replace('.', '/'))

        logger.lifecycle("Component status (package $pkg):")
        for (entry in registry.all()) {
            val installed = entry.files.isNotEmpty() && entry.files.all { File(packageDir, it).exists() }
            logger.lifecycle("  [${if (installed) "x" else " "}] ${entry.name}")
        }
    }
}
