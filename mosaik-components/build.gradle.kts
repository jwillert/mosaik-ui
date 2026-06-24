plugins {
    id("mosaik.vrt")
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

tasks.register<Exec>("installVrtCssDeps") {
    group = "mosaik css"
    description = "Install npm dependencies for VRT CSS compilation."
    workingDir = projectDir
    commandLine("npm", "install")
    inputs.file("package.json")
    outputs.dir("node_modules")
}

tasks.register<Exec>("buildVrtCss") {
    group = "mosaik css"
    description = "Compile Tailwind + DaisyUI CSS for VRT rendering."
    dependsOn("installVrtCssDeps")
    workingDir = projectDir
    commandLine(
        "npx", "@tailwindcss/cli",
        "-i", "input.css",
        "-o", layout.buildDirectory.file("vrt/output.css").get().asFile.absolutePath,
        "--minify",
    )
    inputs.file("input.css")
    inputs.files(fileTree("src") { include("**/*.kt") })
    outputs.file(layout.buildDirectory.file("vrt/output.css"))
}
