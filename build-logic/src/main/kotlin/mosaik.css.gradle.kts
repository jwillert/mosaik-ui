// Convention for modules that need Tailwind CSS + DaisyUI compilation.
// Registers installTailwind, generateTailwindConfig, buildCss, and watchCss tasks.

val css = extensions.create<MosaikCssExtension>("mosaikCss")
css.tailwindVersion.convention("4")
css.daisyuiVersion.convention("5")
css.scanPaths.convention(listOf("src"))
css.themes.convention(listOf("light"))
css.minify.convention(true)

val projectDir = layout.projectDirectory

tasks.register<Exec>("generateTailwindConfig") {
    group = "mosaik css"
    description = "Generate input.css (only if missing)."
    val inputCss = projectDir.file("input.css").asFile
    onlyIf { !inputCss.exists() }
    doFirst {
        val themeList = css.themes.get().joinToString(", ").ifEmpty { "light" }
        val sources = css.scanPaths.get().joinToString("\n") { "@source \"$it\";" }
        inputCss.writeText(buildString {
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
        logger.lifecycle("mosaik: generated input.css")
    }
    commandLine("true")
}

tasks.register<Exec>("installTailwind") {
    group = "mosaik css"
    description = "Install Tailwind CSS and DaisyUI via npm."
    workingDir = projectDir.asFile
    commandLine(
        "npm", "install",
        "tailwindcss@${css.tailwindVersion.get()}",
        "@tailwindcss/cli@${css.tailwindVersion.get()}",
        "daisyui@${css.daisyuiVersion.get()}",
    )
    inputs.property("tailwindVersion", css.tailwindVersion)
    inputs.property("daisyuiVersion", css.daisyuiVersion)
    outputs.dir("node_modules")
}

tasks.register<Exec>("buildCss") {
    group = "mosaik css"
    description = "Compile output.css from component sources with Tailwind/DaisyUI."
    dependsOn("installTailwind", "generateTailwindConfig")
    workingDir = projectDir.asFile
    val args = mutableListOf(
        "npx", "@tailwindcss/cli",
        "-i", "input.css",
        "-o", "output.css",
    )
    doFirst { if (css.minify.get()) args += "--minify" }
    commandLine(args)
    inputs.file("input.css")
    outputs.file("output.css")
}

tasks.register<Exec>("watchCss") {
    group = "mosaik css"
    description = "Rebuild output.css on file changes (Tailwind watch mode)."
    dependsOn("installTailwind", "generateTailwindConfig")
    workingDir = projectDir.asFile
    commandLine(
        "npx", "@tailwindcss/cli",
        "-i", "input.css",
        "-o", "output.css",
        "--watch",
    )
}
