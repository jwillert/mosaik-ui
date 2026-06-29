package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.ButtonVariant
import mosaik.ui.components.LoadingType
import mosaik.ui.components.Size
import mosaik.ui.components.mButton
import mosaik.ui.components.mCard
import mosaik.ui.components.mCardActions
import mosaik.ui.components.mCardBody
import mosaik.ui.components.mCardTitle
import mosaik.ui.components.mFormControl
import mosaik.ui.components.mInput
import mosaik.ui.components.mLabel
import mosaik.ui.components.mLabelText
import mosaik.ui.components.mLoading
import mosaik.ui.components.mTable

const val DEFAULT_INTERACTIVITY_PAGE_VARIANT_ID = "htmx"

private data class InteractivityPageVariant(
    val id: String,
    val label: String,
    val login: Recipe,
    val register: Recipe,
)

private data class Recipe(
    val intro: String,
    val serverRoute: String,
    val pageMarkup: String,
    val notes: List<String>,
    val preview: FlowContent.() -> Unit,
)

/** Interactivity page content block for use in both full-page and partial renders. */
fun FlowContent.interactivityPageContent(variant: String? = DEFAULT_INTERACTIVITY_PAGE_VARIANT_ID) {
    val selectedVariant = interactivityPageVariant(variant)

    h1 { +"Interactivity" }
    p {
        +"Mosaik components are CSS-only — they carry no built-in JavaScript. To add "
        +"client-side behavior (form validation, dropdown menus, live search) you pair "
        +"the same Kotlin markup with a small client library. This guide is rendered as "
        +"page-local variants so the URL shows one practical stack at a time."
    }
    interactivityPageVariantSelector(selectedVariant)

    recipeSection(
        id = "login-form",
        title = "Login form",
        composition = {
            +"A login form built with "
            code { +"mCard" }
            +", "
            code { +"mButton" }
            +", and raw HTML form inputs. The selected variant wires async submit, "
            +"loading state, and result display onto that same component composition."
        },
        recipe = selectedVariant.login,
    )

    recipeSection(
        id = "register-form",
        title = "Register form",
        composition = {
            +"A registration form with multiple fields, validation, and error handling. "
            +"It uses the same "
            code { +"mCard" }
            +" and "
            code { +"mButton" }
            +" structure as Login while the behavior layer changes per variant."
        },
        recipe = selectedVariant.register,
    )

    buildingBlocksSection(selectedVariant)
    comparisonSection()
}

private fun interactivityPageVariant(id: String?): InteractivityPageVariant =
    INTERACTIVITY_PAGE_VARIANTS.firstOrNull { it.id == id }
        ?: INTERACTIVITY_PAGE_VARIANTS.first { it.id == DEFAULT_INTERACTIVITY_PAGE_VARIANT_ID }

private fun FlowContent.interactivityPageVariantSelector(selectedVariant: InteractivityPageVariant) {
    nav(classes = "not-prose mb-6 flex flex-wrap items-center gap-2") {
        attributes["aria-label"] = "Page variant"
        span("text-sm font-medium") { +"Page variant" }
        INTERACTIVITY_PAGE_VARIANTS.forEach { variant ->
            a(classes = "rounded border border-base-300 px-3 py-1 text-sm hover:bg-base-200") {
                href = "${INTERACTIVITY.path}?variant=${variant.id}"
                attributes["data-page-variant"] = variant.id
                if (variant.id == selectedVariant.id) {
                    attributes["aria-current"] = "page"
                    classes += " bg-base-200"
                }
                +variant.label
            }
        }
    }
}

private fun FlowContent.recipeSection(
    id: String,
    title: String,
    composition: FlowContent.() -> Unit,
    recipe: Recipe,
) {
    section("mb-12") {
        attributes["data-recipe-section"] = id
        h2 { +title }
        p { composition.invoke(this) }
        p { +recipe.intro }

        h3 { +"Preview" }
        div("not-prose mb-6") { recipe.preview.invoke(this) }

        h3 { +"Server route" }
        codeBlock(recipe.serverRoute)

        h3 { +"Page markup" }
        codeBlock(recipe.pageMarkup)

        h3 { +"Behavior notes" }
        ul {
            recipe.notes.forEach { note -> li { +note } }
        }
    }
}

