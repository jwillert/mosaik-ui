package mosaik.ui.components

import kotlinx.html.*

/**
 * DSL context for [mTabs]. Wraps the tabs container's [DIV] and exposes [mTab].
 * Marked with [MosaikDsl] so child components are slot-specific. Implements
 * [FlowContent] to allow ordinary HTML content.
 */
@MosaikDsl
class MTabs(
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

internal val MTabs.underlying: DIV get() = div

/**
 * A DaisyUI tab style modifier. Tabs can be rendered with lifted, boxed, or
 * bordered styling, or left unstyled (default).
 */
enum class TabsStyle(
    val token: String?,
) {
    Lifted("tabs-lifted"),
    Boxed("tabs-boxed"),
    Bordered("tabs-bordered"),
}

/**
 * A DaisyUI tabs container, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * a [MTabs] context wrapping the underlying [DIV], so any HTML attribute or
 * third-party extension (e.g. htmx) works natively via delegation. Tabs use
 * radio inputs for native CSS-only tab switching without JavaScript.
 *
 * The [style] parameter applies DaisyUI tab modifiers (lifted, boxed, bordered).
 * The [classes] parameter allows additional utility classes. The container
 * automatically receives `role="tablist"` for accessibility.
 *
 * Tabs compose from [mTab] child components scoped to the [MTabs] context.
 * Each tab is a radio input paired with its content panel. The [MosaikDsl]
 * marker ensures [mTab] is only callable from [mTabs].
 *
 * ```kotlin
 * mTabs(style = TabsStyle.Lifted, classes = "not-prose") {
 *     mTab(name = "example", id = "tab1", label = "Tab 1", checked = true) {
 *         +"Content for tab 1"
 *     }
 *     mTab(name = "example", id = "tab2", label = "Tab 2") {
 *         +"Content for tab 2"
 *     }
 * }
 * ```
 */
fun FlowContent.mTabs(
    style: TabsStyle? = null,
    classes: String? = null,
    block: MTabs.() -> Unit = {},
) {
    div(classes = buildClasses("tabs", style?.token, classes)) {
        attributes["role"] = "tablist"
        MTabs(this).block()
    }
}

/**
 * A single tab within a [mTabs] container — a radio input with an associated
 * content panel. Only callable from [MTabs] context due to [MosaikDsl].
 *
 * The [name] parameter groups tabs together (all tabs in the same group should
 * share the same name). The [id] parameter uniquely identifies this tab's radio
 * input. The [label] parameter sets the visible tab label via `aria-label`.
 *
 * Set [checked] to `true` to make this tab active by default (typically used on
 * the first tab). The [contentClasses] parameter allows additional utility classes
 * on the content panel. The [tabConfig] block receives the raw radio [INPUT] for
 * additional customization (e.g., custom data attributes).
 *
 * The [content] block receives a [FlowContent] and renders the tab's panel content.
 */
fun MTabs.mTab(
    name: String,
    id: String,
    label: String,
    checked: Boolean = false,
    contentClasses: String? = null,
    tabConfig: INPUT.() -> Unit = {},
    content: FlowContent.() -> Unit = {},
) {
    underlying.input(type = InputType.radio, name = name, classes = "tab") {
        this.id = id
        this.checked = checked
        attributes["aria-label"] = label
        tabConfig()
    }
    underlying.div(classes = buildClasses("tab-content", "bg-base-200", "rounded-box", "p-4", contentClasses)) {
        content()
    }
}
