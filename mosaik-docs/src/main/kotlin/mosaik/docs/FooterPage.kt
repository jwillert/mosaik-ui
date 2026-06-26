package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.mFooter

/**
 * Footer page, following the five-section component template: title +
 * description, installation, basic usage, a showcase of footer content
 * arrangements (centred copyright, titled link columns) paired with their Kotlin
 * code, and the API reference table. Footer has no variants, sizes, or
 * sub-components — it is the simplest layout container.
 */
fun footerPage(): String = layout(FOOTER) {
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
        +"Footer has no sub-components: its content is plain kotlinx.html. DaisyUI's "
        code { +"footer-title" }
        +" is just a utility class applied to a heading inside a "
        code { +"nav" }
        +" column — there is no wrapper to learn."
    }

    installSection("footer")

    usageSection(
        """
        import mosaik.ui.components.mFooter

        mFooter("footer-center bg-base-200 text-base-content p-4") {
            aside {
                p { +"© 2026 Mosaik UI — built with Kotlin, Ktor and DaisyUI." }
            }
        }
        """.trimIndent(),
    )

    section {
        h2 { +"Centred footer" }
        p {
            +"The "
            code { +"footer-center" }
            +" utility centres a single line of content — the simplest footer."
        }
        div("not-prose") {
            mFooter("footer-center bg-base-200 text-base-content p-4 rounded-box") {
                aside {
                    p { +"© 2026 Mosaik UI — built with Kotlin, Ktor and DaisyUI." }
                }
            }
        }
        codeBlock(
            """
            mFooter("footer-center bg-base-200 text-base-content p-4") {
                aside {
                    p { +"© 2026 Mosaik UI — built with Kotlin, Ktor and DaisyUI." }
                }
            }
            """.trimIndent(),
        )
    }

    section {
        h2 { +"Link columns" }
        p {
            +"Group links into "
            code { +"nav" }
            +" columns, each headed by a "
            code { +"footer-title" }
            +" — DaisyUI's sitemap footer."
        }
        div("not-prose") {
            mFooter("bg-base-200 text-base-content p-10 rounded-box") {
                nav {
                    h6("footer-title") { +"Services" }
                    a(href = "#", classes = "link link-hover") { +"Branding" }
                    a(href = "#", classes = "link link-hover") { +"Design" }
                }
                nav {
                    h6("footer-title") { +"Company" }
                    a(href = "#", classes = "link link-hover") { +"About us" }
                    a(href = "#", classes = "link link-hover") { +"Contact" }
                }
            }
        }
        codeBlock(
            """
            mFooter("bg-base-200 text-base-content p-10") {
                nav {
                    h6("footer-title") { +"Services" }
                    a(href = "#", classes = "link link-hover") { +"Branding" }
                    a(href = "#", classes = "link link-hover") { +"Design" }
                }
                nav {
                    h6("footer-title") { +"Company" }
                    a(href = "#", classes = "link link-hover") { +"About us" }
                    a(href = "#", classes = "link link-hover") { +"Contact" }
                }
            }
            """.trimIndent(),
        )
    }

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
                "FOOTER.() -> Unit",
                "{}",
                "Receiver block on the raw kotlinx.html FOOTER element — nest nav columns, an aside, attributes, or library extensions.",
            ),
        ),
    )
}
