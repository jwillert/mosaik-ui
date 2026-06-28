package dev.jwillert.mosaik.gradle

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File
import kotlin.io.path.createTempDirectory

class ScanDaisyUiTokensTaskTest :
    FunSpec({

        fun setupProject(dir: File) {
            dir.resolve("settings.gradle.kts").writeText("""rootProject.name = "test-project"""")
            dir.resolve("build.gradle.kts").writeText(
                """
                plugins {
                    id("lifecycle-base")
                    id("dev.jwillert.mosaik")
                }
                """.trimIndent(),
            )
        }

        fun runner(
            dir: File,
            vararg args: String,
        ) = GradleRunner
            .create()
            .withProjectDir(dir)
            .withPluginClasspath()
            .withArguments(*args)
            .forwardOutput()

        test("reports DaisyUI tokens found in source files") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)
            File(dir, "src/main/kotlin/mosaik/docs").apply { mkdirs() }
            File(dir, "src/main/kotlin/mosaik/docs/Page.kt").apply {
                writeText(
                    """package mosaik.docs

import kotlinx.html.*

fun example() {
    h6("footer-title") { +"Services" }
}
""",
                )
            }

            val result = runner(dir, "scanDaisyUiTokens").buildAndFail()

            result.output shouldContain "Found 1 DaisyUI class token(s)"
            result.output shouldContain "mosaik/docs/Page.kt"
            result.output shouldContain "Line 6: footer-title (consider Footer component)"
        }

        test("reports no findings when source is clean") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)
            File(dir, "src/main/kotlin/mosaik/docs").apply { mkdirs() }
            File(dir, "src/main/kotlin/mosaik/docs/Page.kt").apply {
                writeText(
                    """package mosaik.docs

import kotlinx.html.*

fun example() {
    div("flex gap-4") {}
}
""",
                )
            }

            val result = runner(dir, "scanDaisyUiTokens").build()

            result.output shouldContain "No DaisyUI class tokens found"
        }

        test("task is registered in mosaik quality group") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            val result = runner(dir, "tasks", "--group=mosaik quality").build()

            result.output shouldContain "scanDaisyUiTokens"
        }

        test("task fails when DaisyUI tokens are found") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)
            File(dir, "src/main/kotlin/mosaik/docs").apply { mkdirs() }
            File(dir, "src/main/kotlin/mosaik/docs/Page.kt").apply {
                writeText(
                    """package mosaik.docs

import kotlinx.html.*

fun example() {
    h6("footer-title") { +"Services" }
}
""",
                )
            }

            val result = runner(dir, "scanDaisyUiTokens").buildAndFail()

            result.output shouldContain "Found 1 DaisyUI class token(s)"
            result.output shouldContain "mosaik/docs/Page.kt"
        }

        test("check task depends on scanDaisyUiTokens when lifecycle-base plugin is applied") {
            val dir = createTempDirectory().toFile()
            dir.resolve("settings.gradle.kts").writeText("""rootProject.name = "test-project"""")
            dir.resolve("build.gradle.kts").writeText(
                """
                plugins {
                    id("lifecycle-base")
                    id("dev.jwillert.mosaik")
                }
                """.trimIndent(),
            )
            File(dir, "src/main/kotlin/mosaik/docs").apply { mkdirs() }
            File(dir, "src/main/kotlin/mosaik/docs/Page.kt").apply {
                writeText(
                    """package mosaik.docs

import kotlinx.html.*

fun example() {
    div("flex gap-4") {}
}
""",
                )
            }

            val result = runner(dir, "check").build()

            result.task(":scanDaisyUiTokens")?.outcome shouldBe TaskOutcome.SUCCESS
        }
    })
