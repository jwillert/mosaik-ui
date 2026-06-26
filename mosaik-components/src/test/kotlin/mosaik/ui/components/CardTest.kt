package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import kotlinx.html.*
import kotlinx.html.stream.createHTML

/** Renders a flow fragment without pretty-printing so assertions are deterministic. */
private fun render(block: FlowContent.() -> Unit): String =
    createHTML(prettyPrint = false).div { block() }

class CardTest : FunSpec({

    test("an empty card renders the base card class on a div") {
        val html = render { mCard {} }

        html shouldContain "<div class=\"card\"></div>"
    }

    test("custom classes are appended after the base card class") {
        val html = render { mCard("bg-base-100 shadow-sm") {} }

        html shouldContain "class=\"card bg-base-100 shadow-sm\""
    }

    test("mCardBody renders the card-body class inside the card") {
        val html = render { mCard { mCardBody { +"Content" } } }

        html shouldContain "<div class=\"card\"><div class=\"card-body\">Content</div></div>"
    }

    test("mCardTitle renders the card-title class on an h2 inside the body") {
        val html = render { mCard { mCardBody { mCardTitle { +"Title" } } } }

        html shouldContain "<div class=\"card-body\"><h2 class=\"card-title\">Title</h2></div>"
    }

    test("mCardActions renders the card-actions class inside the body") {
        val html = render { mCard { mCardBody { mCardActions { +"Buy" } } } }

        html shouldContain "<div class=\"card-actions\">Buy</div>"
    }

    test("sub-components accept custom classes that are appended after their base class") {
        val html = render {
            mCard {
                mCardBody("p-2") {
                    mCardTitle("text-lg") { +"T" }
                    mCardActions("justify-end") {}
                }
            }
        }

        html shouldContain "class=\"card-body p-2\""
        html shouldContain "class=\"card-title text-lg\""
        html shouldContain "class=\"card-actions justify-end\""
    }

    test("the full title + body + actions structure nests in order") {
        val html = render {
            mCard("w-96 bg-base-100 shadow-sm") {
                mCardBody {
                    mCardTitle { +"Shoes!" }
                    p { +"If a dog chews shoes whose shoes does he choose?" }
                    mCardActions("justify-end") {
                        mButton(Variant.Primary) { +"Buy Now" }
                    }
                }
            }
        }

        html shouldContain "<div class=\"card w-96 bg-base-100 shadow-sm\">" +
            "<div class=\"card-body\"><h2 class=\"card-title\">Shoes!</h2>" +
            "<p>If a dog chews shoes whose shoes does he choose?</p>" +
            "<div class=\"card-actions justify-end\"><button class=\"btn btn-primary\">Buy Now</button></div>" +
            "</div></div>"
    }

    test("the card block receives a context delegating to the div so its html attributes apply natively") {
        val html = render {
            mCard {
                id = "featured"
                attributes["hx-get"] = "/cards/1"
            }
        }

        html shouldContain "id=\"featured\""
        html shouldContain "hx-get=\"/cards/1\""
        html shouldContain "class=\"card\""
    }

    test("a card can hold a figure before the body, matching DaisyUI's image card markup") {
        val html = render {
            mCard("bg-base-100 shadow-sm") {
                figure { img(src = "/shoes.jpg", alt = "Shoes") }
                mCardBody { mCardTitle { +"Shoes!" } }
            }
        }

        html shouldContain "<figure><img alt=\"Shoes\" src=\"/shoes.jpg\"></figure>"
        html shouldContain "<div class=\"card-body\">"
    }
})
