package mosaik.docs.client

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLAnchorElement
import org.w3c.fetch.RequestInit

/**
 * Entry point for the Mosaik docs browser client.
 *
 * Provides type-safe Kotlin/JS behavior for the server-rendered docs app.
 */
fun main() {
    console.log("Mosaik docs client initialized")
    setupShellNavigation()
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
                // Scroll to top after navigation.
                window.scrollTo(0.0, 0.0)
            }
        }.catch { error ->
            console.error("Navigation error:", error)
        }
}
