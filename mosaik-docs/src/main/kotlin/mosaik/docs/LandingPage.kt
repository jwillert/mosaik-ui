package mosaik.docs

import kotlinx.html.*

/** Landing page: a short intro and where to look next. */
fun landingPage(): String =
    layout(HOME) {
        h1 { +"Mosaik UI" }
        p {
            +(
                "ShadCN-style components for Kotlin, Ktor and Gradle. Pick a component from the sidebar to see " +
                    "every variant rendered."
            )
        }
        p {
            +"This app dogfoods "
            span("font-mono") { +"mosaik-components" }
            +" directly: if a page renders correctly, the component works."
        }
        p { +"Use the theme switcher in the sidebar to preview components in any DaisyUI theme." }
    }
