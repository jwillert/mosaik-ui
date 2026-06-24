package dev.jwillert.mosaik.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject

/**
 * Installs the CSS toolchain via npm: `npm install tailwindcss@<v> @tailwindcss/cli@<v> daisyui@<v>`.
 * Versions come from the `css {}` extension so a project can pin exact releases.
 */
abstract class InstallTailwindTask @Inject constructor(
    private val execOps: ExecOperations,
) : DefaultTask() {

    @get:Internal
    abstract val workingDir: DirectoryProperty

    @get:Input
    abstract val tailwindVersion: Property<String>

    @get:Input
    abstract val daisyuiVersion: Property<String>

    @TaskAction
    fun install() {
        val tailwind = tailwindVersion.get()
        val daisyui = daisyuiVersion.get()
        execOps.exec { spec ->
            spec.workingDir = workingDir.get().asFile
            spec.commandLine(
                "npm", "install",
                "tailwindcss@$tailwind",
                "@tailwindcss/cli@$tailwind",
                "daisyui@$daisyui",
            )
        }
    }
}
