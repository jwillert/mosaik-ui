package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.BadgeVariant
import mosaik.ui.components.Size
import mosaik.ui.components.mBadge

/**
 * Badge page content block for use in both full-page and partial renders.
 */
fun FlowContent.badgePageContent() {
    h1 { +"Badge" }
    p {
        +"A badge is a small inline label for status, counts, or categories — "
        +"sitting beside text, in a list, or on the corner of another element. "
        code { +"mBadge" }
        +" wraps DaisyUI's "
        code { +"badge" }
        +" classes and takes its colour role and size as parameters, handing you "
        +"the raw kotlinx.html element so any HTML attribute or library extension "
        +"(e.g. htmx) works natively (ADR-0003)."
    }
    p {
        +"Unlike Button, Badge declares its own "
        code { +"BadgeVariant" }
        +" enum (ADR-0004): DaisyUI's badge palette adds "
        code { +"info" }
        +", "
        code { +"neutral" }
        +", and "
        code { +"outline" }
        +", so the shared "
        code { +"Variant" }
        +" wouldn't fit."
    }

    installSection("badge")

    usageSection(
        """
        import mosaik.ui.components.mBadge
        import mosaik.ui.components.BadgeVariant
        import mosaik.ui.components.Size

        mBadge(BadgeVariant.Success, Size.Sm) {
            +"Active"
        }
        """.trimIndent(),
    )

    section {
        h2 { +"Variants" }
        p {
            +"Every "
            code { +"BadgeVariant" }
            +" maps to a DaisyUI badge colour role."
        }
        div("flex flex-wrap gap-2 not-prose") {
            BadgeVariant.entries.forEach { v ->
                mBadge(variant = v) { +v.name }
            }
        }
        codeBlock(
            """
            BadgeVariant.entries.forEach { v ->
                mBadge(variant = v) { +v.name }
            }
            """.trimIndent(),
        )
    }

    section {
        h2 { +"Sizes" }
        p {
            +"Five "
            code { +"Size" }
            +" steps; "
            code { +"Size.Md" }
            +" is the unstyled baseline and renders no size class."
        }
        div("flex flex-wrap items-center gap-2 not-prose") {
            Size.entries.forEach { s ->
                mBadge(variant = BadgeVariant.Primary, size = s) { +s.name }
            }
        }
        codeBlock(
            """
            Size.entries.forEach { s ->
                mBadge(variant = BadgeVariant.Primary, size = s) { +s.name }
            }
            """.trimIndent(),
        )
    }

    apiReference(
        listOf(
            ApiParam(
                "variant",
                "BadgeVariant",
                "BadgeVariant.Primary",
                "DaisyUI colour role: Primary, Secondary, Accent, Ghost, Info, Success, Warning, Error, Neutral, Outline.",
            ),
            ApiParam(
                "size",
                "Size",
                "Size.Md",
                "Size step: Xs, Sm, Md, Lg, Xl. Md is the baseline and adds no class.",
            ),
            ApiParam(
                "classes",
                "String?",
                "null",
                "Extra CSS classes appended after the generated badge classes.",
            ),
            ApiParam(
                "block",
                "SPAN.() -> Unit",
                "{}",
                "Receiver block on the raw kotlinx.html SPAN element — set text, attributes, or library extensions.",
            ),
        ),
    )
}

/**
 * Badge page, following the five-section component template: title +
 * description, installation, basic usage, a variants/sizes showcase paired with
 * its Kotlin code, and the API reference table.
 */
fun badgePage(): String = layout(BADGE) { badgePageContent() }
