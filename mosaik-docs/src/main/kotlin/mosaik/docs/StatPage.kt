package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.ButtonVariant
import mosaik.ui.components.StatsOrientation
import mosaik.ui.components.mButton
import mosaik.ui.components.mStat
import mosaik.ui.components.mStatActions
import mosaik.ui.components.mStatDesc
import mosaik.ui.components.mStatFigure
import mosaik.ui.components.mStatTitle
import mosaik.ui.components.mStatValue
import mosaik.ui.components.mStats

fun FlowContent.statPageContent() {
    h1 { +"Stat" }
    p {
        +"Stats present dashboard metrics with consistent title, value, description, "
        +"figure, and action sections. The stat sub-components keep DaisyUI's "
        +"structural classes behind Mosaik component functions."
    }

    installSection("stat")

    h2 { +"Basic usage" }
    exampleCard(
        code =
            """
            import mosaik.ui.components.mStats
            import mosaik.ui.components.mStat
            import mosaik.ui.components.mStatTitle
            import mosaik.ui.components.mStatValue
            import mosaik.ui.components.mStatDesc

            mStats(classes = "shadow") {
                mStat {
                    mStatTitle { +"Downloads" }
                    mStatValue { +"31K" }
                    mStatDesc { +"Jan 1st - Feb 1st" }
                }
            }
            """.trimIndent(),
    ) {
        mStats(classes = "shadow") {
            mStat {
                mStatTitle { +"Downloads" }
                mStatValue { +"31K" }
                mStatDesc { +"Jan 1st - Feb 1st" }
            }
        }
    }

    section {
        h2 { +"Figures and actions" }
        exampleCard(
            code =
                """
                mStats(classes = "shadow") {
                    mStat {
                        mStatFigure(classes = "text-primary") { +"↗" }
                        mStatTitle { +"Revenue" }
                        mStatValue { +"\${'$'}89K" }
                        mStatActions { mButton(variant = ButtonVariant.Primary) { +"Details" } }
                    }
                }
                """.trimIndent(),
        ) {
            mStats(classes = "shadow") {
                mStat {
                    mStatFigure(classes = "text-primary text-3xl") { +"↗" }
                    mStatTitle { +"Revenue" }
                    mStatValue { +"\$89K" }
                    mStatActions { mButton(variant = ButtonVariant.Primary) { +"Details" } }
                }
            }
        }
    }

    section {
        h2 { +"Vertical" }
        exampleCard(
            code =
                """
                mStats(orientation = StatsOrientation.Vertical) {
                    mStat { mStatTitle { +"Users" }; mStatValue { +"4,200" } }
                    mStat { mStatTitle { +"Orders" }; mStatValue { +"1,200" } }
                }
                """.trimIndent(),
        ) {
            mStats(orientation = StatsOrientation.Vertical, classes = "shadow") {
                mStat {
                    mStatTitle { +"Users" }
                    mStatValue { +"4,200" }
                }
                mStat {
                    mStatTitle { +"Orders" }
                    mStatValue { +"1,200" }
                }
            }
        }
    }

    apiReference(
        listOf(
            ApiParam(
                "orientation",
                "StatsOrientation",
                "StatsOrientation.Horizontal",
                "Horizontal by default; Vertical renders the vertical stats modifier.",
            ),
            ApiParam("classes", "String?", "null", "Extra CSS classes appended after generated stats classes."),
            ApiParam("block", "MStats.() -> Unit", "{}", "Constrained stats content block."),
            ApiParam("mStat block", "MStat.() -> Unit", "{}", "Constrained stat content block."),
            ApiParam(
                "stat sub-component classes",
                "String?",
                "null",
                "Extra classes for figure, title, value, description, or actions.",
            ),
        ),
    )
}

fun statPage(): String = layout(STAT) { statPageContent() }
