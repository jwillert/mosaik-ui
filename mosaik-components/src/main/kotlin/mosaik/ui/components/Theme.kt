package mosaik.ui.components

/**
 * Shared foundation installed automatically with the first component.
 *
 * Holds the design-token enums every component draws from ([Variant], [Size])
 * and the [buildClasses] helper that assembles the final `class` attribute.
 * Per ADR-0003 components take these tokens as function parameters rather than
 * through a shared scope contract, so there is no `MosaikScope` interface.
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
 * Joins the given class tokens into a single `class` value, dropping any that
 * are `null` or blank so callers can pass optional tokens without guarding.
 */
fun buildClasses(vararg classes: String?): String =
    classes.filterNot { it.isNullOrBlank() }.joinToString(" ")
