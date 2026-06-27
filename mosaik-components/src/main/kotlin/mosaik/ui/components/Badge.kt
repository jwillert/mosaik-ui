package mosaik.ui.components

import kotlinx.html.*

/**
 * The DaisyUI colour roles a badge supports.
 *
 * Per ADR-0004 this is a component-specific enum rather than the shared
 * [Variant]: DaisyUI's badge palette adds `info`, `neutral`, and `outline`,
 * which the shared enum has no use for, while declaring exactly these ten keeps
 * the compiler from accepting a role the badge can't render. The enum lives here,
 * next to [mBadge], not in Theme.kt.
 */
enum class BadgeVariant(
    val token: String,
) {
    Primary("primary"),
    Secondary("secondary"),
    Accent("accent"),
    Ghost("ghost"),
    Info("info"),
    Success("success"),
    Warning("warning"),
    Error("error"),
    Neutral("neutral"),
    Outline("outline"),
}

/**
 * A DaisyUI badge, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * the raw kotlinx.html [SPAN], so any HTML attribute or third-party extension
 * (e.g. htmx) works natively. The colour role is a [BadgeVariant] (ADR-0004),
 * not the shared [Variant], so only roles the badge actually renders are
 * offered.
 *
 * ```kotlin
 * mBadge(BadgeVariant.Success, Size.Sm) {
 *     +"Active"
 * }
 * ```
 */
fun FlowContent.mBadge(
    variant: BadgeVariant = BadgeVariant.Primary,
    size: Size = Size.Md,
    classes: String? = null,
    block: SPAN.() -> Unit = {},
) {
    span(
        classes =
            buildClasses(
                "badge",
                "badge-${variant.token}",
                size.token?.let { "badge-$it" },
                classes,
            ),
        block = block,
    )
}
