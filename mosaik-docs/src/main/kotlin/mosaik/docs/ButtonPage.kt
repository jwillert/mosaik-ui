package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.ButtonVariant
import mosaik.ui.components.Size
import mosaik.ui.components.mButton

/**
 * Button page, following the five-section component template: title +
 * description, installation, basic usage, a variants/sizes showcase paired with
 * its Kotlin code, and the API reference table.
 */
fun buttonPage(): String =
    layout(BUTTON) {
        h1 { +"Button" }
        p {
            +"A button triggers an action or event — submitting a form, opening a dialog, "
            +"or navigating. "
            code { +"mButton" }
            +" is a thin wrapper over DaisyUI's "
            code { +"btn" }
            +" classes that takes its colour role and size as parameters and hands you "
            +"the raw kotlinx.html element, so any HTML attribute or library extension "
            +"(e.g. htmx) works natively (ADR-0003)."
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
            h2 { +"Variants" }
            p {
                +"Every "
                code { +"ButtonVariant" }
                +" maps to a DaisyUI colour role."
            }
            div("flex flex-wrap gap-2 not-prose") {
                ButtonVariant.entries.forEach { v ->
                    mButton(variant = v) { +v.name }
                }
            }
            codeBlock(
                """
                ButtonVariant.entries.forEach { v ->
                    mButton(variant = v) { +v.name }
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
                    mButton(variant = ButtonVariant.Primary, size = s) { +s.name }
                }
            }
            codeBlock(
                """
                Size.entries.forEach { s ->
                    mButton(variant = ButtonVariant.Primary, size = s) { +s.name }
                }
                """.trimIndent(),
            )
        }

        section {
            h2 { +"Disabled" }
            div("flex flex-wrap gap-2 not-prose") {
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
            h2 { +"Interactive usage" }
            p {
                +"Form submit with loading state — htmx posts on click and shows a spinner; "
                +"Alpine.js tracks a loading boolean; Datastar sends a signal and listens for SSE. "
                +"The route path (e.g. "
                code { +"/submit" }
                +") is application-specific and must match your Ktor route definition."
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
                        span("loading loading-spinner htmx-indicator") {
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
                            span("loading loading-spinner") {
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
                                attributes["data-on-click"] =
                                    "\$loading=true; \$result=''; fetch('/_examples/button/submit', {method: 'POST'}).then(r => r.text()).then(t => \$result = t).finally(() => \$loading = false)"
                                attributes["data-bind-disabled"] = "\$loading"
                                +"Submit"
                            }
                            span("loading loading-spinner") {
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
                    mButton(variant = ButtonVariant.Primary) {
                        attributes["hx-post"] = "/submit"
                        attributes["hx-indicator"] = "#spinner"
                        +"Submit"
                    }
                    span {
                        id = "spinner"
                        classes = setOf("loading", "loading-spinner", "htmx-indicator")
                    }
                    """.trimIndent(),
                alpineCode =
                    """
                    div {
                        attributes["x-data"] = "{ loading: false }"
                        mButton(variant = ButtonVariant.Primary) {
                            attributes["x-on:click"] = "loading = true; fetch('/submit', {method: 'POST'}).finally(() => loading = false)"
                            attributes["x-bind:disabled"] = "loading"
                            +"Submit"
                        }
                        span {
                            attributes["x-show"] = "loading"
                            classes = setOf("loading", "loading-spinner")
                        }
                    }
                    """.trimIndent(),
                datastarCode =
                    """
                    div {
                        attributes["data-signals"] = "{ loading: false }"
                        mButton(variant = ButtonVariant.Primary) {
                            attributes["data-on-click"] = "${'$'}loading=true"
                            attributes["data-post"] = "/submit"
                            attributes["data-bind-disabled"] = "${'$'}loading"
                            +"Submit"
                        }
                        span {
                            attributes["data-show"] = "${'$'}loading"
                            classes = setOf("loading", "loading-spinner")
                        }
                    }
                    """.trimIndent(),
            )
        }

        apiReference(
            listOf(
                ApiParam(
                    "variant",
                    "Variant",
                    "Variant.Primary",
                    "DaisyUI colour role: Primary, Secondary, Accent, Ghost, Link, Error, Success, Warning.",
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
