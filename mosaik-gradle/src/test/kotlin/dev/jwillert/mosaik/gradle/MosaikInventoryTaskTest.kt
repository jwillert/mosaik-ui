package dev.jwillert.mosaik.gradle

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.io.path.createTempDirectory

/**
 * TestKit tests for the mosaikInventory task that regenerates .mosaik/components.json by
 * scanning the project's package directory for installed component files.
 */
class MosaikInventoryTaskTest :
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

        test("mosaikInventory regenerates inventory when file is missing") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            // Install components normally
            runner(dir, "mosaikAdd", "--component=button").build()

            // Delete the inventory
            val inventory = dir.resolve(".mosaik/components.json")
            inventory.delete()
            inventory.exists() shouldBe false

            // Regenerate
            val result = runner(dir, "mosaikInventory").build()

            // Inventory should be recreated
            inventory.exists() shouldBe true
            val json = inventory.readText()
            json shouldContain "\"button\""
            json shouldContain "\"theme\""
            result.output shouldContain "Regenerated .mosaik/components.json"
        }

        test("mosaikInventory discovers installed components by matching registry files") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            // Install button (which brings in theme)
            runner(dir, "mosaikAdd", "--component=button").build()

            // Delete inventory
            dir.resolve(".mosaik/components.json").delete()

            // Regenerate
            runner(dir, "mosaikInventory").build()

            val inventory = dir.resolve(".mosaik/components.json")
            val json = inventory.readText()

            // Should discover both button and its dependency theme
            json shouldContain "\"name\": \"button\""
            json shouldContain "\"name\": \"theme\""
            json shouldContain "\"Button.kt\""
            json shouldContain "\"Theme.kt\""
        }

        test("mosaikInventory includes checksums for discovered files") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            runner(dir, "mosaikAdd", "--component=button").build()
            dir.resolve(".mosaik/components.json").delete()

            runner(dir, "mosaikInventory").build()

            val inventory = dir.resolve(".mosaik/components.json")
            val json = inventory.readText()

            json shouldContain "\"checksums\":"
            json shouldContain "\"Button.kt\":"
            // Should be a 64-character SHA-256 hex string
            val checksumPattern = Regex(""""Button\.kt":\s*"([0-9a-f]{64})"""")
            checksumPattern.find(json) shouldNotBe null
        }

        test("mosaikInventory uses registry metadata for API metadata") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            runner(dir, "mosaikAdd", "--component=button").build()
            dir.resolve(".mosaik/components.json").delete()

            runner(dir, "mosaikInventory").build()

            val inventory = dir.resolve(".mosaik/components.json")
            val json = inventory.readText()

            // Should include API metadata from registry
            json shouldContain "\"api\":"
            json shouldContain "\"name\": \"mButton\""
            json shouldContain "\"kind\": \"function\""
        }

        test("mosaikInventory regenerates with same schema fields as mosaikAdd") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            runner(dir, "mosaikAdd", "--component=button").build()

            val addInventory = dir.resolve(".mosaik/components.json").readText()

            // Delete and regenerate
            dir.resolve(".mosaik/components.json").delete()
            runner(dir, "mosaikInventory").build()

            val regeneratedInventory = dir.resolve(".mosaik/components.json").readText()

            // Should have the same structure
            regeneratedInventory shouldContain "\"schemaVersion\": \"1\""
            regeneratedInventory shouldContain "\"generatedBy\":"
            regeneratedInventory shouldContain "\"project\":"
            regeneratedInventory shouldContain "\"package\": \"com.example.ui\""
            regeneratedInventory shouldContain "\"prefix\": \"m\""
            regeneratedInventory shouldContain "\"agentHints\":"
        }

        test("mosaikInventory only includes components with all files present") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            runner(dir, "mosaikAdd", "--component=button").build()

            // Delete one file
            val button = dir.resolve("src/main/kotlin/com/example/ui/Button.kt")
            button.delete()

            // Delete inventory
            dir.resolve(".mosaik/components.json").delete()

            // Regenerate
            runner(dir, "mosaikInventory").build()

            val inventory = dir.resolve(".mosaik/components.json")
            val json = inventory.readText()

            // Button should not be in inventory (missing file)
            json shouldContain "\"theme\""
            // Button should not be included since Button.kt is missing
            val buttonPattern = Regex(""""name":\s*"button"""")
            buttonPattern.find(json) shouldBe null
        }

        test("mosaikInventory handles empty package directory") {
            val dir = createTempDirectory().toFile()
            setupProject(dir)

            // Don't install anything, just run inventory
            val result = runner(dir, "mosaikInventory").build()

            val inventory = dir.resolve(".mosaik/components.json")
            inventory.exists() shouldBe true

            val json = inventory.readText()
            json shouldContain "\"components\": ["
            json shouldContain "\"package\": \"com.example.ui\""

            result.output shouldContain "Regenerated .mosaik/components.json"
        }

        test("mosaikInventory preserves package and prefix config") {
            val dir = createTempDirectory().toFile()
            setupProject(dir, "org.custom.components")

            runner(dir, "mosaikAdd", "--component=button").build()
            dir.resolve(".mosaik/components.json").delete()

            runner(dir, "mosaikInventory").build()

            val inventory = dir.resolve(".mosaik/components.json")
            val json = inventory.readText()

            json shouldContain "\"package\": \"org.custom.components\""
            json shouldContain "\"prefix\": \"m\""
        }
    })
