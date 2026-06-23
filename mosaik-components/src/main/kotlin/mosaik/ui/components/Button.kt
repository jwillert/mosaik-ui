package mosaik.ui.components

import kotlinx.html.*

/**
 * Scope for [mButton]. Exposes the Mosaik design properties together with the
 * HTML attributes of the underlying `<button>`, delegating the latter straight
 * to the [BUTTON] element so they render natively.
 */
class ButtonScope(private val element: BUTTON) : MosaikScope {
    override var variant: Variant = Variant.Primary
    override var size: Size = Size.Md
    override var classes: String? = null

    // Delegated HTML properties — written straight through to the <button>.
    // (kotlinx.html exposes these as extension properties, so they can't be
    // bound with `by element::id`; explicit accessors forward to the element.)
    var id: String
        get() = element.id
        set(value) { element.id = value }
    var name: String
        get() = element.name
        set(value) { element.name = value }
    var type: ButtonType
        get() = element.type
        set(value) { element.type = value }
    var disabled: Boolean
        get() = element.disabled
        set(value) { element.disabled = value }

    /**
     * Buffered content. The `class` attribute is computed from [variant]/[size]
     * after the user block runs, so the tag must not open until then; content
     * added via [unaryPlus] is therefore deferred and replayed on render.
     */
    internal val content = mutableListOf<BUTTON.() -> Unit>()

    /** Adds literal text to the button body: `+"Click me"`. */
    operator fun String.unaryPlus() {
        val text = this
        content += { +text }
    }
}

/**
 * A DaisyUI button. Usable anywhere in a kotlinx.html flow:
 *
 * ```kotlin
 * mButton { variant = Variant.Primary; size = Size.Lg; +"Save" }
 * ```
 */
fun FlowContent.mButton(block: ButtonScope.() -> Unit = {}) {
    BUTTON(emptyMap(), consumer).visit {
        // Inside visit the tag is the consumer's current (still-buffered) tag, so
        // attribute writes are allowed. The user block runs here too: its delegated
        // HTML properties (id/name/type/disabled) write straight to this element,
        // while content is deferred so `class` can be set before the open tag flushes.
        val scope = ButtonScope(this).apply(block)

        attributes["class"] = buildClasses(
            "btn",
            "btn-${scope.variant.token}",
            scope.size.token?.let { "btn-$it" },
            scope.classes,
        )

        scope.content.forEach { it() }
    }
}
