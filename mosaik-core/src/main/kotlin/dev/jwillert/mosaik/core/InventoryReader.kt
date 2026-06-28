package dev.jwillert.mosaik.core

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File

/**
 * Reads the installed component inventory from `.mosaik/components.json`.
 */
object InventoryReader {
    /**
     * Reads the existing inventory from [projectRoot] and returns a map of component
     * name to ComponentEntry. Returns an empty map if the inventory file does not exist
     * or cannot be parsed.
     */
    fun read(projectRoot: File): Map<String, ComponentEntry> {
        val inventoryFile = projectRoot.resolve(".mosaik/components.json")
        if (!inventoryFile.exists()) return emptyMap()

        return try {
            val text = inventoryFile.readText()
            val json = Json.parseToJsonElement(text).jsonObject
            val components = json["components"]?.jsonArray ?: return emptyMap()

            components
                .associate { element ->
                    val obj = element.jsonObject
                    val name =
                        obj["name"]?.jsonPrimitive?.content
                            ?: return@associate "" to
                                ComponentEntry(
                                    "",
                                    "",
                                    emptyList(),
                                    emptyList(),
                                )
                    name to
                        ComponentEntry(
                            name = name,
                            description = obj["description"]?.jsonPrimitive?.content ?: "",
                            files = obj["files"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList(),
                            dependencies =
                                obj["dependencies"]?.jsonArray?.map { it.jsonPrimitive.content }
                                    ?: emptyList(),
                            api = emptyList(),
                        )
                }.filterKeys { it.isNotEmpty() }
        } catch (e: Exception) {
            emptyMap()
        }
    }
}
