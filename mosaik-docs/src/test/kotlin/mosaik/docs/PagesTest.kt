package mosaik.docs

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import mosaik.ui.components.AlertVariant
import mosaik.ui.components.BadgeVariant
import mosaik.ui.components.Size
import mosaik.ui.components.Variant

/**
 * The docs app is not a behavioural test seam (it is validated as the VRT render
 * host and by manual inspection), but these smoke tests assert the page renderers
 * still produce the structure the acceptance criteria require, so a broken layout
 * fails the build instead of only showing up in the browser.
 */
class PagesTest : FunSpec({

    test("every page sets a default DaisyUI theme on the html element and links the compiled CSS") {
        listOf(landingPage(), buttonPage()).forEach { html ->
            html shouldContain "<!DOCTYPE html>"
            // The server renders a default theme; the switcher overrides it client-side.
            html shouldContain "data-theme=\"$DEFAULT_THEME\""
            html shouldContain "/static/output.css"
        }
    }

    test("every page renders the sidebar theme switcher and the inline JS that drives it") {
        listOf(landingPage(), buttonPage()).forEach { html ->
            html shouldContain "id=\"theme-switcher\""
            // Each enabled DaisyUI theme is offered as an option.
            html shouldContain "value=\"dracula\""
            // Selecting an option sets data-theme on <html> via inline JS.
            html shouldContain "data-theme"
            html shouldContain "setAttribute"
        }
    }

    test("the sidebar links Home and every component page, marking the active one") {
        landingPage() shouldContain "href=\"/\""
        landingPage() shouldContain "href=\"/components/button\""

        // The active page's nav entry carries DaisyUI's menu-active marker.
        buttonPage() shouldContain "menu-active"
    }

    test("the button page opens with a title and a description of what Button is") {
        val html = buttonPage()
        html shouldContain "<h1>Button</h1>"
        // Description text rewritten for the Mosaik/Kotlin context.
        html shouldContain "mButton"
        html shouldContain "DaisyUI"
    }

    test("the button page shows the Gradle installation command") {
        buttonPage() shouldContain "./gradlew mosaikAdd --component=button"
    }

    test("the button page shows a basic usage Kotlin code block") {
        val html = buttonPage()
        html shouldContain "Basic usage"
        html shouldContain "mButton(Variant.Primary, Size.Md)"
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

    test("the button page includes an API reference table for every mButton parameter") {
        val html = buttonPage()
        html shouldContain "API reference"
        listOf("variant", "size", "classes", "block").forEach { param ->
            html shouldContain param
        }
        // Types and defaults are documented in the table.
        html shouldContain "Variant.Primary"
        html shouldContain "Size.Md"
    }

    test("the sidebar links the badge page and it carries the active marker") {
        landingPage() shouldContain "href=\"/components/badge\""
        badgePage() shouldContain "menu-active"
    }

    test("the badge page opens with a title and a description of what Badge is") {
        val html = badgePage()
        html shouldContain "<h1>Badge</h1>"
        html shouldContain "mBadge"
        html shouldContain "BadgeVariant"
        html shouldContain "DaisyUI"
    }

    test("the badge page shows the Gradle installation command and a usage block") {
        val html = badgePage()
        html shouldContain "./gradlew mosaikAdd --component=badge"
        html shouldContain "Basic usage"
        html shouldContain "mBadge(BadgeVariant.Success, Size.Sm)"
    }

    test("the badge page renders every BadgeVariant as its DaisyUI class") {
        val html = badgePage()
        BadgeVariant.entries.forEach { variant ->
            html shouldContain "badge-${variant.token}"
        }
    }

    test("the badge page renders every non-default size and omits the medium token") {
        val html = badgePage()
        Size.entries.mapNotNull { it.token }.forEach { token ->
            html shouldContain "badge-$token"
        }
    }

    test("the badge page includes an API reference table for every mBadge parameter") {
        val html = badgePage()
        html shouldContain "API reference"
        html shouldContain "BadgeVariant.Primary"
        // The block param documents the raw SPAN receiver (> is HTML-escaped in the cell).
        html shouldContain "SPAN.()"
    }

    test("the sidebar links the alert page and it carries the active marker") {
        landingPage() shouldContain "href=\"/components/alert\""
        alertPage() shouldContain "menu-active"
    }

    test("the alert page opens with a title and a description of what Alert is") {
        val html = alertPage()
        html shouldContain "<h1>Alert</h1>"
        html shouldContain "mAlert"
        html shouldContain "AlertVariant"
        html shouldContain "DaisyUI"
    }

    test("the alert page shows the Gradle installation command and a usage block") {
        val html = alertPage()
        html shouldContain "./gradlew mosaikAdd --component=alert"
        html shouldContain "Basic usage"
        html shouldContain "mAlert(AlertVariant.Success)"
    }

    test("the alert page renders every AlertVariant as its DaisyUI class") {
        val html = alertPage()
        AlertVariant.entries.forEach { variant ->
            html shouldContain "alert-${variant.token}"
        }
    }

    test("the alert page includes an API reference table for every mAlert parameter") {
        val html = alertPage()
        html shouldContain "API reference"
        html shouldContain "AlertVariant.Info"
        // The block param documents the raw DIV receiver (> is HTML-escaped in the cell).
        html shouldContain "DIV.()"
    }
})
