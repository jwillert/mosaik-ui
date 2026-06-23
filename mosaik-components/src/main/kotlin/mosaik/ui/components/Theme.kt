package mosaik.ui.components

/**
 * Shared foundation installed automatically with the first component.
 *
 * Holds the design-token enums every component draws from ([Variant], [Size]),
 * the [MosaikScope] contract each component's scope class implements, and the
 * [buildClasses] helper that assembles the final `class` attribute.
 */

/**
 * A DaisyUI colour role. Components turn the [token] into their own DaisyUI
 * class (e.g. button → `btn-primary`, badge → `badge-primary`).
 */
enum class Variant(val token: String) {
    Primary("primary"),
    Secondary("secondary"),
    Accent("accent"),
    Ghost("ghost"),
    Link("link"),
    Error("error"),
    Success("success"),
    Warning("warning"),
}

/**
 * A DaisyUI size step. [Md] is the default and carries no [token]: DaisyUI's
 * medium is the unstyled baseline, so it renders no size class.
 */
enum class Size(val token: String?) {
    Xs("xs"),
    Sm("sm"),
    Md(null),
    Lg("lg"),
    Xl("xl"),
}

/**
 * The contract shared by every component scope: the Mosaik design properties
 * exposed in the flat DSL block. HTML element properties are added per scope
 * by delegating to the underlying kotlinx.html tag.
 */
interface MosaikScope {
    var variant: Variant
    var size: Size
    /** Extra classes appended verbatim after the generated component classes. */
    var classes: String?
}

/**
 * Joins the given class tokens into a single `class` value, dropping any that
 * are `null` or blank so callers can pass optional tokens without guarding.
 */
fun buildClasses(vararg classes: String?): String =
    classes.filterNot { it.isNullOrBlank() }.joinToString(" ")
