package mosaik.ui.components

import kotlinx.html.*

/**
 * The DaisyUI button color role variants.
 *
 * Per ADR-0010 this is a component-specific enum rather than the shared
 * [Variant]: Button's color vocabulary excludes ghost and link, which are
 * styles (see [ButtonStyle]), not color roles. The enum lives here, next to
 * [mButton], not in Theme.kt.
 */
enum class ButtonVariant(
    val token: String,
) {
    Neutral("neutral"),
    Primary("primary"),
    Secondary("secondary"),
    Accent("accent"),
    Info("info"),
    Success("success"),
    Warning("warning"),
    Error("error"),
}

/**
 * The DaisyUI button style modifiers.
 *
 * These are orthogonal to [ButtonVariant] and can be combined with color roles.
 * Ghost and link are represented here as styles, not color variants per ADR-0010.
 */
enum class ButtonStyle(
    val token: String,
) {
    Outline("outline"),
    Ghost("ghost"),
    Link("link"),
}

/**
 * The DaisyUI button shape modifiers.
 */
enum class ButtonShape(
    val token: String,
) {
    Circle("circle"),
    Square("square"),
}

/**
 * The DaisyUI button width modifiers.
 */
enum class ButtonWidth(
    val token: String,
) {
    Wide("wide"),
    Block("block"),
}

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
 * mButton(
 *     variant = ButtonVariant.Primary,
 *     style = ButtonStyle.Outline,
 *     size = Size.Lg,
 * ) {
 *     hxGet = "/save"   // htmx extension — works natively
 *     disabled = true
 *     +"Save"
 * }
 * ```
 */
fun FlowContent.mButton(
    variant: ButtonVariant = ButtonVariant.Neutral,
    style: ButtonStyle? = null,
    shape: ButtonShape? = null,
    width: ButtonWidth? = null,
    size: Size = Size.Md,
    classes: String? = null,
    block: BUTTON.() -> Unit = {},
) {
    button(
        classes =
            buildClasses(
                "btn",
                "btn-${variant.token}",
                style?.let { "btn-${it.token}" },
                shape?.let { "btn-${it.token}" },
                width?.let { "btn-${it.token}" },
                size.token?.let { "btn-$it" },
                classes,
            ),
        block = block,
    )
}
