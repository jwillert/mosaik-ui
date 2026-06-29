package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.ButtonStyle
import mosaik.ui.components.ButtonVariant
import mosaik.ui.components.Size
import mosaik.ui.components.TabsStyle
import mosaik.ui.components.mButton
import mosaik.ui.components.mTab
import mosaik.ui.components.mTable
import mosaik.ui.components.mTabs

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
        code("language-kotlin") { +code }
    }
}

/**
 * A reusable preview-first documentation card. The rendered preview is visible
 * immediately; the matching Kotlin snippet stays collapsed behind a View code
 * disclosure by default and includes a copy button.
 */
fun FlowContent.exampleCard(
    code: String,
    title: String? = null,
    preview: FlowContent.() -> Unit,
) {
    div("not-prose card border border-base-300 bg-base-100 shadow-sm mb-6") {
        attributes["data-docs-example-card"] = "true"
        if (title != null) {
            div("card-body pb-0") {
                h3("card-title text-base") { +title }
            }
        }
        div("p-6 border-b border-base-300") {
            preview()
        }
        details("group") {
            summary("cursor-pointer list-none px-6 py-3 font-medium hover:bg-base-200") {
                span("inline-block group-open:hidden") { +"View code" }
                span("hidden group-open:inline-block") { +"Hide code" }
            }
            div("border-t border-base-300 bg-base-200") {
                div("flex justify-end p-2") {
                    mButton(
                        variant = ButtonVariant.Neutral,
                        style = ButtonStyle.Ghost,
                        size = Size.Sm,
                        classes = "copy-code-button",
                    ) {
                        type = ButtonType.button
                        attributes["onclick"] =
                            "navigator.clipboard.writeText(" +
                            "this.closest('[data-docs-example-card]').querySelector('pre code').innerText" +
                            ")"
                        +"Copy"
                    }
                }
                pre("m-0 rounded-none overflow-x-auto p-4") {
                    code("language-kotlin") { +code }
                }
            }
        }
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
    mTabs(style = TabsStyle.Lifted, classes = "not-prose") {
        mTab(
            name = id,
            id = "$id-htmx",
            label = "htmx",
            checked = true,
            tabConfig = {
                attributes["data-interaction-style"] = "htmx"
            },
        ) {
            if (htmxPreview != null) {
                div("mb-4 not-prose") {
                    htmxPreview()
                }
            }
            pre {
                code { +htmxCode }
            }
        }

        mTab(
            name = id,
            id = "$id-alpine",
            label = "Alpine.js",
            tabConfig = {
                attributes["data-interaction-style"] = "alpine"
            },
        ) {
            if (alpinePreview != null) {
                div("mb-4 not-prose") {
                    alpinePreview()
                }
            }
            pre {
                code { +alpineCode }
            }
        }

        mTab(
            name = id,
            id = "$id-datastar",
            label = "Datastar",
            tabConfig = {
                attributes["data-interaction-style"] = "datastar"
            },
        ) {
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
