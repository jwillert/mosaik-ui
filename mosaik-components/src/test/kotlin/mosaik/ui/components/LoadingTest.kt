package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import kotlinx.html.*
import kotlinx.html.stream.createHTML

/** Renders a flow fragment without pretty-printing so assertions are deterministic. */
private fun render(block: FlowContent.() -> Unit): String =
    createHTML(prettyPrint = false).div { block() }

class LoadingTest : FunSpec({

    test("a spinner medium loading renders the base and type classes, omitting the default size") {
        val html = render { mLoading(LoadingType.Spinner, Size.Md) }

        html shouldContain "<span class=\"loading loading-spinner\"></span>"
    }

    test("every loading type maps to its DaisyUI class") {
        val expected = mapOf(
            LoadingType.Spinner to "loading-spinner",
            LoadingType.Dots to "loading-dots",
            LoadingType.Ring to "loading-ring",
            LoadingType.Ball to "loading-ball",
            LoadingType.Bars to "loading-bars",
            LoadingType.Infinity to "loading-infinity",
        )

        expected.forEach { (type, css) ->
            val html = render { mLoading(type) }
            html shouldContain "class=\"loading $css\""
        }
    }

    test("LoadingType declares exactly the six DaisyUI loading types") {
        LoadingType.entries.map { it.name } shouldBe
            listOf("Spinner", "Dots", "Ring", "Ball", "Bars", "Infinity")
    }

    test("every non-default size maps to its DaisyUI class and Md is omitted") {
        val expected = mapOf(
            Size.Xs to "loading-xs",
            Size.Sm to "loading-sm",
            Size.Lg to "loading-lg",
            Size.Xl to "loading-xl",
        )

        expected.forEach { (size, css) ->
            val html = render { mLoading(LoadingType.Spinner, size) }
            html shouldContain "class=\"loading loading-spinner $css\""
        }

        val md = render { mLoading(LoadingType.Spinner, Size.Md) }
        md shouldNotContain "loading-md"
    }

    test("custom classes are appended after the type and size classes") {
        val html = render {
            mLoading(LoadingType.Dots, Size.Lg, "text-primary mr-2")
        }

        html shouldContain "class=\"loading loading-dots loading-lg text-primary mr-2\""
    }

    test("the block receives the raw span element so its html attributes apply natively") {
        val html = render {
            mLoading(LoadingType.Ring) {
                id = "status"
                title = "Loading..."
            }
        }

        html shouldContain "id=\"status\""
        html shouldContain "title=\"Loading...\""
    }

    test("arbitrary attributes work natively via the raw element") {
        val html = render {
            mLoading(LoadingType.Spinner) {
                attributes["aria-label"] = "Loading content"
            }
        }

        html shouldContain "aria-label=\"Loading content\""
        html shouldContain "class=\"loading loading-spinner\""
    }

    test("loading can be composed inside button content") {
        val html = render {
            mButton(Variant.Primary) {
                mLoading(LoadingType.Spinner, Size.Sm, "mr-2")
                +"Processing..."
            }
        }

        html shouldContain "<button class=\"btn btn-primary\">"
        html shouldContain "<span class=\"loading loading-spinner loading-sm mr-2\"></span>"
        html shouldContain "Processing..."
    }
})
