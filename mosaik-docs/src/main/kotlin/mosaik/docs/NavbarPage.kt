package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.ButtonStyle
import mosaik.ui.components.ButtonVariant
import mosaik.ui.components.mButton
import mosaik.ui.components.mButtonLink
import mosaik.ui.components.mNavbar
import mosaik.ui.components.mNavbarCenter
import mosaik.ui.components.mNavbarEnd
import mosaik.ui.components.mNavbarStart
import mosaik.ui.components.mTable

/**
 * Navbar page content block for use in both full-page and partial renders.
 */
fun FlowContent.navbarPageContent() {
    h1 { +"Navbar" }
    p {
        +"A navbar is the bar at the top of a page that holds the brand, navigation "
        +"links, and actions. "
        code { +"mNavbar" }
        +" wraps DaisyUI's "
        code { +"navbar" }
        +" classes and, like Card, takes no colour role or size: it is a layout "
        +"container styled by the utility classes you pass (e.g. "
        code { +"bg-base-100 shadow-sm" }
        +"). It hands you the raw kotlinx.html element, so any HTML attribute or "
        +"library extension (e.g. htmx) works natively (ADR-0003)."
    }
    p {
        +"A navbar composes from three slot sub-components scoped to its receiver, "
        +"mirroring DaisyUI's layout: "
        code { +"mNavbarStart" }
        +" for the leading "
        code { +"navbar-start" }
        +" section (brand or menu), "
        code { +"mNavbarCenter" }
        +" for the centred "
        code { +"navbar-center" }
        +" section (title or links), and "
        code { +"mNavbarEnd" }
        +" for the trailing "
        code { +"navbar-end" }
        +" section (actions). Use only the slots you need."
    }

    installSection("navbar")

    usageSection(
        """
        import mosaik.ui.components.mNavbar
        import mosaik.ui.components.mNavbarStart
        import mosaik.ui.components.mNavbarEnd
        import mosaik.ui.components.mButton
        import mosaik.ui.components.mButtonLink
        import mosaik.ui.components.ButtonStyle
        import mosaik.ui.components.ButtonVariant

        mNavbar("bg-base-100 shadow-sm") {
            mNavbarStart {
                mButtonLink(href = "/", style = ButtonStyle.Ghost, classes = "text-xl") { +"Mosaik" }
            }
            mNavbarEnd {
                mButton(variant = ButtonVariant.Primary) { +"Sign up" }
            }
        }
        """.trimIndent(),
    )

    section {
        h2 { +"Start and end" }
        p {
            +"The most common navbar: a brand in "
            code { +"navbar-start" }
            +" and an action in "
            code { +"navbar-end" }
            +"."
        }
        div("not-prose") {
            mNavbar("bg-base-100 shadow-sm rounded-box") {
                mNavbarStart {
                    mButtonLink(href = "/", style = ButtonStyle.Ghost, classes = "text-xl") { +"Mosaik" }
                }
                mNavbarEnd {
                    mButton(variant = ButtonVariant.Primary) { +"Sign up" }
                }
            }
        }
        codeBlock(
            """
            mNavbar("bg-base-100 shadow-sm") {
                mNavbarStart {
                    mButtonLink(href = "/", style = ButtonStyle.Ghost, classes = "text-xl") { +"Mosaik" }
                }
                mNavbarEnd {
                    mButton(variant = ButtonVariant.Primary) { +"Sign up" }
                }
            }
            """.trimIndent(),
        )
    }

    section {
        h2 { +"Centred title" }
        p {
            +"A single "
            code { +"navbar-center" }
            +" slot centres the content."
        }
        div("not-prose") {
            mNavbar("bg-base-100 shadow-sm rounded-box") {
                mNavbarCenter {
                    mButtonLink(href = "/", style = ButtonStyle.Ghost, classes = "text-xl") { +"Mosaik" }
                }
            }
        }
        codeBlock(
            """
            mNavbar("bg-base-100 shadow-sm") {
                mNavbarCenter {
                    mButtonLink(href = "/", style = ButtonStyle.Ghost, classes = "text-xl") { +"Mosaik" }
                }
            }
            """.trimIndent(),
        )
    }

    section {
        h2 { +"All three slots" }
        p {
            +"Brand, centred links, and a trailing action together — the full "
            +"three-slot layout."
        }
        div("not-prose") {
            mNavbar("bg-base-100 shadow-sm rounded-box") {
                mNavbarStart {
                    mButtonLink(href = "/", style = ButtonStyle.Ghost, classes = "text-xl") { +"Mosaik" }
                }
                mNavbarCenter("hidden lg:flex") {
                    mButtonLink(href = "/docs", style = ButtonStyle.Ghost) { +"Docs" }
                }
                mNavbarEnd {
                    mButton(variant = ButtonVariant.Primary) { +"Sign up" }
                }
            }
        }
        codeBlock(
            """
            mNavbar("bg-base-100 shadow-sm") {
                mNavbarStart {
                    mButtonLink(href = "/", style = ButtonStyle.Ghost, classes = "text-xl") { +"Mosaik" }
                }
                mNavbarCenter("hidden lg:flex") {
                    mButtonLink(href = "/docs", style = ButtonStyle.Ghost) { +"Docs" }
                }
                mNavbarEnd {
                    mButton(variant = ButtonVariant.Primary) { +"Sign up" }
                }
            }
            """.trimIndent(),
        )
    }

    section {
        h2 { +"Interactive usage" }
        p {
            +"Active link from server — htmx boosts navigation with history push; Alpine.js "
            +"binds classes from state; Datastar drives classes via signals."
        }
        interactivityTabs(
            id = "navbar-interactive",
            htmxPreview = {
                mNavbar("bg-base-100 shadow-sm") {
                    attributes["hx-boost"] = "true"
                    mNavbarStart {
                        mButtonLink(href = "#", style = ButtonStyle.Ghost) {
                            attributes["hx-push-url"] = "false"
                            +"Home"
                        }
                        mButtonLink(href = "#", style = ButtonStyle.Ghost) {
                            attributes["hx-push-url"] = "false"
                            +"Docs"
                        }
                    }
                }
            },
            alpinePreview = {
                mNavbar("bg-base-100 shadow-sm") {
                    attributes["x-data"] = "{ active: '/' }"
                    mNavbarStart {
                        mButtonLink(href = "#", style = ButtonStyle.Ghost) {
                            attributes["x-bind:class"] = "active === '/' ? 'btn-active' : ''"
                            attributes["x-on:click.prevent"] = "active = '/'"
                            +"Home"
                        }
                        mButtonLink(href = "#", style = ButtonStyle.Ghost) {
                            attributes["x-bind:class"] = "active === '/docs' ? 'btn-active' : ''"
                            attributes["x-on:click.prevent"] = "active = '/docs'"
                            +"Docs"
                        }
                    }
                }
            },
            datastarPreview = {
                mNavbar("bg-base-100 shadow-sm") {
                    attributes["data-signals"] = "{ active: '/' }"
                    mNavbarStart {
                        mButtonLink(href = "#", style = ButtonStyle.Ghost) {
                            attributes["data-bind-class-btn-active"] = "\$active === '/'"
                            attributes["data-on-click"] = "event.preventDefault(); \$active='/'"
                            +"Home"
                        }
                        mButtonLink(href = "#", style = ButtonStyle.Ghost) {
                            attributes["data-bind-class-btn-active"] = "\$active === '/docs'"
                            attributes["data-on-click"] = "event.preventDefault(); \$active='/docs'"
                            +"Docs"
                        }
                    }
                }
            },
            htmxCode =
                """
                mNavbar("bg-base-100 shadow-sm") {
                    attributes["hx-boost"] = "true"
                    mNavbarStart {
                        mButtonLink(href = "/", style = ButtonStyle.Ghost) {
                            attributes["hx-push-url"] = "true"
                            +"Home"
                        }
                        mButtonLink(href = "/docs", style = ButtonStyle.Ghost) {
                            attributes["hx-push-url"] = "true"
                            +"Docs"
                        }
                    }
                }
                """.trimIndent(),
            alpineCode =
                """
                mNavbar("bg-base-100 shadow-sm") {
                    attributes["x-data"] = "{ active: '/' }"
                    mNavbarStart {
                        mButtonLink(href = "/", style = ButtonStyle.Ghost) {
                            attributes["x-bind:class"] = "active === '/' ? 'btn-active' : ''"
                            attributes["x-on:click"] = "active = '/'"
                            +"Home"
                        }
                        mButtonLink(href = "/docs", style = ButtonStyle.Ghost) {
                            attributes["x-bind:class"] = "active === '/docs' ? 'btn-active' : ''"
                            attributes["x-on:click"] = "active = '/docs'"
                            +"Docs"
                        }
                    }
                }
                """.trimIndent(),
            datastarCode =
                """
                mNavbar("bg-base-100 shadow-sm") {
                    attributes["data-signals"] = "{ active: '/' }"
                    mNavbarStart {
                        mButtonLink(href = "/", style = ButtonStyle.Ghost) {
                            attributes["data-bind-class-btn-active"] = "${'$'}active === '/'"
                            attributes["data-on-click"] = "${'$'}active='/'"
                            +"Home"
                        }
                        mButtonLink(href = "/docs", style = ButtonStyle.Ghost) {
                            attributes["data-bind-class-btn-active"] = "${'$'}active === '/docs'"
                            attributes["data-on-click"] = "${'$'}active='/docs'"
                            +"Docs"
                        }
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
                "Extra CSS classes appended after the generated navbar classes (e.g. bg-base-100 shadow-sm).",
            ),
            ApiParam(
                "block",
                "DIV.() -> Unit",
                "{}",
                "Receiver block on the raw kotlinx.html DIV element — nest the slot sub-components, attributes, or library extensions.",
            ),
        ),
    )

    section {
        h2 { +"Sub-components" }
        p {
            +"Each slot is an extension on the navbar's "
            code { +"DIV" }
            +" receiver, so it autocompletes inside the navbar block. All take the "
            +"same optional "
            code { +"classes" }
            +" parameter and live in "
            code { +"Navbar.kt" }
            +"."
        }
        div("overflow-x-auto") {
            mTable(zebra = true) {
                thead {
                    tr {
                        th { +"Function" }
                        th { +"Element" }
                        th { +"Class" }
                        th { +"Description" }
                    }
                }
                tbody {
                    tr {
                        td { code { +"mNavbarStart" } }
                        td { code { +"DIV" } }
                        td { code { +"navbar-start" } }
                        td { +"The leading section, typically the brand or a dropdown menu." }
                    }
                    tr {
                        td { code { +"mNavbarCenter" } }
                        td { code { +"DIV" } }
                        td { code { +"navbar-center" } }
                        td { +"The centred section, typically the title or primary navigation links." }
                    }
                    tr {
                        td { code { +"mNavbarEnd" } }
                        td { code { +"DIV" } }
                        td { code { +"navbar-end" } }
                        td { +"The trailing section, typically actions such as buttons or an avatar." }
                    }
                }
            }
        }
    }
}

/**
 * Navbar page, following the five-section component template: title +
 * description, installation, basic usage showing the slot pattern, a showcase of
 * slot combinations (start+end, centred title, all three slots) paired with their
 * Kotlin code, and an API reference covering the navbar and its slot
 * sub-components. Navbar has no variants or sizes — it is a layout container.
 */
fun navbarPage(): String = layout(NAVBAR) { navbarPageContent() }
