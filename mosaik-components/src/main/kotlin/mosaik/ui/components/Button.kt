package mosaik.ui.components

import kotlinx.html.*

/**
 * The DaisyUI button default/no-color variant plus color role variants.
 *
 * [Default] represents DaisyUI's plain button styling and emits no `btn-*`
 * color class. Per ADR-0010 this is a component-specific enum rather than the
 * shared [Variant]: Button's color vocabulary excludes ghost and link, which
 * are styles (see [ButtonStyle]), not color roles. The enum lives here, next
 * to [mButton], not in Theme.kt.
 */
enum class ButtonVariant(
    val token: String,
) {
    Default(""),
    Neutral("neutral"),
    Primary("primary"),
    Secondary("secondary"),
    Accent("accent"),
    Info("info"),
    Success("success"),
    Warning("warning"),
    Error("error"),
}

private fun ButtonVariant.cssClass(): String? = token.takeIf { it.isNotBlank() }?.let { "btn-$it" }

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
    variant: ButtonVariant = ButtonVariant.Default,
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
                variant.cssClass(),
                style?.let { "btn-${it.token}" },
                shape?.let { "btn-${it.token}" },
                width?.let { "btn-${it.token}" },
                size.token?.let { "btn-$it" },
                classes,
            ),
        block = block,
    )
}

/**
 * A DaisyUI button rendered as an anchor (`<a>`), combining navigational
 * semantics with button styling. Use this for navbar links, tabs, or other
 * navigational UI that should look like a button while preserving anchor
 * behavior (href, target, rel).
 *
 * Per ADR-0003 and ADR-0013, this hides the DaisyUI `btn` token behind a
 * type-safe API while exposing the raw kotlinx.html [A] element, so HTML
 * attributes and third-party extensions (e.g. htmx, Alpine) work natively.
 *
 * ```kotlin
 * mButtonLink(
 *     href = "/docs",
 *     style = ButtonStyle.Ghost,
 *     classes = "text-xl",
 * ) {
 *     hxBoost = "true"   // htmx extension — works natively
 *     +"Documentation"
 * }
 * ```
 */
fun FlowContent.mButtonLink(
    href: String,
    variant: ButtonVariant = ButtonVariant.Default,
    style: ButtonStyle? = null,
    shape: ButtonShape? = null,
    width: ButtonWidth? = null,
    size: Size = Size.Md,
    classes: String? = null,
    block: A.() -> Unit = {},
) {
    a(
        href = href,
        classes =
            buildClasses(
                "btn",
                variant.cssClass(),
                style?.let { "btn-${it.token}" },
                shape?.let { "btn-${it.token}" },
                width?.let { "btn-${it.token}" },
                size.token?.let { "btn-$it" },
                classes,
            ),
        block = block,
    )
}
