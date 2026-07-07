package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.CheckboxVariant
import mosaik.ui.components.Size
import mosaik.ui.components.mCheckbox

fun FlowContent.checkboxPageContent() {
    h1 { +"Checkbox" }
    p {
        +"Checkboxes let users select one or more independent options. "
        code { +"mCheckbox" }
        +" renders a native checkbox input while exposing colour, size, and "
        +"indeterminate styling as type-safe parameters."
    }

    installSection("checkbox")

    h2 { +"Basic usage" }
    exampleCard(
        code =
            """
            import mosaik.ui.components.mCheckbox

            label("flex items-center gap-2") {
                mCheckbox { name = "terms" }
                span { +"I agree to the terms" }
            }
            """.trimIndent(),
    ) {
        label("flex items-center gap-2") {
            mCheckbox { name = "terms" }
            span { +"I agree to the terms" }
        }
    }

    section {
        h2 { +"Variants" }
        exampleCard(
            code =
                """
                CheckboxVariant.entries.forEach { variant ->
                    mCheckbox(variant = variant) {
                        checked = variant == CheckboxVariant.Primary
                    }
                }
                """.trimIndent(),
        ) {
            div("flex flex-wrap items-center gap-4") {
                CheckboxVariant.entries.forEach { variant ->
                    label("flex items-center gap-2") {
                        mCheckbox(variant = variant) {
                            checked = variant == CheckboxVariant.Primary
                        }
                        span { +variant.name }
                    }
                }
            }
        }
    }

    section {
        h2 { +"Sizes and indeterminate" }
        exampleCard(
            code =
                """
                Size.entries.forEach { size ->
                    mCheckbox(size = size, indeterminate = size == Size.Lg)
                }
                """.trimIndent(),
        ) {
            div("flex flex-wrap items-center gap-4") {
                Size.entries.forEach { size ->
                    label("flex items-center gap-2") {
                        mCheckbox(size = size, indeterminate = size == Size.Lg)
                        span { +size.name }
                    }
                }
            }
        }
    }

    apiReference(
        listOf(
            ApiParam("variant", "CheckboxVariant?", "null", "Optional colour role."),
            ApiParam("size", "Size", "Size.Md", "Size step: Xs, Sm, Md, Lg, Xl. Md adds no size class."),
            ApiParam("indeterminate", "Boolean", "false", "Adds the type-safe indeterminate visual modifier."),
            ApiParam("classes", "String?", "null", "Extra CSS classes appended after generated classes."),
            ApiParam("block", "INPUT.() -> Unit", "{}", "Receiver block on the raw kotlinx.html INPUT element."),
        ),
    )
}

fun checkboxPage(): String = layout(CHECKBOX) { checkboxPageContent() }
