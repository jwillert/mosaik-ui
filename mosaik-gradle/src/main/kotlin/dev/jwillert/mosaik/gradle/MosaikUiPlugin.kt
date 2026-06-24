package dev.jwillert.mosaik.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * The `dev.jwillert.mosaik` Gradle plugin. Wires up two task groups on the `mosaikUi {}` extension:
 *
 * - "mosaik" — the component install pipeline: `mosaikAdd`, `mosaikList`, `mosaikStatus`.
 * - "mosaik css" — the CSS toolchain: `installTailwind`, `generateTailwindConfig`, `buildCss`,
 *   `watchCss`.
 */
class MosaikUiPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("mosaikUi", MosaikUiExtension::class.java)
        registerInstallTasks(project, extension)
        registerCssTasks(project, extension)
    }

    /** The component install pipeline backed by mosaik-core and the bundled registry. */
    private fun registerInstallTasks(project: Project, extension: MosaikUiExtension) {
        val group = "mosaik"
        val sourceRoot = project.layout.projectDirectory.dir("src/main/kotlin")

        project.tasks.register("mosaikAdd", MosaikAddTask::class.java) { task ->
            task.group = group
            task.description = "Install a component and its dependencies (--component=<name> [--force])."
            task.packageName.set(extension.packageName)
            task.sourceRoot.set(sourceRoot)
        }

        project.tasks.register("mosaikList", MosaikListTask::class.java) { task ->
            task.group = group
            task.description = "List all available Mosaik components."
        }

        project.tasks.register("mosaikStatus", MosaikStatusTask::class.java) { task ->
            task.group = group
            task.description = "Show which components are already installed in the project."
            task.packageName.set(extension.packageName)
            task.sourceRoot.set(sourceRoot)
        }
    }

    private fun registerCssTasks(project: Project, extension: MosaikUiExtension) {
        val css = extension.css
        css.tailwindVersion.convention("4")
        css.daisyuiVersion.convention("5")
        css.scanPaths.convention(listOf("src"))
        css.themes.convention(listOf("light"))
        css.minify.convention(true)

        val group = "mosaik css"
        val projectDir = project.layout.projectDirectory

        val generate = project.tasks.register(
            "generateTailwindConfig",
            GenerateTailwindConfigTask::class.java,
        ) { task ->
            task.group = group
            task.description = "Generate input.css, postcss.config.js and package.json (only if missing)."
            task.projectDir.set(projectDir)
            task.scanPaths.set(css.scanPaths)
            task.themes.set(css.themes)
        }

        val install = project.tasks.register(
            "installTailwind",
            InstallTailwindTask::class.java,
        ) { task ->
            task.group = group
            task.description = "Install Tailwind CSS and DaisyUI via npm."
            task.workingDir.set(projectDir)
            task.tailwindVersion.set(css.tailwindVersion)
            task.daisyuiVersion.set(css.daisyuiVersion)
        }

        project.tasks.register("buildCss", BuildCssTask::class.java) { task ->
            task.group = group
            task.description = "Compile output.css from component sources with Tailwind/DaisyUI."
            task.workingDir.set(projectDir)
            task.minify.set(css.minify)
            task.watch.set(false)
            task.dependsOn(install, generate)
        }

        project.tasks.register("watchCss", BuildCssTask::class.java) { task ->
            task.group = group
            task.description = "Rebuild output.css on file changes (Tailwind watch mode)."
            task.workingDir.set(projectDir)
            task.minify.set(false)
            task.watch.set(true)
            task.dependsOn(install, generate)
        }
    }
}
