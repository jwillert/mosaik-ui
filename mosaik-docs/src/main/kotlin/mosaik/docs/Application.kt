package mosaik.docs

import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

/** Port the docs app listens on; overridable for local runs. */
const val DEFAULT_PORT = 8080

/** Server-side rendered documentation app — see [Pages] for the rendered HTML. */
fun Application.module() {
    routing {
        // Compiled Tailwind/DaisyUI output, served from resources/static/output.css.
        staticResources("/static", "static")

        get("/") { call.respondText(landingPage(), ContentType.Text.Html) }
        get("/components/button") { call.respondText(buttonPage(), ContentType.Text.Html) }
    }
}

fun main() {
    embeddedServer(Netty, port = DEFAULT_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}