private val htmxLoginRecipe =
    Recipe(
        intro =
            "htmx keeps the browser behavior declarative: the form posts as regular form data " +
                "and swaps the server response into a result region.",
        serverRoute =
            """
            post("/api/login") {
                val params = call.receiveParameters()
                if (params["email"] == "user@example.com" && params["password"] == "secret") {
                    call.response.headers.append("HX-Redirect", "/dashboard")
                    call.respondText("Login successful", ContentType.Text.Plain)
                } else {
                    call.response.status(HttpStatusCode.Unauthorized)
                    call.respondText("Invalid email or password", ContentType.Text.Plain)
                }
            }
            """.trimIndent(),
        pageMarkup =
            """
            mCard("w-96 bg-base-100 shadow-xl") {
                form {
                    attributes["hx-post"] = "/api/login"
                    attributes["hx-target"] = "#login-result"
                    attributes["hx-indicator"] = "#login-spinner"
                    // fields...
                    mButton(variant = ButtonVariant.Primary) { +"Sign in" }
                }
            }
            """.trimIndent(),
        notes =
            listOf(
                "Use htmx when the server can render the next HTML fragment.",
                "The loading spinner is controlled by the htmx-indicator class.",
            ),
        preview = { loginPreview("htmx") },
    )

private val alpineLoginRecipe =
    Recipe(
        intro = "Alpine.js owns local state in the page and calls fetch from an x-on submit handler.",
        serverRoute =
            """
            @Serializable
            data class LoginRequest(val email: String, val password: String)

            post("/api/login") {
                val req = call.receive<LoginRequest>()
                if (req.email == "user@example.com" && req.password == "secret") {
                    call.respondText("OK", ContentType.Text.Plain)
                } else {
                    call.response.status(HttpStatusCode.Unauthorized)
                    call.respondText("Invalid email or password", ContentType.Text.Plain)
                }
            }
            """.trimIndent(),
        pageMarkup =
            """
            form {
                attributes["x-data"] = "{ email: '', password: '', loading: false, error: '' }"
                attributes["x-on:submit.prevent"] =
                    "loading = true; error = ''; " +
                        "fetch('/api/login', { method: 'POST', headers: { 'Content-Type': 'application/json' }, " +
                        "body: JSON.stringify({ email, password }) })"
                // fields use x-model; button uses x-bind:disabled
            }
            """.trimIndent(),
        notes =
            listOf(
                "Use Alpine.js when validation or UI state can stay local to the form.",
                "The endpoint can return JSON or plain text because Alpine handles the response explicitly.",
            ),
        preview = { loginPreview("alpine") },
    )

private val datastarLoginRecipe =
    Recipe(
        intro = "Datastar stores form state in signals and submits with data-on-submit.",
        serverRoute =
            """
            post("/api/login") {
                val params = call.receiveParameters()
                call.respondText("Login successful", ContentType.Text.Plain)
            }
            """.trimIndent(),
        pageMarkup =
            """
            form {
                attributes["data-signals"] = "{ email: '', password: '', loading: false, error: '' }"
                attributes["data-on-submit"] = "${'$'}${'$'}post('/api/login')"
                // fields use data-model; button uses data-bind-disabled
            }
            """.trimIndent(),
        notes =
            listOf(
                "Use Datastar when the same page also needs signal-driven or SSE updates.",
                "Keep signal names local to the recipe to avoid collisions.",
            ),
        preview = { loginPreview("datastar") },
    )

private val htmxRegisterRecipe =
    Recipe(
        intro = "htmx submits all fields as form data and lets the server enforce validation rules.",
        serverRoute =
            """
            post("/api/register") {
                val params = call.receiveParameters()
                if (params["password"] != params["confirm_password"]) {
                    call.response.status(HttpStatusCode.BadRequest)
                    call.respondText("Passwords do not match", ContentType.Text.Plain)
                    return@post
                }
                call.response.headers.append("HX-Redirect", "/welcome")
                call.respondText("Registration successful", ContentType.Text.Plain)
            }
            """.trimIndent(),
        pageMarkup =
            """
            form {
                attributes["hx-post"] = "/api/register"
                attributes["hx-target"] = "#register-result"
                attributes["hx-indicator"] = "#register-spinner"
                // fields...
            }
            """.trimIndent(),
        notes =
            listOf(
                "Server validation remains authoritative.",
                "The response region doubles as an accessible live region.",
            ),
        preview = { registerPreview("htmx") },
    )

private val alpineRegisterRecipe =
    Recipe(
        intro = "Alpine.js performs immediate password confirmation before calling the server.",
        serverRoute =
            """
            @Serializable
            data class RegisterRequest(val name: String, val email: String, val password: String)

            post("/api/register") {
                val req = call.receive<RegisterRequest>()
                if (req.password.length < 8) {
                    call.response.status(HttpStatusCode.BadRequest)
                    call.respondText("Password must be at least 8 characters", ContentType.Text.Plain)
                    return@post
                }
                call.respondText("OK", ContentType.Text.Plain)
            }
            """.trimIndent(),
        pageMarkup =
            """
            form {
                attributes["x-data"] =
                    "{ name: '', email: '', password: '', confirmPassword: '', loading: false, error: '' }"
                attributes["x-on:submit.prevent"] =
                    "if (password !== confirmPassword) { error = 'Passwords do not match'; return; } " +
                        "loading = true; error = ''; " +
                        "fetch('/api/register', { method: 'POST', headers: { 'Content-Type': 'application/json' }, " +
                        "body: JSON.stringify({ name, email, password }) })"
                // fields use x-model
            }
            """.trimIndent(),
        notes =
            listOf(
                "Client validation improves feedback but does not replace server validation.",
                "Use x-model when multiple controls feed the same submit handler.",
            ),
        preview = { registerPreview("alpine") },
    )

