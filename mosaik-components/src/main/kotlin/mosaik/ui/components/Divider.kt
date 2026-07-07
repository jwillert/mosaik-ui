package mosaik.ui.components

import kotlinx.html.*

enum class DividerOrientation(
    val token: String?,
) {
    Vertical(null),
    Horizontal("horizontal"),
}

enum class DividerPlacement(
    val token: String?,
) {
    Center(null),
    Start("start"),
    End("end"),
}

enum class DividerColor(
    val token: String,
) {
    Neutral("neutral"),
    Primary("primary"),
    Secondary("secondary"),
    Accent("accent"),
    Success("success"),
    Warning("warning"),
    Info("info"),
    Error("error"),
}

fun FlowContent.mDivider(
    orientation: DividerOrientation = DividerOrientation.Vertical,
    placement: DividerPlacement = DividerPlacement.Center,
    color: DividerColor? = null,
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(
        classes =
            buildClasses(
                "divider",
                orientation.token?.let { "divider-$it" },
                placement.token?.let { "divider-$it" },
                color?.token?.let { "divider-$it" },
                classes,
            ),
        block = block,
    )
}
