package mosaik.ui.components

import kotlinx.html.*

/**
 * DSL context for [mNavbar]. Provides access to navbar slot functions
 * ([mNavbarStart], [mNavbarCenter], [mNavbarEnd]) and ordinary HTML through
 * [FlowContent]. HTML attributes ([id]) delegate to the underlying [DIV].
 * Marked with [MosaikDsl] to prevent slot functions from leaking into nested
 * ordinary HTML blocks.
 */
@MosaikDsl
class MNavbar(private val div: DIV) : FlowContent by div {
    var id: String
        get() = div.id
        set(value) { div.id = value }
}

/**
 * A DaisyUI navbar, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * a [MNavbar] context that delegates to the raw kotlinx.html [DIV], so any
 * HTML attribute or third-party extension (e.g. htmx) works natively. Like
 * Card, a navbar has no colour [Variant] and no [Size] — it is a layout
 * container styled by the utility classes the caller passes via [classes]
 * (e.g. `bg-base-100 shadow-sm`).
 *
 * A navbar composes from three slot sub-components scoped to its [MNavbar]
 * context — [mNavbarStart], [mNavbarCenter], and [mNavbarEnd] — mirroring
 * DaisyUI's `navbar-start` / `navbar-center` / `navbar-end` structure. The
 * [MosaikDsl] marker on [MNavbar] ensures slot functions are callable only
 * inside the navbar block and do not leak into nested ordinary HTML blocks.
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
    block: MNavbar.() -> Unit = {},
) {
    div(classes = buildClasses("navbar", classes)) {
        MNavbar(this).block()
    }
}

/**
 * The `navbar-start` slot of a [mNavbar] — the leading section, typically the
 * brand or a dropdown menu. Scoped to the [MNavbar] context so it only
 * autocompletes inside a navbar block. [block] receives the raw [DIV].
 */
fun MNavbar.mNavbarStart(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("navbar-start", classes), block = block)
}

/**
 * The `navbar-center` slot of a [mNavbar] — the centred section, typically the
 * title or primary navigation links. Scoped to the [MNavbar] context so it
 * only autocompletes inside a navbar block. [block] receives the raw [DIV].
 */
fun MNavbar.mNavbarCenter(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("navbar-center", classes), block = block)
}

/**
 * The `navbar-end` slot of a [mNavbar] — the trailing section, typically actions
 * such as buttons or an avatar. Scoped to the [MNavbar] context so it only
 * autocompletes inside a navbar block. [block] receives the raw [DIV].
 */
fun MNavbar.mNavbarEnd(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("navbar-end", classes), block = block)
}
