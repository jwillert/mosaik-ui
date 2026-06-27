package mosaik.ui.components

import dev.jwillert.ktor.vrt.Scenario
import kotlinx.html.a

object NavbarScenarios {
    /** Brand on the left, actions on the right — the most common two-slot navbar. */
    private val startEnd =
        Scenario("navbar-start-end") {
            mNavbar("bg-base-100 shadow-sm") {
                mNavbarStart {
                    a(classes = "btn btn-ghost text-xl") { +"Mosaik" }
                }
                mNavbarEnd {
                    mButton(variant = ButtonVariant.Primary) { +"Sign up" }
                }
            }
        }

    /** A centred title alone — the single-slot navbar. */
    private val centerOnly =
        Scenario("navbar-center-only") {
            mNavbar("bg-base-100 shadow-sm") {
                mNavbarCenter {
                    a(classes = "btn btn-ghost text-xl") { +"Mosaik" }
                }
            }
        }

    /** All three slots: brand, centred links, trailing action — the full layout. */
    private val allSlots =
        Scenario("navbar-all-slots") {
            mNavbar("bg-base-100 shadow-sm") {
                mNavbarStart {
                    a(classes = "btn btn-ghost text-xl") { +"Mosaik" }
                }
                mNavbarCenter("hidden lg:flex") {
                    a(classes = "btn btn-ghost") { +"Docs" }
                }
                mNavbarEnd {
                    mButton(variant = ButtonVariant.Primary) { +"Sign up" }
                }
            }
        }

    val all = listOf(startEnd, centerOnly, allSlots)
}
