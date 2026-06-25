import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/** Generates the Tailwind/DaisyUI input stylesheet without capturing Gradle script objects. */
abstract class GenerateTailwindConfigTask : DefaultTask() {
    @get:OutputFile
    abstract val inputCss: RegularFileProperty

    @get:Input
    abstract val themes: ListProperty<String>

    @get:Input
    abstract val scanPaths: ListProperty<String>

    @TaskAction
    fun generate() {
        val output = inputCss.get().asFile
        if (output.exists()) {
            return
        }

        output.parentFile?.mkdirs()
        val themeList = themes.get().joinToString(", ").ifEmpty { "light" }
        val sources = scanPaths.get().joinToString("\n") { "@source \"$it\";" }
        output.writeText(buildString {
            appendLine("@import \"tailwindcss\";")
            appendLine()
            appendLine("@plugin \"daisyui\" {")
            appendLine("  themes: $themeList;")
            appendLine("}")
            if (sources.isNotEmpty()) {
                appendLine()
                appendLine(sources)
            }
        })
        logger.lifecycle("mosaik: generated ${output.name}")
    }
}
