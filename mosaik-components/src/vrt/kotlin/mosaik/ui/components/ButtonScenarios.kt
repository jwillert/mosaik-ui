package mosaik.ui.components

import dev.jwillert.ktor.vrt.Scenario

object ButtonScenarios {

    private fun variantScenario(variant: ButtonVariant) = Scenario("button-${variant.token}") {
        mButton(variant = variant) { +"Button" }
    }

    private fun styleScenario(style: ButtonStyle) = Scenario("button-style-${style.token}") {
        mButton(variant = ButtonVariant.Primary, style = style) { +"Button" }
    }

    private fun shapeScenario(shape: ButtonShape) = Scenario("button-shape-${shape.token}") {
        mButton(variant = ButtonVariant.Primary, shape = shape) { +"Icon" }
    }

    private fun widthScenario(width: ButtonWidth) = Scenario("button-width-${width.token}") {
        mButton(variant = ButtonVariant.Primary, width = width) { +"Button" }
    }

    private fun sizeScenario(size: Size) = Scenario("button-size-${size.token ?: "md"}") {
        mButton(size = size) { +"Button" }
    }

    val variants = listOf(
        variantScenario(ButtonVariant.Neutral),
        variantScenario(ButtonVariant.Primary),
        variantScenario(ButtonVariant.Secondary),
        variantScenario(ButtonVariant.Accent),
        variantScenario(ButtonVariant.Info),
        variantScenario(ButtonVariant.Success),
        variantScenario(ButtonVariant.Warning),
        variantScenario(ButtonVariant.Error),
    )

    val styles = listOf(
        styleScenario(ButtonStyle.Outline),
        styleScenario(ButtonStyle.Ghost),
        styleScenario(ButtonStyle.Link),
    )

    val shapes = listOf(
        shapeScenario(ButtonShape.Circle),
        shapeScenario(ButtonShape.Square),
    )

    val widths = listOf(
        widthScenario(ButtonWidth.Wide),
        widthScenario(ButtonWidth.Block),
    )

    val sizes = listOf(
        sizeScenario(Size.Xs),
        sizeScenario(Size.Sm),
        sizeScenario(Size.Md),
        sizeScenario(Size.Lg),
        sizeScenario(Size.Xl),
    )

    val all = variants + styles + shapes + widths + sizes
}
