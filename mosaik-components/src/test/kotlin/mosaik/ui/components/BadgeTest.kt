package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import kotlinx.html.*
import kotlinx.html.stream.createHTML

/** Renders a flow fragment without pretty-printing so assertions are deterministic. */
private fun render(block: FlowContent.() -> Unit): String = createHTML(prettyPrint = false).div { block() }

class BadgeTest :
    FunSpec({

        test("a primary medium badge renders the base and variant classes, omitting the default size") {
            val html = render { mBadge(BadgeVariant.Primary, Size.Md) { +"New" } }

            html shouldContain "<span class=\"badge badge-primary\">New</span>"
        }

        test("every variant maps to its DaisyUI class") {
            val expected =
                mapOf(
                    BadgeVariant.Primary to "badge-primary",
                    BadgeVariant.Secondary to "badge-secondary",
                    BadgeVariant.Accent to "badge-accent",
                    BadgeVariant.Ghost to "badge-ghost",
                    BadgeVariant.Info to "badge-info",
                    BadgeVariant.Success to "badge-success",
                    BadgeVariant.Warning to "badge-warning",
                    BadgeVariant.Error to "badge-error",
                    BadgeVariant.Neutral to "badge-neutral",
                    BadgeVariant.Outline to "badge-outline",
                )

            expected.forEach { (variant, css) ->
                val html = render { mBadge(variant) {} }
                html shouldContain "class=\"badge $css\""
            }
        }

        test("BadgeVariant declares exactly the ten DaisyUI badge variants") {
            BadgeVariant.entries.map { it.name } shouldBe
                listOf(
                    "Primary",
                    "Secondary",
                    "Accent",
                    "Ghost",
                    "Info",
                    "Success",
                    "Warning",
                    "Error",
                    "Neutral",
                    "Outline",
                )
        }

        test("every non-default size maps to its DaisyUI class and Md is omitted") {
            val expected =
                mapOf(
                    Size.Xs to "badge-xs",
                    Size.Sm to "badge-sm",
                    Size.Lg to "badge-lg",
                    Size.Xl to "badge-xl",
                )

            expected.forEach { (size, css) ->
                val html = render { mBadge(BadgeVariant.Primary, size) {} }
                html shouldContain "class=\"badge badge-primary $css\""
            }

            val md = render { mBadge(BadgeVariant.Primary, Size.Md) {} }
            md shouldNotContain "badge-md"
        }

        test("custom classes are appended after the variant and size classes") {
            val html =
                render {
                    mBadge(BadgeVariant.Secondary, Size.Lg, "uppercase tracking-wide") {}
                }

            html shouldContain "class=\"badge badge-secondary badge-lg uppercase tracking-wide\""
        }

        test("the block receives the raw span element so its html attributes apply natively") {
            val html =
                render {
                    mBadge(BadgeVariant.Info) {
                        id = "status"
                        title = "Online"
                        +"●"
                    }
                }

            html shouldContain "id=\"status\""
            html shouldContain "title=\"Online\""
            html shouldContain "badge badge-info"
        }

        test("arbitrary attributes (e.g. htmx) work natively via the raw element") {
            val html =
                render {
                    mBadge(BadgeVariant.Primary) {
                        attributes["hx-get"] = "/count"
                        +"3"
                    }
                }

            html shouldContain "hx-get=\"/count\""
            html shouldContain "class=\"badge badge-primary\""
        }
    })
