package mosaik.docs.client

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.asList
import org.w3c.dom.parsing.DOMParser
import org.w3c.dom.url.URL
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
        if (target is HTMLAnchorElement && shouldEnhanceNavigation(target, event)) {
            event.preventDefault()
            val variant = target.getAttribute("data-page-variant")
            if (variant != null) {
                window.localStorage.setItem("mosaik-interaction-style", variant)
            }
            navigateToPage(urlWithSoftPageVariantPreference(target.href))
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
 * Returns true if the navigation should be enhanced via shell navigation.
 *
 * Preserves native browser navigation boundaries for:
 * - Modified clicks (ctrl/cmd/shift/alt)
 * - Non-left-clicks (middle/right clicks)
 * - Links with target attributes
 * - Download links
 * - External links
 * - Static asset URLs
 * - Live example endpoints under /_examples
 */
fun shouldEnhanceNavigation(
    anchor: HTMLAnchorElement,
    event: dynamic,
): Boolean {
    // Don't enhance if the link has a target attribute (e.g., target="_blank")
    if (anchor.target.isNotEmpty()) {
        return false
    }

    // Don't enhance if the link is a download
    if (anchor.hasAttribute("download")) {
        return false
    }

    // Don't enhance if click has modifiers (ctrl, cmd, shift, alt)
    if (event.ctrlKey == true || event.metaKey == true || event.shiftKey == true || event.altKey == true) {
        return false
    }

    // Don't enhance if it's not a left-click (button 0 is left-click)
    if (event.button != 0) {
        return false
    }

    // Don't enhance if the URL is a static asset
    val href = anchor.href
    if (href.contains("/static/")) {
        return false
    }

    // Only enhance internal docs links (checks for same-origin and excludes /_examples)
    return isInternalDocsLink(anchor)
}

/**
 * Fetches and renders partial content for the given [url], updating only the
 * main content area and the browser URL without a full page reload.
 */
fun navigateToPage(
    url: String,
    pushState: Boolean = true,
) {
    val resolvedUrl = urlWithSoftPageVariantPreference(url)
    val partialUrl =
        if (resolvedUrl.contains("?")) {
            "$resolvedUrl&partial=true"
        } else {
            "$resolvedUrl?partial=true"
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
                // Parse the partial HTML to extract title and content
                val parser = DOMParser()
                val doc = parser.parseFromString(html, "text/html")

                // Extract title from the partial content if present
                val titleElement = doc.querySelector("h1")
                if (titleElement != null) {
                    document.title = "Mosaik UI · ${titleElement.textContent}"
                }

                // Replace main content
                mainContent.innerHTML = html

                // Update active sidebar item
                updateActiveSidebarItem(resolvedUrl)

                // Update browser history
                if (pushState) {
                    window.history.pushState(null, "", resolvedUrl)
                }

                // Re-run syntax highlighting on the new content
                js("if (typeof hljs !== 'undefined') hljs.highlightAll();")

                // Rehydrate interactive previews after shell content swap.
                rehydrateInteractivePreviews()

                // Sync preference controls after shell content swap.
                syncPreferenceControls()

                // Focus on the main content area for accessibility
                mainContent.setAttribute("tabindex", "-1")
                mainContent.asDynamic().focus()

                // Scroll to top after navigation
                window.scrollTo(0.0, 0.0)
            } else {
                // Fallback: no main-content element found, do a normal navigation
                window.location.href = url
            }
        }.catch { error ->
            console.error("Navigation error:", error)
            // Fallback to normal navigation on error
            window.location.href = url
        }
}

/**
 * Updates the active sidebar menu item to match the current [url].
 */
fun urlWithSoftPageVariantPreference(url: String): String {
    val parsed = URL(url, window.location.origin)
    if (parsed.pathname == "/components/button" && !parsed.searchParams.has("variant")) {
        val preferred = window.localStorage.getItem("mosaik-interaction-style")
        if (preferred == "htmx" || preferred == "alpine" || preferred == "datastar") {
            parsed.searchParams.set("variant", preferred)
        }
    }
    return parsed.href
}

fun updateActiveSidebarItem(url: String) {
    val currentPath = URL(url).pathname.trimEnd('/')

    document.querySelectorAll(".menu a").asList().forEach { link ->
        if (link is HTMLAnchorElement) {
            val linkPath = URL(link.href).pathname.trimEnd('/')
            if (linkPath == currentPath) {
                link.classList.add("menu-active")
            } else {
                link.classList.remove("menu-active")
            }
        }
    }
}

/**
 * Rehydrates interactive previews after shell content swaps. This ensures that
 * Alpine.js and Datastar properly initialize on dynamically loaded content.
 *
 * htmx automatically handles new content via its MutationObserver, so no manual
 * intervention is needed. Alpine.js requires calling Alpine.initTree() on new
 * elements with x-data. Datastar uses the Datastar.load() API to apply plugins
 * to new content where supported.
 */
fun rehydrateInteractivePreviews() {
    // Alpine.js rehydration: call initTree() on the main content element to initialize all children
    js(
        "if (typeof Alpine !== 'undefined' && Alpine.initTree) { " +
            "var mainContent = document.getElementById('main-content'); " +
            "if (mainContent) { Alpine.initTree(mainContent); } " +
            "}",
    )

    // Datastar rehydration: check if Datastar.load() is available
    js(
        "if (typeof Datastar !== 'undefined' && Datastar.load) { " +
            "var mainContent = document.getElementById('main-content'); " +
            "if (mainContent) Datastar.load(mainContent); " +
            "}",
    )
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
