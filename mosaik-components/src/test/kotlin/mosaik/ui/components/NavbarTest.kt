package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import kotlinx.html.*
import kotlinx.html.stream.createHTML

/** Renders a flow fragment without pretty-printing so assertions are deterministic. */
private fun render(block: FlowContent.() -> Unit): String = createHTML(prettyPrint = false).div { block() }

class NavbarTest :
    FunSpec({

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
            val html =
                render {
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
            val html =
                render {
                    mNavbar("bg-base-100 shadow-sm") {
                        mNavbarStart {
                            a(classes = "btn btn-ghost text-xl") { +"Mosaik" }
                        }
                        mNavbarCenter {
                            a { +"Docs" }
                        }
                        mNavbarEnd {
                            mButton(variant = ButtonVariant.Primary) { +"Sign up" }
                        }
                    }
                }

            html shouldContain "<div class=\"navbar bg-base-100 shadow-sm\">" +
                "<div class=\"navbar-start\"><a class=\"btn btn-ghost text-xl\">Mosaik</a></div>" +
                "<div class=\"navbar-center\"><a>Docs</a></div>" +
                "<div class=\"navbar-end\"><button class=\"btn btn-primary\">Sign up</button></div>" +
                "</div>"
        }

        test("the navbar block receives the MNavbar context with delegated html attributes") {
            val html =
                render {
                    mNavbar {
                        id = "main-nav"
                        style = "z-index: 50;"
                        title = "Main navigation"
                        attributes["hx-get"] = "/nav"
                    }
                }

            html shouldContain "id=\"main-nav\""
            html shouldContain "style=\"z-index: 50;\""
            html shouldContain "title=\"Main navigation\""
            html shouldContain "hx-get=\"/nav\""
            html shouldContain "class=\"navbar\""
        }

        test("ordinary HTML remains callable inside the navbar context") {
            val html =
                render {
                    mNavbar {
                        a(classes = "link") { +"Direct link" }
                        div { +"Direct div" }
                        span { +"Direct span" }
                    }
                }

            html shouldContain "<a class=\"link\">Direct link</a>"
            html shouldContain "<div>Direct div</div>"
            html shouldContain "<span>Direct span</span>"
        }

        test("ordinary HTML remains callable inside navbar slots") {
            val html =
                render {
                    mNavbar {
                        mNavbarStart {
                            a(classes = "btn") { +"Button" }
                            div { +"Content" }
                        }
                        mNavbarEnd {
                            button { +"Action" }
                        }
                    }
                }

            html shouldContain "navbar-start"
            html shouldContain "<a class=\"btn\">Button</a>"
            html shouldContain "<div>Content</div>"
            html shouldContain "navbar-end"
            html shouldContain "<button>Action</button>"
        }

        test("the MosaikDsl marker prevents navbar slots from leaking into nested HTML blocks") {
            val html =
                render {
                    mNavbar {
                        mNavbarStart {
                            // Nested ordinary HTML div
                            div {
                                a { +"Link" }
                                // mNavbarStart() would not compile here due to @MosaikDsl marker
                                // mNavbarCenter() would not compile here
                                // mNavbarEnd() would not compile here
                            }
                        }
                    }
                }

            html shouldContain "navbar-start"
            html shouldContain "<div><a>Link</a></div>"
        }

    /*
     * Compile-time constraint tests (documented as code examples):
     *
     * The following would NOT compile due to the MNavbar context constraint:
     *
     * render {
     *     mNavbarStart { } // ERROR: unresolved reference (not in MNavbar context)
     * }
     *
     * render {
     *     div {
     *         mNavbarStart { } // ERROR: unresolved reference (div is not MNavbar)
     *     }
     * }
     *
     * The @MosaikDsl marker prevents slot leakage into nested blocks:
     *
     * render {
     *     mNavbar {
     *         div {
     *             mNavbarStart { } // ERROR: prevented by @MosaikDsl marker
     *         }
     *     }
     * }
     */
    })
