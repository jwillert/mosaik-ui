package mosaik.ui.components

import kotlinx.html.*

/**
 * The DaisyUI size steps a table supports.
 *
 * Per ADR-0004 this is a component-specific enum rather than the shared [Size]:
 * table sizing has its own public vocabulary. [Default] is DaisyUI's baseline
 * table rendering and carries no [token], so it emits no size class.
 */
enum class TableSize(
    val token: String?,
) {
    Default(null),
    Xs("xs"),
    Sm("sm"),
    Lg("lg"),
    Xl("xl"),
}

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
 * class. [size] accepts [TableSize], whose default value emits no size class.
 * The table preserves normal HTML table structure: `thead`, `tbody`,
 * `tr`, `th`, `td` are all standard kotlinx.html functions callable on the
 * underlying [TABLE] element.
 *
 * ```kotlin
 * mTable(zebra = true, size = TableSize.Sm) {
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
    size: TableSize = TableSize.Default,
    classes: String? = null,
    block: TABLE.() -> Unit = {},
) {
    table(
        classes =
            buildClasses(
                "table",
                if (zebra) "table-zebra" else null,
                size.token?.let { "table-$it" },
                classes,
            ),
        block = block,
    )
}
