package dev.jwillert.ktor.vrt.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.testing.Test
import java.util.Properties

class KtorVrtPlugin : Plugin<Project> {
    private val ktorVrtVersion: String by lazy {
        val props = Properties()
        val stream =
            checkNotNull(javaClass.getResourceAsStream("/ktor-vrt.properties")) {
                "ktor-vrt.properties not found on the plugin classpath"
            }
        stream.use { props.load(it) }
        requireNotNull(props.getProperty("ktorVrtVersion")) {
            "ktorVrtVersion missing from ktor-vrt.properties"
        }
    }

    override fun apply(project: Project) {
        val ext = project.extensions.create("ktorVrt", KtorVrtExtension::class.java)
        ext.goldenDir.convention(project.layout.projectDirectory.dir("src/vrt/resources/golden"))
        ext.diffDir.convention(project.layout.buildDirectory.dir("vrt/diff"))

        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            configure(project, ext)
        }
    }

    private fun configure(
        project: Project,
        ext: KtorVrtExtension,
    ) {
        val sourceSets = project.extensions.getByType(SourceSetContainer::class.java)
        val main = sourceSets.getByName("main")
        val vrt = sourceSets.create("vrt")
        vrt.compileClasspath += main.output
        vrt.runtimeClasspath += main.output

        project.configurations
            .getByName("vrtImplementation")
            .extendsFrom(project.configurations.getByName("implementation"))
        project.configurations
            .getByName("vrtRuntimeOnly")
            .extendsFrom(project.configurations.getByName("runtimeOnly"))

        project.dependencies.add("vrtImplementation", "dev.jwillert:ktor-vrt:$ktorVrtVersion")

        listOf("local" to "vrtTest", "docker" to "vrtTestDocker").forEach { (mode, taskName) ->
            project.tasks.register(taskName, Test::class.java) { task ->
                task.group = "verification"
                task.description = "Visual regression tests (mode=$mode)"
                task.testClassesDirs = vrt.output.classesDirs
                task.classpath = vrt.runtimeClasspath
                task.useJUnitPlatform()
                ext.cssTaskDependency.orNull?.let { task.dependsOn(it) }
                task.systemProperty("vrt.mode", mode)
                task.systemProperty(
                    "vrt.css",
                    ext.css
                        .get()
                        .asFile.absolutePath,
                )
                task.systemProperty(
                    "vrt.goldenDir",
                    ext.goldenDir
                        .get()
                        .asFile.absolutePath,
                )
                task.systemProperty(
                    "vrt.diffDir",
                    ext.diffDir
                        .get()
                        .asFile.absolutePath,
                )
                task.systemProperty(
                    "vrt.htmlAttributes",
                    ext.htmlAttributes
                        .get()
                        .entries
                        .joinToString(",") { "${it.key}=${it.value}" },
                )
                task.systemProperty("vrt.wrapperClasses", ext.wrapperClasses.get().joinToString(" "))
                if (project.hasProperty("updateGoldens")) task.systemProperty("vrt.updateGoldens", "true")
                task.outputs.upToDateWhen { false }
                task.testLogging.showStandardStreams = true
            }
        }
    }
}
