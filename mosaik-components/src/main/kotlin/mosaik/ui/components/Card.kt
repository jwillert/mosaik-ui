package mosaik.ui.components

import kotlinx.html.*

/**
 * A DaisyUI card, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * the raw kotlinx.html [DIV], so any HTML attribute or third-party extension
 * (e.g. htmx) works natively. Unlike Button or Badge, a card has no colour
 * [Variant] and no [Size] — DaisyUI cards are layout containers, styled by the
 * utility classes the caller passes via [classes] (e.g. `bg-base-100 shadow-sm`).
 *
 * A card composes from sub-components scoped to its [DIV] receiver — [mCardBody],
 * and within the body [mCardTitle] and [mCardActions] — mirroring DaisyUI's
 * `card-body` / `card-title` / `card-actions` structure. Because they are
 * extensions on the raw element, IDE autocomplete surfaces them inside the
 * card block. A `<figure>` for an image is plain kotlinx.html — no wrapper needed.
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
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("card", classes), block = block)
}

/**
 * The `card-body` section of a [mCard] — the padded container holding the title,
 * text, and actions. Scoped to the card's [DIV] receiver so it only autocompletes
 * inside a card block. [block] receives the raw [DIV].
 */
fun DIV.mCardBody(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("card-body", classes), block = block)
}

/**
 * The `card-title` of a [mCard], rendered as an `<h2>` to match DaisyUI's
 * documented markup. Called inside [mCardBody]; [block] receives the raw [H2].
 */
fun DIV.mCardTitle(
    classes: String? = null,
    block: H2.() -> Unit = {},
) {
    h2(classes = buildClasses("card-title", classes), block = block)
}

/**
 * The `card-actions` row of a [mCard] — a container for buttons or controls,
 * usually with `justify-end`. Called inside [mCardBody]; [block] receives the
 * raw [DIV].
 */
fun DIV.mCardActions(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("card-actions", classes), block = block)
}
