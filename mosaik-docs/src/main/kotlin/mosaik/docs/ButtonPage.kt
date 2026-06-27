package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.ButtonShape
import mosaik.ui.components.ButtonStyle
import mosaik.ui.components.ButtonVariant
import mosaik.ui.components.ButtonWidth
import mosaik.ui.components.LoadingType
import mosaik.ui.components.Size
import mosaik.ui.components.mButton
import mosaik.ui.components.mLoading

/**
 * Button page rewritten as the first Component Reference Gallery. Leads with visual
 * confidence, teaches the new type-safe Button API, uses Example Cards for static and
 * interactive examples, and avoids raw DaisyUI button modifier classes in normal
 * examples when a Mosaik abstraction exists (issue #59).
 */
fun buttonPage(): String =
    layout(BUTTON) {
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
                mButton(variant = ButtonVariant.Primary) {
                    disabled = true
                    +"Disabled"
                }
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
                                attributes["x-on:click"] =
                                    "loading = true; result = ''; fetch('/_examples/button/submit', {method: 'POST'}).then(r => r.text()).then(t => result = t).finally(() => loading = false)"
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
                        attributes["data-signals"] = "{ loading: false, result: '' }"
                        div("flex items-center gap-2") {
                            mButton(variant = ButtonVariant.Primary) {
                                attributes["data-on-click"] =
                                    "\$loading=true; \$result=''; fetch('/_examples/button/submit', {method: 'POST'}).then(r => r.text()).then(t => \$result = t).finally(() => \$loading = false)"
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
                htmxCode =
                    """
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
                alpineCode =
                    """
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
                datastarCode =
                    """
                    import mosaik.ui.components.mLoading
                    import mosaik.ui.components.LoadingType

                    div {
                        attributes["data-signals"] = "{ loading: false }"
                        mButton(variant = ButtonVariant.Primary) {
                            attributes["data-on-click"] =
                                "${'$'}loading=true; fetch('/submit', { method: 'POST' }).finally(() => ${'$'}loading = false)"
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
