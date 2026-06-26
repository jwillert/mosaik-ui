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
        p {
            +"The htmx example posts to "
            code { +"/api/login" }
            +". Here's the corresponding Ktor route:"
        }
        pre {
            code {
                +"""
fun Application.module() {
    routing {
        post("/api/login") {
            val params = call.receiveParameters()
            val email = params["email"]
            val password = params["password"]

            // Validate credentials (replace with your auth logic)
            if (email == "user@example.com" && password == "secret") {
                call.response.headers.append("HX-Redirect", "/dashboard")
                call.respondText("Login successful", ContentType.Text.Plain)
            } else {
                call.response.status(HttpStatusCode.Unauthorized)
                call.respondText("Invalid email or password", ContentType.Text.Plain)
            }
        }
    }
}
""".trimIndent()
            }
        }
        p {
            +"The Alpine.js and Datastar examples expect JSON. Here's the route for those:"
        }
        pre {
            code {
                +"""
@Serializable
data class LoginRequest(val email: String, val password: String)

fun Application.module() {
    install(ContentNegotiation) { json() }
    routing {
        post("/api/login") {
            val req = call.receive<LoginRequest>()

            // Validate credentials (replace with your auth logic)
            if (req.email == "user@example.com" && req.password == "secret") {
                call.respondText("OK", ContentType.Text.Plain)
            } else {
                call.response.status(HttpStatusCode.Unauthorized)
                call.respondText("Invalid email or password", ContentType.Text.Plain)
            }
        }
    }
}
""".trimIndent()
            }
        }
        interactivityTabs(
            id = "login-form",
            htmxPreview = {
                mCard("w-96 bg-base-100 shadow-xl") {
                    form {
                        attributes["hx-post"] = "/_examples/login"
                        attributes["hx-target"] = "#login-result"
                        attributes["hx-indicator"] = "#login-spinner"

                        div("card-body") {
                            h2("card-title") { +"Login" }

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

                            div("card-actions justify-end") {
                                mButton(Variant.Primary) {
                                    type = ButtonType.submit
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
            },
            alpinePreview = {
                mCard("w-96 bg-base-100 shadow-xl") {
                    form {
                        attributes["x-data"] = "{ email: '', password: '', loading: false, error: '' }"
                        attributes["x-on:submit.prevent"] = "loading = true; error = ''; " +
                            "fetch('/_examples/login', { method: 'POST', headers: { 'Content-Type': 'application/json' }, " +
                            "body: JSON.stringify({ email, password }) }).then(r => r.ok ? alert('Login successful!') : " +
                            "r.text().then(t => error = t)).catch(e => error = 'Network error').finally(() => loading = false)"

                        div("card-body") {
                            h2("card-title") { +"Login" }

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

                            div("card-actions justify-end") {
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
            },
            datastarPreview = {
                mCard("w-96 bg-base-100 shadow-xl") {
                    form {
                        attributes["data-on-submit"] = "\$\$post('/_examples/login')"
                        attributes["data-store"] = "{ email: '', password: '', loading: false, error: '' }"

                        div("card-body") {
                            h2("card-title") { +"Login" }

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
                                attributes["data-show"] = "\$error"
                                attributes["data-text"] = "\$error"
                                attributes["role"] = "alert"
                            }

                            div("card-actions justify-end") {
                                mButton(Variant.Primary) {
                                    attributes["data-bind-disabled"] = "\$loading"
                                    span {
                                        attributes["data-show"] = "!\$loading"
                                        +"Sign in"
                                    }
                                    span("loading loading-spinner loading-sm") {
                                        attributes["data-show"] = "\$loading"
                                    }
                                }
                            }
                        }
                    }
                }
            },
            htmxCode = """
mCard("w-96 bg-base-100 shadow-xl") {
    form {
        attributes["hx-post"] = "/api/login"
        attributes["hx-target"] = "#login-result"
        attributes["hx-indicator"] = "#login-spinner"

        div("card-body") {
            h2("card-title") { +"Login" }

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

            div("card-actions justify-end") {
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

        div("card-body") {
            h2("card-title") { +"Login" }

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

            div("card-actions justify-end") {
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

        div("card-body") {
            h2("card-title") { +"Login" }

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

            div("card-actions justify-end") {
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
        p {
            +"The htmx example posts to "
            code { +"/api/register" }
            +". Here's the corresponding Ktor route:"
        }
        pre {
            code {
                +"""
fun Application.module() {
    routing {
        post("/api/register") {
            val params = call.receiveParameters()
            val name = params["name"]
            val email = params["email"]
            val password = params["password"]
            val confirmPassword = params["confirm_password"]

            // Validate passwords match
            if (password != confirmPassword) {
                call.response.status(HttpStatusCode.BadRequest)
                call.respondText("Passwords do not match", ContentType.Text.Plain)
                return@post
            }

            // Validate password strength (example: minimum 8 characters)
            if (password.isNullOrBlank() || password.length < 8) {
                call.response.status(HttpStatusCode.BadRequest)
                call.respondText("Password must be at least 8 characters", ContentType.Text.Plain)
                return@post
            }

            // Register user (replace with your registration logic)
            // saveUser(name, email, password)

            call.response.headers.append("HX-Redirect", "/welcome")
            call.respondText("Registration successful", ContentType.Text.Plain)
        }
    }
}
""".trimIndent()
            }
        }
        p {
            +"The Alpine.js and Datastar examples expect JSON. Here's the route for those:"
        }
        pre {
            code {
                +"""
@Serializable
data class RegisterRequest(val name: String, val email: String, val password: String)

fun Application.module() {
    install(ContentNegotiation) { json() }
    routing {
        post("/api/register") {
            val req = call.receive<RegisterRequest>()

            // Validate password strength (example: minimum 8 characters)
            if (req.password.length < 8) {
                call.response.status(HttpStatusCode.BadRequest)
                call.respondText("Password must be at least 8 characters", ContentType.Text.Plain)
                return@post
            }

            // Register user (replace with your registration logic)
            // saveUser(req.name, req.email, req.password)

            call.respondText("OK", ContentType.Text.Plain)
        }
    }
}
""".trimIndent()
            }
        }
        interactivityTabs(
            id = "register-form",
            htmxPreview = {
                mCard("w-96 bg-base-100 shadow-xl") {
                    form {
                        attributes["hx-post"] = "/_examples/register"
                        attributes["hx-target"] = "#register-result"
                        attributes["hx-indicator"] = "#register-spinner"

                        div("card-body") {
                            h2("card-title") { +"Create account" }

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

                            div("card-actions justify-end") {
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
            },
            alpinePreview = {
                mCard("w-96 bg-base-100 shadow-xl") {
                    form {
                        attributes["x-data"] = "{ name: '', email: '', password: '', confirmPassword: '', loading: false, error: '' }"
                        attributes["x-on:submit.prevent"] = "if (password !== confirmPassword) { error = 'Passwords do not match'; return; } " +
                            "loading = true; error = ''; " +
                            "fetch('/_examples/register', { method: 'POST', headers: { 'Content-Type': 'application/json' }, " +
                            "body: JSON.stringify({ name, email, password }) }).then(r => r.ok ? alert('Registration successful!') : " +
                            "r.text().then(t => error = t)).catch(e => error = 'Network error').finally(() => loading = false)"

                        div("card-body") {
                            h2("card-title") { +"Create account" }

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

                            div("card-actions justify-end") {
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
            },
            datastarPreview = {
                mCard("w-96 bg-base-100 shadow-xl") {
                    form {
                        attributes["data-on-submit"] = "if (\$password !== \$confirmPassword) { \$error = 'Passwords do not match'; return; } \$\$post('/_examples/register')"
                        attributes["data-store"] = "{ name: '', email: '', password: '', confirmPassword: '', loading: false, error: '' }"

                        div("card-body") {
                            h2("card-title") { +"Create account" }

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
                                attributes["data-show"] = "\$error"
                                attributes["data-text"] = "\$error"
                                attributes["role"] = "alert"
                            }

                            div("card-actions justify-end") {
                                mButton(Variant.Primary) {
                                    attributes["data-bind-disabled"] = "\$loading"
                                    span {
                                        attributes["data-show"] = "!\$loading"
                                        +"Sign up"
                                    }
                                    span("loading loading-spinner loading-sm") {
                                        attributes["data-show"] = "\$loading"
                                    }
                                }
                            }
                        }
                    }
                }
            },
            htmxCode = """
mCard("w-96 bg-base-100 shadow-xl") {
    form {
        attributes["hx-post"] = "/api/register"
        attributes["hx-target"] = "#register-result"
        attributes["hx-indicator"] = "#register-spinner"

        div("card-body") {
            h2("card-title") { +"Create account" }

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

            div("card-actions justify-end") {
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

        div("card-body") {
            h2("card-title") { +"Create account" }

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

            div("card-actions justify-end") {
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
        attributes["data-on-submit"] = "if (${'$'}password !== ${'$'}confirmPassword) { ${'$'}error = 'Passwords do not match'; return; } ${'$'}${'$'}post('/api/register')"
        attributes["data-store"] = "{ name: '', email: '', password: '', confirmPassword: '', loading: false, error: '' }"

        div("card-body") {
            h2("card-title") { +"Create account" }

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

            div("card-actions justify-end") {
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
    h2 { +"Building blocks" }
    p {
        +"Before diving into examples, here's an overview of each library's core primitives, "
        +"best-fit use cases, and limitations."
    }

    h3 { +"htmx" }
    p {
        +"htmx extends HTML with attributes that trigger AJAX requests and swap content. "
        +"Server-driven: the server renders HTML fragments, htmx swaps them into the DOM."
    }
    ul {
        li {
            strong { +"Core primitives: " }
            code { +"hx-get" }
            +", "
            code { +"hx-post" }
            +", "
            code { +"hx-target" }
            +", "
            code { +"hx-swap" }
            +", "
            code { +"hx-trigger" }
        }
        li {
            strong { +"Best for: " }
            +"Server-rendered apps, progressive enhancement, form submissions, live search"
        }
        li {
            strong { +"Script size: " }
            +"~14KB minified + gzipped"
        }
        li {
            strong { +"Limitations: " }
            +"Requires server endpoints for every interaction; not ideal for complex client-side state"
        }
    }
    p {
        +"Note: This project uses Ktor 3.1.3, so htmx attributes are set via the raw attributes map. "
        +"Ktor 3.2 adds typed extensions."
    }
    pre("bg-base-200 rounded-box p-4 overflow-x-auto") {
        code {
            +"""
            // Ktor 3.1.3 syntax
            attributes["hx-post"] = "/endpoint"

            // Ktor 3.2+ syntax
            hxPost = "/endpoint"
            """.trimIndent()
        }
    }

    h3 { +"Alpine.js" }
    p {
        +"Alpine.js brings reactive client-side state to HTML with inline directives. "
        +"Client-side: state lives in JavaScript, DOM updates automatically on change."
    }
    ul {
        li {
            strong { +"Core primitives: " }
            code { +"x-data" }
            +", "
            code { +"x-show" }
            +", "
            code { +"x-on" }
            +", "
            code { +"x-bind" }
            +", "
            code { +"x-model" }
        }
        li {
            strong { +"Best for: " }
            +"Dropdowns, modals, tabs, toggles, form validation — anything with local UI state"
        }
        li {
            strong { +"Script size: " }
            +"~15KB minified + gzipped"
        }
        li {
            strong { +"Limitations: " }
            +"Not built for cross-component or persistent state; no built-in server sync"
        }
    }

    h3 { +"Datastar" }
    p {
        +"Datastar merges server-driven updates with client-side reactivity via Server-Sent Events (SSE). "
        +"Hybrid: server pushes HTML fragments over a persistent connection, client-side store reacts to changes."
    }
    ul {
        li {
            strong { +"Core primitives: " }
            code { +"data-store" }
            +", "
            code { +"data-on" }
            +", "
            code { +"data-bind" }
            +", SSE fragments"
        }
        li {
            strong { +"Best for: " }
            +"Real-time dashboards, live notifications, collaborative editing, streaming updates"
        }
        li {
            strong { +"Script size: " }
            +"~20KB minified + gzipped"
        }
        li {
            strong { +"Limitations: " }
            +"Requires SSE server-side support; less mature ecosystem than htmx or Alpine"
        }
    }
    p {
        +"Note: Datastar requires a Server-Sent Events endpoint on the server to push updates."
    }

    h2 { +"Comparison" }
    p {
        +"Choosing between htmx, Alpine.js, and Datastar depends on your app's needs."
    }
    div("overflow-x-auto") {
        table("table table-zebra") {
            thead {
                tr {
                    th { +"Library" }
                    th { +"Paradigm" }
                    th { +"Server requirement" }
                    th { +"Script size" }
                    th { +"When to choose" }
                }
            }
            tbody {
                tr {
                    td { strong { +"htmx" } }
                    td { +"Server-driven" }
                    td { +"HTML endpoints" }
                    td { +"~14KB" }
                    td { +"Server renders everything; progressive enhancement; no complex client state" }
                }
                tr {
                    td { strong { +"Alpine.js" } }
                    td { +"Client-side" }
                    td { +"None (static HTML)" }
                    td { +"~15KB" }
                    td { +"Local UI state (dropdowns, modals, toggles); no server round-trips needed" }
                }
                tr {
                    td { strong { +"Datastar" } }
                    td { +"Hybrid (SSE-based)" }
                    td { +"SSE endpoints" }
                    td { +"~20KB" }
                    td { +"Real-time updates; server pushes changes; collaborative or live-data features" }
                }
            }
        }
    }
}

/**
 * Renders DaisyUI CSS-only radio tabs for toggling between htmx, Alpine.js,
 * and Datastar code examples. Each tab group uses the [id] parameter as the
 * unique radio group `name` to prevent interference between multiple tab
 * groups on the same page. The htmx tab is checked by default. The wrapper
 * carries the `not-prose` class to prevent Tailwind Typography from
 * overriding DaisyUI's tab styling (ADR-0006).
 *
 * Each tab can optionally show a rendered Preview above the code snippet
 * (ADR-0008). The preview content is provided as a lambda that receives a
 * FlowContent receiver, allowing each style to render its own working example.
 */
fun FlowContent.interactivityTabs(
    id: String,
    htmxCode: String,
    alpineCode: String,
    datastarCode: String,
    htmxPreview: (FlowContent.() -> Unit)? = null,
    alpinePreview: (FlowContent.() -> Unit)? = null,
    datastarPreview: (FlowContent.() -> Unit)? = null,
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
            if (htmxPreview != null) {
                div("mb-4 not-prose") {
                    htmxPreview()
                }
            }
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
            if (alpinePreview != null) {
                div("mb-4 not-prose") {
                    alpinePreview()
                }
            }
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
            if (datastarPreview != null) {
                div("mb-4 not-prose") {
                    datastarPreview()
                }
            }
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
