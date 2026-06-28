package mosaik.ui.components

import kotlinx.html.*

/**
 * DSL context for [mMenu]. Wraps the menu's [UL] and exposes [mMenuItem] and
 * [mMenuTitle]. Marked with [MosaikDsl] so child components are menu-specific.
 * Implements [FlowContent] to allow ordinary HTML content.
 */
@MosaikDsl
class MMenu(
    internal val ul: UL,
) : FlowContent by ul {
    var id: String
        get() = ul.id
        set(value) {
            ul.id = value
        }

    override val attributes: MutableMap<String, String>
        get() = ul.attributes
}

internal val MMenu.underlying: UL get() = ul

/**
 * A DaisyUI menu, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * a [MMenu] context wrapping the underlying [UL], so any HTML attribute or
 * third-party extension (e.g. htmx) works natively via delegation. DaisyUI
 * menus are navigation containers, styled by the utility classes the caller
 * passes via [classes] (e.g. `w-full`).
 *
 * A menu composes from sub-components scoped to its context — [mMenuItem] for
 * clickable navigation links and [mMenuTitle] for section headers. The
 * [MosaikDsl] marker constrains where each child can be called.
 *
 * ```kotlin
 * mMenu("w-full") {
 *     mMenuItem("/", active = true) { +"Home" }
 *     mMenuTitle { +"Components" }
 *     mMenuItem("/components/button") { +"Button" }
 *     mMenuItem("/components/card") { +"Card" }
 * }
 * ```
 */
fun FlowContent.mMenu(
    classes: String? = null,
    block: MMenu.() -> Unit = {},
) {
    ul(classes = buildClasses("menu", classes)) {
        MMenu(this).block()
    }
}

/**
 * A menu item with a clickable link. Only callable from [MMenu] context due to
 * [MosaikDsl]. The [active] parameter controls whether the `menu-active` class
 * is applied to indicate the current page. [block] receives the raw [A].
 */
fun MMenu.mMenuItem(
    href: String,
    active: Boolean = false,
    block: A.() -> Unit = {},
) {
    underlying.li {
        a(href = href, classes = if (active) "menu-active" else null, block = block)
    }
}

/**
 * A menu section title. Only callable from [MMenu] context due to [MosaikDsl].
 * [block] receives the raw [LI] so text and elements can be added directly.
 */
fun MMenu.mMenuTitle(
    classes: String? = null,
    block: LI.() -> Unit = {},
) {
    underlying.li(classes = buildClasses("menu-title", classes), block = block)
}
