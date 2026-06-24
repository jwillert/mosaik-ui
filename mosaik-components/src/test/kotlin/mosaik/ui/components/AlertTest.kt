package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.html.*
import kotlinx.html.stream.createHTML

/** Renders a flow fragment without pretty-printing so assertions are deterministic. */
private fun render(block: FlowContent.() -> Unit): String =
    createHTML(prettyPrint = false).div { block() }

class AlertTest : FunSpec({

    test("an info alert renders the base and variant classes and the alert role") {
        val html = render { mAlert(AlertVariant.Info) { +"Heads up" } }

        html shouldContain "<div class=\"alert alert-info\" role=\"alert\">Heads up</div>"
    }

    test("every variant maps to its DaisyUI class") {
        val expected = mapOf(
            AlertVariant.Info to "alert-info",
            AlertVariant.Success to "alert-success",
            AlertVariant.Warning to "alert-warning",
            AlertVariant.Error to "alert-error",
        )

        expected.forEach { (variant, css) ->
            val html = render { mAlert(variant) {} }
            html shouldContain "class=\"alert $css\""
        }
    }

    test("AlertVariant declares exactly the four DaisyUI alert variants") {
        AlertVariant.entries.map { it.name } shouldBe
            listOf("Info", "Success", "Warning", "Error")
    }

    test("custom classes are appended after the variant class") {
        val html = render {
            mAlert(AlertVariant.Success, "shadow-lg") {}
        }

        html shouldContain "class=\"alert alert-success shadow-lg\""
    }

    test("the block receives the raw div element so its html attributes apply natively") {
        val html = render {
            mAlert(AlertVariant.Warning) {
                id = "session-warning"
                +"Your session is about to expire"
            }
        }

        html shouldContain "id=\"session-warning\""
        html shouldContain "alert alert-warning"
    }

    test("arbitrary attributes (e.g. htmx) work natively via the raw element") {
        val html = render {
            mAlert(AlertVariant.Error) {
                attributes["hx-get"] = "/retry"
                +"Failed"
            }
        }

        html shouldContain "hx-get=\"/retry\""
        html shouldContain "class=\"alert alert-error\""
    }

    test("the block can override the default alert role") {
        val html = render {
            mAlert(AlertVariant.Info) {
                attributes["role"] = "status"
                +"Saved"
            }
        }

        html shouldContain "role=\"status\""
    }
})
