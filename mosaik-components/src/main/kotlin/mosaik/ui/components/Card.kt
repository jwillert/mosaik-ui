package mosaik.ui.components

import kotlinx.html.*

/**
 * DSL context for [mCard]. Wraps the card's [DIV] and exposes [mCardBody].
 * Marked with [MosaikDsl] so child components are slot-specific. Implements
 * [FlowContent] to allow ordinary HTML content.
 */
@MosaikDsl
class MCard(
    internal val div: DIV,
) : FlowContent by div {
    var id: String
        get() = div.id
        set(value) {
            div.id = value
        }

    override val attributes: MutableMap<String, String>
        get() = div.attributes
}

/**
 * DSL context for [mCardBody]. Wraps the card body's [DIV] and exposes
 * [mCardTitle] and [mCardActions]. Marked with [MosaikDsl] so child
 * components are slot-specific. Implements [FlowContent] to allow ordinary
 * HTML content.
 */
@MosaikDsl
class MCardBody(
    internal val div: DIV,
) : FlowContent by div {
    override val attributes: MutableMap<String, String>
        get() = div.attributes
}

internal val MCard.underlying: DIV get() = div
internal val MCardBody.underlying: DIV get() = div

/**
 * A DaisyUI card, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * a [MCard] context wrapping the underlying [DIV], so any HTML attribute or
 * third-party extension (e.g. htmx) works natively via delegation. Unlike
 * Button or Badge, a card has no colour [Variant] and no [Size] — DaisyUI
 * cards are layout containers, styled by the utility classes the caller passes
 * via [classes] (e.g. `bg-base-100 shadow-sm`).
 *
 * A card composes from sub-components scoped to its context — [mCardBody],
 * and within the body [mCardTitle] and [mCardActions] — mirroring DaisyUI's
 * `card-body` / `card-title` / `card-actions` structure. The [MosaikDsl]
 * marker constrains where each child can be called, so [mCardBody] is only
 * callable from [mCard], and [mCardTitle]/[mCardActions] are only callable
 * from [mCardBody]. A `<figure>` for an image is plain kotlinx.html — no
 * wrapper needed.
 *
 * ```kotlin
 * mCard("w-96 bg-base-100 shadow-sm") {
 *     figure { img(src = "/shoes.jpg", alt = "Shoes") }
 *     mCardBody {
 *         mCardTitle { +"Shoes!" }
 *         p { +"If a dog chews shoes whose shoes does he choose?" }
 *         mCardActions("justify-end") {
 *             mButton(Variant.Primary) { +"Buy Now" }
 *         }
 *     }
 * }
 * ```
 */
fun FlowContent.mCard(
    classes: String? = null,
    block: MCard.() -> Unit = {},
) {
    div(classes = buildClasses("card", classes)) {
        MCard(this).block()
    }
}

/**
 * The `card-body` section of a [mCard] — the padded container holding the title,
 * text, and actions. Only callable from [MCard] context due to [MosaikDsl].
 * [block] receives a [MCardBody] context wrapping the underlying [DIV].
 */
fun MCard.mCardBody(
    classes: String? = null,
    block: MCardBody.() -> Unit = {},
) {
    underlying.div(classes = buildClasses("card-body", classes)) {
        MCardBody(this).block()
    }
}

/**
 * The `card-title` of a [mCard], rendered as an `<h2>` to match DaisyUI's
 * documented markup. Only callable from [MCardBody] context due to [MosaikDsl].
 * [block] receives the raw [H2].
 */
fun MCardBody.mCardTitle(
    classes: String? = null,
    block: H2.() -> Unit = {},
) {
    underlying.h2(classes = buildClasses("card-title", classes), block = block)
}

/**
 * The `card-actions` row of a [mCard] — a container for buttons or controls,
 * usually with `justify-end`. Only callable from [MCardBody] context due to
 * [MosaikDsl]. [block] receives the raw [DIV].
 */
fun MCardBody.mCardActions(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    underlying.div(classes = buildClasses("card-actions", classes), block = block)
}
