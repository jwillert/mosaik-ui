package mosaik.docs

import kotlinx.html.FlowContent
import kotlinx.html.code
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.p
import mosaik.ui.components.ButtonVariant
import mosaik.ui.components.DrawerBreakpoint
import mosaik.ui.components.DrawerPlacement
import mosaik.ui.components.mDrawer
import mosaik.ui.components.mDrawerButton
import mosaik.ui.components.mDrawerContent
import mosaik.ui.components.mDrawerSide
import mosaik.ui.components.mMenu
import mosaik.ui.components.mMenuItem
import mosaik.ui.components.mMenuTitle

fun FlowContent.drawerPageContent() {
    h1 { +"Drawer" }
    p {
        code { +"mDrawer" }
        +" renders a page layout with constrained content and side slots. Use "
        code { +"DrawerPlacement" }
        +" to choose the side panel edge and "
        code { +"DrawerBreakpoint" }
        +" with "
        code { +"openFrom" }
        +" when a sidebar should become permanently visible on larger screens."
    }

    installSection("drawer")

    h2 { +"Basic usage" }
    exampleCard(
        code =
            """
            import mosaik.ui.components.mDrawer
            import mosaik.ui.components.mDrawerContent
            import mosaik.ui.components.mDrawerSide

            mDrawer(toggleId = "docs-drawer") {
                mDrawerContent(classes = "p-4") {
                    +"Page content"
                }
                mDrawerSide(classes = "bg-base-200 min-h-full w-64") {
                    +"Sidebar"
                }
            }
            """.trimIndent(),
    ) {
        mDrawer(toggleId = "docs-drawer") {
            mDrawerContent(classes = "p-4") {
                +"Page content"
            }
            mDrawerSide(classes = "bg-base-200 min-h-full w-64") {
                +"Sidebar"
            }
        }
    }

    h2 { +"Responsive app-shell trigger" }
    p {
        +"Use "
        code { +"mDrawerButton" }
        +" for a CSS-only app-shell trigger, then pass "
        code { +"openFrom = DrawerBreakpoint.Lg" }
        +" to keep the drawer overlay-style on small screens while rendering it "
        +"as an always-open sidebar from the large breakpoint upward."
    }
    exampleCard(
        code =
            """
            import mosaik.ui.components.DrawerBreakpoint
            import mosaik.ui.components.ButtonVariant
            import mosaik.ui.components.mDrawer
            import mosaik.ui.components.mDrawerButton
            import mosaik.ui.components.mDrawerContent
            import mosaik.ui.components.mDrawerSide
            import mosaik.ui.components.mMenu
            import mosaik.ui.components.mMenuItem
            import mosaik.ui.components.mMenuTitle

            mDrawer(toggleId = "responsive-docs-drawer", openFrom = DrawerBreakpoint.Lg) {
                mDrawerContent(classes = "p-6") {
                    mDrawerButton(
                        toggleId = "responsive-docs-drawer",
                        variant = ButtonVariant.Primary,
                        classes = "lg:hidden",
                    ) { +"Open menu" }
                    p { +"The sidebar stays open on large screens." }
                }
                mDrawerSide(classes = "bg-base-200 min-h-full w-64") {
                    mMenu(classes = "p-4 w-full") {
                        mMenuTitle { +"Docs" }
                        mMenuItem("/components/drawer", active = true) { +"Drawer" }
                        mMenuItem("/components/navbar") { +"Navbar" }
                    }
                }
            }
            """.trimIndent(),
    ) {
        mDrawer(toggleId = "responsive-docs-drawer", openFrom = DrawerBreakpoint.Lg) {
            mDrawerContent(classes = "p-6") {
                mDrawerButton(
                    toggleId = "responsive-docs-drawer",
                    variant = ButtonVariant.Primary,
                    classes = "lg:hidden",
                ) {
                    +"Open menu"
                }
                p { +"The sidebar stays open on large screens." }
            }
            mDrawerSide(classes = "bg-base-200 min-h-full w-64") {
                mMenu(classes = "p-4 w-full") {
                    mMenuTitle { +"Docs" }
                    mMenuItem("/components/drawer", active = true) { +"Drawer" }
                    mMenuItem("/components/navbar") { +"Navbar" }
                }
            }
        }
    }

    h2 { +"End placement" }
    exampleCard(
        code =
            """
            import mosaik.ui.components.DrawerPlacement

            mDrawer(toggleId = "end-drawer", placement = DrawerPlacement.End, open = true) {
                mDrawerContent(classes = "p-4") { +"Content" }
                mDrawerSide(classes = "bg-base-200 min-h-full w-64") { +"End sidebar" }
            }
            """.trimIndent(),
    ) {
        mDrawer(toggleId = "end-drawer", placement = DrawerPlacement.End, open = true) {
            mDrawerContent(classes = "p-4") { +"Content" }
            mDrawerSide(classes = "bg-base-200 min-h-full w-64") { +"End sidebar" }
        }
    }

    p {
        +"Supported responsive open breakpoints are "
        DrawerBreakpoint.entries.forEachIndexed { index, breakpoint ->
            if (index > 0) +", "
            code { +"DrawerBreakpoint.${breakpoint.name}" }
        }
        +"."
    }

    apiReference(
        listOf(
            ApiParam(
                "mDrawer.toggleId",
                "String",
                "required",
                "ID for the generated checkbox toggle.",
            ),
            ApiParam(
                "mDrawer.placement",
                "DrawerPlacement",
                "DrawerPlacement.Start",
                "Start or end side panel placement.",
            ),
            ApiParam("mDrawer.open", "Boolean", "false", "Adds the always-open drawer modifier."),
            ApiParam(
                "mDrawer.checked",
                "Boolean",
                "false",
                "Initial checked state for the generated toggle.",
            ),
            ApiParam("mDrawer.classes", "String?", "null", "Extra CSS classes for the drawer root."),
            ApiParam(
                "mDrawer.openFrom",
                "DrawerBreakpoint?",
                "null",
                "Breakpoint where the drawer becomes always open.",
            ),
            ApiParam(
                "mDrawer.block",
                "MDrawer.() -> Unit",
                "{}",
                "Constrained receiver where drawer slots are available.",
            ),
            ApiParam(
                "mDrawerButton.toggleId",
                "String",
                "required",
                "ID of the generated drawer checkbox to target with the label trigger.",
            ),
            ApiParam(
                "mDrawerButton.variant/style/shape/width/size/classes",
                "Button modifiers",
                "button defaults",
                "Same modifier vocabulary as mButton, rendered on a label trigger.",
            ),
            ApiParam(
                "mDrawerButton.block",
                "LABEL.() -> Unit",
                "{}",
                "Receiver block on the raw kotlinx.html LABEL element.",
            ),
            ApiParam(
                "mDrawerContent.classes",
                "String?",
                "null",
                "Extra CSS classes for the content slot.",
            ),
            ApiParam(
                "mDrawerContent.block",
                "DIV.() -> Unit",
                "{}",
                "Receiver block on the raw kotlinx.html DIV element.",
            ),
            ApiParam(
                "mDrawerSide.classes",
                "String?",
                "null",
                "Extra CSS classes for the side slot.",
            ),
            ApiParam(
                "mDrawerSide.overlay",
                "Boolean",
                "true",
                "Whether to render the close-overlay label.",
            ),
            ApiParam(
                "mDrawerSide.overlayLabel",
                "String",
                "close sidebar",
                "Accessible label for the overlay.",
            ),
            ApiParam(
                "mDrawerSide.block",
                "DIV.() -> Unit",
                "{}",
                "Receiver block on the raw kotlinx.html DIV element.",
            ),
        ),
    )
}

fun drawerPage(): String = layout(DRAWER) { drawerPageContent() }
