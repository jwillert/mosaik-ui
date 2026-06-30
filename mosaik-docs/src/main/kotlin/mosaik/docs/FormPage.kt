package mosaik.docs

import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.code
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.option
import kotlinx.html.p
import mosaik.ui.components.Size
import mosaik.ui.components.mFormControl
import mosaik.ui.components.mInput
import mosaik.ui.components.mLabel
import mosaik.ui.components.mLabelText
import mosaik.ui.components.mSelect

fun FlowContent.formPageContent() {
    h1 { +"Form" }
    p {
        +"Form helpers cover labels, inputs, and selects as small composable building blocks. "
        code { +"mFormControl" }
        +", "
        code { +"mLabel" }
        +", "
        code { +"mLabelText" }
        +", "
        code { +"mInput" }
        +", and "
        code { +"mSelect" }
        +" expose the underlying kotlinx.html receivers so attributes and validation stay native."
    }

    installSection("form")

    h2 { +"Basic usage" }
    exampleCard(
        code =
            """
            import kotlinx.html.InputType
            import mosaik.ui.components.mFormControl
            import mosaik.ui.components.mInput
            import mosaik.ui.components.mLabel
            import mosaik.ui.components.mLabelText

            mFormControl(classes = "w-full max-w-xs") {
                mLabel { mLabelText { +"Email" } }
                mInput(type = InputType.email) {
                    name = "email"
                    required = true
                    placeholder = "user@example.com"
                }
            }
            """.trimIndent(),
    ) {
        mFormControl(classes = "w-full max-w-xs") {
            mLabel { mLabelText { +"Email" } }
            mInput(type = InputType.email) {
                name = "email"
                required = true
                placeholder = "user@example.com"
            }
        }
    }

    h2 { +"Input sizes" }
    exampleCard(
        code =
            """
            Size.entries.forEach { size ->
                mInput(size = size) { placeholder = size.name }
            }
            """.trimIndent(),
    ) {
        div("grid gap-3 max-w-xs") {
            Size.entries.forEach { size -> mInput(size = size) { placeholder = size.name } }
        }
    }

    h2 { +"Select" }
    exampleCard(
        code =
            """
            mSelect(size = Size.Sm, classes = "w-full max-w-xs") {
                name = "theme"
                option { value = "light"; +"Light" }
                option { value = "dark"; +"Dark" }
            }
            """.trimIndent(),
    ) {
        mSelect(size = Size.Sm, classes = "w-full max-w-xs") {
            name = "theme"
            option {
                value = "light"
                +"Light"
            }
            option {
                value = "dark"
                +"Dark"
            }
        }
    }

    apiReference(
        listOf(
            ApiParam(
                "mFormControl.classes",
                "String?",
                "null",
                "Extra CSS classes for the LABEL container; block is LABEL.() -> Unit.",
            ),
            ApiParam(
                "mLabel.classes",
                "String?",
                "null",
                "Extra CSS classes for the DIV label row; block is DIV.() -> Unit.",
            ),
            ApiParam(
                "mLabelText.classes",
                "String?",
                "null",
                "Extra CSS classes for the SPAN text; block is SPAN.() -> Unit.",
            ),
            ApiParam("mInput.type", "InputType", "InputType.text", "Native input type such as InputType.email."),
            ApiParam("mInput.bordered", "Boolean", "true", "Controls bordered input styling."),
            ApiParam("mInput.size", "Size", "Size.Md", "Size step. Md is the baseline and adds no size token."),
            ApiParam("mSelect.bordered", "Boolean", "true", "Controls bordered select styling."),
            ApiParam("mSelect.size", "Size", "Size.Md", "Size step. Md is the baseline and adds no size token."),
        ),
    )
}

fun formPage(): String = layout(FORM) { formPageContent() }
