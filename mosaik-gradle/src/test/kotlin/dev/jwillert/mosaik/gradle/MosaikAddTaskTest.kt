package dev.jwillert.mosaik.gradle

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.io.path.createTempDirectory

/**
 * TestKit integration tests for the install pipeline. They apply the real plugin to a throwaway
 * project, run the registry tasks, and assert on the files written and the console output.
 */
class MosaikAddTaskTest :
    FunSpec({

        fun setupProject(
            dir: File,
            packageName: String = "com.example.ui",
        ) {
            dir.resolve("settings.gradle.kts").writeText("""rootProject.name = "test-project"""")
            dir.resolve("build.gradle.kts").writeText(
                """
                plugins {
                    id("dev.jwillert.mosaik")
                }
                mosaikUi {
                    packageName.set("$packageName")
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

        test("mosaikAdd installs the component and its transitive theme dependency with the user's package") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            val result = runner(dir, "mosaikAdd", "--component=button").build()

            val pkgDir = dir.resolve("src/main/kotlin/com/example/ui")
            val button = pkgDir.resolve("Button.kt")
            val theme = pkgDir.resolve("Theme.kt")

            button.exists() shouldBe true
            theme.exists() shouldBe true
            button.readText() shouldContain "package com.example.ui"
            button.readText() shouldNotContain "package mosaik.ui.components"
            theme.readText() shouldContain "package com.example.ui"

            // Dependency installed before the component, both logged.
            result.output shouldContain "theme: installed Theme.kt"
            result.output shouldContain "button: installed Button.kt"
        }

        test("mosaikAdd aborts on an existing file but overwrites with --force") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)
            runner(dir, "mosaikAdd", "--component=button").build()

            val button = dir.resolve("src/main/kotlin/com/example/ui/Button.kt")
            button.writeText("// hand-edited by the user")

            // Without --force the user's edits survive.
            val again = runner(dir, "mosaikAdd", "--component=button").build()
            button.readText() shouldBe "// hand-edited by the user"
            again.output shouldContain "already present"

            // With --force the component is reinstalled.
            runner(dir, "mosaikAdd", "--component=button", "--force").build()
            button.readText() shouldContain "package com.example.ui"
        }

        test("mosaikAdd fails with a clear message for an unknown component") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            val result = runner(dir, "mosaikAdd", "--component=nope").buildAndFail()

            result.output shouldContain "Unknown component 'nope'"
        }

        test("mosaikList prints every available component") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            val result = runner(dir, "mosaikList").build()

            result.output shouldContain "theme"
            result.output shouldContain "button"
            result.output shouldContain "depends on: theme"
        }

        test("mosaikStatus shows installed vs available components") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)
            runner(dir, "mosaikAdd", "--component=theme").build()

            val result = runner(dir, "mosaikStatus").build()

            result.output shouldContain "[x] theme"
            result.output shouldContain "[ ] button"
        }

        test("mosaikAdd writes .mosaik/components.json inventory after installing") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            runner(dir, "mosaikAdd", "--component=button").build()

            val inventory = dir.resolve(".mosaik/components.json")
            inventory.exists() shouldBe true

            val json = inventory.readText()
            json shouldContain "\"schemaVersion\": \"1\""
            json shouldContain "\"package\": \"com.example.ui\""
            json shouldContain "\"prefix\": \"m\""
            json shouldContain "\"theme\""
            json shouldContain "\"button\""
            json shouldContain "Theme.kt"
            json shouldContain "Button.kt"
        }

        test("mosaikAdd updates existing inventory when adding more components") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            runner(dir, "mosaikAdd", "--component=button").build()
            runner(dir, "mosaikAdd", "--component=card").build()

            val inventory = dir.resolve(".mosaik/components.json")
            val json = inventory.readText()
            json shouldContain "\"button\""
            json shouldContain "\"card\""
            json shouldContain "\"theme\""
        }

        test("mosaikAdd generates checksums for installed files") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            runner(dir, "mosaikAdd", "--component=button").build()

            val inventory = dir.resolve(".mosaik/components.json")
            val json = inventory.readText()

            // Should have a checksums section with Button.kt
            json shouldContain "\"checksums\":"
            json shouldContain "\"Button.kt\":"
            // Checksum should be a 64-character SHA-256 hex string
            val checksumPattern = Regex(""""Button\.kt":\s*"([0-9a-f]{64})"""")
            val match = checksumPattern.find(json)
            match shouldNotBe null
        }

        test("mosaikAdd checksums are deterministic") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            runner(dir, "mosaikAdd", "--component=button").build()
            val firstInventory = dir.resolve(".mosaik/components.json").readText()

            // Extract the checksum from first install
            val checksumPattern = Regex(""""Button\.kt":\s*"([0-9a-f]{64})"""")
            val firstChecksum = checksumPattern.find(firstInventory)?.groupValues?.get(1)

            // Remove and reinstall
            dir.resolve(".mosaik/components.json").delete()
            runner(dir, "mosaikAdd", "--component=button", "--force").build()
            val secondInventory = dir.resolve(".mosaik/components.json").readText()

            val secondChecksum = checksumPattern.find(secondInventory)?.groupValues?.get(1)

            // Checksums should be the same
            firstChecksum shouldNotBe null
            secondChecksum shouldNotBe null
            firstChecksum shouldBe secondChecksum
        }

        test("mosaikStatus detects modified files via checksum drift") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            runner(dir, "mosaikAdd", "--component=button").build()

            // Modify the installed file
            val button = dir.resolve("src/main/kotlin/com/example/ui/Button.kt")
            button.appendText("\n// user modification")

            val result = runner(dir, "mosaikStatus").build()

            // Status should show drift
            result.output shouldContain "[~] button - modified: Button.kt"
        }

        test("mosaikStatus is read-only and does not modify inventory") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            runner(dir, "mosaikAdd", "--component=button").build()

            val inventoryBefore = dir.resolve(".mosaik/components.json").readText()
            val modTimeBefore = dir.resolve(".mosaik/components.json").lastModified()

            // Wait a moment to ensure timestamp would change if file was written
            Thread.sleep(100)

            runner(dir, "mosaikStatus").build()

            val inventoryAfter = dir.resolve(".mosaik/components.json").readText()
            val modTimeAfter = dir.resolve(".mosaik/components.json").lastModified()

            inventoryBefore shouldBe inventoryAfter
            modTimeBefore shouldBe modTimeAfter
        }

        test("mosaikStatus shows clean status for unmodified components") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            runner(dir, "mosaikAdd", "--component=button").build()

            val result = runner(dir, "mosaikStatus").build()

            // Should show as installed without drift markers
            result.output shouldContain "[x] button"
            result.output shouldNotContain "[~] button"
        }
    })
