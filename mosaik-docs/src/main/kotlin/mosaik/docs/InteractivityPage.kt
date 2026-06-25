package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.Variant
import mosaik.ui.components.mButton
import mosaik.ui.components.mCard
import mosaik.ui.components.mCardActions
import mosaik.ui.components.mCardBody
import mosaik.ui.components.mCardTitle

/**
 * The interactivity guide: how to add client-side behavior (htmx, Alpine.js,
 * Datastar) to components without writing new Kotlin code. Each example shows
 * the same feature implemented in all three libraries, toggled by DaisyUI CSS
 * radio tabs (ADR-0006).
 */
fun interactivityPage(): String = layout(INTERACTIVITY) {
    h1 { +"Interactivity" }
    p {
        +"Mosaik components are CSS-only — they carry no built-in JavaScript. To "
        +"add client-side behavior (form validation, dropdown menus, live search) "
        +"you reach for a JavaScript library. This guide shows the same examples "
        +"built with htmx, Alpine.js, and Datastar, so you can compare patterns and "
        +"pick the one that fits."
    }

    section {
        h2 { +"Login form" }
        p {
            +"A login form built with "
            code { +"mCard" }
            +", "
            code { +"mButton" }
            +", and raw HTML form inputs. Each library wires client-side behavior "
            +"(async submit, loading state, error display) onto the same Kotlin code."
        }
        interactivityTabs(
            id = "login-form",
            htmxCode = """
mCard("w-96 bg-base-100 shadow-xl") {
    form {
        attributes["hx-post"] = "/api/login"
        attributes["hx-target"] = "#login-result"
        attributes["hx-indicator"] = "#login-spinner"

        mCardBody {
            mCardTitle { +"Login" }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Email" }
                }
                input(type = InputType.email, name = "email", classes = "input input-bordered w-full") {
                    required = true
                }
            }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Password" }
                }
                input(type = InputType.password, name = "password", classes = "input input-bordered w-full") {
                    required = true
                }
            }

            div("mt-2") {
                id = "login-result"
                attributes["role"] = "region"
                attributes["aria-live"] = "polite"
            }

            mCardActions("justify-end") {
                mButton(Variant.Primary) {
                    span { +"Sign in" }
                    span("loading loading-spinner loading-sm htmx-indicator") {
                        id = "login-spinner"
                        attributes["style"] = "display:none;"
                    }
                }
            }
        }
    }
}
""".trimIndent(),
            alpineCode = """
mCard("w-96 bg-base-100 shadow-xl") {
    form {
        attributes["x-data"] = "{ email: '', password: '', loading: false, error: '' }"
        attributes["x-on:submit.prevent"] = "loading = true; error = ''; " +
            "fetch('/api/login', { method: 'POST', headers: { 'Content-Type': 'application/json' }, " +
            "body: JSON.stringify({ email, password }) }).then(r => r.ok ? location.href = '/dashboard' : " +
            "r.text().then(t => error = t)).catch(e => error = 'Network error').finally(() => loading = false)"

        mCardBody {
            mCardTitle { +"Login" }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Email" }
                }
                input(type = InputType.email, name = "email", classes = "input input-bordered w-full") {
                    required = true
                    attributes["x-model"] = "email"
                }
            }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Password" }
                }
                input(type = InputType.password, name = "password", classes = "input input-bordered w-full") {
                    required = true
                    attributes["x-model"] = "password"
                }
            }

            div("mt-2 text-error text-sm") {
                attributes["x-show"] = "error"
                attributes["x-text"] = "error"
                attributes["role"] = "alert"
            }

            mCardActions("justify-end") {
                mButton(Variant.Primary) {
                    attributes["x-bind:disabled"] = "loading"
                    span {
                        attributes["x-show"] = "!loading"
                        +"Sign in"
                    }
                    span("loading loading-spinner loading-sm") {
                        attributes["x-show"] = "loading"
                    }
                }
            }
        }
    }
}
""".trimIndent(),
            datastarCode = """
mCard("w-96 bg-base-100 shadow-xl") {
    form {
        attributes["data-on-submit"] = "${'$'}${'$'}post('/api/login')"
        attributes["data-store"] = "{ loading: false, error: '' }"

        mCardBody {
            mCardTitle { +"Login" }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Email" }
                }
                input(type = InputType.email, name = "email", classes = "input input-bordered w-full") {
                    required = true
                    attributes["data-model"] = "email"
                }
            }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Password" }
                }
                input(type = InputType.password, name = "password", classes = "input input-bordered w-full") {
                    required = true
                    attributes["data-model"] = "password"
                }
            }

            div("mt-2 text-error text-sm") {
                attributes["data-show"] = "${'$'}error"
                attributes["data-text"] = "${'$'}error"
                attributes["role"] = "alert"
            }

            mCardActions("justify-end") {
                mButton(Variant.Primary) {
                    attributes["data-bind-disabled"] = "${'$'}loading"
                    span {
                        attributes["data-show"] = "!${'$'}loading"
                        +"Sign in"
                    }
                    span("loading loading-spinner loading-sm") {
                        attributes["data-show"] = "${'$'}loading"
                    }
                }
            }
        }
    }
}
""".trimIndent(),
        )
    }

    section {
        h2 { +"Register form" }
        p {
            +"A registration form with multiple fields, validation, and error "
            +"handling. Built with "
            code { +"mCard" }
            +" and "
            code { +"mButton" }
            +", the form wiring is identical to Login — only the fields change."
        }
        interactivityTabs(
            id = "register-form",
            htmxCode = """
mCard("w-96 bg-base-100 shadow-xl") {
    form {
        attributes["hx-post"] = "/api/register"
        attributes["hx-target"] = "#register-result"
        attributes["hx-indicator"] = "#register-spinner"

        mCardBody {
            mCardTitle { +"Create account" }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Name" }
                }
                input(type = InputType.text, name = "name", classes = "input input-bordered w-full") {
                    required = true
                }
            }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Email" }
                }
                input(type = InputType.email, name = "email", classes = "input input-bordered w-full") {
                    required = true
                }
            }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Password" }
                }
                input(type = InputType.password, name = "password", classes = "input input-bordered w-full") {
                    required = true
                    attributes["minlength"] = "8"
                }
            }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Confirm password" }
                }
                input(type = InputType.password, name = "confirm_password", classes = "input input-bordered w-full") {
                    required = true
                }
            }

            div("mt-2") {
                id = "register-result"
                attributes["role"] = "region"
                attributes["aria-live"] = "polite"
            }

            mCardActions("justify-end") {
                mButton(Variant.Primary) {
                    span { +"Sign up" }
                    span("loading loading-spinner loading-sm htmx-indicator") {
                        id = "register-spinner"
                        attributes["style"] = "display:none;"
                    }
                }
            }
        }
    }
}
""".trimIndent(),
            alpineCode = """
mCard("w-96 bg-base-100 shadow-xl") {
    form {
        attributes["x-data"] = "{ name: '', email: '', password: '', confirmPassword: '', loading: false, error: '' }"
        attributes["x-on:submit.prevent"] = "if (password !== confirmPassword) { error = 'Passwords do not match'; return; } " +
            "loading = true; error = ''; " +
            "fetch('/api/register', { method: 'POST', headers: { 'Content-Type': 'application/json' }, " +
            "body: JSON.stringify({ name, email, password }) }).then(r => r.ok ? location.href = '/welcome' : " +
            "r.text().then(t => error = t)).catch(e => error = 'Network error').finally(() => loading = false)"

        mCardBody {
            mCardTitle { +"Create account" }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Name" }
                }
                input(type = InputType.text, name = "name", classes = "input input-bordered w-full") {
                    required = true
                    attributes["x-model"] = "name"
                }
            }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Email" }
                }
                input(type = InputType.email, name = "email", classes = "input input-bordered w-full") {
                    required = true
                    attributes["x-model"] = "email"
                }
            }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Password" }
                }
                input(type = InputType.password, name = "password", classes = "input input-bordered w-full") {
                    required = true
                    attributes["minlength"] = "8"
                    attributes["x-model"] = "password"
                }
            }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Confirm password" }
                }
                input(type = InputType.password, name = "confirm_password", classes = "input input-bordered w-full") {
                    required = true
                    attributes["x-model"] = "confirmPassword"
                }
            }

            div("mt-2 text-error text-sm") {
                attributes["x-show"] = "error"
                attributes["x-text"] = "error"
                attributes["role"] = "alert"
            }

            mCardActions("justify-end") {
                mButton(Variant.Primary) {
                    attributes["x-bind:disabled"] = "loading"
                    span {
                        attributes["x-show"] = "!loading"
                        +"Sign up"
                    }
                    span("loading loading-spinner loading-sm") {
                        attributes["x-show"] = "loading"
                    }
                }
            }
        }
    }
}
""".trimIndent(),
            datastarCode = """
mCard("w-96 bg-base-100 shadow-xl") {
    form {
        attributes["data-on-submit"] = "${'$'}${'$'}post('/api/register')"
        attributes["data-store"] = "{ name: '', email: '', password: '', confirmPassword: '', loading: false, error: '' }"

        mCardBody {
            mCardTitle { +"Create account" }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Name" }
                }
                input(type = InputType.text, name = "name", classes = "input input-bordered w-full") {
                    required = true
                    attributes["data-model"] = "name"
                }
            }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Email" }
                }
                input(type = InputType.email, name = "email", classes = "input input-bordered w-full") {
                    required = true
                    attributes["data-model"] = "email"
                }
            }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Password" }
                }
                input(type = InputType.password, name = "password", classes = "input input-bordered w-full") {
                    required = true
                    attributes["minlength"] = "8"
                    attributes["data-model"] = "password"
                }
            }

            label("form-control w-full") {
                div("label") {
                    span("label-text") { +"Confirm password" }
                }
                input(type = InputType.password, name = "confirm_password", classes = "input input-bordered w-full") {
                    required = true
                    attributes["data-model"] = "confirmPassword"
                }
            }

            div("mt-2 text-error text-sm") {
                attributes["data-show"] = "${'$'}error"
                attributes["data-text"] = "${'$'}error"
                attributes["role"] = "alert"
            }

            mCardActions("justify-end") {
                mButton(Variant.Primary) {
                    attributes["data-bind-disabled"] = "${'$'}loading"
                    span {
                        attributes["data-show"] = "!${'$'}loading"
                        +"Sign up"
                    }
                    span("loading loading-spinner loading-sm") {
                        attributes["data-show"] = "${'$'}loading"
                    }
                }
            }
        }
    }
}
""".trimIndent(),
        )
    }
}

