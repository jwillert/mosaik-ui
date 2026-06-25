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

    h2 { +"Building blocks" }
    p {
        +"Before diving into examples, here's an overview of each library's core primitives, "
        +"best-fit use cases, and limitations."
    }

    h3 { +"htmx" }
    p {
        +"htmx extends HTML with attributes that trigger AJAX requests and swap content. "
        +"Server-driven: the server renders HTML fragments, htmx swaps them into the DOM."
    }
    ul {
        li {
            strong { +"Core primitives: " }
            code { +"hx-get" }
            +", "
            code { +"hx-post" }
            +", "
            code { +"hx-target" }
            +", "
            code { +"hx-swap" }
            +", "
            code { +"hx-trigger" }
        }
        li {
            strong { +"Best for: " }
            +"Server-rendered apps, progressive enhancement, form submissions, live search"
        }
        li {
            strong { +"Script size: " }
            +"~14KB minified + gzipped"
        }
        li {
            strong { +"Limitations: " }
            +"Requires server endpoints for every interaction; not ideal for complex client-side state"
        }
    }
    p {
        +"Note: This project uses Ktor 3.1.3, so htmx attributes are set via the raw attributes map. "
        +"Ktor 3.2 adds typed extensions."
    }
    pre("bg-base-200 rounded-box p-4 overflow-x-auto") {
        code {
            +"""
            // Ktor 3.1.3 syntax
            attributes["hx-post"] = "/endpoint"

            // Ktor 3.2+ syntax
            hxPost = "/endpoint"
            """.trimIndent()
        }
    }

    h3 { +"Alpine.js" }
    p {
        +"Alpine.js brings reactive client-side state to HTML with inline directives. "
        +"Client-side: state lives in JavaScript, DOM updates automatically on change."
    }
    ul {
        li {
            strong { +"Core primitives: " }
            code { +"x-data" }
            +", "
            code { +"x-show" }
            +", "
            code { +"x-on" }
            +", "
            code { +"x-bind" }
            +", "
            code { +"x-model" }
        }
        li {
            strong { +"Best for: " }
            +"Dropdowns, modals, tabs, toggles, form validation — anything with local UI state"
        }
        li {
            strong { +"Script size: " }
            +"~15KB minified + gzipped"
        }
        li {
            strong { +"Limitations: " }
            +"Not built for cross-component or persistent state; no built-in server sync"
        }
    }

    h3 { +"Datastar" }
    p {
        +"Datastar merges server-driven updates with client-side reactivity via Server-Sent Events (SSE). "
        +"Hybrid: server pushes HTML fragments over a persistent connection, client-side store reacts to changes."
    }
    ul {
        li {
            strong { +"Core primitives: " }
            code { +"data-store" }
            +", "
            code { +"data-on" }
            +", "
            code { +"data-bind" }
            +", SSE fragments"
        }
        li {
            strong { +"Best for: " }
            +"Real-time dashboards, live notifications, collaborative editing, streaming updates"
        }
        li {
            strong { +"Script size: " }
            +"~20KB minified + gzipped"
        }
        li {
            strong { +"Limitations: " }
            +"Requires SSE server-side support; less mature ecosystem than htmx or Alpine"
        }
    }
    p {
        +"Note: Datastar requires a Server-Sent Events endpoint on the server to push updates."
    }

    h2 { +"Comparison" }
    p {
        +"Choosing between htmx, Alpine.js, and Datastar depends on your app's needs."
    }
    div("overflow-x-auto") {
        table("table table-zebra") {
            thead {
                tr {
                    th { +"Library" }
                    th { +"Paradigm" }
                    th { +"Server requirement" }
                    th { +"Script size" }
                    th { +"When to choose" }
                }
            }
            tbody {
                tr {
                    td { strong { +"htmx" } }
                    td { +"Server-driven" }
                    td { +"HTML endpoints" }
                    td { +"~14KB" }
                    td { +"Server renders everything; progressive enhancement; no complex client state" }
                }
                tr {
                    td { strong { +"Alpine.js" } }
                    td { +"Client-side" }
                    td { +"None (static HTML)" }
                    td { +"~15KB" }
                    td { +"Local UI state (dropdowns, modals, toggles); no server round-trips needed" }
                }
                tr {
                    td { strong { +"Datastar" } }
                    td { +"Hybrid (SSE-based)" }
                    td { +"SSE endpoints" }
                    td { +"~20KB" }
                    td { +"Real-time updates; server pushes changes; collaborative or live-data features" }
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
