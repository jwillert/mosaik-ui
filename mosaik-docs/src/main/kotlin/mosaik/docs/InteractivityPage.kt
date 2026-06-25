package mosaik.docs

import kotlinx.html.*

/**
 * The interactivity guide: how to add client-side behavior (htmx, Alpine.js,
 * Datastar) to components without writing new Kotlin code. Each example shows
 * the same feature implemented in all three libraries, toggled by DaisyUI CSS
 * radio tabs (ADR-0006).
 */
fun interactivityPage(): String = layout(INTERACTIVITY) {
    h1 { +"Interactivity" }
    p {
        +"Mosaik components are CSS-only — they carry no built-in JavaScript. To "
        +"add client-side behavior (form validation, dropdown menus, live search) "
        +"you reach for a JavaScript library. This guide shows the same examples "
        +"built with htmx, Alpine.js, and Datastar, so you can compare patterns and "
        +"pick the one that fits."
    }
}

/**
 * Renders DaisyUI CSS-only radio tabs for toggling between htmx, Alpine.js,
 * and Datastar code examples. Each tab group uses the [id] parameter as the
 * unique radio group `name` to prevent interference between multiple tab
 * groups on the same page. The htmx tab is checked by default. The wrapper
 * carries the `not-prose` class to prevent Tailwind Typography from
 * overriding DaisyUI's tab styling (ADR-0006).
 */
fun FlowContent.interactivityTabs(
    id: String,
    htmxCode: String,
    alpineCode: String,
    datastarCode: String,
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
