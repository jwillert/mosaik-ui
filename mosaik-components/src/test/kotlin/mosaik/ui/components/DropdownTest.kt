package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.li
import kotlinx.html.stream.createHTML

private fun render(block: FlowContent.() -> Unit): String = createHTML(prettyPrint = false).div { block() }

class DropdownTest :
    FunSpec({

        test("an empty dropdown renders the base dropdown class on a div") {
            val html = render { mDropdown {} }

            html shouldContain "<div class=\"dropdown\"></div>"
        }

        test("custom classes are appended after the base dropdown class") {
            val html = render { mDropdown(classes = "inline-block") {} }

            html shouldContain "class=\"dropdown inline-block\""
        }

        test("direction alignment hover and open modifiers render as type-safe classes") {
            val html =
                render {
                    mDropdown(
                        direction = DropdownDirection.Top,
                        alignment = DropdownAlignment.End,
                        hover = true,
                        open = true,
                    ) {}
                }

            html shouldContain "class=\"dropdown dropdown-top dropdown-end dropdown-hover dropdown-open\""
        }

        test("mDropdownTrigger renders a focusable role button trigger") {
            val html = render { mDropdown { mDropdownTrigger { +"Open" } } }

            html shouldContain "<div class=\"dropdown\"><div tabindex=\"0\" role=\"button\">Open</div></div>"
        }

        test("mDropdownTrigger custom classes are appended to the trigger") {
            val html = render { mDropdown { mDropdownTrigger("btn") { +"Open" } } }

            html shouldContain "<div class=\"btn\" tabindex=\"0\" role=\"button\">Open</div>"
        }

        test("mDropdownContent renders a focusable menu list with dropdown content classes") {
            val html =
                render {
                    mDropdown {
                        mDropdownContent {
                            li { a(href = "/profile") { +"Profile" } }
                        }
                    }
                }

            html shouldContain
                "<ul class=\"dropdown-content menu\" tabindex=\"0\">" +
                "<li><a href=\"/profile\">Profile</a></li></ul>"
        }

        test("mDropdownContent custom classes are appended after base classes") {
            val html = render { mDropdown { mDropdownContent("w-52 p-2 shadow") {} } }

            html shouldContain "class=\"dropdown-content menu w-52 p-2 shadow\""
        }

        test("the dropdown block receives a context delegating to the div so attributes apply natively") {
            val html =
                render {
                    mDropdown {
                        id = "account-menu"
                        attributes["data-test"] = "account"
                    }
                }

            html shouldContain "id=\"account-menu\""
            html shouldContain "data-test=\"account\""
            html shouldContain "class=\"dropdown\""
        }
    })
