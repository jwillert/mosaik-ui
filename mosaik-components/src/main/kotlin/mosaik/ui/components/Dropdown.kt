package mosaik.ui.components

import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.ul

/** The DaisyUI dropdown direction modifier. */
enum class DropdownDirection(
    val token: String,
) {
    Top("dropdown-top"),
    Bottom("dropdown-bottom"),
    Left("dropdown-left"),
    Right("dropdown-right"),
}

/** The DaisyUI dropdown alignment modifier. */
enum class DropdownAlignment(
    val token: String,
) {
    Start("dropdown-start"),
    Center("dropdown-center"),
    End("dropdown-end"),
}

/**
 * DSL context for [mDropdown]. Wraps the dropdown's [DIV] and exposes
 * [mDropdownTrigger] and [mDropdownContent]. Marked with [MosaikDsl] so child
 * components are dropdown-specific. Implements [FlowContent] to allow ordinary
 * HTML content.
 */
@MosaikDsl
class MDropdown(
    internal val div: DIV,
) : FlowContent by div {
    var id: String
        get() = div.id
        set(value) {
            div.id = value
        }

    override val attributes: MutableMap<String, String>
        get() = div.attributes
}

internal val MDropdown.underlying: DIV get() = div

/**
 * A DaisyUI dropdown, usable anywhere in a kotlinx.html flow.
 *
 * Direction, alignment, hover, and open states are type-safe parameters. The
 * [block] receives an [MDropdown] context so dropdown children are constrained
 * to this component while ordinary HTML remains available through delegation.
 */
fun FlowContent.mDropdown(
    direction: DropdownDirection? = null,
    alignment: DropdownAlignment? = null,
    hover: Boolean = false,
    open: Boolean = false,
    classes: String? = null,
    block: MDropdown.() -> Unit = {},
) {
    div(
        classes =
            buildClasses(
                "dropdown",
                direction?.token,
                alignment?.token,
                if (hover) "dropdown-hover" else null,
                if (open) "dropdown-open" else null,
                classes,
            ),
    ) {
        MDropdown(this).block()
    }
}

/**
 * The focusable trigger for a [mDropdown]. Only callable from [MDropdown].
 * [block] receives the raw [DIV] for native attributes and custom content.
 */
fun MDropdown.mDropdownTrigger(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    underlying.div(classes = classes) {
        attributes["tabindex"] = "0"
        attributes["role"] = "button"
        block()
    }
}

/**
 * The focusable dropdown content menu for a [mDropdown]. Only callable from
 * [MDropdown]. [block] receives an [MMenu] context so menu children such as
 * [mMenuItem] and [mMenuTitle] remain available without raw DaisyUI classes.
 */
fun MDropdown.mDropdownContent(
    classes: String? = null,
    block: MMenu.() -> Unit = {},
) {
    underlying.ul(classes = buildClasses("dropdown-content", "menu", classes)) {
        attributes["tabindex"] = "0"
        MMenu(this).block()
    }
}
