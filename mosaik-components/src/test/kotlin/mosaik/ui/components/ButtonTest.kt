package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.stream.createHTML

/** Renders a flow fragment without pretty-printing so assertions are deterministic. */
private fun render(block: FlowContent.() -> Unit): String =
    createHTML(prettyPrint = false).div { block() }

class ButtonTest : FunSpec({

    test("a primary medium button renders the base and variant classes, omitting the default size") {
        val html = render { mButton { variant = Variant.Primary; size = Size.Md; +"Click" } }

        html shouldContain "<button class=\"btn btn-primary\">Click</button>"
    }

    test("every variant maps to its DaisyUI class") {
        val expected = mapOf(
            Variant.Primary to "btn-primary",
            Variant.Secondary to "btn-secondary",
            Variant.Accent to "btn-accent",
            Variant.Ghost to "btn-ghost",
            Variant.Link to "btn-link",
            Variant.Error to "btn-error",
            Variant.Success to "btn-success",
            Variant.Warning to "btn-warning",
        )

        expected.forEach { (variant, css) ->
            val html = render { mButton { this.variant = variant } }
            html shouldContain "class=\"btn $css\""
        }
    }

    test("every non-default size maps to its DaisyUI class and Md is omitted") {
        val expected = mapOf(
            Size.Xs to "btn-xs",
            Size.Sm to "btn-sm",
            Size.Lg to "btn-lg",
            Size.Xl to "btn-xl",
        )

        expected.forEach { (size, css) ->
            val html = render { mButton { variant = Variant.Primary; this.size = size } }
            html shouldContain "class=\"btn btn-primary $css\""
        }

        val md = render { mButton { variant = Variant.Primary; size = Size.Md } }
        md shouldNotContain "btn-md"
    }

    test("disabled state renders the disabled attribute") {
        val html = render { mButton { variant = Variant.Primary; disabled = true; +"Off" } }

        html shouldContain "disabled=\"disabled\""
        html shouldContain "btn btn-primary"
    }

    test("custom classes are appended after the variant and size classes") {
        val html = render {
            mButton { variant = Variant.Secondary; size = Size.Lg; classes = "w-full shadow" }
        }

        html shouldContain "class=\"btn btn-secondary btn-lg w-full shadow\""
    }

    test("html properties are delegated to the underlying button element") {
        val html = render {
            mButton {
                id = "submit"
                name = "action"
                type = ButtonType.submit
                +"Go"
            }
        }

        html shouldContain "id=\"submit\""
        html shouldContain "name=\"action\""
        html shouldContain "type=\"submit\""
    }

    test("buildClasses joins non-blank tokens and drops null and blank ones") {
        buildClasses("btn", null, "btn-primary", "", "  ", "w-full") shouldBe "btn btn-primary w-full"
    }
})
