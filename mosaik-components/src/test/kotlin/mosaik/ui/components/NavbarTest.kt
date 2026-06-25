package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import kotlinx.html.*
import kotlinx.html.stream.createHTML

/** Renders a flow fragment without pretty-printing so assertions are deterministic. */
private fun render(block: FlowContent.() -> Unit): String =
    createHTML(prettyPrint = false).div { block() }

class NavbarTest : FunSpec({

    test("an empty navbar renders the base navbar class on a div") {
        val html = render { mNavbar {} }

        html shouldContain "<div class=\"navbar\"></div>"
    }

    test("custom classes are appended after the base navbar class") {
        val html = render { mNavbar("bg-base-100 shadow-sm") {} }

        html shouldContain "class=\"navbar bg-base-100 shadow-sm\""
    }

    test("mNavbarStart renders the navbar-start class inside the navbar") {
        val html = render { mNavbar { mNavbarStart { +"Brand" } } }

        html shouldContain "<div class=\"navbar\"><div class=\"navbar-start\">Brand</div></div>"
    }

    test("mNavbarCenter renders the navbar-center class inside the navbar") {
        val html = render { mNavbar { mNavbarCenter { +"Title" } } }

        html shouldContain "<div class=\"navbar-center\">Title</div>"
    }

    test("mNavbarEnd renders the navbar-end class inside the navbar") {
        val html = render { mNavbar { mNavbarEnd { +"Actions" } } }

        html shouldContain "<div class=\"navbar-end\">Actions</div>"
    }

    test("sub-components accept custom classes that are appended after their base class") {
        val html = render {
            mNavbar {
                mNavbarStart("gap-2") {}
                mNavbarCenter("hidden lg:flex") {}
                mNavbarEnd("gap-1") {}
            }
        }

        html shouldContain "class=\"navbar-start gap-2\""
        html shouldContain "class=\"navbar-center hidden lg:flex\""
        html shouldContain "class=\"navbar-end gap-1\""
    }

    test("the full start + center + end structure nests in order") {
        val html = render {
            mNavbar("bg-base-100 shadow-sm") {
                mNavbarStart {
                    a(classes = "btn btn-ghost text-xl") { +"Mosaik" }
                }
                mNavbarCenter {
                    a { +"Docs" }
                }
                mNavbarEnd {
                    mButton(Variant.Primary) { +"Sign up" }
                }
            }
        }

        html shouldContain "<div class=\"navbar bg-base-100 shadow-sm\">" +
            "<div class=\"navbar-start\"><a class=\"btn btn-ghost text-xl\">Mosaik</a></div>" +
            "<div class=\"navbar-center\"><a>Docs</a></div>" +
            "<div class=\"navbar-end\"><button class=\"btn btn-primary\">Sign up</button></div>" +
            "</div>"
    }

    test("the navbar block receives the raw div element so its html attributes apply natively") {
        val html = render {
            mNavbar {
                id = "main-nav"
                attributes["hx-get"] = "/nav"
            }
        }

        html shouldContain "id=\"main-nav\""
        html shouldContain "hx-get=\"/nav\""
        html shouldContain "class=\"navbar\""
    }
})
