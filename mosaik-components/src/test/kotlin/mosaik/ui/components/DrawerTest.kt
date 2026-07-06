package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import kotlinx.html.*
import kotlinx.html.stream.createHTML

private fun render(block: FlowContent.() -> Unit): String = createHTML(prettyPrint = false).div { block() }

class DrawerTest :
    FunSpec({

        test("a drawer renders its toggle, content slot, side slot, and overlay") {
            val html =
                render {
                    mDrawer(toggleId = "docs-drawer") {
                        mDrawerContent { +"Page" }
                        mDrawerSide { +"Sidebar" }
                    }
                }

            html shouldContain "<div class=\"drawer\">"
            html shouldContain "<input type=\"checkbox\" class=\"drawer-toggle\" id=\"docs-drawer\">"
            html shouldContain "<div class=\"drawer-content\">Page</div>"
            html shouldContain
                "<div class=\"drawer-side\">" +
                "<label class=\"drawer-overlay\" for=\"docs-drawer\" aria-label=\"close sidebar\"></label>" +
                "Sidebar</div>"
        }

        test("custom classes and placement/open modifiers are appended to the drawer") {
            val html =
                render {
                    mDrawer(
                        toggleId = "settings-drawer",
                        placement = DrawerPlacement.End,
                        open = true,
                        classes = "min-h-screen",
                    ) {}
                }

            html shouldContain "class=\"drawer drawer-end drawer-open min-h-screen\""
        }

        test("checked marks the hidden toggle as checked") {
            val html = render { mDrawer(toggleId = "open-drawer", checked = true) {} }

            html shouldContain
                "<input type=\"checkbox\" class=\"drawer-toggle\" id=\"open-drawer\" checked=\"checked\">"
        }

        test("responsive open modifier keeps the drawer open from each breakpoint") {
            DrawerBreakpoint.entries.forEach { breakpoint ->
                val html =
                    render {
                        mDrawer(
                            toggleId = "responsive-drawer-${breakpoint.token}",
                            openFrom = breakpoint,
                        ) {}
                    }

                html shouldContain "class=\"drawer ${breakpoint.token}:drawer-open\""
                html shouldNotContain "drawer-open drawer-open"
            }
        }

        test("end placement combines with responsive open modifier") {
            val html =
                render {
                    mDrawer(
                        toggleId = "responsive-end-drawer",
                        placement = DrawerPlacement.End,
                        openFrom = DrawerBreakpoint.Lg,
                    ) {}
                }

            html shouldContain "class=\"drawer drawer-end lg:drawer-open\""
        }

        test("slot custom classes are appended after their base classes") {
            val html =
                render {
                    mDrawer(toggleId = "custom-drawer") {
                        mDrawerContent("p-4") {}
                        mDrawerSide("bg-base-200") {}
                    }
                }

            html shouldContain "class=\"drawer-content p-4\""
            html shouldContain "class=\"drawer-side bg-base-200\""
        }

        test("the drawer side overlay can be disabled for custom side-panel behavior") {
            val html =
                render {
                    mDrawer(toggleId = "custom-side-drawer") {
                        mDrawerSide(overlay = false) { +"Sidebar" }
                    }
                }

            html shouldContain "<div class=\"drawer-side\">Sidebar</div>"
            html shouldNotContain "drawer-overlay"
            html shouldNotContain "aria-label=\"close sidebar\""
        }

        test("the drawer block receives a context with root html attributes") {
            val html =
                render {
                    mDrawer(toggleId = "attr-drawer") {
                        id = "shell"
                        attributes["data-test"] = "drawer"
                    }
                }

            html shouldContain "id=\"shell\""
            html shouldContain "data-test=\"drawer\""
            html shouldContain "class=\"drawer\""
        }

        test("ordinary HTML remains callable inside drawer slots") {
            val html =
                render {
                    mDrawer(toggleId = "html-drawer") {
                        mDrawerContent { main { +"Main" } }
                        mDrawerSide { nav { +"Navigation" } }
                    }
                }

            html shouldContain "<main>Main</main>"
            html shouldContain "<nav>Navigation</nav>"
        }
    })
