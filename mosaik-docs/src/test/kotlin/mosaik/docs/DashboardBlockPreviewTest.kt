package mosaik.docs

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain

class DashboardBlockPreviewTest :
    FunSpec({
        test("dashboard-01 renders as a standalone full document preview") {
            val html = dashboard01PreviewPage()

            html shouldContain "<!DOCTYPE html>"
            html shouldContain "data-theme=\"$DEFAULT_THEME\""
            html shouldContain "/static/output.css"
            html shouldContain "<title>Mosaik UI · Dashboard 01</title>"
            html shouldContain "data-block-preview=\"dashboard-01\""
            html shouldContain "Analytics Dashboard"
            html shouldContain "Revenue"
            html shouldContain "Recent orders"
            html shouldContain "mosaik-logo.svg"
            html shouldContain "href=\"/blocks/dashboard-01/preview\""
            html shouldNotContain "href=\"/blocks/dashboard-01\""
            html shouldNotContain "alpinejs"
            html shouldNotContain "mosaik-docs-client.js"
            html shouldNotContain "id=\"main-content\""
            html shouldNotContain "id=\"theme-switcher\""
        }

        test("dashboard-01 block docs page embeds the standalone preview in an iframe") {
            val html = dashboard01Page()

            html shouldContain "<h1>Dashboard 01</h1>"
            html shouldContain "href=\"/blocks/dashboard-01\""
            html shouldContain "Blocks"
            html shouldContain "iframe"
            html shouldContain "src=\"/blocks/dashboard-01/preview\""
            html shouldContain "title=\"Dashboard 01 preview\""
            html shouldContain "Open standalone preview"
            html shouldContain "./gradlew mosaikAdd --block=dashboard-01"
            html shouldContain "Future CLI boundary"
            html shouldContain "Block installation is not available in the current Gradle plugin yet."
            html shouldContain "Today, install the referenced components with"
            html shouldContain "./gradlew mosaikAdd --component=drawer"
            html shouldNotContain "data-block-preview=\"dashboard-01\""
        }
    })
