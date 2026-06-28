package dev.jwillert.mosaik.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.shouldBe
import kotlin.io.path.createTempDirectory

class InventoryReaderTest :
    FunSpec({

        test("reads existing inventory and returns components by name") {
            val dir = createTempDirectory().toFile()
            val mosaikDir = dir.resolve(".mosaik")
            mosaikDir.mkdirs()
            val inventoryFile = mosaikDir.resolve("components.json")
            inventoryFile.writeText(
                """
                {
                  "schemaVersion": "1",
                  "generatedBy": { "tool": "mosaik-gradle", "version": "1.0.0" },
                  "project": { "package": "com.example.ui", "prefix": "m", "sourceSet": "main" },
                  "agentHints": { "description": "Hints", "prefix": "All start with m" },
                  "components": [
                    {
                      "name": "theme",
                      "description": "Shared foundation",
                      "files": ["Theme.kt"],
                      "dependencies": [],
                      "api": []
                    },
                    {
                      "name": "button",
                      "description": "DaisyUI button",
                      "files": ["Button.kt"],
                      "dependencies": ["theme"],
                      "api": []
                    }
                  ]
                }
                """.trimIndent(),
            )

            val inventory = InventoryReader.read(dir)

            inventory.size shouldBe 2
            inventory["theme"]?.name shouldBe "theme"
            inventory["theme"]?.files shouldBe listOf("Theme.kt")
            inventory["button"]?.name shouldBe "button"
            inventory["button"]?.dependencies shouldBe listOf("theme")
        }

        test("returns empty map when inventory file does not exist") {
            val dir = createTempDirectory().toFile()

            val inventory = InventoryReader.read(dir)

            inventory.shouldBeEmpty()
        }

        test("returns empty map when inventory JSON is malformed") {
            val dir = createTempDirectory().toFile()
            val mosaikDir = dir.resolve(".mosaik")
            mosaikDir.mkdirs()
            val inventoryFile = mosaikDir.resolve("components.json")
            inventoryFile.writeText("{ invalid json }")

            val inventory = InventoryReader.read(dir)

            inventory.shouldBeEmpty()
        }

        test("reads API metadata from inventory") {
            val dir = createTempDirectory().toFile()
            val mosaikDir = dir.resolve(".mosaik")
            mosaikDir.mkdirs()
            val inventoryFile = mosaikDir.resolve("components.json")
            inventoryFile.writeText(
                """
                {
                  "schemaVersion": "1",
                  "generatedBy": { "tool": "mosaik-gradle", "version": "1.0.0" },
                  "project": { "package": "com.example.ui", "prefix": "m", "sourceSet": "main" },
                  "agentHints": { "description": "Hints", "prefix": "All start with m" },
                  "components": [
                    {
                      "name": "button",
                      "description": "DaisyUI button",
                      "files": ["Button.kt"],
                      "dependencies": ["theme"],
                      "api": [
                        { "name": "mButton", "kind": "function" },
                        { "name": "ButtonVariant", "kind": "enum" }
                      ]
                    }
                  ]
                }
                """.trimIndent(),
            )

            val inventory = InventoryReader.read(dir)

            inventory["button"]?.api?.size shouldBe 2
            inventory["button"]?.api?.get(0)?.name shouldBe "mButton"
            inventory["button"]?.api?.get(0)?.kind shouldBe "function"
            inventory["button"]?.api?.get(1)?.name shouldBe "ButtonVariant"
            inventory["button"]?.api?.get(1)?.kind shouldBe "enum"
        }
    })
