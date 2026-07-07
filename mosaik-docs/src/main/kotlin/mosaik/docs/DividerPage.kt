package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.DividerColor
import mosaik.ui.components.DividerOrientation
import mosaik.ui.components.DividerPlacement
import mosaik.ui.components.mDivider

fun FlowContent.dividerPageContent() {
    h1 { +"Divider" }
    p {
        +"Dividers separate content with an optional label. "
        code { +"mDivider" }
        +" provides parameters for orientation, label placement, and colour role."
    }

    installSection("divider")

    h2 { +"Basic usage" }
    exampleCard(
        code =
            """
            import mosaik.ui.components.mDivider

            mDivider { +"OR" }
            """.trimIndent(),
    ) {
        mDivider { +"OR" }
    }

    section {
        h2 { +"Placement and colour" }
        exampleCard(
            code =
                """
                mDivider(placement = DividerPlacement.Start) { +"Start" }
                mDivider(color = DividerColor.Primary) { +"Primary" }
                mDivider(placement = DividerPlacement.End) { +"End" }
                """.trimIndent(),
        ) {
            mDivider(placement = DividerPlacement.Start) { +"Start" }
            mDivider(color = DividerColor.Primary) { +"Primary" }
            mDivider(placement = DividerPlacement.End) { +"End" }
        }
    }

    section {
        h2 { +"Vertical layout" }
        exampleCard(
            code =
                """
                div("flex h-24") {
                    div("grid flex-grow place-items-center") { +"A" }
                    mDivider(orientation = DividerOrientation.Vertical) { +"OR" }
                    div("grid flex-grow place-items-center") { +"B" }
                }
                """.trimIndent(),
        ) {
            div("flex h-24") {
                div("grid flex-grow place-items-center rounded-box bg-base-200") { +"A" }
                mDivider(orientation = DividerOrientation.Vertical) { +"OR" }
                div("grid flex-grow place-items-center rounded-box bg-base-200") { +"B" }
            }
        }
    }

    apiReference(
        listOf(
            ApiParam(
                "orientation",
                "DividerOrientation",
                "DividerOrientation.Horizontal",
                "Horizontal divider by default; Vertical renders the layout modifier.",
            ),
            ApiParam(
                "placement",
                "DividerPlacement",
                "DividerPlacement.Center",
                "Label placement: Center, Start, or End.",
            ),
            ApiParam("color", "DividerColor?", "null", "Optional colour role."),
            ApiParam("classes", "String?", "null", "Extra CSS classes appended after generated classes."),
            ApiParam("block", "DIV.() -> Unit", "{}", "Receiver block on the raw kotlinx.html DIV element."),
        ),
    )
}

fun dividerPage(): String = layout(DIVIDER) { dividerPageContent() }
