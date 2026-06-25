package mosaik.ui.components

import dev.jwillert.ktor.vrt.Scenario
import kotlinx.html.a
import kotlinx.html.aside
import kotlinx.html.h6
import kotlinx.html.nav
import kotlinx.html.p

object FooterScenarios {

    /** A single centred line of copy — the simplest footer. */
    private val simple = Scenario("footer-simple") {
        mFooter("footer-center bg-base-200 text-base-content p-4") {
            aside {
                p { +"© 2026 Mosaik UI — built with Kotlin, Ktor and DaisyUI." }
            }
        }
    }

    /** Multiple titled link columns — DaisyUI's sitemap footer. */
    private val columns = Scenario("footer-columns") {
        mFooter("bg-base-200 text-base-content p-10") {
            nav {
                h6("footer-title") { +"Services" }
                a(href = "#", classes = "link link-hover") { +"Branding" }
                a(href = "#", classes = "link link-hover") { +"Design" }
            }
            nav {
                h6("footer-title") { +"Company" }
                a(href = "#", classes = "link link-hover") { +"About us" }
                a(href = "#", classes = "link link-hover") { +"Contact" }
            }
        }
    }

    val all = listOf(simple, columns)
}
