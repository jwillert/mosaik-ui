package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import kotlinx.html.*
import kotlinx.html.stream.createHTML

private fun render(block: FlowContent.() -> Unit): String = createHTML(prettyPrint = false).div { block() }

class TabsTest :
    FunSpec({

        test("an empty tabs renders the base tabs class") {
            val html = render { mTabs {} }

            html shouldContain "<div class=\"tabs\" role=\"tablist\"></div>"
        }

        test("custom classes are appended after the base tabs class") {
            val html = render { mTabs(classes = "not-prose") {} }

            html shouldContain "class=\"tabs not-prose\""
        }

        test("mTab renders a radio input with tab class") {
            val html =
                render {
                    mTabs {
                        mTab(
                            name = "test-group",
                            id = "test-tab",
                            label = "Test",
                        ) {
                            +"Content"
                        }
                    }
                }

            html shouldContain "type=\"radio\" name=\"test-group\" class=\"tab\""
            html shouldContain "id=\"test-tab\""
            html shouldContain "aria-label=\"Test\""
        }

        test("mTab renders tab-content div after the radio input") {
            val html =
                render {
                    mTabs {
                        mTab(
                            name = "test-group",
                            id = "test-tab",
                            label = "Test",
                        ) {
                            +"Tab content"
                        }
                    }
                }

            html shouldContain "<div class=\"tab-content bg-base-200 rounded-box p-4\">Tab content</div>"
        }

        test("the first mTab can be checked by default") {
            val html =
                render {
                    mTabs {
                        mTab(
                            name = "group",
                            id = "first",
                            label = "First",
                            checked = true,
                        ) {
                            +"First"
                        }
                    }
                }

            html shouldContain "checked=\"checked\""
        }

        test("multiple tabs share the same name attribute") {
            val html =
                render {
                    mTabs {
                        mTab(
                            name = "shared",
                            id = "tab1",
                            label = "Tab 1",
                            checked = true,
                        ) {
                            +"Content 1"
                        }
                        mTab(
                            name = "shared",
                            id = "tab2",
                            label = "Tab 2",
                        ) {
                            +"Content 2"
                        }
                    }
                }

            html shouldContain "name=\"shared\""
            html shouldContain "id=\"tab1\""
            html shouldContain "id=\"tab2\""
            html shouldContain "Content 1"
            html shouldContain "Content 2"
        }

        test("tabs support lifted style modifier") {
            val html = render { mTabs(style = TabsStyle.Lifted) {} }

            html shouldContain "class=\"tabs tabs-lifted\""
        }

        test("tabs support boxed style modifier") {
            val html = render { mTabs(style = TabsStyle.Boxed) {} }

            html shouldContain "class=\"tabs tabs-boxed\""
        }

        test("tabs support bordered style modifier") {
            val html = render { mTabs(style = TabsStyle.Bordered) {} }

            html shouldContain "class=\"tabs tabs-bordered\""
        }

        test("tabs without explicit style render only the base class") {
            val html = render { mTabs {} }

            html shouldContain "class=\"tabs\""
        }

        test("tab content can be customized with additional classes") {
            val html =
                render {
                    mTabs {
                        mTab(
                            name = "test",
                            id = "test-id",
                            label = "Test",
                            contentClasses = "custom-class",
                        ) {
                            +"Content"
                        }
                    }
                }

            html shouldContain "class=\"tab-content bg-base-200 rounded-box p-4 custom-class\""
        }

        test("the tabs block receives a context delegating to the div so its html attributes apply natively") {
            val html =
                render {
                    mTabs {
                        id = "tabs-wrapper"
                        attributes["data-test"] = "value"
                    }
                }

            html shouldContain "id=\"tabs-wrapper\""
            html shouldContain "data-test=\"value\""
            html shouldContain "role=\"tablist\""
        }
    })
