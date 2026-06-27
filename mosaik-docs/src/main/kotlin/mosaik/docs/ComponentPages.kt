package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.AlertVariant
import mosaik.ui.components.BadgeVariant
import mosaik.ui.components.ButtonShape
import mosaik.ui.components.ButtonStyle
import mosaik.ui.components.ButtonVariant
import mosaik.ui.components.ButtonWidth
import mosaik.ui.components.LoadingType
import mosaik.ui.components.Size
import mosaik.ui.components.Variant
import mosaik.ui.components.mAlert
import mosaik.ui.components.mBadge
import mosaik.ui.components.mButton
import mosaik.ui.components.mCard
import mosaik.ui.components.mCardActions
import mosaik.ui.components.mCardBody
import mosaik.ui.components.mCardTitle
import mosaik.ui.components.mFooter
import mosaik.ui.components.mLoading
import mosaik.ui.components.mNavbar
import mosaik.ui.components.mNavbarCenter
import mosaik.ui.components.mNavbarEnd
import mosaik.ui.components.mNavbarStart

// ---------------------------------------------------------------------------
// Page template helpers — every component page is built from these sections so
// the layout stays consistent (PRD #10, issue #11). Code examples are static
// strings here; there is no build-time extraction from component sources.
// ---------------------------------------------------------------------------

/** A row in an [apiReference] table: one component parameter. */
data class ApiParam(
    val name: String,
    val type: String,
    val default: String,
    val description: String,
)

/** The `./gradlew mosaikAdd` install command for [component]. */
private fun FlowContent.installSection(component: String) {
    h2 { +"Installation" }
    codeBlock("./gradlew mosaikAdd --component=$component")
}

/** A minimal Kotlin usage snippet under a "Basic usage" heading. */
private fun FlowContent.usageSection(code: String) {
    h2 { +"Basic usage" }
    codeBlock(code)
}

/** A fenced, monospaced code block. Text is escaped by kotlinx.html. */
private fun FlowContent.codeBlock(code: String) {
    pre("bg-base-200 rounded-box p-4 overflow-x-auto") {
        code { +code }
    }
}

/** The parameter reference table closing every component page. */
private fun FlowContent.apiReference(params: List<ApiParam>) {
    h2 { +"API reference" }
    div("overflow-x-auto") {
        table("table table-zebra") {
            thead {
                tr {
                    th { +"Parameter" }
                    th { +"Type" }
                    th { +"Default" }
                    th { +"Description" }
                }
            }
            tbody {
                params.forEach { p ->
                    tr {
                        td { code { +p.name } }
                        td { code { +p.type } }
                        td { code { +p.default } }
                        td { +p.description }
                    }
                }
            }
        }
    }
}

/** Landing page: a short intro and where to look next. */
fun landingPage(): String = layout(HOME) {
    h1 { +"Mosaik UI" }
    p { +"ShadCN-style components for Kotlin, Ktor and Gradle. Pick a component from the sidebar to see every variant rendered." }
    p {
        +"This app dogfoods "
        span("font-mono") { +"mosaik-components" }
        +" directly: if a page renders correctly, the component works."
    }
    p { +"Use the theme switcher in the sidebar to preview components in any DaisyUI theme." }
}

/**
 * Button page rewritten as the first Component Reference Gallery. Leads with visual
 * confidence, teaches the new type-safe Button API, uses Example Cards for static and
 * interactive examples, and avoids raw DaisyUI button modifier classes in normal
 * examples when a Mosaik abstraction exists (issue #59).
 */
