package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.mTable

/** A row in an [apiReference] table: one component parameter. */
data class ApiParam(
    val name: String,
    val type: String,
    val default: String,
    val description: String,
)

/** The `./gradlew mosaikAdd` install command for [component]. */
fun FlowContent.installSection(component: String) {
    h2 { +"Installation" }
    codeBlock("./gradlew mosaikAdd --component=$component")
}

/** A minimal Kotlin usage snippet under a "Basic usage" heading. */
fun FlowContent.usageSection(code: String) {
    h2 { +"Basic usage" }
    codeBlock(code)
}

/** A fenced, monospaced code block. Text is escaped by kotlinx.html. */
fun FlowContent.codeBlock(code: String) {
    pre("bg-base-200 rounded-box p-4 overflow-x-auto") {
        code { +code }
    }
}

/** The parameter reference table closing every component page. */
fun FlowContent.apiReference(params: List<ApiParam>) {
    h2 { +"API reference" }
    div("overflow-x-auto") {
        mTable(zebra = true) {
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

/**
 * Renders DaisyUI CSS-only radio tabs for toggling between htmx, Alpine.js,
 * and Datastar code examples. Each tab group uses the [id] parameter as the
 * unique radio group `name` to prevent interference between multiple tab
 * groups on the same page. The htmx tab is checked by default. The wrapper
 * carries the `not-prose` class to prevent Tailwind Typography from
 * overriding DaisyUI's tab styling (ADR-0006).
 *
 * Each tab can optionally show a rendered Preview above the code snippet
 * (ADR-0008). The preview content is provided as a lambda that receives a
 * FlowContent receiver, allowing each style to render its own working example.
 */
fun FlowContent.interactivityTabs(
    id: String,
    htmxCode: String,
    alpineCode: String,
    datastarCode: String,
    htmxPreview: (FlowContent.() -> Unit)? = null,
    alpinePreview: (FlowContent.() -> Unit)? = null,
    datastarPreview: (FlowContent.() -> Unit)? = null,
) {
    div("tabs tabs-lifted not-prose") {
        attributes["role"] = "tablist"

        // htmx tab (checked by default)
        input(type = InputType.radio, name = id, classes = "tab") {
            this.id = "$id-htmx"
            checked = true
            attributes["aria-label"] = "htmx"
            attributes["data-interaction-style"] = "htmx"
        }
        div("tab-content bg-base-200 rounded-box p-4") {
            if (htmxPreview != null) {
                div("mb-4 not-prose") {
                    htmxPreview()
                }
            }
            pre {
                code { +htmxCode }
            }
        }

        // Alpine.js tab
        input(type = InputType.radio, name = id, classes = "tab") {
            this.id = "$id-alpine"
            attributes["aria-label"] = "Alpine.js"
            attributes["data-interaction-style"] = "alpine"
        }
        div("tab-content bg-base-200 rounded-box p-4") {
            if (alpinePreview != null) {
                div("mb-4 not-prose") {
                    alpinePreview()
                }
            }
            pre {
                code { +alpineCode }
            }
        }

        // Datastar tab
        input(type = InputType.radio, name = id, classes = "tab") {
            this.id = "$id-datastar"
            attributes["aria-label"] = "Datastar"
            attributes["data-interaction-style"] = "datastar"
        }
        div("tab-content bg-base-200 rounded-box p-4") {
            if (datastarPreview != null) {
                div("mb-4 not-prose") {
                    datastarPreview()
                }
            }
            pre {
                code { +datastarCode }
            }
        }
    }
}
