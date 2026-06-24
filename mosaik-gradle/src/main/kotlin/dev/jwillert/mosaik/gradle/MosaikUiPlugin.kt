package dev.jwillert.mosaik.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * The `dev.jwillert.mosaik` Gradle plugin. This iteration wires up the CSS toolchain (issue #5):
 * the `mosaikUi { css {} }` extension plus the `installTailwind`, `generateTailwindConfig`,
 * `buildCss` and `watchCss` tasks. The registry/install tasks land in a separate issue.
 */
class MosaikUiPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("mosaikUi", MosaikUiExtension::class.java)
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
