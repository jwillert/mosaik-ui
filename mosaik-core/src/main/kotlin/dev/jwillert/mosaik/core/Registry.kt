package dev.jwillert.mosaik.core

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * A single component definition from the registry: its [name], human-readable [description],
 * the source [files] that make it up, and the names of the components it [dependencies] on.
 */
data class ComponentEntry(
    val name: String,
    val description: String,
    val files: List<String>,
    val dependencies: List<String>,
)

/**
 * The catalogue of installable components, parsed from the registry JSON. Lookups are by
 * component name; [all] returns the entries in declaration order for listing.
 *
 * The JSON shape is an object with a `components` array, each element having `name`,
 * `description`, `files` and `dependencies` (see `mosaik-components/registry.json`).
 */
class Registry private constructor(private val entries: Map<String, ComponentEntry>) {

    /** All components in the order they appear in the registry. */
    fun all(): List<ComponentEntry> = entries.values.toList()

    /** The component named [name], or `null` if no such component exists. */
    fun get(name: String): ComponentEntry? = entries[name]

    /** Like [get] but throws [IllegalArgumentException] when the component is unknown. */
    fun require(name: String): ComponentEntry =
        get(name) ?: throw IllegalArgumentException("Unknown component '$name'. Available: ${entries.keys.joinToString(", ")}")

    companion object {
        /** Parses a registry from its JSON [text]. */
        fun fromJson(text: String): Registry {
            val components = Json.parseToJsonElement(text).jsonObject["components"]?.jsonArray
                ?: throw IllegalArgumentException("Registry JSON must have a 'components' array")
            val entries = components.map { element ->
                val obj = element.jsonObject
                ComponentEntry(
                    name = obj["name"]?.jsonPrimitive?.content
                        ?: throw IllegalArgumentException("A component entry is missing its 'name'"),
                    description = obj["description"]?.jsonPrimitive?.content ?: "",
                    files = obj["files"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList(),
                    dependencies = obj["dependencies"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList(),
                )
            }
            // Preserve declaration order for listing while keeping name lookups O(1).
            return Registry(entries.associateByTo(LinkedHashMap()) { it.name })
        }
    }
}
