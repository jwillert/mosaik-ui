package mosaik.ui.components

import dev.jwillert.ktor.vrt.Scenario
import kotlinx.html.p

object DrawerScenarios {
    private val responsiveLargeSidebar =
        Scenario("drawer-responsive-large-sidebar") {
            mDrawer(
                toggleId = "vrt-responsive-drawer",
                openFrom = DrawerBreakpoint.Lg,
                classes = "min-h-80 w-[72rem] bg-base-100",
            ) {
                mDrawerContent(classes = "p-6") {
                    mDrawerButton(
                        toggleId = "vrt-responsive-drawer",
                        variant = ButtonVariant.Primary,
                        classes = "lg:hidden",
                    ) { +"Open menu" }
                    p { +"Responsive drawer content with the sidebar pinned open on large screens." }
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

    val all = listOf(responsiveLargeSidebar)
}
