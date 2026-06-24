package dev.jwillert.mosaik.core

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly

class ResolverTest : FunSpec({

    fun registry(json: String) = Registry.fromJson(json)

    test("returns dependencies before dependents in topological order") {
        val registry = registry(
            """
            {
              "components": [
                { "name": "theme",  "files": ["Theme.kt"],  "dependencies": [] },
                { "name": "button", "files": ["Button.kt"], "dependencies": ["theme"] }
              ]
            }
            """.trimIndent(),
        )

        Resolver(registry).resolve("button").map { it.name } shouldContainExactly listOf("theme", "button")
    }

    test("resolving a leaf component returns just that component") {
        val registry = registry("""{ "components": [ { "name": "theme", "files": [], "dependencies": [] } ] }""")

        Resolver(registry).resolve("theme").map { it.name } shouldContainExactly listOf("theme")
    }

    test("deduplicates a diamond dependency, keeping topological order") {
        val registry = registry(
            """
            {
              "components": [
                { "name": "theme",  "dependencies": [] },
                { "name": "left",   "dependencies": ["theme"] },
                { "name": "right",  "dependencies": ["theme"] },
                { "name": "modal",  "dependencies": ["left", "right"] }
              ]
            }
            """.trimIndent(),
        )

        Resolver(registry).resolve("modal").map { it.name } shouldContainExactly
            listOf("theme", "left", "right", "modal")
    }

    test("detects a dependency cycle") {
        val registry = registry(
            """
            {
              "components": [
                { "name": "a", "dependencies": ["b"] },
                { "name": "b", "dependencies": ["a"] }
              ]
            }
            """.trimIndent(),
        )

        shouldThrow<IllegalStateException> { Resolver(registry).resolve("a") }
    }

    test("throws when a dependency is unknown") {
        val registry = registry("""{ "components": [ { "name": "button", "dependencies": ["theme"] } ] }""")

        shouldThrow<IllegalArgumentException> { Resolver(registry).resolve("button") }
    }
})
