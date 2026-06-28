plugins {
    id("mosaik.ktor-app")
    id("mosaik.css")
    id("mosaik.ktlint")
    application
}

dependencies {
    // Module dependency (not the installed-source flow) so component edits are
    // picked up immediately when iterating on the docs app — see PRD user story 22.
    implementation(project(":mosaik-components"))
    implementation(libs.kotlinx.html)
}

application {
    mainClass = "mosaik.docs.ApplicationKt"
}

mosaikCss {
    scanPaths.set(listOf("src", "../mosaik-components/src"))
}

tasks.named<Exec>("buildCss") {
    setCommandLine(
        "npx",
        "@tailwindcss/cli",
        "-i",
        "input.css",
        "-o",
        "src/main/resources/static/output.css",
        "--minify",
    )
    outputs.file("src/main/resources/static/output.css")
}

val copyDatastar by tasks.registering(Copy::class) {
    dependsOn("installTailwind")
    from("node_modules/@starfederation/datastar/dist/datastar.js")
    into("src/main/resources/static/vendor")
    outputs.file("src/main/resources/static/vendor/datastar.js")
}

val copyDocsClient by tasks.registering(Copy::class) {
    val clientProject = project(":mosaik-docs-client")
    val jsBundle =
        clientProject.layout.buildDirectory.file(
            "kotlin-webpack/js/productionExecutable/mosaik-docs-client.js",
        )
    dependsOn(clientProject.tasks.named("jsBrowserProductionWebpack"))
    from(jsBundle)
    into("src/main/resources/static")
    inputs.file(jsBundle)
    outputs.file("src/main/resources/static/mosaik-docs-client.js")
}

tasks.named("processResources") {
    dependsOn("buildCss", copyDatastar, copyDocsClient)
}
