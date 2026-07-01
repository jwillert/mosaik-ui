package mosaik.ui.components

import kotlinx.html.*

/**
 * The DaisyUI colour roles a file input supports.
 *
 * Per ADR-0004 this is a component-specific enum rather than the shared
 * [Variant], because file inputs support neutral and info roles but not the
 * full shared palette.
 */
enum class FileInputVariant(
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
 * A DaisyUI form control container, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * the raw kotlinx.html [LABEL]. The form-control class provides consistent
 * spacing and layout for form fields. Typically wraps [mLabel], [mLabelText],
 * and [mInput] or [mSelect].
 *
 * ```kotlin
 * mFormControl(classes = "w-full") {
 *     mLabel {
 *         mLabelText { +"Email" }
 *     }
 *     mInput(type = InputType.email) {
 *         name = "email"
 *         required = true
 *     }
 * }
 * ```
 */
fun FlowContent.mFormControl(
    classes: String? = null,
    block: LABEL.() -> Unit = {},
) {
    label(
        classes = buildClasses("form-control", classes),
        block = block,
    )
}

/**
 * A DaisyUI label container for form field labels.
 *
 * Renders a `div` with the `label` class. Typically contains [mLabelText] for
 * the label content. The block receives the raw kotlinx.html [DIV] so all HTML
 * attributes work natively.
 *
 * ```kotlin
 * mLabel {
 *     mLabelText { +"Username" }
 * }
 * ```
 */
fun FlowContent.mLabel(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(
        classes = buildClasses("label", classes),
        block = block,
    )
}

/**
 * A DaisyUI label text element for form field labels.
 *
 * Renders a `span` with the `label-text` class. Used inside [mLabel] to provide
 * the visible label text. The block receives the raw kotlinx.html [SPAN] so all
 * HTML attributes work natively.
 *
 * ```kotlin
 * mLabelText { +"Password" }
 * ```
 */
fun FlowContent.mLabelText(
    classes: String? = null,
    block: SPAN.() -> Unit = {},
) {
    span(
        classes = buildClasses("label-text", classes),
        block = block,
    )
}

/**
 * A DaisyUI input element, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * the raw kotlinx.html [INPUT]. This keeps the call site terse for the common
 * tokens while exposing the element directly, so any extension property a
 * third-party library adds to [INPUT] (e.g. htmx's `hxGet`/`hxTarget`) is
 * usable natively — Kotlin extension resolution can't see members of a wrapper
 * scope class, but it can see extensions on the real receiver.
 *
 * ```kotlin
 * mInput(type = InputType.email, size = Size.Lg) {
 *     name = "email"
 *     required = true
 *     placeholder = "user@example.com"
 *     attributes["x-model"] = "email"
 * }
 * ```
 */
fun FlowContent.mInput(
    type: InputType = InputType.text,
    bordered: Boolean = true,
    size: Size = Size.Md,
    classes: String? = null,
    block: INPUT.() -> Unit = {},
) {
    input(
        type = type,
        classes =
            buildClasses(
                "input",
                if (bordered) "input-bordered" else null,
                size.token?.let { "input-$it" },
                classes,
            ),
        block = block,
    )
}

/**
 * A DaisyUI select element, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * the raw kotlinx.html [SELECT]. This keeps the call site terse for the common
 * tokens while exposing the element directly, so any extension property a
 * third-party library adds to [SELECT] (e.g. htmx's `hxGet`/`hxTarget`) is
 * usable natively — Kotlin extension resolution can't see members of a wrapper
 * scope class, but it can see extensions on the real receiver.
 *
 * ```kotlin
 * mSelect(size = Size.Sm, classes = "w-full") {
 *     name = "theme"
 *     option { value = "light"; +"Light" }
 *     option { value = "dark"; +"Dark" }
 * }
 * ```
 */
fun FlowContent.mSelect(
    bordered: Boolean = true,
    size: Size = Size.Md,
    classes: String? = null,
    block: SELECT.() -> Unit = {},
) {
    select(
        classes =
            buildClasses(
                "select",
                if (bordered) "select-bordered" else null,
                size.token?.let { "select-$it" },
                classes,
            ),
        block = block,
    )
}

/**
 * A DaisyUI file input element, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * the raw kotlinx.html [INPUT]. Callers can set native file-upload attributes
 * such as `name`, `required`, `accept`, `multiple`, and any third-party
 * extension attributes directly on the element.
 *
 * ```kotlin
 * mFileInput(variant = FileInputVariant.Secondary, classes = "w-full") {
 *     name = "avatar"
 *     attributes["accept"] = "image/png,image/jpeg"
 * }
 * ```
 */
fun FlowContent.mFileInput(
    variant: FileInputVariant? = null,
    bordered: Boolean = true,
    size: Size = Size.Md,
    classes: String? = null,
    block: INPUT.() -> Unit = {},
) {
    input(
        type = InputType.file,
        classes =
            buildClasses(
                "file-input",
                if (bordered) "file-input-bordered" else null,
                variant?.token?.let { "file-input-$it" },
                size.token?.let { "file-input-$it" },
                classes,
            ),
        block = block,
    )
}
