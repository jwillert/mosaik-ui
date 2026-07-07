package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.ButtonVariant
import mosaik.ui.components.JoinOrientation
import mosaik.ui.components.mButton
import mosaik.ui.components.mJoin
import mosaik.ui.components.mJoinItem

fun FlowContent.joinPageContent() {
    h1 { +"Join" }
    p {
        +"Join groups visually connect related controls. "
        code { +"mJoin" }
        +" exposes join items and vertical layout through Mosaik functions instead "
        +"of raw DaisyUI class names."
    }

    installSection("join")

    h2 { +"Basic usage" }
    exampleCard(
        code =
            """
            import mosaik.ui.components.mJoin
            import mosaik.ui.components.mJoinItem

            mJoin {
                mJoinItem { +"First" }
                mJoinItem { +"Second" }
                mJoinItem { +"Third" }
            }
            """.trimIndent(),
    ) {
        mJoin {
            mJoinItem { +"First" }
            mJoinItem { +"Second" }
            mJoinItem { +"Third" }
        }
    }

    section {
        h2 { +"Controls" }
        exampleCard(
            code =
                """
                mJoin {
                    mJoinItem { mButton { +"Left" } }
                    mJoinItem { mButton(variant = ButtonVariant.Primary) { +"Right" } }
                }
                """.trimIndent(),
        ) {
            mJoin {
                mJoinItem { mButton { +"Left" } }
                mJoinItem { mButton(variant = ButtonVariant.Primary) { +"Right" } }
            }
        }
    }

    section {
        h2 { +"Vertical" }
        exampleCard(
            code =
                """
                mJoin(orientation = JoinOrientation.Vertical) {
                    mJoinItem { +"Top" }
                    mJoinItem { +"Middle" }
                    mJoinItem { +"Bottom" }
                }
                """.trimIndent(),
        ) {
            mJoin(orientation = JoinOrientation.Vertical, classes = "w-48") {
                mJoinItem { +"Top" }
                mJoinItem { +"Middle" }
                mJoinItem { +"Bottom" }
            }
        }
    }

    apiReference(
        listOf(
            ApiParam(
                "orientation",
                "JoinOrientation",
                "JoinOrientation.Horizontal",
                "Horizontal by default; Vertical renders the vertical group modifier.",
            ),
            ApiParam("classes", "String?", "null", "Extra CSS classes appended after generated join classes."),
            ApiParam("block", "MJoin.() -> Unit", "{}", "Constrained join content block."),
            ApiParam(
                "mJoinItem classes",
                "String?",
                "null",
                "Extra CSS classes appended after the generated join item class.",
            ),
            ApiParam("mJoinItem block", "DIV.() -> Unit", "{}", "Receiver block on the raw kotlinx.html DIV element."),
        ),
    )
}

fun joinPage(): String = layout(JOIN) { joinPageContent() }
