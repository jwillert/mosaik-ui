package dev.jwillert.mosaik.gradle

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.io.path.createTempDirectory

/**
 * TestKit integration tests for the zero-config generator. Config generation is pure file I/O
 * (no npm/network), so it is fully exercised here.
 */
class GenerateTailwindConfigTaskTest :
    FunSpec({

        fun setupProject(
            dir: File,
            extension: String = "",
        ) {
            dir.resolve("settings.gradle.kts").writeText("""rootProject.name = "test-project"""")
            dir.resolve("build.gradle.kts").writeText(
                """
                plugins {
                    id("dev.jwillert.mosaik")
                }
                $extension
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

        test("generateTailwindConfig creates input.css, postcss.config.js and package.json") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            runner(dir, "generateTailwindConfig").build()

            dir.resolve("input.css").exists() shouldBe true
            dir.resolve("postcss.config.js").exists() shouldBe true
            dir.resolve("package.json").exists() shouldBe true
            dir.resolve("input.css").readText() shouldContain "@plugin \"daisyui\""
        }

        test("generateTailwindConfig does not overwrite an existing input.css") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)
            val existing = "/* user's own tailwind entrypoint */"
            dir.resolve("input.css").writeText(existing)

            runner(dir, "generateTailwindConfig").build()

            dir.resolve("input.css").readText() shouldBe existing
        }

        test("css extension drives the generated input.css themes and sources") {
            val dir = createTempDirectory().toFile()
            setupProject(
                dir,
                extension =
                    """
                    mosaikUi {
                        css {
                            themes.set(listOf("dark", "cupcake"))
                            scanPaths.set(listOf("ui", "templates"))
                        }
                    }
                    """.trimIndent(),
            )

            runner(dir, "generateTailwindConfig").build()

            val css = dir.resolve("input.css").readText()
            css shouldContain "themes: dark, cupcake;"
            css shouldContain "@source \"ui\";"
            css shouldContain "@source \"templates\";"
        }
    })
