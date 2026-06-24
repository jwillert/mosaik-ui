package mosaik.ui.components

import dev.jwillert.ktor.vrt.Scenario

object ButtonScenarios {

    private fun variantScenario(variant: Variant) = Scenario("button-${variant.token}") {
        mButton(variant = variant) { +"Button" }
    }

    private fun sizeScenario(size: Size) = Scenario("button-size-${size.token ?: "md"}") {
        mButton(size = size) { +"Button" }
    }

    val variants = listOf(
        variantScenario(Variant.Primary),
        variantScenario(Variant.Secondary),
        variantScenario(Variant.Accent),
        variantScenario(Variant.Ghost),
        variantScenario(Variant.Error),
    )

    val sizes = listOf(
        sizeScenario(Size.Xs),
        sizeScenario(Size.Sm),
        sizeScenario(Size.Md),
        sizeScenario(Size.Lg),
        sizeScenario(Size.Xl),
    )

    val all = variants + sizes
}
