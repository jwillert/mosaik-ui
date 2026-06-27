package mosaik.ui.components

import kotlinx.html.*

/**
 * A DaisyUI table, usable anywhere in a kotlinx.html flow.
 *
 * Per ADR-0003 the design tokens are function parameters and [block] receives
 * the raw kotlinx.html [TABLE]. This keeps the call site terse for the common
 * tokens while exposing the element directly, so any extension property a
 * third-party library adds to [TABLE] (e.g. htmx's `hxGet`/`hxTarget`) is
 * usable natively — Kotlin extension resolution can't see members of a wrapper
 * scope class, but it can see extensions on the real receiver.
 *
 * The zebra modifier toggles striped row styling via DaisyUI's `table-zebra`
 * class. The table preserves normal HTML table structure: `thead`, `tbody`,
 * `tr`, `th`, `td` are all standard kotlinx.html functions callable on the
 * underlying [TABLE] element.
 *
 * ```kotlin
 * mTable(zebra = true) {
 *     thead {
 *         tr {
 *             th { +"Name" }
 *             th { +"Age" }
 *         }
 *     }
 *     tbody {
 *         tr {
 *             td { +"Alice" }
 *             td { +"30" }
 *         }
 *     }
 * }
 * ```
 */
fun FlowContent.mTable(
    zebra: Boolean = false,
    classes: String? = null,
    block: TABLE.() -> Unit = {},
) {
    table(
        classes =
            buildClasses(
                "table",
                if (zebra) "table-zebra" else null,
                classes,
            ),
        block = block,
    )
}
