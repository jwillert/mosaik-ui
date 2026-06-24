package dev.jwillert.mosaik.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Zero-config generator: writes `input.css`, `postcss.config.js` and `package.json` into the
 * project directory, but only when a file is missing. Existing setups are left untouched so the
 * plugin never clobbers a user's hand-tuned Tailwind configuration.
 *
 * The generated `input.css` uses the Tailwind 4 `@import "tailwindcss"`, `@plugin "daisyui"` and
 * `@source` directives — no separate `tailwind.config.js` is needed.
 */
abstract class GenerateTailwindConfigTask : DefaultTask() {

    @get:Internal
    abstract val projectDir: DirectoryProperty

    @get:Input
    abstract val scanPaths: ListProperty<String>

    @get:Input
    abstract val themes: ListProperty<String>

    @TaskAction
    fun generate() {
        val dir = projectDir.get().asFile
        writeIfMissing(File(dir, "input.css"), inputCss())
        writeIfMissing(File(dir, "postcss.config.js"), POSTCSS_CONFIG)
        writeIfMissing(File(dir, "package.json"), PACKAGE_JSON)
    }

    private fun inputCss(): String {
        val themeList = themes.get().joinToString(", ").ifEmpty { "light" }
        val sources = scanPaths.get().joinToString("\n") { "@source \"$it\";" }
        return buildString {
            appendLine("@import \"tailwindcss\";")
            appendLine()
            appendLine("@plugin \"daisyui\" {")
            appendLine("  themes: $themeList;")
            appendLine("}")
            if (sources.isNotEmpty()) {
                appendLine()
                appendLine(sources)
            }
        }
    }

    private fun writeIfMissing(file: File, content: String) {
        if (file.exists()) {
            logger.lifecycle("mosaik: ${file.name} already exists — leaving it untouched")
        } else {
            file.writeText(content)
            logger.lifecycle("mosaik: generated ${file.name}")
        }
    }

    private companion object {
        val POSTCSS_CONFIG =
            """
            module.exports = {
              plugins: {
                "@tailwindcss/postcss": {},
              },
            };
            """.trimIndent() + "\n"

        val PACKAGE_JSON =
            """
            {
              "name": "mosaik-ui-css",
              "version": "1.0.0",
              "private": true
            }
            """.trimIndent() + "\n"
    }
}
