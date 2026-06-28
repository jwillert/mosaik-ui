package dev.jwillert.mosaik.gradle

import dev.jwillert.mosaik.core.FileWriter
import dev.jwillert.mosaik.core.InventoryReader
import dev.jwillert.mosaik.core.InventoryWriter
import dev.jwillert.mosaik.core.Resolver
import dev.jwillert.mosaik.core.WriteOutcome
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.File

/**
 * Installs a component and all of its transitive dependencies into the project. The component
 * to install is given by `--component=<name>`; existing files are preserved unless `--force`
 * is passed. Every file is logged so the user sees exactly what changed.
 */
abstract class MosaikAddTask : DefaultTask() {
    @get:Optional
    @get:Input
    @get:Option(option = "component", description = "Name of the component to install.")
    abstract val component: Property<String>

    @get:Optional
    @get:Input
    @get:Option(option = "force", description = "Overwrite component files that already exist.")
    abstract val force: Property<Boolean>

    @get:Optional
    @get:Input
    abstract val packageName: Property<String>

    /** Source root the component package directory is created under (`src/main/kotlin`). */
    @get:Internal
    abstract val sourceRoot: DirectoryProperty

    @TaskAction
    fun add() {
        val name =
            component.orNull
                ?: throw GradleException("Specify a component: ./gradlew mosaikAdd --component=<name>")
        val pkg =
            packageName.orNull
                ?: throw GradleException("Set mosaikUi { packageName } before installing components.")

        val registry = BundledRegistry.load()
        val resolved =
            try {
                Resolver(registry).resolve(name)
            } catch (e: RuntimeException) {
                throw GradleException(e.message ?: "Could not resolve component '$name'.", e)
            }

        val writer = FileWriter(pkg)
        val packageDir = File(sourceRoot.get().asFile, pkg.replace('.', '/'))
        val forced = force.getOrElse(false)

        logger.lifecycle("Installing: $name")
        var created = 0
        var overwritten = 0
        var present = 0
        for (entry in resolved) {
            for (fileName in entry.files) {
                val target = File(packageDir, fileName)
                when (writer.write(target, BundledRegistry.componentSource(fileName), forced)) {
                    WriteOutcome.CREATED -> {
                        created++
                        logger.lifecycle("  → ${entry.name}: installed $fileName")
                    }
                    WriteOutcome.OVERWRITTEN -> {
                        overwritten++
                        logger.lifecycle("  → ${entry.name}: overwrote $fileName")
                    }
                    WriteOutcome.ALREADY_EXISTS -> {
                        present++
                        logger.lifecycle("  → ${entry.name}: $fileName already present (use --force to overwrite)")
                    }
                }
            }
        }
        logger.lifecycle("Done. $created installed, $overwritten overwritten, $present already present.")

        // Update the installed component inventory.
        val projectRoot = project.projectDir
        val existing = InventoryReader.read(projectRoot)
        val allInstalled = (existing.values + resolved).distinctBy { it.name }
        val inventoryWriter = InventoryWriter(pkg)
        inventoryWriter.write(projectRoot, allInstalled)
    }
}
