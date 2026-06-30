package mosaik.docs

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import mosaik.ui.components.AlertVariant
import mosaik.ui.components.BadgeVariant
import mosaik.ui.components.ButtonVariant
import mosaik.ui.components.FileInputVariant
import mosaik.ui.components.Size
import mosaik.ui.components.mButton

/**
 * The docs app is not a behavioural test seam (it is validated as the VRT render
 * host and by manual inspection), but these smoke tests assert the page renderers
 * still produce the structure the acceptance criteria require, so a broken layout
 * fails the build instead of only showing up in the browser.
 */
class PagesTest :
    FunSpec({

        test("every page sets a default DaisyUI theme on the html element and links the compiled CSS") {
            listOf(landingPage(), buttonPage()).forEach { html ->
                html shouldContain "<!DOCTYPE html>"
                // The server renders a default theme; the switcher overrides it client-side.
                html shouldContain "data-theme=\"$DEFAULT_THEME\""
                html shouldContain "/static/output.css"
            }
        }

        test("every page links the generated Kotlin/JS docs client asset") {
            listOf(landingPage(), buttonPage()).forEach { html ->
                html shouldContain "/static/mosaik-docs-client.js"
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
            listOf("variant", "style", "shape", "width", "size", "classes", "block").forEach { param ->
                html shouldContain param
            }
            // Types and defaults are documented in the table.
            html shouldContain "ButtonVariant.Neutral"
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

        test("the interactivity page has URL-driven Page Variants instead of recipe tabs") {
            val html = interactivityPage()
            html shouldContain "Page variant"
            html shouldContain "href=\"/guides/interactivity?variant=htmx\""
            html shouldContain "href=\"/guides/interactivity?variant=alpine\""
            html shouldContain "href=\"/guides/interactivity?variant=datastar\""
            html shouldContain "aria-current=\"page\""
            html shouldNotContain "login-form-alpine"
            html shouldNotContain "register-form-datastar"
        }

        test("the interactivity page renders only the selected recipe variant") {
            val html = interactivityPage("alpine")
            html shouldContain "Alpine.js"
            html shouldContain "x-data"
            html shouldContain "x-on:submit.prevent"
            html shouldContain "Login"
            html shouldContain "Create account"
            html shouldNotContain "hx-post"
            html shouldNotContain "data-on-submit"
        }

        test("the Alpine interactivity previews submit JSON to live example routes") {
            val html = interactivityPage("alpine")
            html shouldContain "fetch('/_examples/login'"
            html shouldContain "fetch('/_examples/register'"
            html shouldContain "headers: { 'Content-Type': 'application/json' }"
            html shouldContain "body: JSON.stringify({ email, password })"
            html shouldContain "body: JSON.stringify({ name, email, password })"
        }

        test("the interactivity page uses Recipe Sections for composed examples") {
            val html = interactivityPage()
            html shouldContain "data-recipe-section=\"login-form\""
            html shouldContain "data-recipe-section=\"register-form\""
            html shouldContain "<h3>Preview</h3>"
            html shouldContain "<h3>Server route</h3>"
            html shouldContain "<h3>Page markup</h3>"
            html shouldContain "<h3>Behavior notes</h3>"
            html shouldContain "class=\"not-prose mb-6\""
            html shouldContain "login-result"
            html shouldContain "register-result"
        }
        test("the button page has URL-driven Page Variants instead of interactive tabs") {
            val html = buttonPage()
            html shouldContain "<h2>Interactive usage</h2>"
            html shouldContain "Page variant"
            html shouldContain "href=\"/components/button?variant=htmx\""
            html shouldContain "href=\"/components/button?variant=alpine\""
            html shouldContain "href=\"/components/button?variant=datastar\""
            html shouldContain "aria-current=\"page\""
            html shouldNotContain "name=\"button-interactive\""
            html shouldNotContain "button-interactive-alpine"
        }

        test("the button page renders only the selected interactive Page Variant") {
            val html = buttonPage("alpine")
            html shouldContain "Alpine.js"
            html shouldContain "x-data"
            html shouldContain "x-on:click"
            html shouldContain "/_examples/button/submit"
            html shouldNotContain "hx-post"
            html shouldNotContain "data-on-click"
        }

        test("the button page shows button styles (Outline, Ghost, Link)") {
            val html = buttonPage()
            html shouldContain "<h2>Styles</h2>"
            html shouldContain "btn-outline"
            html shouldContain "btn-ghost"
            html shouldContain "btn-link"
            html shouldContain "ButtonStyle"
        }

        test("the button page shows button shapes (Circle, Square)") {
            val html = buttonPage()
            html shouldContain "<h2>Shapes</h2>"
            html shouldContain "btn-circle"
            html shouldContain "btn-square"
            html shouldContain "ButtonShape"
        }

        test("the button page shows button widths (Wide, Block)") {
            val html = buttonPage()
            html shouldContain "<h2>Widths</h2>"
            html shouldContain "btn-wide"
            html shouldContain "btn-block"
            html shouldContain "ButtonWidth"
        }

        test("the button page shows icon and content composition") {
            val html = buttonPage()
            html shouldContain "<h2>Icon and content composition</h2>"
            html shouldContain "Next"
            html shouldContain "Back"
        }

        test("the button page shows loading content with mLoading component") {
            val html = buttonPage()
            html shouldContain "<h2>Loading content</h2>"
            html shouldContain "mLoading"
            html shouldContain "LoadingType"
            html shouldContain "loading-spinner"
            html shouldContain "loading-dots"
            html shouldContain "Processing..."
        }

        test("the button page Interactive section uses mLoading instead of raw loading classes") {
            val html = buttonPage()
            // Interactive examples should mention mLoading in code
            html shouldContain "mLoading(LoadingType.Spinner"
            // Should not use raw loading classes in the code examples
            html shouldContain "import mosaik.ui.components.mLoading"
            html shouldContain "import mosaik.ui.components.LoadingType"
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

        test("the interactivity page contains building blocks for the selected library") {
            val html = interactivityPage("datastar")
            html shouldContain "Building blocks"
            html shouldContain "Datastar"
            html shouldContain "data-signals"
            html shouldNotContain "hx-post"
            html shouldNotContain "x-data"
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
            val html = interactivityPage("alpine")
            html shouldContain "x-data"
            html shouldContain "x-show"
            html shouldContain "x-on"
            html shouldContain "x-bind"
            html shouldContain "x-model"
        }

        test("the Datastar building blocks section documents core primitives and notes SSE requirement") {
            val html = interactivityPage("datastar")
            html shouldContain "data-signals"
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

        test("pages do not render a fixed global Interaction Style switcher") {
            listOf(landingPage(), buttonPage(), interactivityPage()).forEach { html ->
                html shouldNotContain "id=\"interaction-style-switcher\""
                html shouldNotContain "Interaction Style"
                html shouldNotContain "mosaik-interaction-style"
                html shouldNotContain "data-interaction-style"
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
            html shouldContain "API"
            // The block param documents the MFooter receiver (> is HTML-escaped in the cell).
            html shouldContain "MFooter.()"
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

        test("the sidebar links the file input page and it carries the active marker") {
            landingPage() shouldContain "href=\"/components/file-input\""
            fileInputPage() shouldContain "menu-active"
        }

        test("the file input page opens with a title and component description") {
            val html = fileInputPage()
            html shouldContain "<h1>File input</h1>"
            html shouldContain "mFileInput"
            html shouldContain "FileInputVariant"
            html shouldContain "DaisyUI"
        }

        test("the file input page shows the Gradle installation command and a usage block") {
            val html = fileInputPage()
            html shouldContain "./gradlew mosaikAdd --component=form"
            html shouldContain "Basic usage"
            html shouldContain "mFileInput(classes = &quot;w-full&quot;)"
        }

        test("the file input page renders every FileInputVariant and non-default size") {
            val html = fileInputPage()
            FileInputVariant.entries.forEach { variant ->
                html shouldContain "file-input-${variant.token}"
            }
            Size.entries.mapNotNull { it.token }.forEach { token ->
                html shouldContain "file-input-$token"
            }
        }

        test("the file input page documents native attributes and API parameters") {
            val html = fileInputPage()
            html shouldContain "API reference"
            html shouldContain "attributes[&quot;accept&quot;]"
            html shouldContain "attributes[&quot;hx-post&quot;]"
            html shouldContain "INPUT.()"
            listOf("variant", "bordered", "size", "classes", "block").forEach { param ->
                html shouldContain param
            }
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
            val htmlWithPreviews =
                layout(INTERACTIVITY) {
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

        test("exampleCard renders preview first with collapsed Kotlin code and copy support") {
            val html =
                layout(BUTTON) {
                    exampleCard(
                        title = "Preview first",
                        code = "mButton { +\"Save\" }",
                    ) {
                        mButton(variant = ButtonVariant.Primary) { +"Save" }
                    }
                }

            html shouldContain "data-docs-example-card=\"true\""
            html shouldContain "Save"
            html shouldContain "<details"
            html shouldContain "View code"
            html shouldContain "Copy"
            html shouldContain "language-kotlin"
            html shouldContain "mButton { +&quot;Save&quot; }"
        }

        test("component reference pages use preview-first example cards for static examples") {
            mapOf(
                "button" to (buttonPage() to 10),
                "card" to (cardPage() to 4),
                "navbar" to (navbarPage() to 4),
                "footer" to (footerPage() to 3),
                "badge" to (badgePage() to 3),
                "alert" to (alertPage() to 2),
                "file-input" to (fileInputPage() to 4),
            ).forEach { (_, page) ->
                val (html, expectedStaticExampleCount) = page

                Regex("data-docs-example-card=\"true\"").findAll(html).count() shouldBe expectedStaticExampleCount
                html shouldContain "Basic usage"
                html shouldContain "View code"
                html shouldContain "Copy"
                html shouldContain "language-kotlin"
            }
        }

        test("interactivityTabs renders code-only tabs when no previews are provided") {
            val htmlCodeOnly =
                layout(INTERACTIVITY) {
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

        test("the interactivity page has URL-driven Page Variants instead of recipe tabs") {
            val html = interactivityPage()
            html shouldContain "Page variant"
            html shouldContain "href=\"/guides/interactivity?variant=htmx\""
            html shouldContain "href=\"/guides/interactivity?variant=alpine\""
            html shouldContain "href=\"/guides/interactivity?variant=datastar\""
            html shouldContain "aria-current=\"page\""
            html shouldNotContain "login-form-alpine"
            html shouldNotContain "register-form-datastar"
        }

        test("the interactivity page renders only the selected recipe variant") {
            val html = interactivityPage("alpine")
            html shouldContain "Alpine.js"
            html shouldContain "x-data"
            html shouldContain "x-on:submit.prevent"
            html shouldContain "Login"
            html shouldContain "Create account"
            html shouldNotContain "hx-post"
            html shouldNotContain "data-on-submit"
        }

        test("the Alpine interactivity previews submit JSON to live example routes") {
            val html = interactivityPage("alpine")
            html shouldContain "fetch('/_examples/login'"
            html shouldContain "fetch('/_examples/register'"
            html shouldContain "headers: { 'Content-Type': 'application/json' }"
            html shouldContain "body: JSON.stringify({ email, password })"
            html shouldContain "body: JSON.stringify({ name, email, password })"
        }

        test("the interactivity page uses Recipe Sections for composed examples") {
            val html = interactivityPage()
            html shouldContain "data-recipe-section=\"login-form\""
            html shouldContain "data-recipe-section=\"register-form\""
            html shouldContain "<h3>Preview</h3>"
            html shouldContain "<h3>Server route</h3>"
            html shouldContain "<h3>Page markup</h3>"
            html shouldContain "<h3>Behavior notes</h3>"
            html shouldContain "class=\"not-prose mb-6\""
            html shouldContain "login-result"
            html shouldContain "register-result"
        }
        test("the default button interactive Page Variant is htmx") {
            val html = buttonPage()
            html shouldContain "htmx"
            html shouldContain "hx-post"
            html shouldContain "/_examples/button/submit"
            html shouldContain "loading-spinner"
            html shouldNotContain "x-on:click"
            html shouldNotContain "data-on-click"
        }

        test("unknown button interactive Page Variant falls back to htmx") {
            val html = buttonPage("unknown")
            html shouldContain "hx-post"
            html shouldNotContain "x-on:click"
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

        test("the interactivity page contains building blocks for the selected library") {
            val html = interactivityPage("datastar")
            html shouldContain "Building blocks"
            html shouldContain "Datastar"
            html shouldContain "data-signals"
            html shouldNotContain "hx-post"
            html shouldNotContain "x-data"
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
            val html = interactivityPage("alpine")
            html shouldContain "x-data"
            html shouldContain "x-show"
            html shouldContain "x-on"
            html shouldContain "x-bind"
            html shouldContain "x-model"
        }

        test("the Datastar building blocks section documents core primitives and notes SSE requirement") {
            val html = interactivityPage("datastar")
            html shouldContain "data-signals"
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

        test("pages do not render a fixed global Interaction Style switcher") {
            listOf(landingPage(), buttonPage(), interactivityPage()).forEach { html ->
                html shouldNotContain "id=\"interaction-style-switcher\""
                html shouldNotContain "Interaction Style"
                html shouldNotContain "mosaik-interaction-style"
                html shouldNotContain "data-interaction-style"
            }
        }

        test("every page loads highlight.js from CDN for syntax highlighting") {
            listOf(landingPage(), buttonPage()).forEach { html ->
                html shouldContain "highlight.js"
                html shouldContain "highlightAll"
            }
        }

        test("every page marks the main content area with an id for shell navigation") {
            listOf(landingPage(), buttonPage(), cardPage()).forEach { html ->
                html shouldContain "id=\"main-content\""
            }
        }
    })