fun buttonPage(): String = layout(BUTTON) {
    h1 { +"Button" }
    p {
        +"A button triggers an action or event — submitting a form, opening a dialog, "
        +"or navigating. Mosaik's Button component provides a type-safe API with "
        +"parameters for variant, style, shape, width, and size, eliminating the need "
        +"to remember raw DaisyUI class names."
    }

    // Hero preview
    section {
        div("not-prose mb-6") {
            div("flex flex-wrap items-center gap-2") {
                mButton(variant = ButtonVariant.Neutral) { +"Neutral" }
                mButton(variant = ButtonVariant.Primary) { +"Primary" }
                mButton(variant = ButtonVariant.Secondary) { +"Secondary" }
                mButton(variant = ButtonVariant.Accent) { +"Accent" }
            }
        }
    }

    installSection("button")

    usageSection(
        """
        import mosaik.ui.components.mButton
        import mosaik.ui.components.ButtonVariant
        import mosaik.ui.components.Size

        mButton(variant = ButtonVariant.Primary, size = Size.Md) {
            +"Save"
        }
        """.trimIndent(),
    )

    section {
        h2 { +"Color variants" }
        p {
            +"Button supports eight color variants. Each maps to a DaisyUI color role."
        }
        div("flex flex-wrap gap-2 not-prose mb-4") {
            ButtonVariant.entries.forEach { v ->
                mButton(variant = v) { +v.name }
            }
        }
        codeBlock(
            """
            mButton(variant = ButtonVariant.Neutral) { +"Neutral" }
            mButton(variant = ButtonVariant.Primary) { +"Primary" }
            mButton(variant = ButtonVariant.Secondary) { +"Secondary" }
            mButton(variant = ButtonVariant.Accent) { +"Accent" }
            mButton(variant = ButtonVariant.Info) { +"Info" }
            mButton(variant = ButtonVariant.Success) { +"Success" }
            mButton(variant = ButtonVariant.Warning) { +"Warning" }
            mButton(variant = ButtonVariant.Error) { +"Error" }
            """.trimIndent(),
        )
    }

    section {
        h2 { +"Sizes" }
        p {
            +"Five size steps: Xs, Sm, Md, Lg, Xl. "
            code { +"Size.Md" }
            +" is the default baseline."
        }
        div("flex flex-wrap items-center gap-2 not-prose mb-4") {
            Size.entries.forEach { s ->
                mButton(variant = ButtonVariant.Primary, size = s) { +s.name }
            }
        }
        codeBlock(
            """
            mButton(variant = ButtonVariant.Primary, size = Size.Xs) { +"Xs" }
            mButton(variant = ButtonVariant.Primary, size = Size.Sm) { +"Sm" }
            mButton(variant = ButtonVariant.Primary, size = Size.Md) { +"Md" }
            mButton(variant = ButtonVariant.Primary, size = Size.Lg) { +"Lg" }
            mButton(variant = ButtonVariant.Primary, size = Size.Xl) { +"Xl" }
            """.trimIndent(),
        )
    }

    section {
        h2 { +"Styles" }
        p {
            +"Button styles are orthogonal to color variants. The "
            code { +"style" }
            +" parameter accepts "
            code { +"Outline" }
            +", "
            code { +"Ghost" }
            +", or "
            code { +"Link" }
            +"."
        }
        div("flex flex-wrap gap-2 not-prose mb-4") {
            mButton(variant = ButtonVariant.Primary, style = ButtonStyle.Outline) { +"Outline" }
            mButton(variant = ButtonVariant.Primary, style = ButtonStyle.Ghost) { +"Ghost" }
            mButton(variant = ButtonVariant.Primary, style = ButtonStyle.Link) { +"Link" }
        }
        codeBlock(
            """
            import mosaik.ui.components.ButtonStyle

            mButton(variant = ButtonVariant.Primary, style = ButtonStyle.Outline) { +"Outline" }
            mButton(variant = ButtonVariant.Primary, style = ButtonStyle.Ghost) { +"Ghost" }
            mButton(variant = ButtonVariant.Primary, style = ButtonStyle.Link) { +"Link" }
            """.trimIndent(),
        )
    }

    section {
        h2 { +"Shapes" }
        p {
            +"The "
            code { +"shape" }
            +" parameter controls button geometry: "
            code { +"Circle" }
            +" or "
            code { +"Square" }
            +"."
        }
        div("flex flex-wrap gap-2 not-prose mb-4") {
            mButton(variant = ButtonVariant.Primary, shape = ButtonShape.Circle) { +"C" }
            mButton(variant = ButtonVariant.Primary, shape = ButtonShape.Square) { +"S" }
        }
        codeBlock(
            """
            import mosaik.ui.components.ButtonShape

            mButton(variant = ButtonVariant.Primary, shape = ButtonShape.Circle) { +"C" }
            mButton(variant = ButtonVariant.Primary, shape = ButtonShape.Square) { +"S" }
            """.trimIndent(),
        )
    }

    section {
        h2 { +"Widths" }
        p {
            +"The "
            code { +"width" }
            +" parameter accepts "
            code { +"Wide" }
            +" or "
            code { +"Block" }
            +"."
        }
        div("flex flex-col gap-2 not-prose mb-4") {
            mButton(variant = ButtonVariant.Primary, width = ButtonWidth.Wide) { +"Wide" }
            mButton(variant = ButtonVariant.Primary, width = ButtonWidth.Block) { +"Block" }
        }
        codeBlock(
            """
            import mosaik.ui.components.ButtonWidth

            mButton(variant = ButtonVariant.Primary, width = ButtonWidth.Wide) { +"Wide" }
            mButton(variant = ButtonVariant.Primary, width = ButtonWidth.Block) { +"Block" }
            """.trimIndent(),
        )
    }

    section {
        h2 { +"Disabled state" }
        p {
            +"Use the "
            code { +"disabled" }
            +" HTML attribute to mark a button as non-interactive."
        }
        div("flex flex-wrap gap-2 not-prose mb-4") {
            mButton(variant = ButtonVariant.Primary) { disabled = true; +"Disabled" }
        }
        codeBlock(
            """
            mButton(variant = ButtonVariant.Primary) {
                disabled = true
                +"Disabled"
            }
            """.trimIndent(),
        )
    }

    section {
        h2 { +"Icon and content composition" }
        p {
            +"Buttons compose with icons and text. Add icons as inline content."
        }
        div("flex flex-wrap gap-2 not-prose mb-4") {
            mButton(variant = ButtonVariant.Primary) {
                span { +"→" }
                span("ml-2") { +"Next" }
            }
            mButton(variant = ButtonVariant.Secondary) {
                span("mr-2") { +"←" }
                span { +"Back" }
            }
        }
        codeBlock(
            """
            mButton(variant = ButtonVariant.Primary) {
                span { +"→" }
                span("ml-2") { +"Next" }
            }
            mButton(variant = ButtonVariant.Secondary) {
                span("mr-2") { +"←" }
                span { +"Back" }
            }
            """.trimIndent(),
        )
    }

    section {
        h2 { +"Loading content" }
        p {
            +"Use the "
            code { +"mLoading" }
            +" component for loading states. It's composable content, not a button modifier."
        }
        div("flex flex-wrap gap-2 not-prose mb-4") {
            mButton(variant = ButtonVariant.Primary) {
                mLoading(LoadingType.Spinner, Size.Sm, "mr-2")
                +"Processing..."
            }
            mButton(variant = ButtonVariant.Secondary) {
                mLoading(LoadingType.Dots, Size.Sm, "mr-2")
                +"Loading..."
            }
        }
        codeBlock(
            """
            import mosaik.ui.components.mLoading
            import mosaik.ui.components.LoadingType

            mButton(variant = ButtonVariant.Primary) {
                mLoading(LoadingType.Spinner, Size.Sm, "mr-2")
                +"Processing..."
            }
            mButton(variant = ButtonVariant.Secondary) {
                mLoading(LoadingType.Dots, Size.Sm, "mr-2")
                +"Loading..."
            }
            """.trimIndent(),
        )
    }

    section {
        h2 { +"Interactive usage" }
        p {
            +"Form submit with loading state. Each library (htmx, Alpine.js, Datastar) "
            +"wires client-side behavior differently onto the same Button API."
        }
        interactivityTabs(
            id = "button-interactive",
            htmxPreview = {
                div("flex items-center gap-2") {
                    mButton(variant = ButtonVariant.Primary) {
                        attributes["hx-post"] = "/_examples/button/submit"
                        attributes["hx-indicator"] = "#button-spinner-htmx"
                        attributes["hx-target"] = "#button-result-htmx"
                        +"Submit"
                    }
                    mLoading(LoadingType.Spinner, classes = "htmx-indicator") {
                        id = "button-spinner-htmx"
                        attributes["style"] = "display:none;"
                    }
                }
                div("mt-2 text-sm") {
                    id = "button-result-htmx"
                    attributes["role"] = "region"
                    attributes["aria-live"] = "polite"
                }
            },
            alpinePreview = {
                div {
                    attributes["x-data"] = "{ loading: false, result: '' }"
                    div("flex items-center gap-2") {
                        mButton(variant = ButtonVariant.Primary) {
                            attributes["x-on:click"] = "loading = true; result = ''; fetch('/_examples/button/submit', {method: 'POST'}).then(r => r.text()).then(t => result = t).finally(() => loading = false)"
                            attributes["x-bind:disabled"] = "loading"
                            +"Submit"
                        }
                        mLoading(LoadingType.Spinner) {
                            attributes["x-show"] = "loading"
                        }
                    }
                    div("mt-2 text-sm") {
                        attributes["x-show"] = "result"
                        attributes["x-text"] = "result"
                        attributes["role"] = "region"
                        attributes["aria-live"] = "polite"
                    }
                }
            },
            datastarPreview = {
                div {
                    attributes["data-store"] = "{ loading: false, result: '' }"
                    div("flex items-center gap-2") {
                        mButton(variant = ButtonVariant.Primary) {
                            attributes["data-on-click"] = "\$loading=true; \$result=''; fetch('/_examples/button/submit', {method: 'POST'}).then(r => r.text()).then(t => \$result = t).finally(() => \$loading = false)"
                            attributes["data-bind-disabled"] = "\$loading"
                            +"Submit"
                        }
                        mLoading(LoadingType.Spinner) {
                            attributes["data-show"] = "\$loading"
                        }
                    }
                    div("mt-2 text-sm") {
                        attributes["data-show"] = "\$result"
                        attributes["data-text"] = "\$result"
                        attributes["role"] = "region"
                        attributes["aria-live"] = "polite"
                    }
                }
            },
            htmxCode = """
                import mosaik.ui.components.mLoading
                import mosaik.ui.components.LoadingType

                mButton(variant = ButtonVariant.Primary) {
                    attributes["hx-post"] = "/submit"
                    attributes["hx-indicator"] = "#spinner"
                    +"Submit"
                }
                mLoading(LoadingType.Spinner, classes = "htmx-indicator") {
                    id = "spinner"
                }
            """.trimIndent(),
            alpineCode = """
                import mosaik.ui.components.mLoading
                import mosaik.ui.components.LoadingType

                div {
                    attributes["x-data"] = "{ loading: false }"
                    mButton(variant = ButtonVariant.Primary) {
                        attributes["x-on:click"] = "loading = true; fetch('/submit', {method: 'POST'}).finally(() => loading = false)"
                        attributes["x-bind:disabled"] = "loading"
                        +"Submit"
                    }
                    mLoading(LoadingType.Spinner) {
                        attributes["x-show"] = "loading"
                    }
                }
            """.trimIndent(),
            datastarCode = """
                import mosaik.ui.components.mLoading
                import mosaik.ui.components.LoadingType

                div {
                    attributes["data-signals"] = "{ loading: false }"
                    mButton(variant = ButtonVariant.Primary) {
                        attributes["data-on-click"] = "${'$'}loading=true"
                        attributes["data-post"] = "/submit"
                        attributes["data-bind-disabled"] = "${'$'}loading"
                        +"Submit"
                    }
                    mLoading(LoadingType.Spinner) {
                        attributes["data-show"] = "${'$'}loading"
                    }
                }
            """.trimIndent(),
        )
    }

    apiReference(
        listOf(
            ApiParam(
                "variant",
                "ButtonVariant",
                "ButtonVariant.Neutral",
                "DaisyUI colour role: Neutral, Primary, Secondary, Accent, Info, Success, Warning, Error.",
            ),
            ApiParam(
                "style",
                "ButtonStyle?",
                "null",
                "Style modifier: Outline, Ghost, Link. Orthogonal to variant.",
            ),
            ApiParam(
                "shape",
                "ButtonShape?",
                "null",
                "Shape modifier: Circle, Square.",
            ),
            ApiParam(
                "width",
                "ButtonWidth?",
                "null",
                "Width modifier: Wide, Block.",
            ),
            ApiParam(
                "size",
                "Size",
                "Size.Md",
                "Size step: Xs, Sm, Md, Lg, Xl. Md is the baseline and adds no class.",
            ),
            ApiParam(
                "classes",
                "String?",
                "null",
                "Extra CSS classes appended after the generated btn classes.",
            ),
            ApiParam(
                "block",
                "BUTTON.() -> Unit",
                "{}",
                "Receiver block on the raw kotlinx.html BUTTON element — set text, attributes, or library extensions.",
            ),
        ),
    )
}

