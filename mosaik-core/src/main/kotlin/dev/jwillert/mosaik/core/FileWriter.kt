package dev.jwillert.mosaik.core

import java.io.File

/**
 * What happened when [FileWriter] processed a single target file.
 */
enum class WriteOutcome {
    /** The file did not exist and was written. */
    CREATED,

    /** The file already existed and was overwritten because `force` was set. */
    OVERWRITTEN,

    /** The file already existed and was left untouched (no `force`). */
    ALREADY_EXISTS,
}

/**
 * Installs component sources into a user's project. Each file's placeholder package
 * declaration ([placeholderPackage]) is rewritten to the user's [packageName] before it
 * is written, so the installed code lives in the user's own package.
 *
 * Following the copy-paste philosophy, an existing target file is never clobbered unless
 * the caller explicitly forces it: [write] reports [WriteOutcome.ALREADY_EXISTS] and leaves
 * the file as-is. With `force = true` it is overwritten instead.
 */
class FileWriter(
    private val packageName: String,
    private val placeholderPackage: String = "mosaik.ui.components",
) {
    /** Rewrites the placeholder package in [source] to the configured [packageName]. */
    fun transform(source: String): String = source.replace(placeholderPackage, packageName)

    /**
     * Writes the transformed [source] to [target], creating parent directories as needed.
     * Existing files are only overwritten when [force] is true; otherwise the file is left
     * untouched. Returns the [WriteOutcome] describing what happened.
     */
    fun write(
        target: File,
        source: String,
        force: Boolean = false,
    ): WriteOutcome {
        val existed = target.exists()
        if (existed && !force) return WriteOutcome.ALREADY_EXISTS

        target.parentFile?.mkdirs()
        target.writeText(transform(source))
        return if (existed) WriteOutcome.OVERWRITTEN else WriteOutcome.CREATED
    }
}
