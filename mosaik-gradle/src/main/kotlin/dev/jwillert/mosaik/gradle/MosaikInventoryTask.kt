package dev.jwillert.mosaik.gradle

import dev.jwillert.mosaik.core.ComponentEntry
import dev.jwillert.mosaik.core.InventoryWriter
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
 * Regenerates the `.mosaik/components.json` inventory by scanning the configured package
 * directory for registry-known component files. The task discovers installed components
 * without deep Kotlin parsing and uses registry metadata for API information.
 */
abstract class MosaikInventoryTask : DefaultTask() {
    @get:Optional
    @get:Input
    abstract val packageName: Property<String>

    @get:Internal
    abstract val sourceRoot: DirectoryProperty

    @TaskAction
    fun regenerate() {
        val pkg =
            packageName.orNull
                ?: throw GradleException("Set mosaikUi { packageName } before regenerating inventory.")

        val registry = BundledRegistry.load()
        val packageDir = File(sourceRoot.get().asFile, pkg.replace('.', '/'))
        val projectRoot = project.projectDir

        // Discover installed components by checking which registry components have all
        // their files present in the package directory
        val installedComponents = mutableListOf<ComponentEntry>()

        for (entry in registry.all()) {
            val allFilesExist = entry.files.isNotEmpty() && entry.files.all { File(packageDir, it).exists() }

            if (allFilesExist) {
                val checksums =
                    entry.files.associateWith { fileName ->
                        val file = File(packageDir, fileName)
                        InventoryWriter.computeChecksum(file.readText())
                    }

                installedComponents.add(entry.copy(checksums = checksums))
            }
        }

        // Write the inventory
        val inventoryWriter = InventoryWriter(pkg)
        inventoryWriter.write(projectRoot, installedComponents)

        logger.lifecycle("Regenerated .mosaik/components.json (${installedComponents.size} components)")
    }
}
