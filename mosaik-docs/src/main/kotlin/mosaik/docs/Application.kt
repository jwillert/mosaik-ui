package mosaik.docs

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

/** Port the docs app listens on; overridable for local runs. */
const val DEFAULT_PORT = 8080

/** Server-side rendered documentation app — see [Pages] for the rendered HTML. */
fun Application.module() {
    routing {
        // Compiled Tailwind/DaisyUI output, served from resources/static/output.css.
        staticResources("/static", "static")

        // One route per nav item, so the sidebar and the routing table can't drift.
        (listOf(HOME) + COMPONENTS + GUIDES).forEach { page ->
            get(page.path) { call.respondText(page.render(), ContentType.Text.Html) }
            // Partial content routes for progressive shell navigation.
            get("${page.path}?partial=true") { call.respondText(page.renderPartial(), ContentType.Text.Html) }
        }

        // Live interactivity demo endpoints — namespaced under /_examples per ADR-0007.
        post("/_examples/button/submit") {
            call.respondText("Submitted successfully!", status = HttpStatusCode.OK)
        }
        get("/_examples/card/content") {
            call.respondText("Loaded content from server", status = HttpStatusCode.OK)
        }
        post("/_examples/login") {
            call.respondText("Login successful!", status = HttpStatusCode.OK)
        }
        post("/_examples/register") {
            call.respondText("Registration successful!", status = HttpStatusCode.OK)
        }
    }
}

fun main() {
    embeddedServer(Netty, port = DEFAULT_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}
