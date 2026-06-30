package mosaik.docs

import kotlinx.html.FlowContent
import kotlinx.html.code
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.p
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr
import mosaik.ui.components.mTable

fun FlowContent.tablePageContent() {
    h1 { +"Table" }
    p {
        code { +"mTable" }
        +" provides the styled table shell while leaving normal kotlinx.html table structure available on the raw "
        code { +"TABLE" }
        +" receiver. Use "
        code { +"zebra" }
        +" for striped rows."
    }

    installSection("table")

    h2 { +"Basic usage" }
    exampleCard(
        code =
            """
            import mosaik.ui.components.mTable

            mTable {
                thead { tr { th { +"Name" }; th { +"Role" } } }
                tbody { tr { td { +"Ada" }; td { +"Admin" } } }
            }
            """.trimIndent(),
    ) {
        mTable {
            thead {
                tr {
                    th { +"Name" }
                    th { +"Role" }
                }
            }
            tbody {
                tr {
                    td { +"Ada" }
                    td { +"Admin" }
                }
            }
        }
    }

    h2 { +"Zebra rows" }
    exampleCard(
        code =
            """
            mTable(zebra = true) {
                thead { tr { th { +"Component" }; th { +"Status" } } }
                tbody { tr { td { +"Button" }; td { +"Ready" } } }
            }
            """.trimIndent(),
    ) {
        mTable(zebra = true) {
            thead {
                tr {
                    th { +"Component" }
                    th { +"Status" }
                }
            }
            tbody {
                tr {
                    td { +"Button" }
                    td { +"Ready" }
                }
                tr {
                    td { +"Table" }
                    td { +"Ready" }
                }
            }
        }
    }

    apiReference(
        listOf(
            ApiParam("zebra", "Boolean", "false", "Adds striped row styling for easier scanning."),
            ApiParam("classes", "String?", "null", "Extra CSS classes appended after generated table classes."),
            ApiParam(
                "block",
                "TABLE.() -> Unit",
                "{}",
                "Receiver block on the raw kotlinx.html TABLE element; call thead, tbody, tr, th, and td normally.",
            ),
        ),
    )
}

fun tablePage(): String = layout(TABLE_PAGE) { tablePageContent() }
