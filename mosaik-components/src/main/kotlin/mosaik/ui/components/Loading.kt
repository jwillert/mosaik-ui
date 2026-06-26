package mosaik.ui.components

import kotlinx.html.*

/**
 * The DaisyUI loading indicator types.
 *
 * Per ADR-0004 this is a component-specific enum rather than the shared
 * [Variant]: DaisyUI's loading types are purely presentational without color
 * roles, so a dedicated enum keeps the type system from accepting values the
 * component can't render. The enum lives here, next to [mLoading], not in
 * Theme.kt.
 */
enum class LoadingType(val token: String) {
    Spinner("spinner"),
    Dots("dots"),
    Ring("ring"),
    Ball("ball"),
    Bars("bars"),
    Infinity("infinity"),
}

/**
 * A DaisyUI loading indicator, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * the raw kotlinx.html [SPAN], so any HTML attribute or third-party extension
 * (e.g. htmx) works natively. Loading is composable content, not a button
 * modifier — it can appear before text, after text, or standalone.
 *
 * ```kotlin
 * mButton(Variant.Primary) {
 *     mLoading(LoadingType.Spinner, Size.Sm, "mr-2")
 *     +"Processing..."
 * }
 * ```
 */
fun FlowContent.mLoading(
    type: LoadingType = LoadingType.Spinner,
    size: Size = Size.Md,
    classes: String? = null,
    block: SPAN.() -> Unit = {},
) {
    span(
        classes = buildClasses(
            "loading",
            "loading-${type.token}",
            size.token?.let { "loading-$it" },
            classes,
        ),
        block = block,
    )
}
