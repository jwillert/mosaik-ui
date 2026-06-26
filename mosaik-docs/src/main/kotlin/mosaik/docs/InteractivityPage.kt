package mosaik.docs

import kotlinx.html.*

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

/**
 * A test page that renders the [interactivityTabs] helper with example code
 * blocks, so smoke tests can verify the tab HTML structure without depending
 * on the interactivity guide content.
 */
fun interactivityTabsTestPage(): String = layout(INTERACTIVITY) {
    h1 { +"Test page" }
    interactivityTabs(
        id = "test-tabs",
        htmxCode = "htmx example",
        alpineCode = "alpine example",
        datastarCode = "datastar example",
    )
}
