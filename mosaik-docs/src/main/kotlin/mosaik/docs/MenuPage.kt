package mosaik.docs

import kotlinx.html.FlowContent
import kotlinx.html.code
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.p
import mosaik.ui.components.mMenu
import mosaik.ui.components.mMenuItem
import mosaik.ui.components.mMenuTitle

fun FlowContent.menuPageContent() {
    h1 { +"Menu" }
    p {
        code { +"mMenu" }
        +" renders a navigation list with constrained children. Use "
        code { +"mMenuTitle" }
        +" for section labels and "
        code { +"mMenuItem" }
        +" for links; the "
        code { +"active" }
        +" parameter marks the current item."
    }

    installSection("menu")

    h2 { +"Basic usage" }
    exampleCard(
        code =
            """
            import mosaik.ui.components.mMenu
            import mosaik.ui.components.mMenuItem
            import mosaik.ui.components.mMenuTitle

            mMenu(classes = "w-56") {
                mMenuTitle { +"Components" }
                mMenuItem("/components/button", active = true) { +"Button" }
                mMenuItem("/components/card") { +"Card" }
            }
            """.trimIndent(),
    ) {
        mMenu(classes = "w-56") {
            mMenuTitle { +"Components" }
            mMenuItem("/components/button", active = true) { +"Button" }
            mMenuItem("/components/card") { +"Card" }
        }
    }

    h2 { +"Sections" }
    exampleCard(
        code =
            """
            mMenu(classes = "w-56") {
                mMenuTitle { +"Guides" }
                mMenuItem("/guides/interactivity") { +"Interactivity" }
            }
            """.trimIndent(),
    ) {
        mMenu(classes = "w-56") {
            mMenuTitle { +"Guides" }
            mMenuItem("/guides/interactivity") { +"Interactivity" }
        }
    }

    apiReference(
        listOf(
            ApiParam("mMenu.classes", "String?", "null", "Extra CSS classes for the UL menu container."),
            ApiParam(
                "mMenu.block",
                "MMenu.() -> Unit",
                "{}",
                "Constrained receiver where menu child components are available.",
            ),
            ApiParam("mMenuItem.href", "String", "required", "Destination for the rendered anchor."),
            ApiParam("mMenuItem.active", "Boolean", "false", "Adds the active marker for the current page."),
            ApiParam("mMenuTitle.classes", "String?", "null", "Extra CSS classes for the menu title LI."),
        ),
    )
}

fun menuPage(): String = layout(MENU) { menuPageContent() }
