package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.AlertVariant
import mosaik.ui.components.mAlert

/**
 * Alert page, following the five-section component template: title +
 * description, installation, basic usage, a variants showcase paired with its
 * Kotlin code, and the API reference table. Alert has no size showcase —
 * DaisyUI alerts have no size modifiers.
 */
fun alertPage(): String =
    layout(ALERT) {
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
                htmxCode =
                    """
                    mAlert(AlertVariant.Success) {
                        +"Your changes have been saved."
                        button(classes = "btn btn-sm btn-ghost") {
                            attributes["hx-on:click"] = "this.closest('.alert').remove()"
                            +"Dismiss"
                        }
                    }
                    """.trimIndent(),
                alpineCode =
                    """
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
                datastarCode =
                    """
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
