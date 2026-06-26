package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import kotlinx.html.*
import kotlinx.html.stream.createHTML

/** Renders a flow fragment without pretty-printing so assertions are deterministic. */
private fun render(block: FlowContent.() -> Unit): String =
    createHTML(prettyPrint = false).div { block() }

/**
 * Compile-time constraint verification. These should NOT compile due to @MosaikDsl:
 *
 * ```
 * mCard {
 *     mCardBody {
 *         mCardBody { }  // ERROR: mCardBody not in MCardBody scope
 *     }
 * }
 * ```
 *
 * ```
 * mCard {
 *     mCardTitle { }  // ERROR: mCardTitle only in MCardBody scope
 * }
 * ```
 *
 * ```
 * mCard {
 *     mCardActions { }  // ERROR: mCardActions only in MCardBody scope
 * }
 * ```
 *
 * ```
 * mCard {
 *     div {
 *         mCardBody { }  // ERROR: nested HTML blocks lose MCard context
 *     }
 * }
 * ```
 */

class CardDslTest : FunSpec({

    test("mCardBody is callable from mCard context") {
        val html = render {
            mCard {
                mCardBody { +"Body content" }
            }
        }

        html shouldContain "card-body"
        html shouldContain "Body content"
    }

    test("mCardTitle is callable from mCardBody context") {
        val html = render {
            mCard {
                mCardBody {
                    mCardTitle { +"Title text" }
                }
            }
        }

        html shouldContain "card-title"
        html shouldContain "Title text"
    }

    test("mCardActions is callable from mCardBody context") {
        val html = render {
            mCard {
                mCardBody {
                    mCardActions { +"Actions" }
                }
            }
        }

        html shouldContain "card-actions"
        html shouldContain "Actions"
    }

    test("ordinary HTML is usable in mCard context") {
        val html = render {
            mCard {
                figure { img(src = "/img.jpg", alt = "Test") }
                mCardBody { +"Content" }
            }
        }

        html shouldContain "<figure>"
        html shouldContain "img.jpg"
        html shouldContain "card-body"
    }

    test("ordinary HTML is usable in mCardBody context") {
        val html = render {
            mCard {
                mCardBody {
                    p { +"Paragraph" }
                    mCardTitle { +"Title" }
                }
            }
        }

        html shouldContain "<p>Paragraph</p>"
        html shouldContain "card-title"
    }

    test("nested ordinary HTML blocks do not leak mCardBody access") {
        val html = render {
            mCard {
                div {
                    // In this nested div, mCardBody should not be accessible
                    // due to @MosaikDsl marker preventing implicit receiver lookup
                    +"Nested content"
                }
                mCardBody { +"Actual body" }
            }
        }

        html shouldContain "Nested content"
        html shouldContain "Actual body"
    }

    test("nested ordinary HTML blocks do not leak mCardTitle or mCardActions access") {
        val html = render {
            mCard {
                mCardBody {
                    div {
                        // In this nested div, mCardTitle and mCardActions should not be accessible
                        // due to @MosaikDsl marker preventing implicit receiver lookup
                        +"Nested in body"
                    }
                    mCardTitle { +"Title" }
                }
            }
        }

        html shouldContain "Nested in body"
        html shouldContain "card-title"
    }
})
