package mosaik.docs

import kotlinx.html.FlowContent
import kotlinx.html.code
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.iframe
import kotlinx.html.p
import kotlinx.html.section
import mosaik.ui.components.ButtonVariant
import mosaik.ui.components.mButtonLink

fun FlowContent.dashboard01PageContent() {
    h1 { +"Dashboard 01" }
    p {
        +"A responsive page block for an analytics dashboard. It composes Mosaik drawer, navbar, "
        +"card, stats, table, menu, avatar, checkbox, divider, dropdown, join, and button components "
        +"into a complete route-sized example."
    }

    h2 { +"Installation" }
    codeBlock("./gradlew mosaikAdd --block=dashboard-01")

    section {
        h2 { +"Preview" }
        p {
            +"The embedded preview loads the same standalone route used for viewport-level inspection. "
            +"Open it directly when testing drawer breakpoints, sticky headers, and full-page scrolling."
        }
        div("not-prose overflow-hidden rounded-box border border-base-300 bg-base-200 shadow-sm") {
            iframe(classes = "h-[720px] w-full bg-base-100") {
                src = DASHBOARD_01_STANDALONE_PREVIEW_PATH
                attributes["title"] = "Dashboard 01 preview"
                attributes["loading"] = "lazy"
            }
        }
        p("not-prose mt-4") {
            mButtonLink(
                href = DASHBOARD_01_STANDALONE_PREVIEW_PATH,
                variant = ButtonVariant.Primary,
            ) {
                attributes["target"] = "_blank"
                attributes["rel"] = "noreferrer"
                +"Open standalone preview"
            }
        }
    }

    section {
        h2 { +"Block contents" }
        p {
            +"The block installs route-sized source rather than a new DaisyUI reference component. "
            +"Keep the generated code with the page it belongs to, then customize the sample data, "
            +"routes, and interaction attributes for your application."
        }
        codeBlock(
            """
            // Preview route shape
            get("/dashboard") {
                call.respondHtml {
                    body {
                        // dashboard-01 page block content
                    }
                }
            }
            """.trimIndent(),
        )
        p {
            +"See the standalone preview at "
            code { +DASHBOARD_01_STANDALONE_PREVIEW_PATH }
            +" for the rendered page shell."
        }
    }
}

fun dashboard01Page(): String = layout(DASHBOARD_01) { dashboard01PageContent() }
