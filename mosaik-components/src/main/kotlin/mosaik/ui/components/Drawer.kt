package mosaik.ui.components

import kotlinx.html.*

/** The side of the viewport where a drawer panel appears. */
enum class DrawerPlacement(
    val token: String?,
) {
    Start(null),
    End("end"),
}

/** The Tailwind breakpoint where a drawer becomes always open. */
enum class DrawerBreakpoint(
    val token: String,
) {
    Sm("sm"),
    Md("md"),
    Lg("lg"),
    Xl("xl"),
    TwoXl("2xl"),
}

/**
 * DSL context for [mDrawer]. Collects drawer slots before rendering so root
 * attributes can be applied before the generated checkbox opens the element.
 */
@MosaikDsl
class MDrawer internal constructor(
    internal val toggleId: String,
) {
    private val slots = mutableListOf<FlowContent.() -> Unit>()

    var id: String? = null

    val attributes: MutableMap<String, String> = linkedMapOf()

    internal fun slot(block: FlowContent.() -> Unit) {
        slots += block
    }

    internal fun renderSlots(flowContent: FlowContent) {
        slots.forEach { flowContent.it() }
    }
}

/**
 * A DaisyUI drawer layout with a generated checkbox toggle and constrained
 * content/side slots.
 *
 * The [toggleId] identifies the hidden checkbox and is used by [mDrawerSide]'s
 * overlay label. Use any ordinary label or button elsewhere with `for` set to
 * the same id to open and close the drawer.
 */
fun FlowContent.mDrawer(
    toggleId: String,
    placement: DrawerPlacement = DrawerPlacement.Start,
    open: Boolean = false,
    checked: Boolean = false,
    classes: String? = null,
    openFrom: DrawerBreakpoint? = null,
    block: MDrawer.() -> Unit = {},
) {
    val drawer = MDrawer(toggleId).apply(block)
    div(
        classes =
            buildClasses(
                "drawer",
                placement.token?.let { "drawer-$it" },
                if (open) "drawer-open" else null,
                openFrom?.let { "${it.token}:drawer-open" },
                classes,
            ),
    ) {
        drawer.id?.let { id = it }
        drawer.attributes.forEach { (name, value) -> attributes[name] = value }
        input(
            type = InputType.checkBox,
            classes = "drawer-toggle",
        ) {
            id = toggleId
            this.checked = checked
        }
        drawer.renderSlots(this)
    }
}

/** The main page content slot of a [mDrawer]. */
fun MDrawer.mDrawerContent(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    slot { div(classes = buildClasses("drawer-content", classes), block = block) }
}

/**
 * The drawer side panel slot. By default an overlay label is emitted first and
 * linked to the drawer toggle so clicking outside the panel closes it.
 */
fun MDrawer.mDrawerSide(
    classes: String? = null,
    overlay: Boolean = true,
    overlayLabel: String = "close sidebar",
    block: DIV.() -> Unit = {},
) {
    val drawerToggleId = toggleId
    slot {
        div(classes = buildClasses("drawer-side", classes)) {
            if (overlay) {
                label(classes = "drawer-overlay") {
                    htmlFor = drawerToggleId
                    attributes["aria-label"] = overlayLabel
                }
            }
            block()
        }
    }
}
