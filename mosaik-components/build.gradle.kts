plugins {
    id("mosaik.vrt")
    id("mosaik.css")
    id("mosaik.ktlint")
}

dependencies {
    implementation(libs.kotlinx.html)
}

vrt {
    css.set(layout.buildDirectory.file("vrt/output.css"))
    cssTaskDependency.set("buildVrtCss")
    htmlAttributes.put("data-theme", "light")
    wrapperClasses.set(listOf("inline-block", "p-4"))
}

tasks.named<Exec>("buildCss") {
    setCommandLine("npx", "@tailwindcss/cli", "-i", "input.css", "-o", "output.css", "--minify")
}

tasks.register<Exec>("buildVrtCss") {
    group = "mosaik css"
    description = "Compile CSS for VRT rendering (into build dir)."
    dependsOn("installTailwind")
    workingDir = projectDir
    commandLine(
        "npx",
        "@tailwindcss/cli",
        "-i",
        "input.css",
        "-o",
        layout.buildDirectory
            .file("vrt/output.css")
            .get()
            .asFile.absolutePath,
        "--minify",
    )
    inputs.file("input.css")
    inputs.files(fileTree("src") { include("**/*.kt") })
    outputs.file(layout.buildDirectory.file("vrt/output.css"))
}