/**
 * Renders DaisyUI CSS-only radio tabs for toggling between htmx, Alpine.js,
 * and Datastar code examples. Each tab group uses the [id] parameter as the
 * unique radio group `name` to prevent interference between multiple tab
 * groups on the same page. The htmx tab is checked by default. The wrapper
 * carries the `not-prose` class to prevent Tailwind Typography from
 * overriding DaisyUI's tab styling (ADR-0006).
 */
fun FlowContent.interactivityTabs(
    id: String,
    htmxCode: String,
    alpineCode: String,
    datastarCode: String,
) {
    div("tabs tabs-lifted not-prose") {
        attributes["role"] = "tablist"

        // htmx tab (checked by default)
        input(type = InputType.radio, name = id, classes = "tab") {
            this.id = "$id-htmx"
            checked = true
            attributes["aria-label"] = "htmx"
        }
        div("tab-content bg-base-200 rounded-box p-4") {
            pre {
                code { +htmxCode }
            }
        }

        // Alpine.js tab
        input(type = InputType.radio, name = id, classes = "tab") {
            this.id = "$id-alpine"
            attributes["aria-label"] = "Alpine.js"
        }
        div("tab-content bg-base-200 rounded-box p-4") {
            pre {
                code { +alpineCode }
            }
        }

        // Datastar tab
        input(type = InputType.radio, name = id, classes = "tab") {
            this.id = "$id-datastar"
            attributes["aria-label"] = "Datastar"
        }
        div("tab-content bg-base-200 rounded-box p-4") {
            pre {
                code { +datastarCode }
            }
        }
    }
}

/**
 * A test page that renders the [interactivityTabs] helper with example code
 * blocks, so smoke tests can verify the tab HTML structure without depending
 * on the interactivity guide content.
 */
fun interactivityTabsTestPage(): String = layout(INTERACTIVITY) {
    h1 { +"Test page" }
    interactivityTabs(
        id = "test-tabs",
        htmxCode = "htmx example",
        alpineCode = "alpine example",
        datastarCode = "datastar example",
    )
}
