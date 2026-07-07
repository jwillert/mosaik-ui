package mosaik.docs

import kotlinx.html.FlowContent
import kotlinx.html.code
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.p
import mosaik.ui.components.DropdownAlignment
import mosaik.ui.components.DropdownDirection
import mosaik.ui.components.mDropdown
import mosaik.ui.components.mDropdownContent
import mosaik.ui.components.mDropdownTrigger
import mosaik.ui.components.mMenuItem

fun FlowContent.dropdownPageContent() {
    h1 { +"Dropdown" }
    p {
        code { +"mDropdown" }
        +" renders a dropdown container with constrained trigger and content children. Use type-safe "
        code { +"direction" }
        +", "
        code { +"alignment" }
        +", "
        code { +"hover" }
        +", and "
        code { +"open" }
        +" parameters instead of DaisyUI class names."
    }

    installSection("dropdown")

    h2 { +"Basic usage" }
    exampleCard(
        code =
            """
            import mosaik.ui.components.mDropdown
            import mosaik.ui.components.mDropdownContent
            import mosaik.ui.components.mDropdownTrigger
            import mosaik.ui.components.mMenuItem

            mDropdown {
                mDropdownTrigger(classes = "rounded-md border px-4 py-2") { +"Account" }
                mDropdownContent(classes = "w-52 rounded-box bg-base-100 p-2 shadow-sm") {
                    mMenuItem("/profile") { +"Profile" }
                    mMenuItem("/settings") { +"Settings" }
                }
            }
            """.trimIndent(),
    ) {
        mDropdown {
            mDropdownTrigger(classes = "rounded-md border px-4 py-2") { +"Account" }
            mDropdownContent(classes = "w-52 rounded-box bg-base-100 p-2 shadow-sm") {
                mMenuItem("/profile") { +"Profile" }
                mMenuItem("/settings") { +"Settings" }
            }
        }
    }

    h2 { +"Placement" }
    exampleCard(
        code =
            """
            mDropdown(
                direction = DropdownDirection.Top,
                alignment = DropdownAlignment.End,
                open = true,
            ) {
                mDropdownTrigger(classes = "rounded-md border px-4 py-2") { +"Open upward" }
                mDropdownContent(classes = "w-52 rounded-box bg-base-100 p-2 shadow-sm") {
                    mMenuItem("#") { +"First action" }
                    mMenuItem("#") { +"Second action" }
                }
            }
            """.trimIndent(),
    ) {
        mDropdown(
            direction = DropdownDirection.Top,
            alignment = DropdownAlignment.End,
            open = true,
        ) {
            mDropdownTrigger(classes = "rounded-md border px-4 py-2") { +"Open upward" }
            mDropdownContent(classes = "w-52 rounded-box bg-base-100 p-2 shadow-sm") {
                mMenuItem("#") { +"First action" }
                mMenuItem("#") { +"Second action" }
            }
        }
    }

    apiReference(
        listOf(
            ApiParam("mDropdown.direction", "DropdownDirection?", "null", "Optional dropdown opening direction."),
            ApiParam("mDropdown.alignment", "DropdownAlignment?", "null", "Optional dropdown alignment."),
            ApiParam("mDropdown.hover", "Boolean", "false", "Opens the dropdown on hover."),
            ApiParam("mDropdown.open", "Boolean", "false", "Forces the dropdown open."),
            ApiParam("mDropdown.classes", "String?", "null", "Extra CSS classes for the dropdown container."),
            ApiParam(
                "mDropdown.block",
                "MDropdown.() -> Unit",
                "{}",
                "Constrained receiver where dropdown child components are available.",
            ),
            ApiParam("mDropdownTrigger.classes", "String?", "null", "Extra CSS classes for the trigger DIV."),
            ApiParam(
                "mDropdownTrigger.block",
                "DIV.() -> Unit",
                "{}",
                "Receiver block on the raw kotlinx.html DIV element.",
            ),
            ApiParam(
                "mDropdownContent.classes",
                "String?",
                "null",
                "Extra CSS classes for the dropdown content UL.",
            ),
            ApiParam(
                "mDropdownContent.block",
                "MMenu.() -> Unit",
                "{}",
                "Constrained receiver where menu child components are available.",
            ),
        ),
    )
}

fun dropdownPage(): String = layout(DROPDOWN) { dropdownPageContent() }
