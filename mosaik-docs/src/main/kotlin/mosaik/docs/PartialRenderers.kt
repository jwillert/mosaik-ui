package mosaik.docs

/**
 * Partial content renderers for progressive shell navigation. Each wraps the
 * corresponding full-page renderer's content in [partialContent] to strip the
 * document shell (head, sidebar, scripts).
 */

fun landingPagePartial(): String = partialContent { landingPageContent() }

fun buttonPagePartial(): String = buttonPage()

fun cardPagePartial(): String = cardPage()

fun navbarPagePartial(): String = navbarPage()

fun footerPagePartial(): String = footerPage()

fun badgePagePartial(): String = badgePage()

fun alertPagePartial(): String = alertPage()

fun interactivityPagePartial(): String = interactivityPage()
