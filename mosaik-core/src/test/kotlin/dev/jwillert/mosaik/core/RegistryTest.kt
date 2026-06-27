package dev.jwillert.mosaik.core

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

private val SAMPLE =
    """
    {
      "components": [
        { "name": "theme",  "description": "Foundation",        "files": ["Theme.kt"],  "dependencies": [] },
        { "name": "button", "description": "DaisyUI button",    "files": ["Button.kt"], "dependencies": ["theme"] }
      ]
    }
    """.trimIndent()

class RegistryTest :
    FunSpec({

        test("parses component entries from JSON and looks them up by name") {
            val registry = Registry.fromJson(SAMPLE)

            val button = registry.get("button")!!
            button.name shouldBe "button"
            button.description shouldBe "DaisyUI button"
            button.files shouldContainExactly listOf("Button.kt")
            button.dependencies shouldContainExactly listOf("theme")
        }

        test("get returns null for an unknown component") {
            Registry.fromJson(SAMPLE).get("modal").shouldBeNull()
        }

        test("require throws for an unknown component") {
            shouldThrow<IllegalArgumentException> { Registry.fromJson(SAMPLE).require("modal") }
        }

        test("all preserves declaration order") {
            Registry.fromJson(SAMPLE).all().map { it.name } shouldContainExactly listOf("theme", "button")
        }
    })
