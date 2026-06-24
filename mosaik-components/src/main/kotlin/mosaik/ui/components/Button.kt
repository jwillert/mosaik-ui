package mosaik.ui.components

import kotlinx.html.*

/**
 * A DaisyUI button, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * the raw kotlinx.html [BUTTON]. This keeps the call site terse for the common
 * tokens while exposing the element directly, so any extension property a
 * third-party library adds to [BUTTON] (e.g. htmx's `hxGet`/`hxTarget`) is
 * usable natively — Kotlin extension resolution can't see members of a wrapper
 * scope class, but it can see extensions on the real receiver.
 *
 * ```kotlin
 * mButton(Variant.Primary, Size.Lg) {
 *     hxGet = "/save"   // htmx extension — works natively
 *     disabled = true
 *     +"Save"
 * }
 * ```
 */
fun FlowContent.mButton(
    variant: Variant = Variant.Primary,
    size: Size = Size.Md,
    classes: String? = null,
    block: BUTTON.() -> Unit = {},
) {
    button(
        classes = buildClasses(
            "btn",
            "btn-${variant.token}",
            size.token?.let { "btn-$it" },
            classes,
        ),
        block = block,
    )
}
