package mosaik.ui.components

import kotlinx.html.*

/**
 * A DaisyUI navbar, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * the raw kotlinx.html [DIV], so any HTML attribute or third-party extension
 * (e.g. htmx) works natively. Like Card, a navbar has no colour [Variant] and
 * no [Size] — it is a layout container styled by the utility classes the caller
 * passes via [classes] (e.g. `bg-base-100 shadow-sm`).
 *
 * A navbar composes from three slot sub-components scoped to its [DIV] receiver
 * — [mNavbarStart], [mNavbarCenter], and [mNavbarEnd] — mirroring DaisyUI's
 * `navbar-start` / `navbar-center` / `navbar-end` structure. Because they are
 * extensions on the raw element, IDE autocomplete surfaces them inside the
 * navbar block.
 *
 * ```kotlin
 * mNavbar("bg-base-100 shadow-sm") {
 *     mNavbarStart {
 *         a(classes = "btn btn-ghost text-xl") { +"Mosaik" }
 *     }
 *     mNavbarEnd {
 *         mButton(Variant.Primary) { +"Sign up" }
 *     }
 * }
 * ```
 */
fun FlowContent.mNavbar(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("navbar", classes), block = block)
}

/**
 * The `navbar-start` slot of a [mNavbar] — the leading section, typically the
 * brand or a dropdown menu. Scoped to the navbar's [DIV] receiver so it only
 * autocompletes inside a navbar block. [block] receives the raw [DIV].
 */
fun DIV.mNavbarStart(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("navbar-start", classes), block = block)
}

/**
 * The `navbar-center` slot of a [mNavbar] — the centred section, typically the
 * title or primary navigation links. Scoped to the navbar's [DIV] receiver.
 * [block] receives the raw [DIV].
 */
fun DIV.mNavbarCenter(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("navbar-center", classes), block = block)
}

/**
 * The `navbar-end` slot of a [mNavbar] — the trailing section, typically actions
 * such as buttons or an avatar. Scoped to the navbar's [DIV] receiver. [block]
 * receives the raw [DIV].
 */
fun DIV.mNavbarEnd(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("navbar-end", classes), block = block)
}
