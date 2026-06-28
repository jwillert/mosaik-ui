package dev.jwillert.mosaik.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class InventoryWriterTest :
    FunSpec({

        test("generates valid JSON inventory with schema version and project metadata") {
            val writer = InventoryWriter("com.example.ui", "m", "main")
            val components =
                listOf(
                    ComponentEntry(
                        "theme",
                        "Shared foundation",
                        listOf("Theme.kt"),
                        emptyList(),
                        listOf(
                            ApiMetadata("Variant", "enum"),
                            ApiMetadata("Size", "enum"),
                        ),
                    ),
                    ComponentEntry(
                        "button",
                        "DaisyUI button",
                        listOf("Button.kt"),
                        listOf("theme"),
                        listOf(
                            ApiMetadata("mButton", "function"),
                            ApiMetadata("ButtonVariant", "enum"),
                        ),
                    ),
                )

            val json = writer.generate(components)

            json shouldContain "\"schemaVersion\": \"1\""
            json shouldContain "\"package\": \"com.example.ui\""
            json shouldContain "\"prefix\": \"m\""
            json shouldContain "\"sourceSet\": \"main\""
            json shouldContain "\"tool\": \"mosaik-gradle\""

            val parsed = Json.parseToJsonElement(json).jsonObject
            val componentsArray = parsed["components"]?.jsonArray!!
            componentsArray.size shouldBe 2

            val theme = componentsArray[0].jsonObject
            theme["name"]?.jsonPrimitive?.content shouldBe "theme"
            theme["files"]
                ?.jsonArray
                ?.get(0)
                ?.jsonPrimitive
                ?.content shouldBe "Theme.kt"

            val themeApi = theme["api"]?.jsonArray!!
            themeApi.size shouldBe 2
            themeApi[0].jsonObject["name"]?.jsonPrimitive?.content shouldBe "Variant"
            themeApi[0].jsonObject["kind"]?.jsonPrimitive?.content shouldBe "enum"
        }

        test("handles components with no API metadata") {
            val writer = InventoryWriter("com.example.ui")
            val components =
                listOf(
                    ComponentEntry(
                        "simple",
                        "Simple component",
                        listOf("Simple.kt"),
                        emptyList(),
                        emptyList(),
                    ),
                )

            val json = writer.generate(components)
            val parsed = Json.parseToJsonElement(json).jsonObject
            val comp = parsed["components"]?.jsonArray?.get(0)?.jsonObject!!
            comp["api"]?.jsonArray?.size shouldBe 0
        }

        test("escapes JSON special characters in descriptions") {
            val writer = InventoryWriter("com.example.ui")
            val components =
                listOf(
                    ComponentEntry(
                        "special",
                        """Component with "quotes" and newlines""",
                        listOf("Special.kt"),
                        emptyList(),
                        emptyList(),
                    ),
                )

            val json = writer.generate(components)
            json shouldContain """\"quotes\""""
        }

        test("includes checksums for each file") {
            val writer = InventoryWriter("com.example.ui")
            val components =
                listOf(
                    ComponentEntry(
                        "button",
                        "DaisyUI button",
                        listOf("Button.kt"),
                        emptyList(),
                        emptyList(),
                        mapOf("Button.kt" to "abc123"),
                    ),
                )

            val json = writer.generate(components)

            val parsed = Json.parseToJsonElement(json).jsonObject
            val comp = parsed["components"]?.jsonArray?.get(0)?.jsonObject!!
            val checksums = comp["checksums"]?.jsonObject!!
            checksums["Button.kt"]?.jsonPrimitive?.content shouldBe "abc123"
        }

        test("generates deterministic checksums") {
            val content = "package mosaik.ui.components\n\nfun test() {}"
            val checksum1 = InventoryWriter.computeChecksum(content)
            val checksum2 = InventoryWriter.computeChecksum(content)

            checksum1 shouldBe checksum2
        }

        test("different content produces different checksums") {
            val content1 = "package mosaik.ui.components\n\nfun test1() {}"
            val content2 = "package mosaik.ui.components\n\nfun test2() {}"

            val checksum1 = InventoryWriter.computeChecksum(content1)
            val checksum2 = InventoryWriter.computeChecksum(content2)

            checksum1 shouldBe checksum1 // self-check
            checksum2 shouldBe checksum2 // self-check
            // They should be different
            (checksum1 == checksum2) shouldBe false
        }
    })
