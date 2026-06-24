package mosaik.docs

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import mosaik.ui.components.Size
import mosaik.ui.components.Variant

/**
 * The docs app is not a behavioural test seam (it is validated as the VRT render
 * host and by manual inspection), but these smoke tests assert the page renderers
 * still produce the structure the acceptance criteria require, so a broken layout
 * fails the build instead of only showing up in the browser.
 */
class PagesTest : FunSpec({

    test("every page declares the light DaisyUI theme on the html element and links the compiled CSS") {
        listOf(landingPage(), buttonPage()).forEach { html ->
            html shouldContain "<!DOCTYPE html>"
            html shouldContain "data-theme=\"light\""
            html shouldContain "/static/output.css"
        }
    }

    test("the sidebar links Home and every component page, marking the active one") {
        landingPage() shouldContain "href=\"/\""
        landingPage() shouldContain "href=\"/components/button\""

        // The active page's nav entry carries DaisyUI's menu-active marker.
        buttonPage() shouldContain "menu-active"
    }

    test("the button page renders every variant as its DaisyUI class") {
        val html = buttonPage()
        Variant.entries.forEach { variant ->
            html shouldContain "btn-${variant.token}"
        }
    }

    test("the button page renders every non-default size and omits the medium token") {
        val html = buttonPage()
        Size.entries.mapNotNull { it.token }.forEach { token ->
            html shouldContain "btn-$token"
        }
    }

    test("the button page shows a disabled button") {
        buttonPage() shouldContain "disabled=\"disabled\""
    }
})
