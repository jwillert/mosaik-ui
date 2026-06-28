package mosaik.docs

/**
 * Partial content renderers for progressive shell navigation. Each wraps the
 * corresponding full-page renderer's content in [partialContent] to strip the
 * document shell (head, sidebar, scripts).
 */

fun landingPagePartial(): String = partialContent { landingPageContent() }

fun buttonPagePartial(): String = partialContent { buttonPageContent() }

fun cardPagePartial(): String = partialContent { cardPageContent() }

fun navbarPagePartial(): String = partialContent { navbarPageContent() }

fun footerPagePartial(): String = partialContent { footerPageContent() }

fun badgePagePartial(): String = partialContent { badgePageContent() }

fun alertPagePartial(): String = partialContent { alertPageContent() }

fun interactivityPagePartial(): String = partialContent { interactivityPageContent() }
