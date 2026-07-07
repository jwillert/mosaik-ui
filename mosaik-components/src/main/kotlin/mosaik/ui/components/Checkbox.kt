package mosaik.ui.components

import kotlinx.html.*

enum class CheckboxVariant(
    val token: String,
) {
    Primary("primary"),
    Secondary("secondary"),
    Accent("accent"),
    Neutral("neutral"),
    Success("success"),
    Warning("warning"),
    Info("info"),
    Error("error"),
}

fun FlowContent.mCheckbox(
    variant: CheckboxVariant? = null,
    size: Size = Size.Md,
    indeterminate: Boolean = false,
    classes: String? = null,
    block: INPUT.() -> Unit = {},
) {
    input(
        type = InputType.checkBox,
        classes =
            buildClasses(
                "checkbox",
                variant?.token?.let { "checkbox-$it" },
                size.token?.let { "checkbox-$it" },
                if (indeterminate) "checkbox-indeterminate" else null,
                classes,
            ),
        block = block,
    )
}
