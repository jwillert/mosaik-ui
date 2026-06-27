package mosaik.docs

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain

/**
 * Verifies that Button and Card component pages include working interactive
 * previews alongside their code snippets (ADR-0008).
 */
class InteractivityPreviewTest :
    FunSpec({

        test("button page includes htmx preview with working demo route") {
            val html = buttonPage()
            html shouldContain "/_examples/button/submit"
            html shouldContain "button-spinner-htmx"
            html shouldContain "button-result-htmx"
        }

        test("button page includes Alpine.js preview with working demo route") {
            val html = buttonPage()
            html shouldContain "x-on:click"
            html shouldContain "/_examples/button/submit"
            html shouldContain "x-bind:disabled"
        }

        test("button page includes Datastar preview with working demo route") {
            val html = buttonPage()
            html shouldContain "data-on-click"
            html shouldContain "/_examples/button/submit"
            html shouldContain "data-bind-disabled"
        }

        test("card page includes htmx preview with working demo route") {
            val html = cardPage()
            html shouldContain "/_examples/card/content"
            html shouldContain "hx-get"
            html shouldContain "hx-trigger"
        }

        test("card page includes Alpine.js preview with working demo route") {
            val html = cardPage()
            html shouldContain "x-intersect"
            html shouldContain "/_examples/card/content"
            html shouldContain "x-text"
        }

        test("card page includes Datastar preview with working demo route") {
            val html = cardPage()
            html shouldContain "data-intersects"
            html shouldContain "data-get"
            html shouldContain "/_examples/card/content"
            html shouldContain "data-text"
        }

        test("pages load htmx library from CDN") {
            val html = buttonPage()
            html shouldContain "htmx.org"
        }

        test("pages load Alpine.js library from CDN") {
            val html = buttonPage()
            html shouldContain "alpinejs"
        }

        test("pages load Datastar library from CDN") {
            val html = buttonPage()
            html shouldContain "datastar"
        }
    })