private val datastarRegisterRecipe =
    Recipe(
        intro = "Datastar keeps fields as signals and can reject mismatched passwords before posting.",
        serverRoute =
            """
            post("/api/register") {
                val params = call.receiveParameters()
                call.respondText("Registration successful", ContentType.Text.Plain)
            }
            """.trimIndent(),
        pageMarkup =
            """
            form {
                attributes["data-signals"] =
                    "{ name: '', email: '', password: '', confirmPassword: '', loading: false, error: '' }"
                attributes["data-on-submit"] =
                    "if (${'$'}password !== ${'$'}confirmPassword) { ${'$'}error = 'Passwords do not match'; return; } " +
                        "${'$'}${'$'}post('/api/register')"
                // fields use data-model
            }
            """.trimIndent(),
        notes =
            listOf(
                "Signals keep behavior declarative in markup.",
                "Pair this with SSE endpoints when the registration flow needs live server feedback.",
            ),
        preview = { registerPreview("datastar") },
    )

private val INTERACTIVITY_PAGE_VARIANTS =
    listOf(
        InteractivityPageVariant(
            id = "htmx",
            label = "htmx",
            login = htmxLoginRecipe,
            register = htmxRegisterRecipe,
        ),
        InteractivityPageVariant(
            id = "alpine",
            label = "Alpine.js",
            login = alpineLoginRecipe,
            register = alpineRegisterRecipe,
        ),
        InteractivityPageVariant(
            id = "datastar",
            label = "Datastar",
            login = datastarLoginRecipe,
            register = datastarRegisterRecipe,
        ),
    )

val INTERACTIVITY_PAGE_VARIANT_IDS: List<String> = INTERACTIVITY_PAGE_VARIANTS.map { it.id }

private fun FlowContent.loginPreview(style: String) {
    mCard("w-96 bg-base-100 shadow-xl") {
        form {
            when (style) {
                "htmx" -> {
                    attributes["hx-post"] = "/_examples/login"
                    attributes["hx-target"] = "#login-result"
                    attributes["hx-indicator"] = "#login-spinner"
                }
                "alpine" -> {
                    attributes["x-data"] = "{ email: '', password: '', loading: false, error: '' }"
                    attributes["x-on:submit.prevent"] =
                        "loading = true; error = ''; " +
                        "fetch('/_examples/login', { method: 'POST', " +
                        "headers: { 'Content-Type': 'application/json' }, " +
                        "body: JSON.stringify({ email, password }) })" +
                        ".then(r => r.ok ? alert('Login successful!') : r.text().then(t => error = t))" +
                        ".catch(() => error = 'Network error').finally(() => loading = false)"
                }
                "datastar" -> {
                    attributes["data-signals"] = "{ email: '', password: '', loading: false, error: '' }"
                    attributes["data-on-submit"] = "${'$'}${'$'}post('/_examples/login')"
                }
            }
            mCardBody {
                mCardTitle { +"Login" }
                authField("Email", InputType.email, "email", style)
                authField("Password", InputType.password, "password", style)
                resultRegion("login-result", style)
                mCardActions("justify-end") { submitButton("Sign in", "login-spinner", style) }
            }
        }
    }
}

private fun FlowContent.registerPreview(style: String) {
    mCard("w-96 bg-base-100 shadow-xl") {
        form {
            when (style) {
                "htmx" -> {
                    attributes["hx-post"] = "/_examples/register"
                    attributes["hx-target"] = "#register-result"
                    attributes["hx-indicator"] = "#register-spinner"
                }
                "alpine" -> {
                    attributes["x-data"] =
                        "{ name: '', email: '', password: '', confirmPassword: '', loading: false, error: '' }"
                    attributes["x-on:submit.prevent"] =
                        "if (password !== confirmPassword) { error = 'Passwords do not match'; return; } " +
                        "loading = true; error = ''; " +
                        "fetch('/_examples/register', { method: 'POST', " +
                        "headers: { 'Content-Type': 'application/json' }, " +
                        "body: JSON.stringify({ name, email, password }) })" +
                        ".then(r => r.ok ? alert('Registration successful!') : r.text().then(t => error = t))" +
                        ".catch(() => error = 'Network error').finally(() => loading = false)"
                }
                "datastar" -> {
                    attributes["data-signals"] =
                        "{ name: '', email: '', password: '', confirmPassword: '', loading: false, error: '' }"
                    attributes["data-on-submit"] =
                        "if (${'$'}password !== ${'$'}confirmPassword) { " +
                        "${'$'}error = 'Passwords do not match'; return; } " +
                        "${'$'}${'$'}post('/_examples/register')"
                }
            }
            mCardBody {
                mCardTitle { +"Create account" }
                authField("Name", InputType.text, "name", style)
                authField("Email", InputType.email, "email", style)
                authField("Password", InputType.password, "password", style, minlength = "8")
                authField("Confirm password", InputType.password, "confirm_password", style, model = "confirmPassword")
                resultRegion("register-result", style)
                mCardActions("justify-end") { submitButton("Sign up", "register-spinner", style) }
            }
        }
    }
}

