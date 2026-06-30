package mosaik.docs

import kotlinx.html.FlowContent
import kotlinx.html.code
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.p
import mosaik.ui.components.TabsStyle
import mosaik.ui.components.mTab
import mosaik.ui.components.mTabs

fun FlowContent.tabsPageContent() {
    h1 { +"Tabs" }
    p {
        code { +"mTabs" }
        +" renders CSS-only radio tabs with "
        code { +"role=\"tablist\"" }
        +" on the container. "
        code { +"mTab" }
        +" is scoped to the "
        code { +"MTabs" }
        +" receiver so tab panels stay inside their parent."
    }

    installSection("tabs")

    h2 { +"Basic usage" }
    exampleCard(
        code =
            """
            import mosaik.ui.components.TabsStyle
            import mosaik.ui.components.mTab
            import mosaik.ui.components.mTabs

            mTabs(style = TabsStyle.Lifted) {
                mTab(name = "docs-tabs", id = "preview", label = "Preview", checked = true) {
                    +"Preview content"
                }
                mTab(name = "docs-tabs", id = "code", label = "Code") {
                    +"Code content"
                }
            }
            """.trimIndent(),
    ) {
        mTabs(style = TabsStyle.Lifted) {
            mTab(name = "docs-tabs", id = "preview", label = "Preview", checked = true) { +"Preview content" }
            mTab(name = "docs-tabs", id = "code", label = "Code") { +"Code content" }
        }
    }

    h2 { +"Styles" }
    exampleCard(
        code =
            """
            TabsStyle.entries.forEach { style ->
                mTabs(style = style) {
                    mTab(name = style.name, id = "${'$'}{style.name}-one", label = style.name, checked = true) {
                        +"Panel"
                    }
                }
            }
            """.trimIndent(),
    ) {
        TabsStyle.entries.forEach { style ->
            mTabs(style = style, classes = "mb-4") {
                mTab(name = style.name, id = "${style.name}-one", label = style.name, checked = true) { +"Panel" }
            }
        }
    }

    apiReference(
        listOf(
            ApiParam("mTabs.style", "TabsStyle?", "null", "Optional style: Lifted, Boxed, or Bordered."),
            ApiParam("mTabs.classes", "String?", "null", "Extra CSS classes for the tabs container."),
            ApiParam("mTabs.block", "MTabs.() -> Unit", "{}", "Constrained receiver where mTab can be called."),
            ApiParam("mTab.name", "String", "required", "Radio group name shared by tabs in the same set."),
            ApiParam("mTab.id", "String", "required", "Unique id for the radio input."),
            ApiParam("mTab.label", "String", "required", "Visible tab label via aria-label."),
            ApiParam("mTab.checked", "Boolean", "false", "Selects the default active tab."),
        ),
    )
}

fun tabsPage(): String = layout(TABS) { tabsPageContent() }
