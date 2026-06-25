package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import kotlinx.html.*
import kotlinx.html.stream.createHTML

/** Renders a flow fragment without pretty-printing so assertions are deterministic. */
private fun render(block: FlowContent.() -> Unit): String =
    createHTML(prettyPrint = false).div { block() }

class FooterTest : FunSpec({

    test("an empty footer renders the base footer class on a footer element") {
        val html = render { mFooter {} }

        html shouldContain "<footer class=\"footer\"></footer>"
    }

    test("custom classes are appended after the base footer class") {
        val html = render { mFooter("footer-center bg-base-200 p-4") {} }

        html shouldContain "class=\"footer footer-center bg-base-200 p-4\""
    }

    test("footer content renders inside the footer element") {
        val html = render { mFooter { p { +"© 2026 Mosaik UI" } } }

        html shouldContain "<footer class=\"footer\"><p>© 2026 Mosaik UI</p></footer>"
    }

    test("a footer composes nav sections with headings and links") {
        val html = render {
            mFooter("bg-base-200 p-10") {
                nav {
                    h6("footer-title") { +"Services" }
                    a(href = "/branding", classes = "link link-hover") { +"Branding" }
                }
            }
        }

        html shouldContain "<footer class=\"footer bg-base-200 p-10\">"
        html shouldContain "<h6 class=\"footer-title\">Services</h6>"
        html shouldContain "<a href=\"/branding\" class=\"link link-hover\">Branding</a>"
    }

    test("the footer block receives the raw footer element so its html attributes apply natively") {
        val html = render {
            mFooter {
                id = "site-footer"
                attributes["hx-get"] = "/footer"
            }
        }

        html shouldContain "id=\"site-footer\""
        html shouldContain "hx-get=\"/footer\""
        html shouldContain "class=\"footer\""
    }
})