private fun FlowContent.authField(
    label: String,
    type: InputType,
    fieldName: String,
    style: String,
    minlength: String? = null,
    model: String = fieldName,
) {
    mFormControl("w-full") {
        mLabel { mLabelText { +label } }
        mInput(type = type, classes = "w-full") {
            name = fieldName
            required = true
            if (minlength != null) attributes["minlength"] = minlength
            when (style) {
                "alpine" -> attributes["x-model"] = model
                "datastar" -> attributes["data-model"] = model
            }
        }
    }
}

private fun FlowContent.resultRegion(
    idValue: String,
    style: String,
) {
    div(if (style == "htmx") "mt-2" else "mt-2 text-error text-sm") {
        id = idValue
        attributes["role"] = if (style == "htmx") "region" else "alert"
        attributes["aria-live"] = "polite"
        when (style) {
            "alpine" -> {
                attributes["x-show"] = "error"
                attributes["x-text"] = "error"
            }
            "datastar" -> {
                attributes["data-show"] = "${'$'}error"
                attributes["data-text"] = "${'$'}error"
            }
        }
    }
}

private fun FlowContent.submitButton(
    label: String,
    spinnerId: String,
    style: String,
) {
    mButton(variant = ButtonVariant.Primary) {
        type = ButtonType.submit
        when (style) {
            "alpine" -> attributes["x-bind:disabled"] = "loading"
            "datastar" -> attributes["data-bind-disabled"] = "${'$'}loading"
        }
        span { +label }
        mLoading(LoadingType.Spinner, Size.Sm, if (style == "htmx") "htmx-indicator" else "") {
            if (style == "htmx") {
                id = spinnerId
                attributes["style"] = "display:none;"
            } else if (style == "alpine") {
                attributes["x-show"] = "loading"
            } else {
                attributes["data-show"] = "${'$'}loading"
            }
        }
    }
}

private fun FlowContent.buildingBlocksSection(variant: InteractivityPageVariant) {
    h2 { +"Building blocks" }
    p { +"Each page variant focuses on the primitives used by the selected recipe." }
    when (variant.id) {
        "htmx" -> {
            h3 { +"htmx" }
            p {
                +"Server-driven HTML fragments with hx-get, hx-post, hx-target, hx-swap, and hx-trigger. "
                +"In Ktor 3.1 set attributes manually; Ktor 3.2 adds typed extensions."
            }
        }
        "alpine" -> {
            h3 { +"Alpine.js" }
            p { +"Client-side state with x-data, x-show, x-on, x-bind, and x-model." }
        }
        "datastar" -> {
            h3 { +"Datastar" }
            p {
                +"Signal-oriented behavior with data-signals, data-on, data-bind, "
                +"and Server-Sent Events (SSE) fragments."
            }
        }
    }
}

private fun FlowContent.comparisonSection() {
    h2 { +"Comparison" }
    div("overflow-x-auto") {
        mTable(zebra = true) {
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
                    td { +"Server renders everything; progressive enhancement" }
                }
                tr {
                    td { strong { +"Alpine.js" } }
                    td { +"Client-side" }
                    td { +"Any endpoint or static HTML" }
                    td { +"~15KB" }
                    td { +"Local UI state and validation" }
                }
                tr {
                    td { strong { +"Datastar" } }
                    td { +"Hybrid" }
                    td { +"HTTP plus optional SSE endpoints" }
                    td { +"~20KB" }
                    td { +"Signals, live updates, and server-pushed fragments" }
                }
            }
        }
    }
}

/** The interactivity guide rendered with a page-local variant selector. */
fun interactivityPage(variant: String? = DEFAULT_INTERACTIVITY_PAGE_VARIANT_ID): String =
    layout(INTERACTIVITY) { interactivityPageContent(variant) }

/** Test page for the legacy tab helper used by per-component interactive snippets. */
fun interactivityTabsTestPage(): String =
    layout(INTERACTIVITY) {
        h1 { +"Test page" }
        interactivityTabs(
            id = "test-tabs",
            htmxCode = "htmx example",
            alpineCode = "alpine example",
            datastarCode = "datastar example",
        )
    }
