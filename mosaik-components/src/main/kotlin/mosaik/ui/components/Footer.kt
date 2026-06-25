package mosaik.ui.components

import kotlinx.html.*

/**
 * A DaisyUI footer, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * the raw kotlinx.html [FOOTER], so any HTML attribute or third-party extension
 * (e.g. htmx) works natively. Like Card and Navbar, a footer has no colour
 * [Variant] and no [Size] — it is a layout container styled by the utility
 * classes the caller passes via [classes] (e.g. `footer-center bg-base-200 p-4`).
 *
 * Footer has no sub-components: its content is plain kotlinx.html. DaisyUI's
 * `footer-title` is just a utility class on a heading, applied via the normal
 * `classes` argument of the element.
 *
 * ```kotlin
 * mFooter("footer-center bg-base-200 text-base-content p-4") {
 *     aside {
 *         p { +"© 2026 Mosaik UI — built with Kotlin, Ktor and DaisyUI." }
 *     }
 * }
 * ```
 */
fun FlowContent.mFooter(
    classes: String? = null,
    block: FOOTER.() -> Unit = {},
) {
    footer(classes = buildClasses("footer", classes), block = block)
}
