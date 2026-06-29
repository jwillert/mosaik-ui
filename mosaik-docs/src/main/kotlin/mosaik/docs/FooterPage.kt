package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.mFooter
import mosaik.ui.components.mFooterTitle
import mosaik.ui.components.mLink

/**
 * Footer page content block for use in both full-page and partial renders.
 */
fun FlowContent.footerPageContent() {
    h1 { +"Footer" }
    p {
        +"A footer sits at the bottom of a page and holds copyright, navigation "
        +"columns, social links, or a logo. "
        code { +"mFooter" }
        +" wraps DaisyUI's "
        code { +"footer" }
        +" classes and, like Card and Navbar, takes no colour role or size: it is "
        +"a layout container styled by the utility classes you pass (e.g. "
        code { +"footer-center bg-base-200 p-4" }
        +"). It hands you the raw kotlinx.html element, so any HTML attribute or "
        +"library extension (e.g. htmx) works natively (ADR-0003)."
    }
    p {
        +"Mosaik provides "
        code { +"mFooterTitle" }
        +" and "
        code { +"mLink" }
        +" to hide DaisyUI's "
        code { +"footer-title" }
        +" and "
        code { +"link link-hover" }
        +" class tokens. These functions work anywhere in kotlinx.html, including "
        +"inside "
        code { +"nav" }
        +" columns within the footer."
    }

    installSection("footer")

    h2 { +"Basic usage" }
    exampleCard(
        code =
            """
            import mosaik.ui.components.mFooter

            mFooter("footer-center bg-base-200 text-base-content p-4") {
                aside {
                    p { +"© 2026 Mosaik UI — built with Kotlin, Ktor and DaisyUI." }
                }
            }
            """.trimIndent(),
    ) {
        mFooter("footer-center bg-base-200 text-base-content p-4 rounded-box") {
            aside {
                p { +"© 2026 Mosaik UI — built with Kotlin, Ktor and DaisyUI." }
            }
        }
    }

    section {
        h2 { +"Centred footer" }
        p {
            +"The "
            code { +"footer-center" }
            +" utility centres a single line of content — the simplest footer."
        }
        exampleCard(
            code =
                """
                mFooter("footer-center bg-base-200 text-base-content p-4") {
                    aside {
                        p { +"© 2026 Mosaik UI — built with Kotlin, Ktor and DaisyUI." }
                    }
                }
                """.trimIndent(),
        ) {
            mFooter("footer-center bg-base-200 text-base-content p-4 rounded-box") {
                aside {
                    p { +"© 2026 Mosaik UI — built with Kotlin, Ktor and DaisyUI." }
                }
            }
        }
    }

    section {
        h2 { +"Link columns" }
        p {
            +"Group links into "
            code { +"nav" }
            +" columns, each headed by "
            code { +"mFooterTitle" }
            +" — DaisyUI's sitemap footer."
        }
        exampleCard(
            code =
                """
                mFooter("bg-base-200 text-base-content p-10") {
                    nav {
                        mFooterTitle { +"Services" }
                        mLink(href = "#") { +"Branding" }
                        mLink(href = "#") { +"Design" }
                    }
                    nav {
                        mFooterTitle { +"Company" }
                        mLink(href = "#") { +"About us" }
                        mLink(href = "#") { +"Contact" }
                    }
                }
                """.trimIndent(),
        ) {
            mFooter("bg-base-200 text-base-content p-10 rounded-box") {
                nav {
                    mFooterTitle { +"Services" }
                    mLink(href = "#") { +"Branding" }
                    mLink(href = "#") { +"Design" }
                }
                nav {
                    mFooterTitle { +"Company" }
                    mLink(href = "#") { +"About us" }
                    mLink(href = "#") { +"Contact" }
                }
            }
        }
    }

    section {
        h2 { +"API" }
        h3 { +"mFooter" }
        apiReference(
            listOf(
                ApiParam(
                    "classes",
                    "String?",
                    "null",
                    "Extra CSS classes appended after the generated footer classes (e.g. footer-center bg-base-200 p-4).",
                ),
                ApiParam(
                    "block",
                    "MFooter.() -> Unit",
                    "{}",
                    "Receiver block on the MFooter context wrapping the kotlinx.html FOOTER element — nest nav columns, an aside, attributes, or library extensions.",
                ),
            ),
        )
        h3 { +"mFooterTitle" }
        apiReference(
            listOf(
                ApiParam(
                    "classes",
                    "String?",
                    "null",
                    "Extra CSS classes appended after footer-title.",
                ),
                ApiParam(
                    "block",
                    "H6.() -> Unit",
                    "{}",
                    "Receiver block on the raw kotlinx.html H6 element.",
                ),
            ),
        )
        h3 { +"mLink" }
        apiReference(
            listOf(
                ApiParam(
                    "href",
                    "String",
                    "required",
                    "The link destination URL.",
                ),
                ApiParam(
                    "classes",
                    "String?",
                    "null",
                    "Extra CSS classes appended after link link-hover.",
                ),
                ApiParam(
                    "block",
                    "A.() -> Unit",
                    "{}",
                    "Receiver block on the raw kotlinx.html A element.",
                ),
            ),
        )
    }
}

/**
 * Footer page, following the five-section component template: title +
 * description, installation, basic usage, a showcase of footer content
 * arrangements (centred copyright, titled link columns) paired with their Kotlin
 * code, and the API reference table. Footer has no variants, sizes, or
 * sub-components — it is the simplest layout container.
 */
fun footerPage(): String = layout(FOOTER) { footerPageContent() }
