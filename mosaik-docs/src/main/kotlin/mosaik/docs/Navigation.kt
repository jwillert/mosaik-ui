package mosaik.docs

/** A documentation page: the route it lives at, its sidebar label, and how it renders. */
data class NavItem(
    val path: String,
    val label: String,
    val render: () -> String,
    val renderPartial: () -> String,
    val pageVariantIds: List<String> = emptyList(),
)

/** The landing page. */
val HOME = NavItem("/", "Home", ::landingPage, ::landingPagePartial)

/** The Button documentation page. */
val BUTTON =
    NavItem(
        "/components/button",
        "Button",
        ::buttonPage,
        ::buttonPagePartial,
        BUTTON_PAGE_VARIANT_IDS,
    )

/** The Card documentation page. */
val CARD = NavItem("/components/card", "Card", ::cardPage, ::cardPagePartial)

/** The Navbar documentation page. */
val NAVBAR = NavItem("/components/navbar", "Navbar", ::navbarPage, ::navbarPagePartial)

/** The Footer documentation page. */
val FOOTER = NavItem("/components/footer", "Footer", ::footerPage, ::footerPagePartial)

/** The Badge documentation page. */
val BADGE = NavItem("/components/badge", "Badge", ::badgePage, ::badgePagePartial)

/** The Alert documentation page. */
val ALERT = NavItem("/components/alert", "Alert", ::alertPage, ::alertPagePartial)

/** The Form documentation page. */
val FORM = NavItem("/components/form", "Form", ::formPage, ::formPagePartial)

/** The Loading documentation page. */
val LOADING = NavItem("/components/loading", "Loading", ::loadingPage, ::loadingPagePartial)

/** The Menu documentation page. */
val MENU = NavItem("/components/menu", "Menu", ::menuPage, ::menuPagePartial)

/** The Table documentation page. */
val TABLE_PAGE = NavItem("/components/table", "Table", ::tablePage, ::tablePagePartial)

/** The Tabs documentation page. */
val TABS = NavItem("/components/tabs", "Tabs", ::tabsPage, ::tabsPagePartial)

/** The Interactivity guide page. */
val INTERACTIVITY =
    NavItem(
        "/guides/interactivity",
        "Interactivity",
        ::interactivityPage,
        ::interactivityPagePartial,
        INTERACTIVITY_PAGE_VARIANT_IDS,
    )

/**
 * Every component documentation page. Together with [HOME] and [GUIDES] this is
 * the single source of truth for the sidebar links and the routing table (see
 * [mosaik.docs.module]), so adding a component page is one [NavItem] plus its
 * renderer.
 */
val COMPONENTS = listOf(BUTTON, CARD, NAVBAR, FOOTER, BADGE, ALERT, FORM, LOADING, MENU, TABLE_PAGE, TABS)

/**
 * Every guide documentation page. Together with [HOME] and [COMPONENTS] this is
 * the single source of truth for the sidebar links and the routing table.
 */
val GUIDES = listOf(INTERACTIVITY)
