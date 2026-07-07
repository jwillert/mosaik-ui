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
            html shouldNotContain "id=\"main-content\""
            html shouldNotContain "id=\"theme-switcher\""
        }
    })
