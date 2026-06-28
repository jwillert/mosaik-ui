package mosaik.ui.components

import kotlinx.html.*

/**
 * DSL context for [mFooter]. Delegates to the underlying [FOOTER] through
 * [FlowContent], so ordinary HTML and Mosaik's [FlowContent] extensions
 * ([mFooterTitle], [mLink]) work natively. Common HTML attributes ([id],
 * [style], [title]) are exposed as properties; other attributes are accessible
 * via [attributes]. Marked with [MosaikDsl] for consistency with other compound
 * components and to support potential future footer-scoped child functions.
 */
@MosaikDsl
class MFooter(
    private val footer: FOOTER,
) : FlowContent by footer {
    var id: String
        get() = footer.id
        set(value) {
            footer.id = value
        }

    var style: String
        get() = footer.style
        set(value) {
            footer.style = value
        }

    var title: String
        get() = footer.title
        set(value) {
            footer.title = value
        }

    override val attributes: MutableMap<String, String>
        get() = footer.attributes
}

/**
 * A DaisyUI footer, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * a [MFooter] context that delegates to the raw kotlinx.html [FOOTER], so any
 * HTML attribute or third-party extension (e.g. htmx) works natively. Like Card
 * and Navbar, a footer has no colour [Variant] and no [Size] — it is a layout
 * container styled by the utility classes the caller passes via [classes]
 * (e.g. `footer-center bg-base-200 p-4`).
 *
 * Mosaik provides [mFooterTitle] for hiding the `footer-title` DaisyUI class
 * token and [mLink] for hiding the `link link-hover` class tokens, per the
 * domain glossary's DaisyUI Class Token constraint. These functions are usable
 * anywhere in kotlinx.html (including inside `nav` blocks within the footer).
 *
 * ```kotlin
 * mFooter("bg-base-200 text-base-content p-10") {
 *     nav {
 *         mFooterTitle { +"Services" }
 *         mLink(href = "/branding") { +"Branding" }
 *         mLink(href = "/design") { +"Design" }
 *     }
 * }
 * ```
 */
fun FlowContent.mFooter(
    classes: String? = null,
    block: MFooter.() -> Unit = {},
) {
    footer(classes = buildClasses("footer", classes)) {
        MFooter(this).block()
    }
}

/**
 * A footer title — an `h6` element with the `footer-title` DaisyUI class.
 * Usable anywhere in a kotlinx.html flow (typically inside a footer's `nav`
 * column as the first element to label a group of links). Hides the
 * `footer-title` DaisyUI class token per the domain glossary constraint.
 * [block] receives the raw [H6].
 */
fun FlowContent.mFooterTitle(
    classes: String? = null,
    block: H6.() -> Unit = {},
) {
    h6(classes = buildClasses("footer-title", classes), block = block)
}

/**
 * A link with the `link link-hover` DaisyUI classes, usable anywhere in a
 * kotlinx.html flow. Hides the `link` and `link-hover` DaisyUI class tokens per
 * the domain glossary constraint. [href] is required per standard HTML link
 * semantics. [block] receives the raw [A].
 */
fun FlowContent.mLink(
    href: String,
    classes: String? = null,
    block: A.() -> Unit = {},
) {
    a(href = href, classes = buildClasses("link link-hover", classes), block = block)
}
