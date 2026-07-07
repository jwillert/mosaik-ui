package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.stream.createHTML

private fun renderReference(block: FlowContent.() -> Unit): String = createHTML(prettyPrint = false).div { block() }

class CheckboxDividerJoinStatTest :
    FunSpec({
        test("checkbox renders the base class with type checkbox") {
            val html = renderReference { mCheckbox { name = "terms" } }

            html shouldContain "<input type=\"checkbox\" class=\"checkbox\" name=\"terms\">"
        }

        test("checkbox maps variants, size, indeterminate and custom classes") {
            val html =
                renderReference {
                    mCheckbox(
                        variant = CheckboxVariant.Success,
                        size = Size.Lg,
                        indeterminate = true,
                        classes = "rounded-sm",
                    ) {}
                }

            html shouldContain "class=\"checkbox checkbox-success checkbox-lg checkbox-indeterminate rounded-sm\""
        }

        test("checkbox variants match DaisyUI checkbox roles") {
            CheckboxVariant.entries.map { entry -> entry.name } shouldBe
                listOf(
                    "Primary",
                    "Secondary",
                    "Accent",
                    "Neutral",
                    "Success",
                    "Warning",
                    "Info",
                    "Error",
                )
        }

        test("divider renders content and omits default orientation and placement modifiers") {
            val html = renderReference { mDivider { +"OR" } }

            html shouldContain "<div class=\"divider\">OR</div>"
            html shouldNotContain "divider-horizontal"
            html shouldNotContain "divider-center"
        }

        test("divider maps vertical orientation, placement, color, and custom classes") {
            val html =
                renderReference {
                    mDivider(
                        orientation = DividerOrientation.Vertical,
                        placement = DividerPlacement.Start,
                        color = DividerColor.Primary,
                        classes = "my-0",
                    ) {}
                }

            html shouldContain "class=\"divider divider-horizontal divider-start divider-primary my-0\""
        }

        test("join renders a wrapper and join items") {
            val html =
                renderReference {
                    mJoin {
                        mJoinItem { +"First" }
                        mJoinItem { +"Second" }
                    }
                }

            html shouldContain "<div class=\"join\"><div class=\"join-item\">First</div>" +
                "<div class=\"join-item\">Second</div></div>"
        }

        test("join supports orientation and item attributes") {
            val html =
                renderReference {
                    mJoin(
                        orientation = JoinOrientation.Vertical,
                        classes = "w-full",
                    ) {
                        mJoinItem { attributes["id"] = "one" }
                    }
                }

            html shouldContain "class=\"join join-vertical w-full\""
            html shouldContain "<div class=\"join-item\" id=\"one\"></div>"
        }

        test("stats render constrained stat sections") {
            val html =
                renderReference {
                    mStats(classes = "shadow") {
                        mStat {
                            mStatTitle { +"Downloads" }
                            mStatValue { +"31K" }
                            mStatDesc { +"Jan 1st - Feb 1st" }
                        }
                    }
                }

            html shouldContain "<div class=\"stats shadow\"><div class=\"stat\">" +
                "<div class=\"stat-title\">Downloads</div><div class=\"stat-value\">31K</div>" +
                "<div class=\"stat-desc\">Jan 1st - Feb 1st</div></div></div>"
        }

        test("stats support vertical orientation") {
            val html = renderReference { mStats(orientation = StatsOrientation.Vertical) }

            html shouldContain "class=\"stats stats-vertical\""
        }

        test("stat subcomponents append custom classes") {
            val html =
                renderReference {
                    mStat {
                        mStatFigure("text-primary") {}
                        mStatActions("justify-end") {}
                    }
                }

            html shouldContain "class=\"stat-figure text-primary\""
            html shouldContain "class=\"stat-actions justify-end\""
        }
    })
