package mosaik.ui.components

import dev.jwillert.ktor.vrt.Scenario
import kotlinx.html.figure
import kotlinx.html.img
import kotlinx.html.p

object CardScenarios {

    /** The bare container: card class plus the surface utilities a real card needs. */
    private val basic = Scenario("card-basic") {
        mCard("w-96 bg-base-100 shadow-sm") {
            mCardBody { +"A plain card body." }
        }
    }

    /** Body with a title above descriptive text — the most common card. */
    private val withTitle = Scenario("card-with-title") {
        mCard("w-96 bg-base-100 shadow-sm") {
            mCardBody {
                mCardTitle { +"Card title" }
                p { +"A card with a title and a short description below it." }
            }
        }
    }

    /** Title, text, and a right-aligned actions row — the full body composition. */
    private val withActions = Scenario("card-with-actions") {
        mCard("w-96 bg-base-100 shadow-sm") {
            mCardBody {
                mCardTitle { +"Buy these shoes" }
                p { +"If a dog chews shoes whose shoes does he choose?" }
                mCardActions("justify-end") {
                    mButton(variant = ButtonVariant.Primary) { +"Buy Now" }
                }
            }
        }
    }

    /** A figure above the body — DaisyUI's image card layout. */
    private val withImage = Scenario("card-with-image") {
        mCard("w-96 bg-base-100 shadow-sm") {
            figure {
                img(src = "https://placehold.co/384x192", alt = "Placeholder")
            }
            mCardBody {
                mCardTitle { +"Image card" }
                p { +"An image sits in a figure above the body." }
            }
        }
    }

    val all = listOf(basic, withTitle, withActions, withImage)
}
