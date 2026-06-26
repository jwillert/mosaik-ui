package mosaik.docs

import kotlinx.html.FlowContent
import kotlinx.html.UL
import kotlinx.html.a
import kotlinx.html.aside
import kotlinx.html.h1
import kotlinx.html.li
import kotlinx.html.ul

/** A documentation page: the route it lives at, its sidebar label, and how it renders. */
data class NavItem(val path: String, val label: String, val render: () -> String)

/** The landing page. */
val HOME = NavItem("/", "Home", ::landingPage)

/** The Button documentation page. */
val BUTTON = NavItem("/components/button", "Button", ::buttonPage)

/** The Card documentation page. */
val CARD = NavItem("/components/card", "Card", ::cardPage)

/** The Navbar documentation page. */
val NAVBAR = NavItem("/components/navbar", "Navbar", ::navbarPage)

/** The Footer documentation page. */
val FOOTER = NavItem("/components/footer", "Footer", ::footerPage)

/** The Badge documentation page. */
val BADGE = NavItem("/components/badge", "Badge", ::badgePage)

/** The Alert documentation page. */
val ALERT = NavItem("/components/alert", "Alert", ::alertPage)

/** The Interactivity guide page. */
val INTERACTIVITY = NavItem("/guides/interactivity", "Interactivity", ::interactivityPage)

/**
 * Every component documentation page. Together with [HOME] and [GUIDES] this is
 * the single source of truth for the sidebar links and the routing table (see
 * [mosaik.docs.module]), so adding a component page is one [NavItem] plus its
 * renderer.
 */
val COMPONENTS = listOf(BUTTON, CARD, NAVBAR, FOOTER, BADGE, ALERT)

/**
 * Every guide documentation page. Together with [HOME] and [COMPONENTS] this is
 * the single source of truth for the sidebar links and the routing table.
 */
val GUIDES = listOf(INTERACTIVITY)

/** Left navigation listing Home, component pages, guide pages, and the theme switcher. */
internal fun FlowContent.sidebar(activePath: String) {
    aside(classes = "w-64 shrink-0 bg-base-200 p-4") {
        h1("text-xl font-bold px-2 pb-4") { +"Mosaik UI" }
        ul("menu w-full") {
            navLink(HOME, activePath)
            li("menu-title") { +"Components" }
            COMPONENTS.forEach { navLink(it, activePath) }
            li("menu-title") { +"Guides" }
            GUIDES.forEach { navLink(it, activePath) }
        }
        themeSwitcher()
    }
}

private fun UL.navLink(item: NavItem, activePath: String) {
    li {
        // DaisyUI marks the current menu entry with `menu-active`.
        val active = if (item.path == activePath) "menu-active" else ""
        a(href = item.path, classes = active) { +item.label }
    }
}
