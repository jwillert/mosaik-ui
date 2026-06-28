package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import kotlinx.html.*
import kotlinx.html.stream.createHTML

/** Renders a flow fragment without pretty-printing so assertions are deterministic. */
private fun render(block: FlowContent.() -> Unit): String = createHTML(prettyPrint = false).div { block() }

class TableTest :
    FunSpec({

        test("a basic table renders with the table class") {
            val html =
                render {
                    mTable {
                        thead {
                            tr {
                                th { +"Name" }
                                th { +"Age" }
                            }
                        }
                        tbody {
                            tr {
                                td { +"Alice" }
                                td { +"30" }
                            }
                        }
                    }
                }

            html shouldContain "<table class=\"table\">"
            html shouldContain "<thead>"
            html shouldContain "<th>Name</th>"
        }

        test("zebra modifier adds the table-zebra class") {
            val html =
                render {
                    mTable(zebra = true) {
                        tbody {
                            tr {
                                td { +"Row 1" }
                            }
                        }
                    }
                }

            html shouldContain "class=\"table table-zebra\""
        }

        test("zebra defaults to false") {
            val html =
                render {
                    mTable {
                        tbody {
                            tr {
                                td { +"Row 1" }
                            }
                        }
                    }
                }

            html shouldContain "class=\"table\""
            html shouldNotContain "table-zebra"
        }

        test("custom classes are appended after the generated table classes") {
            val html =
                render {
                    mTable(zebra = true, classes = "w-full") {
                        tbody {
                            tr {
                                td { +"Data" }
                            }
                        }
                    }
                }

            html shouldContain "class=\"table table-zebra w-full\""
        }

        test("the block receives the raw table element so its html attributes apply natively") {
            val html =
                render {
                    mTable {
                        id = "data-table"
                        thead {
                            tr {
                                th { +"Column" }
                            }
                        }
                    }
                }

            html shouldContain "id=\"data-table\""
            html shouldContain "class=\"table\""
        }

        test("arbitrary attributes (e.g. htmx) work natively via the raw element") {
            val html =
                render {
                    mTable(zebra = true) {
                        attributes["hx-get"] = "/data"
                        attributes["hx-trigger"] = "load"
                        tbody {
                            tr {
                                td { +"Loading..." }
                            }
                        }
                    }
                }

            html shouldContain "hx-get=\"/data\""
            html shouldContain "hx-trigger=\"load\""
            html shouldContain "class=\"table table-zebra\""
        }
    })
