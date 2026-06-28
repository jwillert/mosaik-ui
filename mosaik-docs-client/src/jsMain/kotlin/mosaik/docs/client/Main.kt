package mosaik.docs.client

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.fetch.RequestInit

const val DEFAULT_INTERACTION_STYLE = "htmx"

/**
 * Entry point for the Mosaik docs browser client.
 *
 * Provides type-safe Kotlin/JS behavior for the server-rendered docs app.
 */
fun main() {
    console.log("Mosaik docs client initialized")
    setupShellNavigation()
    syncPreferenceControls()
}

/**
 * Progressive shell navigation: intercept sidebar link clicks, fetch partial
 * content, and swap only the main content area. This avoids full-page reloads
 * and the white flash when switching between pages, especially in dark themes.
 */
fun setupShellNavigation() {
    document.addEventListener("click", { event ->
        val target = event.target
        if (target is HTMLAnchorElement && isInternalDocsLink(target)) {
            event.preventDefault()
            navigateToPage(target.href)
        }
    })

    // Handle browser back/forward navigation.
    window.addEventListener("popstate", {
        navigateToPage(window.location.href, pushState = false)
    })
}

/**
 * Returns true if the anchor is an internal docs navigation link (sidebar links).
 */
fun isInternalDocsLink(anchor: HTMLAnchorElement): Boolean {
    val href = anchor.href
    val origin = window.location.origin
    // Only intercept same-origin links that are docs pages (not /_examples routes).
    return href.startsWith(origin) && !href.contains("/_examples/")
}

/**
 * Fetches and renders partial content for the given [url], updating only the
 * main content area and the browser URL without a full page reload.
 */
fun navigateToPage(
    url: String,
    pushState: Boolean = true,
) {
    val partialUrl =
        if (url.contains("?")) {
            "$url&partial=true"
        } else {
            "$url?partial=true"
        }

    window
        .fetch(partialUrl, RequestInit())
        .then { response ->
            if (response.ok) {
                response.text()
            } else {
                throw Exception("Failed to load page: ${response.status}")
            }
        }.then { html ->
            val mainContent = document.getElementById("main-content")
            if (mainContent != null) {
                mainContent.innerHTML = html
                if (pushState) {
                    window.history.pushState(null, "", url)
                }
                // Re-run syntax highlighting on the new content.
                js("if (typeof hljs !== 'undefined') hljs.highlightAll();")
                // Sync preference controls after shell content swap.
                syncPreferenceControls()
                // Scroll to top after navigation.
                window.scrollTo(0.0, 0.0)
            }
        }.catch { error ->
            console.error("Navigation error:", error)
        }
}

/**
 * Synchronizes the theme selector, interaction style selector, and interactivity
 * tab radios to match the preferences already restored by the inline head script.
 * Called on initial load and after shell content swaps to ensure controls reflect
 * the current state.
 */
fun syncPreferenceControls() {
    val docElement = document.documentElement ?: return

    // Sync theme selector to the current data-theme attribute.
    val themeSwitcher = document.getElementById("theme-switcher")
    if (themeSwitcher is HTMLSelectElement) {
        val currentTheme = docElement.getAttribute("data-theme")
        if (currentTheme != null) {
            themeSwitcher.value = currentTheme
        }
    }

    // Sync interaction style selector to the current data-interaction-style attribute.
    val interactionStyle = docElement.getAttribute("data-interaction-style") ?: DEFAULT_INTERACTION_STYLE
    val styleSwitcher = document.getElementById("interaction-style-switcher")
    if (styleSwitcher is HTMLSelectElement) {
        styleSwitcher.value = interactionStyle
    }

    // Sync interactivity tab radios to the current interaction style.
    val radios = document.querySelectorAll(".tabs input[type=\"radio\"]")
    for (i in 0 until radios.length) {
        val element = radios.item(i)
        if (element is HTMLInputElement) {
            val tabStyle = element.getAttribute("data-interaction-style")
            if (tabStyle == interactionStyle) {
                element.checked = true
            }
        }
    }
}
