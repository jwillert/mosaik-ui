// Convention for modules that need Tailwind CSS + DaisyUI compilation.
// Registers installTailwind, generateTailwindConfig, buildCss, and watchCss tasks.

val css = extensions.create<MosaikCssExtension>("mosaikCss")
css.tailwindVersion.convention("4")
css.daisyuiVersion.convention("5")
css.scanPaths.convention(listOf("src"))
css.themes.convention(listOf("light"))
css.minify.convention(true)

val projectDir = layout.projectDirectory

tasks.register<GenerateTailwindConfigTask>("generateTailwindConfig") {
    group = "mosaik css"
    description = "Generate input.css (only if missing)."
    inputCss.set(projectDir.file("input.css"))
    themes.convention(css.themes)
    scanPaths.convention(css.scanPaths)
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
    commandLine(
        "npx", "@tailwindcss/cli",
        "-i", "input.css",
        "-o", "output.css",
    )
    argumentProviders.add(objects.newInstance<CssMinifyArgumentProvider>().apply {
        minify.convention(css.minify)
    })
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
