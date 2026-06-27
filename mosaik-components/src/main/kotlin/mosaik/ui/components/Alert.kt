package mosaik.ui.components

import kotlinx.html.*

/**
 * The DaisyUI colour roles an alert supports.
 *
 * Per ADR-0004 this is a component-specific enum rather than the shared
 * [Variant]: a DaisyUI alert only renders the four feedback roles below, so
 * accepting `Ghost`, `Link`, or `Accent` would compile yet produce no visual
 * distinction — a silent bug. Declaring exactly these four lets the compiler
 * reject a role the alert can't render. The enum lives here, next to [mAlert],
 * not in Theme.kt.
 */
enum class AlertVariant(
    val token: String,
) {
    Info("info"),
    Success("success"),
    Warning("warning"),
    Error("error"),
}

/**
 * A DaisyUI alert, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * the raw kotlinx.html [DIV], so any HTML attribute or third-party extension
 * (e.g. htmx) works natively. The colour role is an [AlertVariant] (ADR-0004),
 * not the shared [Variant], so only the four feedback roles the alert renders
 * are offered. Unlike Button or Badge, an alert takes no [Size] — DaisyUI
 * alerts have no size modifiers.
 *
 * The element carries `role="alert"` by default, matching DaisyUI's documented
 * markup so assistive tech announces the message. The [block] runs after this
 * default is set, so it can override the role (e.g. `role="status"`) when a
 * less assertive announcement is wanted.
 *
 * ```kotlin
 * mAlert(AlertVariant.Success) {
 *     +"Saved"
 * }
 * ```
 */
fun FlowContent.mAlert(
    variant: AlertVariant = AlertVariant.Info,
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(
        classes =
            buildClasses(
                "alert",
                "alert-${variant.token}",
                classes,
            ),
    ) {
        attributes["role"] = "alert"
        block()
    }
}
