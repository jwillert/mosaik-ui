package dev.jwillert.mosaik.core

import java.io.File
import java.security.MessageDigest

/**
 * Writes the installed component inventory to `.mosaik/components.json` in the user's project.
 *
 * The inventory is a strict JSON file with schema version, tool metadata, project config
 * (package, prefix, source set), agent hints, and installed components with their files,
 * dependencies, and inline API metadata. It is the primary way agents discover what
 * Mosaik components are available in a project.
 */
class InventoryWriter(
    private val packageName: String,
    private val prefix: String = "m",
    private val sourceSet: String = "main",
) {
    /**
     * Generates the inventory JSON for the given [installedComponents]. The output is
     * formatted for readability with 2-space indents.
     */
    fun generate(installedComponents: List<ComponentEntry>): String {
        val components =
            installedComponents.joinToString(",\n    ") { entry ->
                val files = entry.files.joinToString(", ") { "\"$it\"" }
                val deps = entry.dependencies.joinToString(", ") { "\"$it\"" }
                val apis =
                    entry.api.joinToString(",\n        ") { api ->
                        """{ "name": "${api.name}", "kind": "${api.kind}" }"""
                    }
                val checksums =
                    entry.checksums.entries.joinToString(",\n        ") { (file, checksum) ->
                        """"$file": "$checksum""""
                    }
                """
                {
                  "name": "${entry.name}",
                  "description": "${escapeJson(entry.description)}",
                  "files": [$files],
                  "dependencies": [$deps],
                  "api": [${if (apis.isNotEmpty()) "\n        $apis\n      " else ""}],
                  "checksums": {${if (checksums.isNotEmpty()) "\n        $checksums\n      " else ""}}
                }
                """.trimIndent()
            }
        return """
{
  "schemaVersion": "1",
  "generatedBy": {
    "tool": "mosaik-gradle",
    "version": "1.0.0"
  },
  "project": {
    "package": "$packageName",
    "prefix": "$prefix",
    "sourceSet": "$sourceSet"
  },
  "agentHints": {
    "description": "Source-installed Mosaik UI components. After install, the user owns the code.",
    "prefix": "All component functions start with '$prefix' (e.g., ${prefix}Button, ${prefix}Card)."
  },
  "components": [
$components
  ]
}
""".trim()
    }

    private fun escapeJson(text: String): String = text.replace("\"", "\\\"").replace("\n", "\\n")

    /**
     * Writes the inventory to `.mosaik/components.json` in the [projectRoot], creating
     * the directory if needed. Returns the file that was written.
     */
    fun write(
        projectRoot: File,
        installedComponents: List<ComponentEntry>,
    ): File {
        val mosaikDir = projectRoot.resolve(".mosaik")
        mosaikDir.mkdirs()
        val inventoryFile = mosaikDir.resolve("components.json")
        inventoryFile.writeText(generate(installedComponents))
        return inventoryFile
    }

    companion object {
        /**
         * Computes a deterministic SHA-256 checksum of the given [content].
         */
        fun computeChecksum(content: String): String {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(content.toByteArray())
            return hash.joinToString("") { "%02x".format(it) }
        }
    }
}
