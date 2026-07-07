package mosaik.ui.components

import kotlinx.html.*

enum class JoinOrientation(
    val token: String?,
) {
    Horizontal(null),
    Vertical("vertical"),
}

@MosaikDsl
class MJoin(
    internal val div: DIV,
) : FlowContent by div {
    override val attributes: MutableMap<String, String>
        get() = div.attributes
}

internal val MJoin.underlying: DIV get() = div

fun FlowContent.mJoin(
    orientation: JoinOrientation = JoinOrientation.Horizontal,
    classes: String? = null,
    block: MJoin.() -> Unit = {},
) {
    div(
        classes =
            buildClasses(
                "join",
                orientation.token?.let { "join-$it" },
                classes,
            ),
    ) {
        MJoin(this).block()
    }
}

fun MJoin.mJoinItem(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    underlying.mJoinItem(classes, block)
}

fun FlowContent.mJoinItem(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("join-item", classes), block = block)
}
