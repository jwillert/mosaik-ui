package dev.jwillert.mosaik.gradle

import dev.jwillert.mosaik.core.InventoryReader
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
        val pkg =
            packageName.orNull
                ?: throw GradleException("Set mosaikUi { packageName } before checking component status.")

        val registry = BundledRegistry.load()
        val packageDir = File(sourceRoot.get().asFile, pkg.replace('.', '/'))
        val projectRoot = project.projectDir

        // Read the inventory if it exists
        val inventory = InventoryReader.read(projectRoot)

        logger.lifecycle("Component status (package $pkg):")

        if (inventory.isEmpty()) {
            // No inventory: fall back to checking files directly
            for (entry in registry.all()) {
                val installed = entry.files.isNotEmpty() && entry.files.all { File(packageDir, it).exists() }
                logger.lifecycle("  [${if (installed) "x" else " "}] ${entry.name}")
            }
        } else {
            // Inventory exists: check each registry component against inventory and files
            for (entry in registry.all()) {
                val inventoryEntry = inventory[entry.name]

                if (inventoryEntry == null) {
                    // Not in inventory
                    val installed = entry.files.isNotEmpty() && entry.files.all { File(packageDir, it).exists() }
                    if (installed) {
                        logger.lifecycle("  [?] ${entry.name} - installed but not in inventory")
                    } else {
                        logger.lifecycle("  [ ] ${entry.name}")
                    }
                } else {
                    // In inventory: check for missing files or drift
                    val allFilesExist = inventoryEntry.files.all { File(packageDir, it).exists() }

                    if (!allFilesExist) {
                        logger.lifecycle("  [!] ${entry.name} - missing installed files")
                        continue
                    }

                    // Check for drift if checksums are available
                    val driftedFiles = mutableListOf<String>()
                    for ((fileName, expectedChecksum) in inventoryEntry.checksums) {
                        val file = File(packageDir, fileName)
                        if (file.exists()) {
                            val actualChecksum = InventoryWriter.computeChecksum(file.readText())
                            if (actualChecksum != expectedChecksum) {
                                driftedFiles.add(fileName)
                            }
                        }
                    }

                    if (driftedFiles.isNotEmpty()) {
                        logger.lifecycle("  [~] ${entry.name} - modified: ${driftedFiles.joinToString(", ")}")
                    } else {
                        logger.lifecycle("  [x] ${entry.name}")
                    }
                }
            }
        }
    }
}
