package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import kotlinx.html.*
import kotlinx.html.stream.createHTML

private fun render(block: FlowContent.() -> Unit): String = createHTML(prettyPrint = false).div { block() }

class MenuTest :
    FunSpec({

        test("an empty menu renders the base menu class on a ul") {
            val html = render { mMenu {} }

            html shouldContain "<ul class=\"menu\"></ul>"
        }

        test("custom classes are appended after the base menu class") {
            val html = render { mMenu("w-full") {} }

            html shouldContain "class=\"menu w-full\""
        }

        test("mMenuItem renders a menu item with an anchor inside a list item") {
            val html = render { mMenu { mMenuItem("/home") { +"Home" } } }

            html shouldContain "<ul class=\"menu\"><li><a href=\"/home\">Home</a></li></ul>"
        }

        test("mMenuItem with active state renders the menu-active class on the anchor") {
            val html = render { mMenu { mMenuItem("/home", active = true) { +"Home" } } }

            html shouldContain "<a href=\"/home\" class=\"menu-active\">Home</a>"
        }

        test("mMenuItem without active state renders no class on the anchor") {
            val html = render { mMenu { mMenuItem("/home", active = false) { +"Home" } } }

            html shouldContain "<a href=\"/home\">Home</a>"
        }

        test("mMenuTitle renders the menu-title class on a list item") {
            val html = render { mMenu { mMenuTitle { +"Components" } } }

            html shouldContain "<li class=\"menu-title\">Components</li>"
        }

        test("mMenuTitle custom classes are appended after the base menu-title class") {
            val html = render { mMenu { mMenuTitle("mt-2") { +"Components" } } }

            html shouldContain "<li class=\"menu-title mt-2\">Components</li>"
        }

        test("menu can contain multiple titles and items in order") {
            val html =
                render {
                    mMenu("w-full") {
                        mMenuItem("/", active = true) { +"Home" }
                        mMenuTitle { +"Components" }
                        mMenuItem("/components/button", active = false) { +"Button" }
                        mMenuItem("/components/card", active = false) { +"Card" }
                        mMenuTitle { +"Guides" }
                        mMenuItem("/guides/setup", active = false) { +"Setup" }
                    }
                }

            html shouldContain "<ul class=\"menu w-full\">"
            html shouldContain "<li><a href=\"/\" class=\"menu-active\">Home</a></li>"
            html shouldContain "<li class=\"menu-title\">Components</li>"
            html shouldContain "<a href=\"/components/button\">Button</a>"
            html shouldContain "<a href=\"/components/card\">Card</a>"
            html shouldContain "<li class=\"menu-title\">Guides</li>"
            html shouldContain "<a href=\"/guides/setup\">Setup</a>"
        }

        test("the menu block receives a context delegating to the ul so its html attributes apply natively") {
            val html =
                render {
                    mMenu {
                        id = "sidebar-nav"
                        attributes["data-test"] = "navigation"
                    }
                }

            html shouldContain "id=\"sidebar-nav\""
            html shouldContain "data-test=\"navigation\""
            html shouldContain "class=\"menu\""
        }
    })
