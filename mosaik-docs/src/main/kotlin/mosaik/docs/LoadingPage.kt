package mosaik.docs

import kotlinx.html.FlowContent
import kotlinx.html.code
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.p
import mosaik.ui.components.LoadingType
import mosaik.ui.components.Size
import mosaik.ui.components.mLoading

fun FlowContent.loadingPageContent() {
    h1 { +"Loading" }
    p {
        +"Loading indicators communicate background work without owning any interaction state. "
        code { +"mLoading" }
        +" wraps DaisyUI's loading tokens behind a type-safe "
        code { +"LoadingType" }
        +" and shared "
        code { +"Size" }
        +" parameter."
    }

    installSection("loading")

    h2 { +"Basic usage" }
    exampleCard(
        code =
            """
            import mosaik.ui.components.LoadingType
            import mosaik.ui.components.Size
            import mosaik.ui.components.mLoading

            mLoading(LoadingType.Spinner, Size.Md)
            """.trimIndent(),
    ) {
        mLoading(LoadingType.Spinner, Size.Md)
    }

    h2 { +"Types" }
    exampleCard(
        code =
            """
            LoadingType.entries.forEach { type ->
                mLoading(type = type)
            }
            """.trimIndent(),
    ) {
        div("flex flex-wrap items-center gap-4") {
            LoadingType.entries.forEach { type -> mLoading(type = type) }
        }
    }

    h2 { +"Sizes" }
    exampleCard(
        code =
            """
            Size.entries.forEach { size ->
                mLoading(type = LoadingType.Dots, size = size)
            }
            """.trimIndent(),
    ) {
        div("flex flex-wrap items-center gap-4") {
            Size.entries.forEach { size -> mLoading(type = LoadingType.Dots, size = size) }
        }
    }

    apiReference(
        listOf(
            ApiParam(
                "type",
                "LoadingType",
                "LoadingType.Spinner",
                "Indicator shape: Spinner, Dots, Ring, Ball, Bars, or Infinity.",
            ),
            ApiParam("size", "Size", "Size.Md", "Size step. Md is the baseline and adds no size token."),
            ApiParam("classes", "String?", "null", "Extra CSS classes appended after generated loading classes."),
            ApiParam("block", "SPAN.() -> Unit", "{}", "Receiver block on the raw kotlinx.html SPAN element."),
        ),
    )
}

fun loadingPage(): String = layout(LOADING) { loadingPageContent() }
