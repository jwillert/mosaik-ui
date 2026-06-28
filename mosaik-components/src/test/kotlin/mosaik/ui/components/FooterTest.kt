package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import kotlinx.html.*
import kotlinx.html.stream.createHTML

/** Renders a flow fragment without pretty-printing so assertions are deterministic. */
private fun render(block: FlowContent.() -> Unit): String = createHTML(prettyPrint = false).div { block() }

class FooterTest :
    FunSpec({

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
            val html =
                render {
                    mFooter("bg-base-200 p-10") {
                        nav {
                            mFooterTitle { +"Services" }
                            mLink(href = "/branding") { +"Branding" }
                        }
                    }
                }

            html shouldContain "<footer class=\"footer bg-base-200 p-10\">"
            html shouldContain "<h6 class=\"footer-title\">Services</h6>"
            html shouldContain "<a href=\"/branding\" class=\"link link-hover\">Branding</a>"
        }

        test(
            "the footer block receives a DSL context wrapping the footer element " +
                "so its html attributes apply natively",
        ) {
            val html =
                render {
                    mFooter {
                        id = "site-footer"
                        attributes["hx-get"] = "/footer"
                    }
                }

            html shouldContain "id=\"site-footer\""
            html shouldContain "hx-get=\"/footer\""
            html shouldContain "class=\"footer\""
        }

        test("mFooterTitle renders an h6 with footer-title class") {
            val html =
                render {
                    mFooter {
                        mFooterTitle { +"Services" }
                    }
                }

            html shouldContain "<h6 class=\"footer-title\">Services</h6>"
        }

        test("mFooterTitle accepts extra classes") {
            val html =
                render {
                    mFooter {
                        mFooterTitle("opacity-50") { +"Services" }
                    }
                }

            html shouldContain "<h6 class=\"footer-title opacity-50\">Services</h6>"
        }

        test("mLink renders an anchor with link and link-hover classes") {
            val html =
                render {
                    mFooter {
                        mLink(href = "/about") { +"About us" }
                    }
                }

            html shouldContain "<a href=\"/about\" class=\"link link-hover\">About us</a>"
        }

        test("mLink accepts extra classes") {
            val html =
                render {
                    mFooter {
                        mLink(href = "/contact", classes = "font-bold") { +"Contact" }
                    }
                }

            html shouldContain "<a href=\"/contact\" class=\"link link-hover font-bold\">Contact</a>"
        }

        test("mLink href parameter is required") {
            val html =
                render {
                    mFooter {
                        mLink(href = "#") { +"Placeholder" }
                    }
                }

            html shouldContain "<a href=\"#\" class=\"link link-hover\">Placeholder</a>"
        }
    })
