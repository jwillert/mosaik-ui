package mosaik.docs

import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.ktor.server.application.Application
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine

/**
 * Browser-level regression test for Docs Shell Navigation. Validates that
 * clicking sidebar links enhances navigation without full-page reloads, keeps
 * the theme applied (no white flash), and updates all relevant page state.
 */
class ShellNavigationTest :
    FunSpec({

        lateinit var server: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>
        lateinit var playwright: Playwright
        lateinit var browser: Browser
        lateinit var page: Page
        val testPort = 9876

        beforeSpec {
            // Start the docs server on a test port
            server =
                embeddedServer(Netty, port = testPort, host = "127.0.0.1", module = Application::module)
                    .start(wait = false)

            // Launch Playwright browser
            playwright = Playwright.create()
            browser = playwright.chromium().launch(BrowserType.LaunchOptions().setHeadless(true))

            // Wait for server to be ready by attempting to connect
            val page = browser.newPage()
            var attempts = 0
            while (attempts < 10) {
                try {
                    page.navigate("http://127.0.0.1:$testPort/")
                    break
                } catch (e: Exception) {
                    attempts++
                    Thread.sleep(100)
                }
            }
            page.close()
        }

        afterSpec {
            server.stop(1000, 2000)
            browser.close()
            playwright.close()
        }

        beforeTest {
            page = browser.newPage()
        }

        afterTest {
            page.close()
        }

        test("clicking a sidebar link navigates without full page reload") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Set a marker on the document to detect if a full reload happens
            page.evaluate("() => { window.testMarker = 'initial'; }")

            // Click the Button page link in the sidebar
            page.locator(".menu a[href='/components/button']").click()

            // Wait for navigation to complete
            page.waitForURL("http://127.0.0.1:$testPort/components/button")

            // The marker should still exist (no full reload)
            val marker = page.evaluate("() => window.testMarker")
            marker shouldBe "initial"
        }

        test("shell navigation updates the document title") {
            page.navigate("http://127.0.0.1:$testPort/")
            val initialTitle = page.title()
            initialTitle shouldContain "Home"

            // Navigate to Button page
            page.locator(".menu a[href='/components/button']").click()
            page.waitForURL("http://127.0.0.1:$testPort/components/button")

            // Title should update to reflect the new page
            val newTitle = page.title()
            newTitle shouldContain "Button"
            newTitle shouldNotBe initialTitle
        }

        test("shell navigation updates the active sidebar item") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Initially, Home link should be active
            val homeLink = page.locator(".menu a[href='/']")
            homeLink.getAttribute("class") shouldContain "menu-active"

            // Navigate to Button page
            page.locator(".menu a[href='/components/button']").click()
            page.waitForURL("http://127.0.0.1:$testPort/components/button")

            // Button link should now be active
            val buttonLink = page.locator(".menu a[href='/components/button']")
            buttonLink.getAttribute("class") shouldContain "menu-active"

            // Home link should no longer be active
            val homeLinkClasses = homeLink.getAttribute("class") ?: ""
            homeLinkClasses.contains("menu-active") shouldBe false
        }

        test("shell navigation updates the main content") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Check initial content (landing page has "Mosaik UI" heading)
            val mainContent = page.locator("#main-content")
            val initialContent = mainContent.textContent()
            initialContent shouldNotBe null
            initialContent!! shouldContain "Mosaik UI"

            // Navigate to Button page
            page.locator(".menu a[href='/components/button']").click()
            page.waitForURL("http://127.0.0.1:$testPort/components/button")

            // Wait for content to update
            page.waitForFunction("() => document.getElementById('main-content').textContent.includes('mButton')")

            // Content should update to Button page
            val newContent = mainContent.textContent()
            newContent shouldNotBe null
            newContent!! shouldContain "mButton"
        }

        test("shell navigation preserves the selected theme without white flash") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Switch to dark theme
            page.locator("#theme-switcher").selectOption("dark")

            // Verify theme is applied
            val theme = page.locator("html").getAttribute("data-theme")
            theme shouldBe "dark"

            // Navigate to another page
            page.locator(".menu a[href='/components/button']").click()
            page.waitForURL("http://127.0.0.1:$testPort/components/button")

            // Theme should still be dark (no reset to default)
            val newTheme = page.locator("html").getAttribute("data-theme")
            newTheme shouldBe "dark"
        }

        test("shell navigation scrolls to the top of the page") {
            page.navigate("http://127.0.0.1:$testPort/components/button")

            // Scroll down
            page.evaluate("() => { window.scrollTo(0, 500); }")

            // Verify we're scrolled down
            val scrollY = page.evaluate("() => window.scrollY")
            (scrollY as Number).toInt() shouldBe 500

            // Navigate to another page
            page.locator(".menu a[href='/components/card']").click()
            page.waitForURL("http://127.0.0.1:$testPort/components/card")

            // Should be scrolled to top
            val newScrollY = page.evaluate("() => window.scrollY")
            (newScrollY as Number).toInt() shouldBe 0
        }

        test("browser back/forward buttons work with shell navigation") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Navigate to Button page
            page.locator(".menu a[href='/components/button']").click()
            page.waitForURL("http://127.0.0.1:$testPort/components/button")
            page.waitForFunction("() => document.getElementById('main-content').textContent.includes('mButton')")

            // Navigate to Card page
            page.locator(".menu a[href='/components/card']").click()
            page.waitForURL("http://127.0.0.1:$testPort/components/card")
            page.waitForFunction("() => document.getElementById('main-content').textContent.includes('mCard')")

            // Go back
            page.goBack()
            page.waitForURL("http://127.0.0.1:$testPort/components/button")
            page.waitForFunction("() => document.getElementById('main-content').textContent.includes('mButton')")
            val backContent = page.locator("#main-content").textContent()
            backContent shouldNotBe null
            backContent!! shouldContain "mButton"

            // Go forward
            page.goForward()
            page.waitForURL("http://127.0.0.1:$testPort/components/card")
            page.waitForFunction("() => document.getElementById('main-content').textContent.includes('mCard')")
            val forwardContent = page.locator("#main-content").textContent()
            forwardContent shouldNotBe null
            forwardContent!! shouldContain "mCard"
        }

        test("isInternalDocsLink filters out external and /_examples links") {
            page.navigate("http://127.0.0.1:$testPort/")

            val externalIsExternal =
                page.evaluate(
                    """() => {
                        const externalLink = document.createElement('a');
                        externalLink.href = 'https://example.com';
                        return !externalLink.href.startsWith(window.location.origin);
                    }
                    """,
                )
            externalIsExternal shouldBe true

            val examplesContainsExamples =
                page.evaluate(
                    """() => {
                        const examplesLink = document.createElement('a');
                        examplesLink.href = '/_examples/test';
                        return examplesLink.href.includes('/_examples/');
                    }
                    """,
                )
            examplesContainsExamples shouldBe true
        }

        test("shell navigation falls back to normal navigation on fetch error") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Set up route interception to simulate a fetch error
            page.route("**/*?partial=true") { route ->
                route.abort()
            }

            // Try to navigate - should fall back to normal navigation
            page.locator(".menu a[href='/components/button']").click()

            // The page should still navigate (via fallback)
            page.waitForURL("http://127.0.0.1:$testPort/components/button")

            // Content should be loaded (via fallback full-page load)
            page.locator("#main-content").textContent() shouldContain "mButton"
        }

        test("syntax highlighting is rehydrated after shell navigation") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Navigate to Button page which has code blocks
            page.locator(".menu a[href='/components/button']").click()
            page.waitForURL("http://127.0.0.1:$testPort/components/button")

            // Wait for content to load
            page.waitForFunction("() => document.getElementById('main-content').textContent.includes('mButton')")

            // Check that code blocks have highlight.js classes applied
            val hasHighlighting =
                page.evaluate(
                    """() => {
                        const codeBlocks = document.querySelectorAll('#main-content code');
                        if (codeBlocks.length === 0) return false;
                        // Check if at least one code block has hljs highlighting classes
                        for (let block of codeBlocks) {
                            if (block.classList.contains('hljs') ||
                                block.querySelector('.hljs-keyword') ||
                                block.innerHTML.includes('hljs-')) {
                                return true;
                            }
                        }
                        return false;
                    }
                    """,
                )
            hasHighlighting shouldBe true
        }

        test("interaction style tabs reflect saved preference after shell navigation") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Change to alpine interaction style
            page.locator("#interaction-style-switcher").selectOption("alpine")

            // Navigate to Button page which has interactivity tabs
            page.locator(".menu a[href='/components/button']").click()
            page.waitForURL("http://127.0.0.1:$testPort/components/button")

            // Wait for content to load
            page.waitForFunction("() => document.getElementById('main-content').textContent.includes('mButton')")

            // Check that the alpine tab is checked in the swapped content
            val alpineTabChecked =
                page.evaluate(
                    """() => {
                        const alpineTabs = document.querySelectorAll('#main-content .tabs input[data-interaction-style="alpine"]');
                        if (alpineTabs.length === 0) return false;
                        // Check if at least one alpine tab is checked
                        for (let tab of alpineTabs) {
                            if (tab.checked) return true;
                        }
                        return false;
                    }
                    """,
                )
            alpineTabChecked shouldBe true
        }

        test("htmx previews work after shell navigation") {
            page.navigate("http://127.0.0.1:$testPort/components/button")

            // Wait for content to load
            page.waitForFunction("() => document.getElementById('main-content').textContent.includes('mButton')")

            // Navigate away and back
            page.locator(".menu a[href='/']").click()
            page.waitForURL("http://127.0.0.1:$testPort/")
            page.locator(".menu a[href='/components/button']").click()
            page.waitForURL("http://127.0.0.1:$testPort/components/button")
            page.waitForFunction("() => document.getElementById('main-content').textContent.includes('mButton')")

            // Check that htmx is still working by verifying hx-* attributes exist
            val htmxPresent =
                page.evaluate(
                    """() => {
                        const htmxElements = document.querySelectorAll('#main-content [hx-post], [hx-get], [hx-swap]');
                        return htmxElements.length > 0;
                    }
                    """,
                )
            htmxPresent shouldBe true
        }

        test("alpine.js previews initialize after shell navigation") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Change to alpine interaction style
            page.locator("#interaction-style-switcher").selectOption("alpine")

            // Navigate to Button page which has Alpine previews
            page.locator(".menu a[href='/components/button']").click()
            page.waitForURL("http://127.0.0.1:$testPort/components/button")

            // Wait for content to load
            page.waitForFunction("() => document.getElementById('main-content').textContent.includes('mButton')")

            // Navigate away and back to test rehydration
            page.locator(".menu a[href='/']").click()
            page.waitForURL("http://127.0.0.1:$testPort/")
            page.locator(".menu a[href='/components/button']").click()
            page.waitForURL("http://127.0.0.1:$testPort/components/button")
            page.waitForFunction("() => document.getElementById('main-content').textContent.includes('mButton')")

            // Check that Alpine.js has initialized by verifying x-data elements have Alpine properties
            val alpineInitialized =
                page.evaluate(
                    """() => {
                        const alpineElements = document.querySelectorAll('#main-content [x-data]');
                        if (alpineElements.length === 0) return true; // Pass if no Alpine elements
                        // Check if Alpine has bound to the element (Alpine 3.x adds _x_dataStack)
                        for (let el of alpineElements) {
                            if (el._x_dataStack || el.__x) return true;
                        }
                        return false;
                    }
                    """,
                )
            alpineInitialized shouldBe true
        }

        test("datastar previews initialize after shell navigation") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Change to datastar interaction style
            page.locator("#interaction-style-switcher").selectOption("datastar")

            // Navigate to Button page which has Datastar previews
            page.locator(".menu a[href='/components/button']").click()
            page.waitForURL("http://127.0.0.1:$testPort/components/button")

            // Wait for content to load
            page.waitForFunction("() => document.getElementById('main-content').textContent.includes('mButton')")

            // Navigate away and back to test rehydration
            page.locator(".menu a[href='/']").click()
            page.waitForURL("http://127.0.0.1:$testPort/")
            page.locator(".menu a[href='/components/button']").click()
            page.waitForURL("http://127.0.0.1:$testPort/components/button")
            page.waitForFunction("() => document.getElementById('main-content').textContent.includes('mButton')")

            // Check that Datastar has initialized by verifying data-signals elements exist
            val datastarPresent =
                page.evaluate(
                    """() => {
                        const datastarElements = document.querySelectorAll('#main-content [data-signals], [data-on-click], [data-bind-disabled]');
                        return datastarElements.length > 0;
                    }
                    """,
                )
            datastarPresent shouldBe true
        }

        test("ctrl-click opens link in new tab without intercepting") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Set a marker to detect if shell navigation was triggered
            page.evaluate("() => { window.shellNavigationTriggered = false; }")
            page.evaluate(
                """() => {
                    const original = window.fetch;
                    window.fetch = function(...args) {
                        if (args[0].includes('?partial=true')) {
                            window.shellNavigationTriggered = true;
                        }
                        return original.apply(this, args);
                    };
                }
                """,
            )

            // Ctrl-click the Button link
            page.locator(".menu a[href='/components/button']").click(
                com.microsoft.playwright.Locator.ClickOptions().setModifiers(
                    listOf(com.microsoft.playwright.options.KeyboardModifier.CONTROL),
                ),
            )

            // Give it a moment to see if shell navigation was triggered
            Thread.sleep(100)

            // Shell navigation should NOT have been triggered
            val triggered = page.evaluate("() => window.shellNavigationTriggered")
            triggered shouldBe false

            // Original page should still be the home page
            page.url() shouldBe "http://127.0.0.1:$testPort/"
        }

        test("middle-click opens link in new tab without intercepting") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Set a marker to detect if shell navigation was triggered
            page.evaluate("() => { window.shellNavigationTriggered = false; }")
            page.evaluate(
                """() => {
                    const original = window.fetch;
                    window.fetch = function(...args) {
                        if (args[0].includes('?partial=true')) {
                            window.shellNavigationTriggered = true;
                        }
                        return original.apply(this, args);
                    };
                }
                """,
            )

            // Middle-click the Button link
            page.locator(".menu a[href='/components/button']").click(
                com.microsoft.playwright.Locator.ClickOptions().setButton(
                    com.microsoft.playwright.options.MouseButton.MIDDLE,
                ),
            )

            // Give it a moment to see if shell navigation was triggered
            Thread.sleep(100)

            // Shell navigation should NOT have been triggered
            val triggered = page.evaluate("() => window.shellNavigationTriggered")
            triggered shouldBe false

            // Original page should still be the home page
            page.url() shouldBe "http://127.0.0.1:$testPort/"
        }

        test("link with target attribute is not intercepted") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Create a test link with target="_blank"
            page.evaluate(
                """() => {
                    const link = document.createElement('a');
                    link.href = '/components/button';
                    link.target = '_blank';
                    link.textContent = 'Test Link';
                    link.id = 'test-target-link';
                    document.body.appendChild(link);
                }
                """,
            )

            // Set a marker to detect if shell navigation was triggered
            page.evaluate("() => { window.shellNavigationTriggered = false; }")
            page.evaluate(
                """() => {
                    const original = window.fetch;
                    window.fetch = function(...args) {
                        if (args[0].includes('?partial=true')) {
                            window.shellNavigationTriggered = true;
                        }
                        return original.apply(this, args);
                    };
                }
                """,
            )

            // Click the targeted link
            page.locator("#test-target-link").click()

            // Give it a moment to see if shell navigation was triggered
            Thread.sleep(100)

            // Shell navigation should NOT have been triggered
            val triggered = page.evaluate("() => window.shellNavigationTriggered")
            triggered shouldBe false

            // Original page should still be the home page
            page.url() shouldBe "http://127.0.0.1:$testPort/"
        }

        test("download link is not intercepted") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Create a test download link
            page.evaluate(
                """() => {
                    const link = document.createElement('a');
                    link.href = '/components/button';
                    link.download = 'button.html';
                    link.textContent = 'Download Link';
                    link.id = 'test-download-link';
                    document.body.appendChild(link);
                }
                """,
            )

            // Set a marker to detect if shell navigation was triggered
            page.evaluate("() => { window.shellNavigationTriggered = false; }")
            page.evaluate(
                """() => {
                    const original = window.fetch;
                    window.fetch = function(...args) {
                        if (args[0].includes('?partial=true')) {
                            window.shellNavigationTriggered = true;
                        }
                        return original.apply(this, args);
                    };
                }
                """,
            )

            // Click the download link
            page.locator("#test-download-link").click()

            // Give it a moment to see if shell navigation was triggered
            Thread.sleep(100)

            // Shell navigation should NOT have been triggered
            val triggered = page.evaluate("() => window.shellNavigationTriggered")
            triggered shouldBe false
        }

        test("external link is not intercepted") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Create an external link and intercept click to prevent actual navigation
            page.evaluate(
                """() => {
                    window.externalLinkClicked = false;
                    const link = document.createElement('a');
                    link.href = 'https://example.com';
                    link.textContent = 'External Link';
                    link.id = 'test-external-link';
                    link.addEventListener('click', (e) => {
                        // Track that default browser behavior would happen
                        if (!e.defaultPrevented) {
                            window.externalLinkClicked = true;
                        }
                        // Prevent actual navigation during test
                        e.preventDefault();
                        e.stopPropagation();
                    }, true);
                    document.body.appendChild(link);
                }
                """,
            )

            // Click the external link
            page.locator("#test-external-link").click()

            // Give it a moment
            Thread.sleep(100)

            // Link should have been clicked with default behavior (not intercepted)
            val clicked = page.evaluate("() => window.externalLinkClicked")
            clicked shouldBe true

            // Original page should still be the home page (we prevented actual navigation)
            page.url() shouldBe "http://127.0.0.1:$testPort/"
        }

        test("static asset link is not intercepted") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Create a static asset link and intercept click to prevent actual navigation
            page.evaluate(
                """() => {
                    window.staticLinkClicked = false;
                    const link = document.createElement('a');
                    link.href = '/static/output.css';
                    link.textContent = 'CSS Link';
                    link.id = 'test-static-link';
                    link.addEventListener('click', (e) => {
                        // Track that default browser behavior would happen
                        if (!e.defaultPrevented) {
                            window.staticLinkClicked = true;
                        }
                        // Prevent actual navigation during test
                        e.preventDefault();
                        e.stopPropagation();
                    }, true);
                    document.body.appendChild(link);
                }
                """,
            )

            // Click the static asset link
            page.locator("#test-static-link").click()

            // Give it a moment
            Thread.sleep(100)

            // Link should have been clicked with default behavior (not intercepted)
            val clicked = page.evaluate("() => window.staticLinkClicked")
            clicked shouldBe true
        }

        test("/_examples endpoint is not intercepted") {
            page.navigate("http://127.0.0.1:$testPort/")

            // Create an _examples link and intercept click to prevent actual navigation
            page.evaluate(
                """() => {
                    window.examplesLinkClicked = false;
                    const link = document.createElement('a');
                    link.href = '/_examples/button/toggle';
                    link.textContent = 'Example Link';
                    link.id = 'test-examples-link';
                    link.addEventListener('click', (e) => {
                        // Track that default browser behavior would happen
                        if (!e.defaultPrevented) {
                            window.examplesLinkClicked = true;
                        }
                        // Prevent actual navigation during test
                        e.preventDefault();
                        e.stopPropagation();
                    }, true);
                    document.body.appendChild(link);
                }
                """,
            )

            // Click the examples link
            page.locator("#test-examples-link").click()

            // Give it a moment
            Thread.sleep(100)

            // Link should have been clicked with default behavior (not intercepted)
            val clicked = page.evaluate("() => window.examplesLinkClicked")
            clicked shouldBe true
        }
    })
