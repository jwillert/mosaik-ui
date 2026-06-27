package dev.jwillert.mosaik.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.io.File

class DaisyUiTokenScannerTest :
    FunSpec({

        test("finds DaisyUI component token in a docs file") {
            val tempDir = tempdir()
            val file =
                File(tempDir, "DocsPage.kt").apply {
                    writeText(
                        """package mosaik.docs

import kotlinx.html.*

fun example() {
    h6("footer-title") { +"Services" }
}
""",
                    )
                }

            val findings = DaisyUiTokenScanner.scan(tempDir)

            findings shouldHaveSize 1
            findings.first().file.name shouldBe "DocsPage.kt"
            findings.first().line shouldBe 6
            findings.first().token shouldBe "footer-title"
        }

        test("finds DaisyUI modifier token in a docs file") {
            val tempDir = tempdir()
            val file =
                File(tempDir, "DocsPage.kt").apply {
                    writeText(
                        """package mosaik.docs

import kotlinx.html.*

fun example() {
    table("table table-zebra") {
    }
}
""",
                    )
                }

            val findings = DaisyUiTokenScanner.scan(tempDir)

            findings shouldHaveSize 2
            findings shouldContain Finding(file, 6, "table", "Table")
            findings shouldContain Finding(file, 6, "table-zebra", "Table")
        }

        test("allowlists component implementation files") {
            val tempDir = tempdir()
            File(tempDir, "mosaik/ui/components").apply { mkdirs() }
            File(tempDir, "mosaik/ui/components/Button.kt").apply {
                writeText(
                    """package mosaik.ui.components

import kotlinx.html.*

fun FlowContent.mButton() {
    button(classes = "btn btn-primary") {}
}
""",
                )
            }

            val findings = DaisyUiTokenScanner.scan(tempDir)

            findings.shouldBeEmpty()
        }

        test("allowlists component test files") {
            val tempDir = tempdir()
            File(tempDir, "mosaik/ui/components").apply { mkdirs() }
            File(tempDir, "mosaik/ui/components/ButtonTest.kt").apply {
                writeText(
                    """package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain

class ButtonTest : FunSpec({
    test("renders btn class") {
        result shouldContain "btn"
    }
})
""",
                )
            }

            val findings = DaisyUiTokenScanner.scan(tempDir)

            findings.shouldBeEmpty()
        }

        test("does not flag Tailwind utility classes") {
            val tempDir = tempdir()
            val file =
                File(tempDir, "DocsPage.kt").apply {
                    writeText(
                        """package mosaik.docs

import kotlinx.html.*

fun example() {
    div("flex gap-4 p-4 text-blue-500") {
    }
}
""",
                    )
                }

            val findings = DaisyUiTokenScanner.scan(tempDir)

            findings.shouldBeEmpty()
        }

        test("suggests component family when known") {
            val tempDir = tempdir()
            val file =
                File(tempDir, "DocsPage.kt").apply {
                    writeText(
                        """package mosaik.docs

import kotlinx.html.*

fun example() {
    div("card-body") {}
}
""",
                    )
                }

            val findings = DaisyUiTokenScanner.scan(tempDir)

            findings shouldHaveSize 1
            findings.first().suggestedComponent shouldBe "Card"
        }
    })
