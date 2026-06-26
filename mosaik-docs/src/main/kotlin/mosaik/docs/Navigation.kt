package mosaik.docs

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
