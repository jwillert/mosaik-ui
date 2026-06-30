package mosaik.docs

/**
 * Partial content renderers for progressive shell navigation. Each wraps the
 * corresponding full-page renderer's content in [partialContent] to strip the
 * document shell (head, sidebar, scripts).
 */

fun landingPagePartial(): String = partialContent { landingPageContent() }

fun buttonPagePartial(variant: String? = DEFAULT_BUTTON_PAGE_VARIANT_ID): String =
    partialContent {
        buttonPageContent(variant)
    }

fun cardPagePartial(): String = partialContent { cardPageContent() }

fun navbarPagePartial(): String = partialContent { navbarPageContent() }

fun footerPagePartial(): String = partialContent { footerPageContent() }

fun badgePagePartial(): String = partialContent { badgePageContent() }

fun alertPagePartial(): String = partialContent { alertPageContent() }

fun formPagePartial(): String = partialContent { formPageContent() }

fun loadingPagePartial(): String = partialContent { loadingPageContent() }

fun menuPagePartial(): String = partialContent { menuPageContent() }

fun tablePagePartial(): String = partialContent { tablePageContent() }

fun tabsPagePartial(): String = partialContent { tabsPageContent() }

fun interactivityPagePartial(variant: String? = DEFAULT_INTERACTIVITY_PAGE_VARIANT_ID): String =
    partialContent { interactivityPageContent(variant) }
