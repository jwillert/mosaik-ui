package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.TableSize
import mosaik.ui.components.mTable

/**
 * Table page content block for use in both full-page and partial renders.
 */
fun FlowContent.tablePageContent() {
    h1 { +"Table" }
    p {
        +"A table presents structured rows and columns while preserving normal HTML table semantics. "
        code { +"mTable" }
        +" wraps DaisyUI's "
        code { +"table" }
        +" classes with type-safe modifiers for zebra rows and "
        code { +"TableSize" }
        +", then hands you the raw kotlinx.html "
        code { +"TABLE" }
        +" receiver so attributes and third-party extensions work natively (ADR-0003)."
    }
    p {
        code { +"TableSize" }
        +" is component-specific instead of reusing the shared "
        code { +"Size" }
        +" enum: table sizes are a table vocabulary. "
        code { +"TableSize.Default" }
        +" is DaisyUI's baseline table size and emits no size modifier class."
    }

    installSection("table")

    h2 { +"Basic usage" }
    exampleCard(
        code =
            """
            import mosaik.ui.components.mTable

            mTable {
                thead {
                    tr {
                        th { +"Name" }
                        th { +"Role" }
                        th { +"Status" }
                    }
                }
                tbody {
                    tr {
                        td { +"Ada" }
                        td { +"Engineer" }
                        td { +"Active" }
                    }
                }
            }
            """.trimIndent(),
    ) {
        div("overflow-x-auto") {
            mTable { demoTableRows() }
        }
    }

    section {
        h2 { +"Zebra rows" }
        p { +"Set zebra to true for striped body rows." }
        exampleCard(
            code =
                """
                mTable(zebra = true) {
                    // thead/tbody/tr/th/td are raw kotlinx.html table tags
                }
                """.trimIndent(),
        ) {
            div("overflow-x-auto") {
                mTable(zebra = true) { demoTableRows() }
            }
        }
    }

    section {
        h2 { +"Sizes" }
        p {
            +"Use "
            code { +"TableSize" }
            +" for table-specific sizing. Default is unstyled; every other value emits a table size modifier."
        }
        exampleCard(
            code =
                """
                listOf(TableSize.Xs, TableSize.Sm, TableSize.Lg, TableSize.Xl).forEach { size ->
                    mTable(size = size) {
                        // table content
                    }
                }
                """.trimIndent(),
        ) {
            div("grid gap-6") {
                listOf(TableSize.Xs, TableSize.Sm, TableSize.Lg, TableSize.Xl).forEach { size ->
                    div {
                        h3("mb-2 text-sm font-semibold") { +size.name }
                        div("overflow-x-auto") {
                            mTable(size = size) { compactTableRows(size.name) }
                        }
                    }
                }
            }
        }
    }

    section {
        h2 { +"Custom Tailwind utilities" }
        p {
            +"Keep using "
            code { +"classes" }
            +" for utility styling such as width or whitespace behavior; table sizing no longer needs raw class tokens."
        }
        exampleCard(
            code =
                """
                mTable(size = TableSize.Sm, classes = "w-full whitespace-nowrap") {
                    // table content
                }
                """.trimIndent(),
        ) {
            div("overflow-x-auto") {
                mTable(size = TableSize.Sm, classes = "w-full whitespace-nowrap") {
                    demoTableRows()
                }
            }
        }
    }

    section {
        h2 { +"Raw table receiver" }
        p {
            +"The block receiver is the underlying kotlinx.html TABLE, so native attributes and table children "
            +"are available directly."
        }
        exampleCard(
            code =
                """
                mTable(zebra = true, size = TableSize.Sm) {
                    id = "team-table"
                    attributes["data-demo"] = "table"
                    thead { /* ... */ }
                    tbody { /* ... */ }
                }
                """.trimIndent(),
        ) {
            div("overflow-x-auto") {
                mTable(zebra = true, size = TableSize.Sm) {
                    id = "team-table"
                    attributes["data-demo"] = "table"
                    demoTableRows()
                }
            }
        }
    }

    apiReference(
        listOf(
            ApiParam(
                "zebra",
                "Boolean",
                "false",
                "Adds striped row styling when true.",
            ),
            ApiParam(
                "size",
                "TableSize",
                "TableSize.Default",
                "Table-specific size: Default, Xs, Sm, Lg, Xl. Default is the baseline and adds no size class.",
            ),
            ApiParam(
                "classes",
                "String?",
                "null",
                "Extra CSS classes appended after the generated table classes, intended for Tailwind utilities like whitespace-nowrap.",
            ),
            ApiParam(
                "block",
                "TABLE.() -> Unit",
                "{}",
                "Receiver block on the raw kotlinx.html TABLE element — set attributes, library extensions, thead, tbody, rows, and cells.",
            ),
        ),
    )
}

private fun TABLE.demoTableRows() {
    thead {
        tr {
            th { +"Name" }
            th { +"Role" }
            th { +"Status" }
        }
    }
    tbody {
        tr {
            td { +"Ada" }
            td { +"Engineer" }
            td { +"Active" }
        }
        tr {
            td { +"Grace" }
            td { +"Researcher" }
            td { +"Invited" }
        }
        tr {
            td { +"Katherine" }
            td { +"Analyst" }
            td { +"Active" }
        }
    }
}

private fun TABLE.compactTableRows(label: String) {
    thead {
        tr {
            th { +"Size" }
            th { +"Use case" }
        }
    }
    tbody {
        tr {
            td { +label }
            td { +"Dense data" }
        }
    }
}

/** Table page, following the component reference gallery style. */
fun tablePage(): String = layout(TABLE_PAGE) { tablePageContent() }
