package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import kotlinx.html.*
import kotlinx.html.stream.createHTML

/** Renders a flow fragment without pretty-printing so assertions are deterministic. */
private fun render(block: FlowContent.() -> Unit): String = createHTML(prettyPrint = false).div { block() }

class ButtonTest :
    FunSpec({

        test("a neutral medium button renders the base and variant classes, omitting the default size") {
            val html = render { mButton(variant = ButtonVariant.Neutral, size = Size.Md) { +"Click" } }

            html shouldContain "<button class=\"btn btn-neutral\">Click</button>"
        }

        test("every non-default size maps to its DaisyUI class and Md is omitted") {
            val expected =
                mapOf(
                    Size.Xs to "btn-xs",
                    Size.Sm to "btn-sm",
                    Size.Lg to "btn-lg",
                    Size.Xl to "btn-xl",
                )

            expected.forEach { (size, css) ->
                val html = render { mButton(variant = ButtonVariant.Primary, size = size) {} }
                html shouldContain "class=\"btn btn-primary $css\""
            }

            val md = render { mButton(variant = ButtonVariant.Primary, size = Size.Md) {} }
            md shouldNotContain "btn-md"
        }

        test("disabled state renders the disabled attribute") {
            val html =
                render {
                    mButton(variant = ButtonVariant.Primary) {
                        disabled = true
                        +"Off"
                    }
                }

            html shouldContain "disabled=\"disabled\""
            html shouldContain "btn btn-primary"
        }

        test("custom classes are appended after the variant and size classes") {
            val html =
                render {
                    mButton(
                        variant = ButtonVariant.Secondary,
                        size = Size.Lg,
                        classes = "w-full shadow",
                    ) {}
                }

            html shouldContain "class=\"btn btn-secondary btn-lg w-full shadow\""
        }

        test("the block receives the raw button element so its html attributes apply natively") {
            val html =
                render {
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

        test("arbitrary attributes (e.g. htmx) work natively via the raw element") {
            val html =
                render {
                    mButton(variant = ButtonVariant.Primary) {
                        attributes["hx-get"] = "/save"
                        +"Save"
                    }
                }

            html shouldContain "hx-get=\"/save\""
            html shouldContain "class=\"btn btn-primary\""
        }

        test("buildClasses joins non-blank tokens and drops null and blank ones") {
            buildClasses("btn", null, "btn-primary", "", "  ", "w-full") shouldBe "btn btn-primary w-full"
        }

        // Type-safe modifier tests - Component-specific enums

        test("ButtonVariant declares exactly the color role variants without ghost or link") {
            ButtonVariant.entries.map { it.name } shouldBe
                listOf("Neutral", "Primary", "Secondary", "Accent", "Info", "Success", "Warning", "Error")
        }

        test("ButtonStyle declares outline, ghost, and link styles") {
            ButtonStyle.entries.map { it.name } shouldBe
                listOf("Outline", "Ghost", "Link")
        }

        test("ButtonShape declares circle and square shapes") {
            ButtonShape.entries.map { it.name } shouldBe
                listOf("Circle", "Square")
        }

        test("ButtonWidth declares wide and block widths") {
            ButtonWidth.entries.map { it.name } shouldBe
                listOf("Wide", "Block")
        }

        // Variant rendering tests

        test("ButtonVariant neutral is the default and renders btn-neutral") {
            val html = render { mButton(variant = ButtonVariant.Neutral) { +"Click" } }

            html shouldContain "class=\"btn btn-neutral\""
        }

        test("every ButtonVariant maps to its DaisyUI class") {
            val expected =
                mapOf(
                    ButtonVariant.Neutral to "btn-neutral",
                    ButtonVariant.Primary to "btn-primary",
                    ButtonVariant.Secondary to "btn-secondary",
                    ButtonVariant.Accent to "btn-accent",
                    ButtonVariant.Info to "btn-info",
                    ButtonVariant.Success to "btn-success",
                    ButtonVariant.Warning to "btn-warning",
                    ButtonVariant.Error to "btn-error",
                )

            expected.forEach { (variant, css) ->
                val html = render { mButton(variant = variant) {} }
                html shouldContain "class=\"btn $css\""
            }
        }

        // Style rendering tests

        test("button style defaults to null and renders no style class") {
            val html = render { mButton { +"Click" } }

            html shouldNotContain "btn-outline"
            html shouldNotContain "btn-ghost"
            html shouldNotContain "btn-link"
        }

        test("every ButtonStyle maps to its DaisyUI class") {
            val expected =
                mapOf(
                    ButtonStyle.Outline to "btn-outline",
                    ButtonStyle.Ghost to "btn-ghost",
                    ButtonStyle.Link to "btn-link",
                )

            expected.forEach { (style, css) ->
                val html = render { mButton(style = style) {} }
                html shouldContain "class=\"btn btn-neutral $css\""
            }
        }

        test("ghost style can combine with color variants") {
            val html =
                render {
                    mButton(variant = ButtonVariant.Primary, style = ButtonStyle.Ghost) { +"Click" }
                }

            html shouldContain "class=\"btn btn-primary btn-ghost\""
        }

        // Shape rendering tests

        test("button shape defaults to null and renders no shape class") {
            val html = render { mButton { +"Click" } }

            html shouldNotContain "btn-circle"
            html shouldNotContain "btn-square"
        }

        test("every ButtonShape maps to its DaisyUI class") {
            val expected =
                mapOf(
                    ButtonShape.Circle to "btn-circle",
                    ButtonShape.Square to "btn-square",
                )

            expected.forEach { (shape, css) ->
                val html = render { mButton(shape = shape) {} }
                html shouldContain "class=\"btn btn-neutral $css\""
            }
        }

        // Width rendering tests

        test("button width defaults to null and renders no width class") {
            val html = render { mButton { +"Click" } }

            html shouldNotContain "btn-wide"
            html shouldNotContain "btn-block"
        }

        test("every ButtonWidth maps to its DaisyUI class") {
            val expected =
                mapOf(
                    ButtonWidth.Wide to "btn-wide",
                    ButtonWidth.Block to "btn-block",
                )

            expected.forEach { (width, css) ->
                val html = render { mButton(width = width) {} }
                html shouldContain "class=\"btn btn-neutral $css\""
            }
        }

        // Combined modifier tests

        test("variant, style, shape, width, size, and classes combine correctly") {
            val html =
                render {
                    mButton(
                        variant = ButtonVariant.Primary,
                        style = ButtonStyle.Outline,
                        shape = ButtonShape.Square,
                        width = ButtonWidth.Block,
                        size = Size.Lg,
                        classes = "shadow-lg",
                    ) { +"Save" }
                }

            html shouldContain "class=\"btn btn-primary btn-outline btn-square btn-block btn-lg shadow-lg\""
        }

        // mButtonLink tests - anchor-compatible button API

        test("mButtonLink renders an anchor with button classes") {
            val html = render { mButtonLink(href = "/docs") { +"Docs" } }

            html shouldContain "<a href=\"/docs\" class=\"btn btn-neutral\">Docs</a>"
        }

        test("mButtonLink supports all ButtonVariant options") {
            val html = render { mButtonLink(href = "/home", variant = ButtonVariant.Primary) { +"Home" } }

            html shouldContain "class=\"btn btn-primary\""
            html shouldContain "href=\"/home\""
        }

        test("mButtonLink supports ButtonStyle including ghost") {
            val html = render { mButtonLink(href = "/", style = ButtonStyle.Ghost) { +"Brand" } }

            html shouldContain "class=\"btn btn-neutral btn-ghost\""
        }

        test("mButtonLink combines variant, style, size, and classes") {
            val html =
                render {
                    mButtonLink(
                        href = "/about",
                        variant = ButtonVariant.Primary,
                        style = ButtonStyle.Ghost,
                        size = Size.Lg,
                        classes = "text-xl",
                    ) { +"About" }
                }

            html shouldContain "href=\"/about\""
            html shouldContain "class=\"btn btn-primary btn-ghost btn-lg text-xl\""
        }

        test("mButtonLink receives the raw anchor element so html attributes apply natively") {
            val html =
                render {
                    mButtonLink(href = "/search") {
                        id = "search-link"
                        target = "_blank"
                        rel = "noopener"
                        +"Search"
                    }
                }

            html shouldContain "href=\"/search\""
            html shouldContain "id=\"search-link\""
            html shouldContain "target=\"_blank\""
            html shouldContain "rel=\"noopener\""
        }

        test("mButtonLink supports arbitrary attributes via the raw element") {
            val html =
                render {
                    mButtonLink(href = "/nav", style = ButtonStyle.Ghost) {
                        attributes["hx-get"] = "/api/nav"
                        attributes["hx-target"] = "#content"
                        +"Navigation"
                    }
                }

            html shouldContain "hx-get=\"/api/nav\""
            html shouldContain "hx-target=\"#content\""
            html shouldContain "class=\"btn btn-neutral btn-ghost\""
        }
    })
