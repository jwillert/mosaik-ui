package mosaik.docs

import kotlinx.html.*

/**
 * Shared documentation building blocks used across component reference pages.
 * These helpers are extracted because they are genuinely reused by multiple
 * component pages (button, card, navbar, footer, badge, alert). Each component
 * page imports what it needs.
 */

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
        table("table table-zebra") {
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
