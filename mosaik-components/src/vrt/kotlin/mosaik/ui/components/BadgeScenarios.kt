package mosaik.ui.components

import dev.jwillert.ktor.vrt.Scenario

object BadgeScenarios {
    private fun variantScenario(variant: BadgeVariant) =
        Scenario("badge-${variant.token}") {
            mBadge(variant = variant) { +"Badge" }
        }

    val variants = BadgeVariant.entries.map(::variantScenario)

    val all = variants
}