/**
 * Card page, following the five-section component template: title +
 * description, installation, basic usage showing the sub-component nesting,
 * a showcase of card compositions (plain, with actions, with image) paired with
 * their Kotlin code, and an API reference covering the card and its
 * sub-components. Card has no variants or sizes — it is a layout container.
 */
fun cardPage(): String = layout(CARD) {
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
                    attributes["x-intersect"] = "if (!loaded) { fetch('/_examples/card/content').then(r => r.text()).then(t => { content = t; loaded = true; }) }"
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
            htmxCode = """
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
            alpineCode = """
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
            datastarCode = """
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

/**
 * Navbar page, following the five-section component template: title +
 * description, installation, basic usage showing the slot pattern, a showcase of
 * slot combinations (start+end, centred title, all three slots) paired with their
 * Kotlin code, and an API reference covering the navbar and its slot
 * sub-components. Navbar has no variants or sizes — it is a layout container.
 */
fun navbarPage(): String = layout(NAVBAR) {
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
        import mosaik.ui.components.Variant

        mNavbar("bg-base-100 shadow-sm") {
            mNavbarStart {
                a(classes = "btn btn-ghost text-xl") { +"Mosaik" }
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
                    a(classes = "btn btn-ghost text-xl") { +"Mosaik" }
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
                    a(classes = "btn btn-ghost text-xl") { +"Mosaik" }
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
                    a(classes = "btn btn-ghost text-xl") { +"Mosaik" }
                }
            }
        }
        codeBlock(
            """
            mNavbar("bg-base-100 shadow-sm") {
                mNavbarCenter {
                    a(classes = "btn btn-ghost text-xl") { +"Mosaik" }
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
                    a(classes = "btn btn-ghost text-xl") { +"Mosaik" }
                }
                mNavbarCenter("hidden lg:flex") {
                    a(classes = "btn btn-ghost") { +"Docs" }
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
                    a(classes = "btn btn-ghost text-xl") { +"Mosaik" }
                }
                mNavbarCenter("hidden lg:flex") {
                    a(classes = "btn btn-ghost") { +"Docs" }
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
                        a(href = "#", classes = "btn btn-ghost") {
                            attributes["hx-push-url"] = "false"
                            +"Home"
                        }
                        a(href = "#", classes = "btn btn-ghost") {
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
                        a(href = "#", classes = "btn btn-ghost") {
                            attributes["x-bind:class"] = "active === '/' ? 'btn-active' : ''"
                            attributes["x-on:click.prevent"] = "active = '/'"
                            +"Home"
                        }
                        a(href = "#", classes = "btn btn-ghost") {
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
                        a(href = "#", classes = "btn btn-ghost") {
                            attributes["data-bind-class-btn-active"] = "\$active === '/'"
                            attributes["data-on-click"] = "event.preventDefault(); \$active='/'"
                            +"Home"
                        }
                        a(href = "#", classes = "btn btn-ghost") {
                            attributes["data-bind-class-btn-active"] = "\$active === '/docs'"
                            attributes["data-on-click"] = "event.preventDefault(); \$active='/docs'"
                            +"Docs"
                        }
                    }
                }
            },
            htmxCode = """
                mNavbar("bg-base-100 shadow-sm") {
                    attributes["hx-boost"] = "true"
                    mNavbarStart {
                        a(href = "/", classes = "btn btn-ghost") {
                            attributes["hx-push-url"] = "true"
                            +"Home"
                        }
                        a(href = "/docs", classes = "btn btn-ghost") {
                            attributes["hx-push-url"] = "true"
                            +"Docs"
                        }
                    }
                }
            """.trimIndent(),
            alpineCode = """
                mNavbar("bg-base-100 shadow-sm") {
                    attributes["x-data"] = "{ active: '/' }"
                    mNavbarStart {
                        a(href = "/", classes = "btn btn-ghost") {
                            attributes["x-bind:class"] = "active === '/' ? 'btn-active' : ''"
                            attributes["x-on:click"] = "active = '/'"
                            +"Home"
                        }
                        a(href = "/docs", classes = "btn btn-ghost") {
                            attributes["x-bind:class"] = "active === '/docs' ? 'btn-active' : ''"
                            attributes["x-on:click"] = "active = '/docs'"
                            +"Docs"
                        }
                    }
                }
            """.trimIndent(),
            datastarCode = """
                mNavbar("bg-base-100 shadow-sm") {
                    attributes["data-signals"] = "{ active: '/' }"
                    mNavbarStart {
                        a(href = "/", classes = "btn btn-ghost") {
                            attributes["data-bind-class-btn-active"] = "${'$'}active === '/'"
                            attributes["data-on-click"] = "${'$'}active='/'"
                            +"Home"
                        }
                        a(href = "/docs", classes = "btn btn-ghost") {
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

/**
 * Badge page, following the five-section component template: title +
 * description, installation, basic usage, a variants/sizes showcase paired with
 * its Kotlin code, and the API reference table.
 */
fun badgePage(): String = layout(BADGE) {
    h1 { +"Badge" }
    p {
        +"A badge is a small inline label for status, counts, or categories — "
        +"sitting beside text, in a list, or on the corner of another element. "
        code { +"mBadge" }
        +" wraps DaisyUI's "
        code { +"badge" }
        +" classes and takes its colour role and size as parameters, handing you "
        +"the raw kotlinx.html element so any HTML attribute or library extension "
        +"(e.g. htmx) works natively (ADR-0003)."
    }
    p {
        +"Unlike Button, Badge declares its own "
        code { +"BadgeVariant" }
        +" enum (ADR-0004): DaisyUI's badge palette adds "
        code { +"info" }
        +", "
        code { +"neutral" }
        +", and "
        code { +"outline" }
        +", so the shared "
        code { +"Variant" }
        +" wouldn't fit."
    }

    installSection("badge")

    usageSection(
        """
        import mosaik.ui.components.mBadge
        import mosaik.ui.components.BadgeVariant
        import mosaik.ui.components.Size

        mBadge(BadgeVariant.Success, Size.Sm) {
            +"Active"
        }
        """.trimIndent(),
    )

    section {
        h2 { +"Variants" }
        p {
            +"Every "
            code { +"BadgeVariant" }
            +" maps to a DaisyUI badge colour role."
        }
        div("flex flex-wrap gap-2 not-prose") {
            BadgeVariant.entries.forEach { v ->
                mBadge(variant = v) { +v.name }
            }
        }
        codeBlock(
            """
            BadgeVariant.entries.forEach { v ->
                mBadge(variant = v) { +v.name }
            }
            """.trimIndent(),
        )
    }

    section {
        h2 { +"Sizes" }
        p {
            +"Five "
            code { +"Size" }
            +" steps; "
            code { +"Size.Md" }
            +" is the unstyled baseline and renders no size class."
        }
        div("flex flex-wrap items-center gap-2 not-prose") {
            Size.entries.forEach { s ->
                mBadge(variant = BadgeVariant.Primary, size = s) { +s.name }
            }
        }
        codeBlock(
            """
            Size.entries.forEach { s ->
                mBadge(variant = BadgeVariant.Primary, size = s) { +s.name }
            }
            """.trimIndent(),
        )
    }

    apiReference(
        listOf(
            ApiParam(
                "variant",
                "BadgeVariant",
                "BadgeVariant.Primary",
                "DaisyUI colour role: Primary, Secondary, Accent, Ghost, Info, Success, Warning, Error, Neutral, Outline.",
            ),
            ApiParam(
                "size",
                "Size",
                "Size.Md",
                "Size step: Xs, Sm, Md, Lg, Xl. Md is the baseline and adds no class.",
            ),
            ApiParam(
                "classes",
                "String?",
                "null",
                "Extra CSS classes appended after the generated badge classes.",
            ),
            ApiParam(
                "block",
                "SPAN.() -> Unit",
                "{}",
                "Receiver block on the raw kotlinx.html SPAN element — set text, attributes, or library extensions.",
            ),
        ),
    )
}

/**
 * Alert page, following the five-section component template: title +
 * description, installation, basic usage, a variants showcase paired with its
 * Kotlin code, and the API reference table. Alert has no size showcase —
 * DaisyUI alerts have no size modifiers.
 */
fun alertPage(): String = layout(ALERT) {
    h1 { +"Alert" }
    p {
        +"An alert conveys a feedback message — informational, success, warning, "
        +"or error — drawing attention without interrupting the flow. "
        code { +"mAlert" }
        +" wraps DaisyUI's "
        code { +"alert" }
        +" classes and takes its colour role as a parameter, handing you the raw "
        +"kotlinx.html element so any HTML attribute or library extension "
        +"(e.g. htmx) works natively (ADR-0003)."
    }
    p {
        +"Like Badge, Alert declares its own "
        code { +"AlertVariant" }
        +" enum (ADR-0004), scoped to the four roles DaisyUI alerts render: "
        code { +"info" }
        +", "
        code { +"success" }
        +", "
        code { +"warning" }
        +", and "
        code { +"error" }
        +". It carries "
        code { +"role=\"alert\"" }
        +" by default for assistive tech, and takes no "
        code { +"Size" }
        +" — alerts have no size modifiers."
    }

    installSection("alert")

    usageSection(
        """
        import mosaik.ui.components.mAlert
        import mosaik.ui.components.AlertVariant

        mAlert(AlertVariant.Success) {
            +"Your changes have been saved."
        }
        """.trimIndent(),
    )

    section {
        h2 { +"Variants" }
        p {
            +"Every "
            code { +"AlertVariant" }
            +" maps to a DaisyUI alert colour role."
        }
        div("flex flex-col gap-2 not-prose") {
            AlertVariant.entries.forEach { v ->
                mAlert(variant = v) { +v.name }
            }
        }
        codeBlock(
            """
            AlertVariant.entries.forEach { v ->
                mAlert(variant = v) { +v.name }
            }
            """.trimIndent(),
        )
    }

    section {
        h2 { +"Interactive usage" }
        p {
            +"Dismissible alert — htmx removes the element after animation; Alpine.js "
            +"toggles visibility with x-show; Datastar toggles via signal."
        }
        interactivityTabs(
            id = "alert-interactive",
            htmxPreview = {
                mAlert(AlertVariant.Success) {
                    +"Your changes have been saved."
                    button(classes = "btn btn-sm btn-ghost") {
                        attributes["hx-on:click"] = "this.closest('.alert').remove()"
                        +"Dismiss"
                    }
                }
            },
            alpinePreview = {
                div {
                    attributes["x-data"] = "{ show: true }"
                    mAlert(AlertVariant.Success) {
                        attributes["x-show"] = "show"
                        +"Your changes have been saved."
                        button(classes = "btn btn-sm btn-ghost") {
                            attributes["x-on:click"] = "show = false"
                            +"Dismiss"
                        }
                    }
                }
            },
            datastarPreview = {
                div {
                    attributes["data-signals"] = "{ show: true }"
                    mAlert(AlertVariant.Success) {
                        attributes["data-show"] = "\$show"
                        +"Your changes have been saved."
                        button(classes = "btn btn-sm btn-ghost") {
                            attributes["data-on-click"] = "\$show=false"
                            +"Dismiss"
                        }
                    }
                }
            },
            htmxCode = """
                mAlert(AlertVariant.Success) {
                    +"Your changes have been saved."
                    button(classes = "btn btn-sm btn-ghost") {
                        attributes["hx-on:click"] = "this.closest('.alert').remove()"
                        +"Dismiss"
                    }
                }
            """.trimIndent(),
            alpineCode = """
                div {
                    attributes["x-data"] = "{ show: true }"
                    mAlert(AlertVariant.Success) {
                        attributes["x-show"] = "show"
                        +"Your changes have been saved."
                        button(classes = "btn btn-sm btn-ghost") {
                            attributes["x-on:click"] = "show = false"
                            +"Dismiss"
                        }
                    }
                }
            """.trimIndent(),
            datastarCode = """
                div {
                    attributes["data-signals"] = "{ show: true }"
                    mAlert(AlertVariant.Success) {
                        attributes["data-show"] = "${'$'}show"
                        +"Your changes have been saved."
                        button(classes = "btn btn-sm btn-ghost") {
                            attributes["data-on-click"] = "${'$'}show=false"
                            +"Dismiss"
                        }
                    }
                }
            """.trimIndent(),
        )
    }

    apiReference(
        listOf(
            ApiParam(
                "variant",
                "AlertVariant",
                "AlertVariant.Info",
                "DaisyUI colour role: Info, Success, Warning, Error.",
            ),
            ApiParam(
                "classes",
                "String?",
                "null",
                "Extra CSS classes appended after the generated alert classes.",
            ),
            ApiParam(
                "block",
                "DIV.() -> Unit",
                "{}",
                "Receiver block on the raw kotlinx.html DIV element — set text, attributes, or library extensions. Runs after role=\"alert\" is set, so it can override the role.",
            ),
        ),
    )
}
