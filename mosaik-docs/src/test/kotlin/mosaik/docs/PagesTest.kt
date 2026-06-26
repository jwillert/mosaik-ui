package mosaik.docs

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import mosaik.ui.components.AlertVariant
import mosaik.ui.components.BadgeVariant
import mosaik.ui.components.Size
import mosaik.ui.components.ButtonVariant
import mosaik.ui.components.mButton

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
        html shouldContain "mButton(variant = ButtonVariant.Primary, size = Size.Md)"
    }

    test("the button page renders every variant as its DaisyUI class") {
        val html = buttonPage()
        ButtonVariant.entries.forEach { variant ->
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

    test("the sidebar links the card page and it carries the active marker") {
        landingPage() shouldContain "href=\"/components/card\""
        cardPage() shouldContain "menu-active"
    }

    test("the card page opens with a title and a description of what Card is") {
        val html = cardPage()
        html shouldContain "<h1>Card</h1>"
        html shouldContain "mCard"
        html shouldContain "DaisyUI"
    }

    test("the card page shows the Gradle installation command and a usage block") {
        val html = cardPage()
        html shouldContain "./gradlew mosaikAdd --component=card"
        html shouldContain "Basic usage"
        html shouldContain "mCardBody"
    }

    test("the card page renders the card and every sub-component DaisyUI class") {
        val html = cardPage()
        listOf("card", "card-body", "card-title", "card-actions").forEach { css ->
            html shouldContain "class=\"$css"
        }
    }

    test("the card page documents the card and its sub-components in the API reference") {
        val html = cardPage()
        html shouldContain "API reference"
        // The block param documents the raw DIV receiver (> is HTML-escaped in the cell).
        html shouldContain "DIV.()"
        listOf("mCardBody", "mCardTitle", "mCardActions").forEach { fn ->
            html shouldContain fn
        }
    }

    test("the sidebar links the navbar page and it carries the active marker") {
        landingPage() shouldContain "href=\"/components/navbar\""
        navbarPage() shouldContain "menu-active"
    }

    test("the navbar page opens with a title and a description of what Navbar is") {
        val html = navbarPage()
        html shouldContain "<h1>Navbar</h1>"
        html shouldContain "mNavbar"
        html shouldContain "DaisyUI"
    }

    test("the navbar page shows the Gradle installation command and a usage block") {
        val html = navbarPage()
        html shouldContain "./gradlew mosaikAdd --component=navbar"
        html shouldContain "Basic usage"
        html shouldContain "mNavbarStart"
    }

    test("the navbar page renders the navbar and every slot sub-component DaisyUI class") {
        val html = navbarPage()
        listOf("navbar", "navbar-start", "navbar-center", "navbar-end").forEach { css ->
            html shouldContain "class=\"$css"
        }
    }

    test("the navbar page documents the navbar and its sub-components in the API reference") {
        val html = navbarPage()
        html shouldContain "API reference"
        // The block param documents the raw DIV receiver (> is HTML-escaped in the cell).
        html shouldContain "DIV.()"
        listOf("mNavbarStart", "mNavbarCenter", "mNavbarEnd").forEach { fn ->
            html shouldContain fn
        }
    }

    test("the sidebar links the footer page and it carries the active marker") {
        landingPage() shouldContain "href=\"/components/footer\""
        footerPage() shouldContain "menu-active"
    }

    test("the footer page opens with a title and a description of what Footer is") {
        val html = footerPage()
        html shouldContain "<h1>Footer</h1>"
        html shouldContain "mFooter"
        html shouldContain "DaisyUI"
    }

    test("the footer page shows the Gradle installation command and a usage block") {
        val html = footerPage()
        html shouldContain "./gradlew mosaikAdd --component=footer"
        html shouldContain "Basic usage"
        html shouldContain "mFooter("
    }

    test("the footer page renders the footer DaisyUI classes across its showcases") {
        val html = footerPage()
        html shouldContain "class=\"footer"
        html shouldContain "footer-center"
        html shouldContain "footer-title"
    }

    test("the footer page includes an API reference table for every mFooter parameter") {
        val html = footerPage()
        html shouldContain "API reference"
        // The block param documents the raw FOOTER receiver (> is HTML-escaped in the cell).
        html shouldContain "FOOTER.()"
        listOf("classes", "block").forEach { param ->
            html shouldContain param
        }
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

    test("the sidebar renders menu-title headers for Components and Guides sections") {
        val html = landingPage()
        html shouldContain "menu-title\">Components"
        html shouldContain "menu-title\">Guides"
    }

    test("the sidebar Guides section contains a link to the interactivity page") {
        val html = landingPage()
        html shouldContain "href=\"/guides/interactivity\""
        html shouldContain ">Interactivity"
    }

    test("the interactivity page is reachable and renders title and intro") {
        val html = interactivityPage()
        html shouldContain "<h1>Interactivity</h1>"
        html shouldContain "htmx"
        html shouldContain "Alpine.js"
        html shouldContain "Datastar"
    }

    test("the sidebar links the interactivity page and it carries the active marker") {
        interactivityPage() shouldContain "menu-active"
    }

    test("interactivityTabs renders DaisyUI radio tabs with unique name attributes") {
        val html = interactivityTabsTestPage()
        // Wrapper with not-prose class to prevent Tailwind Typography interference.
        html shouldContain "class=\"tabs tabs-lifted not-prose\""
        // Radio inputs with the unique name attribute (id parameter).
        html shouldContain "type=\"radio\" name=\"test-tabs\""
        // Three tabs: htmx, Alpine.js, Datastar with unique IDs derived from the id parameter.
        html shouldContain "test-tabs-htmx"
        html shouldContain "test-tabs-alpine"
        html shouldContain "test-tabs-datastar"
        // htmx tab is checked by default.
        html shouldContain "checked=\"checked\""
        // Tab content panels.
        html shouldContain "class=\"tab-content"
    }

    test("interactivityTabs supports per-style preview content above code blocks") {
        val htmlWithPreviews = layout(INTERACTIVITY) {
            interactivityTabs(
                id = "preview-test",
                htmxCode = "htmx code",
                alpineCode = "alpine code",
                datastarCode = "datastar code",
                htmxPreview = {
                    mButton(variant = ButtonVariant.Primary) { +"htmx preview content" }
                },
                alpinePreview = {
                    mButton(variant = ButtonVariant.Secondary) { +"alpine preview content" }
                },
                datastarPreview = {
                    mButton(variant = ButtonVariant.Accent) { +"datastar preview content" }
                },
            )
        }
        // Preview content appears before code blocks.
        htmlWithPreviews shouldContain "htmx preview content"
        htmlWithPreviews shouldContain "alpine preview content"
        htmlWithPreviews shouldContain "datastar preview content"
        // Preview wrappers have not-prose class and mb-4 spacing.
        htmlWithPreviews shouldContain "class=\"mb-4 not-prose\""
        // Code blocks still render.
        htmlWithPreviews shouldContain "htmx code"
        htmlWithPreviews shouldContain "alpine code"
        htmlWithPreviews shouldContain "datastar code"
    }

    test("interactivityTabs renders code-only tabs when no previews are provided") {
        val htmlCodeOnly = layout(INTERACTIVITY) {
            interactivityTabs(
                id = "code-only-test",
                htmxCode = "htmx only code",
                alpineCode = "alpine only code",
                datastarCode = "datastar only code",
            )
        }
        // Code blocks render without previews.
        htmlCodeOnly shouldContain "htmx only code"
        htmlCodeOnly shouldContain "alpine only code"
        htmlCodeOnly shouldContain "datastar only code"
        // No mb-4 preview wrapper when previews are null.
        htmlCodeOnly shouldContain "tab-content bg-base-200"
        htmlCodeOnly shouldContain "<pre><code>htmx only code"
    }

    test("the interactivity page includes a Login form composed example section") {
        val html = interactivityPage()
        html shouldContain "Login form"
        // The example describes the composition pattern.
        html shouldContain "mCard"
        html shouldContain "mButton"
    }

    test("the interactivity page includes a Register form composed example section") {
        val html = interactivityPage()
        html shouldContain "Register form"
        // The example describes the composition pattern.
        html shouldContain "mCard"
        html shouldContain "mButton"
    }

    test("the interactivity page Login form shows all three library implementations") {
        val html = interactivityPage()
        // Login form tabs exist with unique IDs.
        html shouldContain "login-form-htmx"
        html shouldContain "login-form-alpine"
        html shouldContain "login-form-datastar"
        // htmx implementation uses hx-post.
        html shouldContain "hx-post"
        // Alpine.js implementation uses x-data.
        html shouldContain "x-data"
        // Datastar implementation uses data-on-submit.
        html shouldContain "data-on-submit"
    }

    test("the interactivity page Login form shows working previews per style") {
        val html = interactivityPage()
        // Preview wrapper with mb-4 not-prose class.
        html shouldContain "class=\"mb-4 not-prose\""
        // All three previews render the login form card.
        html shouldContain "Login"
        // Form inputs are present in previews.
        html shouldContain "type=\"email\" name=\"email\""
        html shouldContain "type=\"password\" name=\"password\""
        // Submit button is present.
        html shouldContain "Sign in"
        // Result/error display elements are present.
        html shouldContain "login-result"
    }

    test("the interactivity page Register form shows all three library implementations") {
        val html = interactivityPage()
        // Register form tabs exist with unique IDs.
        html shouldContain "register-form-htmx"
        html shouldContain "register-form-alpine"
        html shouldContain "register-form-datastar"
        // htmx implementation uses hx-post.
        html shouldContain "hx-post"
        // Alpine.js implementation uses x-data.
        html shouldContain "x-data"
        // Datastar implementation uses data-on-submit.
        html shouldContain "data-on-submit"

    }

    test("the interactivity page Register form shows working previews per style") {
        val html = interactivityPage()
        // Preview wrapper with mb-4 not-prose class.
        html shouldContain "class=\"mb-4 not-prose\""
        // All three previews render the registration form card.
        html shouldContain "Create account"
        // Form inputs are present in previews.
        html shouldContain "type=\"text\" name=\"name\""
        html shouldContain "type=\"email\" name=\"email\""
        html shouldContain "type=\"password\" name=\"password\""
        html shouldContain "name=\"confirm_password\""
        // Submit button is present.
        html shouldContain "Sign up"
        // Result/error display elements are present.
        html shouldContain "register-result"
    }
    test("the button page has an Interactive usage section with interactivityTabs") {
        val html = buttonPage()
        html shouldContain "<h2>Interactive usage</h2>"
        // The tab structure uses the button-interactive id.
        html shouldContain "name=\"button-interactive\""
        html shouldContain "button-interactive-htmx"
        html shouldContain "button-interactive-alpine"
        html shouldContain "button-interactive-datastar"
        // Form submit example mentions hx-post for htmx.
        html shouldContain "hx-post"
    }

    test("the button page Interactive section has working previews per style") {
        val html = buttonPage()
        // Preview wrapper with mb-4 not-prose class.
        html shouldContain "class=\"mb-4 not-prose\""
        // All three previews render the submit button.
        html shouldContain "Submit"
        // Preview uses /_examples/button/submit route.
        html shouldContain "/_examples/button/submit"
        // Loading spinner is present in previews.
        html shouldContain "loading-spinner"
    }

    test("the card page has an Interactive usage section with interactivityTabs") {
        val html = cardPage()
        html shouldContain "<h2>Interactive usage</h2>"
        // The tab structure uses the card-interactive id.
        html shouldContain "name=\"card-interactive\""
        html shouldContain "card-interactive-htmx"
        html shouldContain "card-interactive-alpine"
        html shouldContain "card-interactive-datastar"
        // Lazy-load example mentions hx-get for htmx.
        html shouldContain "hx-get"
    }

    test("the card page Interactive section has working previews per style") {
        val html = cardPage()
        // Preview wrapper with mb-4 not-prose class.
        html shouldContain "class=\"mb-4 not-prose\""
        // All three previews render the lazy card.
        html shouldContain "Lazy Card"
        // Preview uses /_examples/card/content route.
        html shouldContain "/_examples/card/content"
        // Loading placeholder is present in htmx preview.
        html shouldContain "Loading..."
    }

    test("the alert page has an Interactive usage section with interactivityTabs") {
        val html = alertPage()
        html shouldContain "<h2>Interactive usage</h2>"
        // The tab structure uses the alert-interactive id.
        html shouldContain "name=\"alert-interactive\""
        html shouldContain "alert-interactive-htmx"
        html shouldContain "alert-interactive-alpine"
        html shouldContain "alert-interactive-datastar"
    }

    test("the alert page shows working previews for dismissible alerts per style") {
        val html = alertPage()
        // Preview wrapper with mb-4 not-prose class.
        html shouldContain "class=\"mb-4 not-prose\""
        // All three previews render the success alert with dismissible behavior.
        html shouldContain "Your changes have been saved."
        // Dismiss button is present in previews.
        html shouldContain "Dismiss"
        // htmx uses hx-on:click to remove the alert.
        html shouldContain "hx-on:click"
        // Alpine.js uses x-data and x-show for visibility control.
        html shouldContain "x-data"
        html shouldContain "x-show"
        // Datastar uses data-signals and data-show.
        html shouldContain "data-signals"
        html shouldContain "data-show"
    }

    test("the navbar page has an Interactive usage section with interactivityTabs") {
        val html = navbarPage()
        html shouldContain "<h2>Interactive usage</h2>"
        // The tab structure uses the navbar-interactive id.
        html shouldContain "name=\"navbar-interactive\""
        html shouldContain "navbar-interactive-htmx"
        html shouldContain "navbar-interactive-alpine"
        html shouldContain "navbar-interactive-datastar"
        // Active link example mentions hx-boost for htmx.
        html shouldContain "hx-boost"

    }

    test("the navbar page shows working previews for active link highlighting per style") {
        val html = navbarPage()
        // Preview wrapper with mb-4 not-prose class.
        html shouldContain "class=\"mb-4 not-prose\""
        // All three previews render the navbar with navigation links.
        html shouldContain "Home"
        html shouldContain "Docs"
        // htmx uses hx-boost for navigation.
        html shouldContain "hx-boost"
        // Alpine.js uses x-data for state and x-bind:class for active styling.
        html shouldContain "x-data"
        html shouldContain "x-bind:class"
        html shouldContain "btn-active"
        // Datastar uses data-signals and data-bind-class-btn-active.
        html shouldContain "data-signals"
        html shouldContain "data-bind-class-btn-active"
    }

    test("the navbar page Interactive section has working previews per style") {
        val html = navbarPage()
        // Preview wrapper with mb-4 not-prose class.
        html shouldContain "class=\"mb-4 not-prose\""
        // All three previews render the navbar with links.
        html shouldContain "Home"
        html shouldContain "Docs"
        // Client-side only, no server endpoints needed for this example.
    }

    test("the interactivity page contains building blocks sections for all three libraries") {
        val html = interactivityPage()
        html shouldContain "Building blocks"
        html shouldContain "htmx"
        html shouldContain "Alpine.js"
        html shouldContain "Datastar"
    }

    test("the htmx building blocks section documents core primitives and uses attributes syntax") {
        val html = interactivityPage()
        html shouldContain "hx-get"
        html shouldContain "hx-post"
        html shouldContain "hx-target"
        html shouldContain "hx-swap"
        html shouldContain "hx-trigger"
        html shouldContain "attributes"
        html shouldContain "Ktor 3.2"
    }

    test("the Alpine.js building blocks section documents core primitives") {
        val html = interactivityPage()
        html shouldContain "x-data"
        html shouldContain "x-show"
        html shouldContain "x-on"
        html shouldContain "x-bind"
        html shouldContain "x-model"
    }

    test("the Datastar building blocks section documents core primitives and notes SSE requirement") {
        val html = interactivityPage()
        html shouldContain "data-store"
        html shouldContain "data-on"
        html shouldContain "SSE"
        html shouldContain "Server-Sent Events"
    }

    test("the interactivity page contains a comparison table") {
        val html = interactivityPage()
        html shouldContain "Comparison"
        html shouldContain "table"
        html shouldContain "Paradigm"
        html shouldContain "Server requirement"
        html shouldContain "Script size"
    }

    test("the comparison table includes all three libraries and their paradigms") {
        val html = interactivityPage()
        // Check that all three libraries appear in the table
        html shouldContain "htmx"
        html shouldContain "Alpine.js"
        html shouldContain "Datastar"
        // Check for paradigm values that distinguish the libraries
        html shouldContain "Server-driven"
        html shouldContain "Client-side"
        html shouldContain "Hybrid"
    }

    test("every page renders the sidebar Interaction Style switcher") {
        listOf(landingPage(), buttonPage(), interactivityPage()).forEach { html ->
            html shouldContain "id=\"interaction-style-switcher\""
            html shouldContain "value=\"htmx\""
            html shouldContain "value=\"alpine\""
            html shouldContain "value=\"datastar\""
        }
    }

    test("the Interaction Style switcher has a label and is in the sidebar") {
        val html = landingPage()
        html shouldContain "Interaction Style"
        html shouldContain "for=\"interaction-style-switcher\""
    }

    test("interactivityTabs supports data-interaction-style attribute for style selection") {
        val html = interactivityTabsTestPage()
        html shouldContain "data-interaction-style=\"htmx\""
        html shouldContain "data-interaction-style=\"alpine\""
        html shouldContain "data-interaction-style=\"datastar\""
    }

    test("exampleCard renders a preview area and a code area") {
        val html = layout(HOME) {
            exampleCard(
                title = "Test Example",
                preview = {
                    mButton(Variant.Primary) { +"Test Button" }
                },
                code = """
                    mButton(Variant.Primary) {
                        +"Test Button"
                    }
                """.trimIndent(),
            )
        }
        html shouldContain "Test Example"
        html shouldContain "Test Button"
        html shouldContain "mButton(Variant.Primary)"
    }

    test("exampleCard code blocks expose copy controls") {
        val html = layout(HOME) {
            exampleCard(
                title = "Copy Test",
                preview = { mButton(Variant.Primary) { +"Copy Me" } },
                code = "mButton(Variant.Primary) { +\"Copy Me\" }",
            )
        }
        html shouldContain "data-copy-target"
        html shouldContain "onclick"
        html shouldContain "Copy"
    }

    test("exampleCard code blocks use markup compatible with Kotlin syntax highlighting") {
        val html = layout(HOME) {
            exampleCard(
                title = "Highlight Test",
                preview = { mButton(Variant.Primary) { +"Highlight" } },
                code = "mButton(Variant.Primary) { +\"Highlight\" }",
            )
        }
        html shouldContain "pre"
        html shouldContain "code"
        html shouldContain "language-kotlin"
    }

    test("every page loads highlight.js from CDN for syntax highlighting") {
        listOf(landingPage(), buttonPage()).forEach { html ->
            html shouldContain "highlight.js"
            html shouldContain "highlightAll"
        }
    }

    test("exampleCard is reusable across different preview and code content") {
        val html = layout(HOME) {
            exampleCard(
                title = "Example 1",
                preview = { mButton(Variant.Primary) { +"Button 1" } },
                code = "mButton(Variant.Primary) { +\"Button 1\" }",
            )
            exampleCard(
                title = "Example 2",
                preview = { mButton(Variant.Secondary) { +"Button 2" } },
                code = "mButton(Variant.Secondary) { +\"Button 2\" }",
            )
        }
        html shouldContain "Example 1"
        html shouldContain "Example 2"
        html shouldContain "Button 1"
        html shouldContain "Button 2"
    }

    test("exampleCard handles titles with special characters safely") {
        val html = layout(HOME) {
            exampleCard(
                title = "Test's \"Example\" with <symbols>",
                preview = { mButton(Variant.Primary) { +"Test" } },
                code = "mButton(Variant.Primary) { +\"Test\" }",
            )
        }
        html shouldContain "Test's &quot;Example&quot; with &lt;symbols&gt;"
        html shouldContain "getElementById('code-test-s--example--with--symbols-"
        html shouldContain "language-kotlin"
    }

    test("exampleCard generates unique IDs for duplicate titles") {
        val html = layout(HOME) {
            exampleCard(
                title = "Same Title",
                preview = { mButton(Variant.Primary) { +"First" } },
                code = "// First",
            )
            exampleCard(
                title = "Same Title",
                preview = { mButton(Variant.Secondary) { +"Second" } },
                code = "// Second",
            )
        }
        html shouldContain "First"
        html shouldContain "Second"
        html shouldContain "// First"
        html shouldContain "// Second"
    }
})
