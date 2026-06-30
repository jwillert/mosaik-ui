package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.FileInputVariant
import mosaik.ui.components.Size
import mosaik.ui.components.mFileInput
import mosaik.ui.components.mFormControl
import mosaik.ui.components.mLabel
import mosaik.ui.components.mLabelText

/**
 * File input page content block for use in both full-page and partial renders.
 */
fun FlowContent.fileInputPageContent() {
    h1 { +"File input" }
    p {
        code { +"mFileInput" }
        +" renders a native "
        code { +"<input type=\"file\">" }
        +" with DaisyUI styling hidden behind typed parameters. The block receives the raw "
        code { +"INPUT" }
        +" receiver, so native attributes and extension attributes work directly."
    }

    installSection("form")

    h2 { +"Basic usage" }
    exampleCard(
        code =
            """
            import mosaik.ui.components.mFileInput
            import mosaik.ui.components.mFormControl
            import mosaik.ui.components.mLabel
            import mosaik.ui.components.mLabelText

            mFormControl(classes = "w-full max-w-sm") {
                mLabel { mLabelText { +"Avatar" } }
                mFileInput(classes = "w-full") {
                    name = "avatar"
                    attributes["accept"] = "image/*"
                }
            }
            """.trimIndent(),
    ) {
        mFormControl(classes = "w-full max-w-sm") {
            mLabel { mLabelText { +"Avatar" } }
            mFileInput(classes = "w-full") {
                name = "avatar"
                attributes["accept"] = "image/*"
            }
        }
    }

    section {
        h2 { +"Variants" }
        p {
            +"Use "
            code { +"FileInputVariant" }
            +" for supported file-input colour roles. Leave "
            code { +"variant" }
            +" as "
            code { +"null" }
            +" for the neutral base style."
        }
        exampleCard(
            code =
                """
                FileInputVariant.entries.forEach { variant ->
                    mFileInput(variant = variant, classes = "w-full max-w-xs")
                }
                """.trimIndent(),
        ) {
            div("grid gap-3 sm:grid-cols-2") {
                FileInputVariant.entries.forEach { variant ->
                    div("space-y-1") {
                        mLabelText { +variant.name }
                        mFileInput(variant = variant, classes = "w-full max-w-xs")
                    }
                }
            }
        }
    }

    section {
        h2 { +"Sizes and bordered modifier" }
        p {
            +"File inputs reuse the shared "
            code { +"Size" }
            +" enum. "
            code { +"Size.Md" }
            +" is the default and emits no size class. Set "
            code { +"bordered = false" }
            +" to omit the bordered modifier."
        }
        exampleCard(
            code =
                """
                Size.entries.forEach { size ->
                    mFileInput(size = size, classes = "w-full max-w-xs")
                }
                mFileInput(bordered = false, classes = "w-full max-w-xs")
                """.trimIndent(),
        ) {
            div("grid gap-3") {
                Size.entries.forEach { size ->
                    div("space-y-1") {
                        mLabelText { +size.name }
                        mFileInput(size = size, classes = "w-full max-w-xs")
                    }
                }
                div("space-y-1") {
                    mLabelText { +"Unbordered" }
                    mFileInput(bordered = false, classes = "w-full max-w-xs")
                }
            }
        }
    }

    section {
        h2 { +"Native attributes" }
        exampleCard(
            code =
                """
                mFileInput(variant = FileInputVariant.Primary, classes = "w-full") {
                    id = "documents"
                    name = "documents"
                    required = true
                    attributes["multiple"] = "multiple"
                    attributes["hx-post"] = "/uploads"
                }
                """.trimIndent(),
        ) {
            mFileInput(variant = FileInputVariant.Primary, classes = "w-full max-w-sm") {
                id = "documents"
                name = "documents"
                required = true
                attributes["multiple"] = "multiple"
                attributes["hx-post"] = "/uploads"
            }
        }
    }

    apiReference(
        listOf(
            ApiParam(
                "variant",
                "FileInputVariant?",
                "null",
                "Optional colour role: Neutral, Primary, Secondary, Accent, Info, Success, Warning, Error.",
            ),
            ApiParam(
                "bordered",
                "Boolean",
                "true",
                "Emits the bordered file input modifier when true.",
            ),
            ApiParam(
                "size",
                "Size",
                "Size.Md",
                "Size step: Xs, Sm, Md, Lg, Xl. Md is the baseline and adds no class.",
            ),
            ApiParam(
                "classes",
                "String?",
                "null",
                "Extra CSS classes appended after generated file input classes, such as w-full.",
            ),
            ApiParam(
                "block",
                "INPUT.() -> Unit",
                "{}",
                "Receiver block on the raw kotlinx.html INPUT element for native attributes and third-party extensions.",
            ),
        ),
    )
}

/** File input page using the component reference gallery format. */
fun fileInputPage(): String = layout(FILE_INPUT) { fileInputPageContent() }
