package dev.jwillert.mosaik.gradle

import dev.jwillert.mosaik.core.DaisyUiTokenScanner
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

/**
 * Scans for raw DaisyUI class tokens in source files and fails the build if any are found.
 *
 * This task identifies DaisyUI component and modifier tokens that should be wrapped by
 * Mosaik components, structural sub-components, or type-safe modifiers per ADR 0013.
 * Component implementation files and component tests are allowlisted.
 */
abstract class ScanDaisyUiTokensTask : DefaultTask() {
    @get:Internal
    abstract val sourceRoot: DirectoryProperty

    @TaskAction
    fun scan() {
        val findings = DaisyUiTokenScanner.scan(sourceRoot.get().asFile)

        if (findings.isEmpty()) {
            logger.lifecycle("No DaisyUI class tokens found outside allowlisted locations.")
            return
        }

        logger.lifecycle("Found ${findings.size} DaisyUI class token(s) in source files:")
        logger.lifecycle("")

        var currentFile: String? = null
        findings.forEach { finding ->
            val fileDisplay =
                finding.file.relativeTo(sourceRoot.get().asFile).path
            if (fileDisplay != currentFile) {
                if (currentFile != null) {
                    logger.lifecycle("")
                }
                logger.lifecycle("  $fileDisplay:")
                currentFile = fileDisplay
            }

            val suggestion =
                if (finding.suggestedComponent != null) {
                    " (consider ${finding.suggestedComponent} component)"
                } else {
                    ""
                }

            logger.lifecycle("    Line ${finding.line}: ${finding.token}$suggestion")
        }

        logger.lifecycle("")
        logger.lifecycle(
            "DaisyUI class tokens should be wrapped by components, structural sub-components, or type-safe modifiers per ADR 0013.",
        )

        throw GradleException("DaisyUI class token violations found. See output above for details.")
    }
}
