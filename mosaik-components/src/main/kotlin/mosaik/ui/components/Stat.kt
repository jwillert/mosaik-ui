package mosaik.ui.components

import kotlinx.html.*

@MosaikDsl
class MStats(
    internal val div: DIV,
) : FlowContent by div {
    override val attributes: MutableMap<String, String>
        get() = div.attributes
}

@MosaikDsl
class MStat(
    internal val div: DIV,
) : FlowContent by div {
    override val attributes: MutableMap<String, String>
        get() = div.attributes
}

internal val MStats.underlying: DIV get() = div
internal val MStat.underlying: DIV get() = div

fun FlowContent.mStats(
    classes: String? = null,
    block: MStats.() -> Unit = {},
) {
    div(classes = buildClasses("stats", classes)) {
        MStats(this).block()
    }
}

fun MStats.mStat(
    classes: String? = null,
    block: MStat.() -> Unit = {},
) {
    underlying.mStat(classes, block)
}

fun FlowContent.mStat(
    classes: String? = null,
    block: MStat.() -> Unit = {},
) {
    div(classes = buildClasses("stat", classes)) {
        MStat(this).block()
    }
}

fun MStat.mStatFigure(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    underlying.mStatFigure(classes, block)
}

fun FlowContent.mStatFigure(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("stat-figure", classes), block = block)
}

fun MStat.mStatTitle(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    underlying.mStatTitle(classes, block)
}

fun FlowContent.mStatTitle(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("stat-title", classes), block = block)
}

fun MStat.mStatValue(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    underlying.mStatValue(classes, block)
}

fun FlowContent.mStatValue(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("stat-value", classes), block = block)
}

fun MStat.mStatDesc(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    underlying.mStatDesc(classes, block)
}

fun FlowContent.mStatDesc(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("stat-desc", classes), block = block)
}

fun MStat.mStatActions(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    underlying.mStatActions(classes, block)
}

fun FlowContent.mStatActions(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("stat-actions", classes), block = block)
}
