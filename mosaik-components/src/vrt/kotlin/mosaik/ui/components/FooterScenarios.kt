package mosaik.ui.components

import dev.jwillert.ktor.vrt.Scenario
import kotlinx.html.aside
import kotlinx.html.nav
import kotlinx.html.p

object FooterScenarios {
    /** A single centred line of copy — the simplest footer. */
    private val simple =
        Scenario("footer-simple") {
            mFooter("footer-center bg-base-200 text-base-content p-4") {
                aside {
                    p { +"© 2026 Mosaik UI — built with Kotlin, Ktor and DaisyUI." }
                }
            }
        }

    /** Multiple titled link columns — DaisyUI's sitemap footer. */
    private val columns =
        Scenario("footer-columns") {
            mFooter("bg-base-200 text-base-content p-10") {
                nav {
                    mFooterTitle { +"Services" }
                    mLink(href = "#") { +"Branding" }
                    mLink(href = "#") { +"Design" }
                }
                nav {
                    mFooterTitle { +"Company" }
                    mLink(href = "#") { +"About us" }
                    mLink(href = "#") { +"Contact" }
                }
            }
        }

    val all = listOf(simple, columns)
}
