package dev.jwillert.ktor.vrt

import kotlinx.html.FlowContent

data class Scenario(
    val name: String,
    val captureSelector: String = "#capture",
    val beforeShot: String? = null,
    val render: FlowContent.() -> Unit,
)
