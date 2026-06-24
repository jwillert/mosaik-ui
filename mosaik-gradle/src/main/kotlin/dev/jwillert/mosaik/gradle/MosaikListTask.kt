package dev.jwillert.mosaik.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Lists every component available in the bundled registry, with its description and any
 * dependencies, so users can discover what they can install.
 */
abstract class MosaikListTask : DefaultTask() {

    @TaskAction
    fun list() {
        val registry = BundledRegistry.load()
        logger.lifecycle("Available components:")
        for (entry in registry.all()) {
            val deps = if (entry.dependencies.isEmpty()) {
                ""
            } else {
                " (depends on: ${entry.dependencies.joinToString(", ")})"
            }
            logger.lifecycle("  ${entry.name} — ${entry.description}$deps")
        }
    }
}
