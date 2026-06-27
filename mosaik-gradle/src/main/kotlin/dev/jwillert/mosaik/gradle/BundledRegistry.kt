package dev.jwillert.mosaik.gradle

import dev.jwillert.mosaik.core.Registry
import org.gradle.api.GradleException

/**
 * Loads the component registry and component sources that are bundled as resources inside the
 * plugin jar (placed there by the `processResources` wiring in `mosaik-gradle/build.gradle.kts`).
 * Reading from the classpath keeps `mosaikAdd` fully offline.
 */
internal object BundledRegistry {
    private const val REGISTRY = "/mosaik/registry.json"
    private const val COMPONENTS_DIR = "/mosaik/components"

    /** Parses the bundled registry JSON. */
    fun load(): Registry = Registry.fromJson(read(REGISTRY))

    /** Returns the bundled source text for a component file (e.g. `Button.kt`). */
    fun componentSource(fileName: String): String = read("$COMPONENTS_DIR/$fileName")

    private fun read(path: String): String =
        BundledRegistry::class.java
            .getResourceAsStream(path)
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: throw GradleException(
                "Bundled resource '$path' not found on the plugin classpath. " +
                    "This is a packaging error in the mosaik-gradle plugin.",
            )
}
