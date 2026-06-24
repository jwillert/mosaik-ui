package mosaik.ui.components

import dev.jwillert.ktor.vrt.Scenario

object AlertScenarios {

    private fun variantScenario(variant: AlertVariant) = Scenario("alert-${variant.token}") {
        mAlert(variant = variant) { +"Alert" }
    }

    val variants = AlertVariant.entries.map(::variantScenario)

    val all = variants
}
