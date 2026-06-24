// Convention for modules that run visual regression tests against ktor-vrt.
// Sets up the `vrt` source set, wires `project(":ktor-vrt")` as a dependency,
// and registers `vrtTest` (local) + `vrtTestDocker` (container) tasks.

plugins {
    id("mosaik.kotlin-library")
}

val ext = extensions.create<VrtExtension>("vrt")
ext.goldenDir.convention(layout.projectDirectory.dir("src/vrt/resources/golden"))
ext.diffDir.convention(layout.buildDirectory.dir("vrt/diff"))

val sourceSets = extensions.getByType<SourceSetContainer>()
val main = sourceSets.getByName("main")
val vrt = sourceSets.create("vrt")
vrt.compileClasspath += main.output
vrt.runtimeClasspath += main.output

configurations.getByName("vrtImplementation")
    .extendsFrom(configurations.getByName("implementation"))
configurations.getByName("vrtRuntimeOnly")
    .extendsFrom(configurations.getByName("runtimeOnly"))

dependencies {
    "vrtImplementation"(project(":ktor-vrt"))
}

listOf("local" to "vrtTest", "docker" to "vrtTestDocker").forEach { (mode, taskName) ->
    tasks.register<Test>(taskName) {
        group = "verification"
        description = "Visual regression tests (mode=$mode)"
        testClassesDirs = vrt.output.classesDirs
        classpath = vrt.runtimeClasspath
        useJUnitPlatform()
        ext.cssTaskDependency.orNull?.let { dependsOn(it) }
        systemProperty("vrt.mode", mode)
        systemProperty("vrt.css", ext.css.get().asFile.absolutePath)
        systemProperty("vrt.goldenDir", ext.goldenDir.get().asFile.absolutePath)
        systemProperty("vrt.diffDir", ext.diffDir.get().asFile.absolutePath)
        systemProperty(
            "vrt.htmlAttributes",
            ext.htmlAttributes.get().entries.joinToString(",") { "${it.key}=${it.value}" },
        )
        systemProperty("vrt.wrapperClasses", ext.wrapperClasses.get().joinToString(" "))
        if (project.hasProperty("updateGoldens")) systemProperty("vrt.updateGoldens", "true")
        outputs.upToDateWhen { false }
        testLogging.showStandardStreams = true
    }
}
