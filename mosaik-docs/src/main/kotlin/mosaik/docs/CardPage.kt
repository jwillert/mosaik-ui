package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.ButtonVariant
import mosaik.ui.components.mButton
import mosaik.ui.components.mCard
import mosaik.ui.components.mCardActions
import mosaik.ui.components.mCardBody
import mosaik.ui.components.mCardTitle

/**
 * Card page, following the five-section component template: title +
 * description, installation, basic usage showing the sub-component nesting,
 * a showcase of card compositions (plain, with actions, with image) paired with
 * their Kotlin code, and an API reference covering the card and its
 * sub-components. Card has no variants or sizes — it is a layout container.
 */
fun cardPage(): String =
    layout(CARD) {
        h1 { +"Card" }
        p {
            +"A card groups related content and actions into a single surface — an "
            +"image, a title, body text, and a row of buttons. "
            code { +"mCard" }
            +" wraps DaisyUI's "
            code { +"card" }
            +" classes and, unlike Button or Badge, takes no colour role or size: it "
            +"is a layout container styled by the utility classes you pass (e.g. "
            code { +"bg-base-100 shadow-sm" }
            +"). It hands you the raw kotlinx.html element, so any HTML attribute or "
            +"library extension (e.g. htmx) works natively (ADR-0003)."
        }
        p {
            +"A card composes from sub-components scoped to its receiver: "
            code { +"mCardBody" }
            +" for the padded "
            code { +"card-body" }
            +" container, and inside it "
            code { +"mCardTitle" }
            +" (an "
            code { +"<h2>" }
            +" with "
            code { +"card-title" }
            +") and "
            code { +"mCardActions" }
            +" for a "
            code { +"card-actions" }
            +" button row. An image is a plain "
            code { +"figure" }
            +" — no wrapper needed."
        }

        installSection("card")

        usageSection(
            """
            import mosaik.ui.components.mCard
            import mosaik.ui.components.mCardBody
            import mosaik.ui.components.mCardTitle
            import mosaik.ui.components.mCardActions
            import mosaik.ui.components.mButton
            import mosaik.ui.components.Variant

            mCard("w-96 bg-base-100 shadow-sm") {
                mCardBody {
                    mCardTitle { +"Shoes!" }
                    p { +"If a dog chews shoes whose shoes does he choose?" }
                    mCardActions("justify-end") {
                        mButton(variant = ButtonVariant.Primary) { +"Buy Now" }
                    }
                }
            }
            """.trimIndent(),
        )

        section {
            h2 { +"Basic card" }
            p { +"A card with a title and body text." }
            div("not-prose") {
                mCard("w-96 bg-base-100 shadow-sm") {
                    mCardBody {
                        mCardTitle { +"Card title" }
                        p { +"A card with a title and a short description below it." }
                    }
                }
            }
            codeBlock(
                """
                mCard("w-96 bg-base-100 shadow-sm") {
                    mCardBody {
                        mCardTitle { +"Card title" }
                        p { +"A card with a title and a short description below it." }
                    }
                }
                """.trimIndent(),
            )
        }

        section {
            h2 { +"With actions" }
            p {
                +"A "
                code { +"card-actions" }
                +" row holds buttons or controls, usually right-aligned with "
                code { +"justify-end" }
                +"."
            }
            div("not-prose") {
                mCard("w-96 bg-base-100 shadow-sm") {
                    mCardBody {
                        mCardTitle { +"Buy these shoes" }
                        p { +"If a dog chews shoes whose shoes does he choose?" }
                        mCardActions("justify-end") {
                            mButton(variant = ButtonVariant.Primary) { +"Buy Now" }
                        }
                    }
                }
            }
            codeBlock(
                """
                mCard("w-96 bg-base-100 shadow-sm") {
                    mCardBody {
                        mCardTitle { +"Buy these shoes" }
                        p { +"If a dog chews shoes whose shoes does he choose?" }
                        mCardActions("justify-end") {
                            mButton(variant = ButtonVariant.Primary) { +"Buy Now" }
                        }
                    }
                }
                """.trimIndent(),
            )
        }

        section {
            h2 { +"With image" }
            p {
                +"Place a "
                code { +"figure" }
                +" before the body for DaisyUI's image card layout."
            }
            div("not-prose") {
                mCard("w-96 bg-base-100 shadow-sm") {
                    figure {
                        img(src = "https://placehold.co/384x192", alt = "Placeholder")
                    }
                    mCardBody {
                        mCardTitle { +"Image card" }
                        p { +"An image sits in a figure above the body." }
                    }
                }
            }
            codeBlock(
                """
                mCard("w-96 bg-base-100 shadow-sm") {
                    figure {
                        img(src = "/shoes.jpg", alt = "Shoes")
                    }
                    mCardBody {
                        mCardTitle { +"Image card" }
                        p { +"An image sits in a figure above the body." }
                    }
                }
                """.trimIndent(),
            )
        }

        section {
            h2 { +"Interactive usage" }
            p {
                +"Lazy-load card content — htmx fetches on scroll reveal; Alpine.js watches "
                +"intersection; Datastar merges SSE fragments. "
                +"The route path (e.g. "
                code { +"/card-content" }
                +") is application-specific and must match your Ktor route definition."
            }
            interactivityTabs(
                id = "card-interactive",
                htmxPreview = {
                    mCard("w-96 bg-base-100 shadow-sm") {
                        mCardBody {
                            mCardTitle { +"Lazy Card" }
                            div {
                                attributes["hx-get"] = "/_examples/card/content"
                                attributes["hx-trigger"] = "revealed"
                                +"Loading..."
                            }
                        }
                    }
                },
                alpinePreview = {
                    mCard("w-96 bg-base-100 shadow-sm") {
                        attributes["x-data"] = "{ content: 'Loading...', loaded: false }"
                        attributes["x-intersect"] =
                            "if (!loaded) { fetch('/_examples/card/content').then(r => r.text()).then(t => { content = t; loaded = true; }) }"
                        mCardBody {
                            mCardTitle { +"Lazy Card" }
                            div {
                                attributes["x-text"] = "content"
                            }
                        }
                    }
                },
                datastarPreview = {
                    mCard("w-96 bg-base-100 shadow-sm") {
                        attributes["data-store"] = "{ content: 'Loading...' }"
                        attributes["data-intersects"] = "once"
                        attributes["data-get"] = "/_examples/card/content"
                        mCardBody {
                            mCardTitle { +"Lazy Card" }
                            div {
                                attributes["data-text"] = "\$content"
                            }
                        }
                    }
                },
                htmxCode =
                    """
                    mCard("w-96 bg-base-100 shadow-sm") {
                        mCardBody {
                            mCardTitle { +"Lazy Card" }
                            div {
                                attributes["hx-get"] = "/card-content"
                                attributes["hx-trigger"] = "revealed"
                                +"Loading..."
                            }
                        }
                    }
                    """.trimIndent(),
                alpineCode =
                    """
                    mCard("w-96 bg-base-100 shadow-sm") {
                        attributes["x-data"] = "{ content: 'Loading...', loaded: false }"
                        attributes["x-intersect"] = "if (!loaded) { fetch('/card-content').then(r => r.text()).then(t => { content = t; loaded = true; }) }"
                        mCardBody {
                            mCardTitle { +"Lazy Card" }
                            div {
                                attributes["x-text"] = "content"
                            }
                        }
                    }
                    """.trimIndent(),
                datastarCode =
                    """
                    mCard("w-96 bg-base-100 shadow-sm") {
                        attributes["data-store"] = "{ content: 'Loading...' }"
                        attributes["data-intersects"] = "once"
                        attributes["data-get"] = "/card-content"
                        mCardBody {
                            mCardTitle { +"Lazy Card" }
                            div {
                                attributes["data-text"] = "${'$'}content"
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
                    "Extra CSS classes appended after the generated card classes (e.g. bg-base-100 shadow-sm w-96).",
                ),
                ApiParam(
                    "block",
                    "DIV.() -> Unit",
                    "{}",
                    "Receiver block on the raw kotlinx.html DIV element — nest mCardBody, a figure, attributes, or library extensions.",
                ),
            ),
        )

        section {
            h2 { +"Sub-components" }
            p {
                +"Each sub-component is an extension on the card's "
                code { +"DIV" }
                +" receiver, so it autocompletes inside the card block. All take the "
                +"same optional "
                code { +"classes" }
                +" parameter and live in "
                code { +"Card.kt" }
                +"."
            }
            div("overflow-x-auto") {
                table("table table-zebra") {
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
                            td { code { +"mCardBody" } }
                            td { code { +"DIV" } }
                            td { code { +"card-body" } }
                            td { +"The padded container for the title, text, and actions." }
                        }
                        tr {
                            td { code { +"mCardTitle" } }
                            td { code { +"H2" } }
                            td { code { +"card-title" } }
                            td { +"The card heading, rendered as an h2. Called inside mCardBody." }
                        }
                        tr {
                            td { code { +"mCardActions" } }
                            td { code { +"DIV" } }
                            td { code { +"card-actions" } }
                            td { +"A row for buttons or controls, usually with justify-end. Called inside mCardBody." }
                        }
                    }
                }
            }
        }
    }
