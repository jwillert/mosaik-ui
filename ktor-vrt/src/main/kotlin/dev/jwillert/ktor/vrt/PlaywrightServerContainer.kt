package dev.jwillert.ktor.vrt

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

// Keep PLAYWRIGHT_VERSION in sync with the `playwright` version in gradle/libs.versions.toml.
class PlaywrightServerContainer :
    GenericContainer<PlaywrightServerContainer>(
        DockerImageName.parse("mcr.microsoft.com/playwright:v$PLAYWRIGHT_VERSION-jammy"),
    ) {
    init {
        withExposedPorts(SERVER_PORT)
        withCommand(
            "/bin/sh",
            "-c",
            "npx playwright@$PLAYWRIGHT_VERSION run-server --port $SERVER_PORT --host 0.0.0.0",
        )
        waitingFor(Wait.forListeningPort())
    }

    fun wsEndpoint(): String = "ws://$host:${getMappedPort(SERVER_PORT)}/"

    companion object {
        private const val PLAYWRIGHT_VERSION = "1.49.0"
        private const val SERVER_PORT = 3000
    }
}
