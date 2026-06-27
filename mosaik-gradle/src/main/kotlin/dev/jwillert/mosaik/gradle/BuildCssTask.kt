package dev.jwillert.mosaik.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject

/**
 * Compiles `input.css` to `output.css` with `npx @tailwindcss/cli`. Optionally minifies and/or
 * runs in watch mode. `buildCss` and `watchCss` are both instances of this task type, differing
 * only in the [watch] flag.
 */
abstract class BuildCssTask
    @Inject
    constructor(
        private val execOps: ExecOperations,
    ) : DefaultTask() {
        @get:Internal
        abstract val workingDir: DirectoryProperty

        @get:Input
        @get:Optional
        abstract val input: Property<String>

        @get:Input
        @get:Optional
        abstract val output: Property<String>

        @get:Input
        abstract val minify: Property<Boolean>

        /** When true, run the CLI in `--watch` mode (used by `watchCss`). */
        @get:Input
        abstract val watch: Property<Boolean>

        @TaskAction
        fun build() {
            val args =
                mutableListOf(
                    "npx",
                    "@tailwindcss/cli",
                    "-i",
                    input.getOrElse("input.css"),
                    "-o",
                    output.getOrElse("output.css"),
                )
            if (minify.get()) args += "--minify"
            if (watch.get()) args += "--watch"
            execOps.exec { spec ->
                spec.workingDir = workingDir.get().asFile
                spec.commandLine(args)
            }
        }
    }
