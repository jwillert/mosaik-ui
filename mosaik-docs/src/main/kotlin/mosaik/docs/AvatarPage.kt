package mosaik.docs

import kotlinx.html.*
import mosaik.ui.components.AvatarStatus
import mosaik.ui.components.mAvatar
import mosaik.ui.components.mAvatarGroup

fun FlowContent.avatarPageContent() {
    h1 { +"Avatar" }
    p {
        +"Avatars represent people or entities with images, initials, and presence "
        +"states. "
        code { +"mAvatar" }
        +" exposes online/offline and placeholder modifiers as parameters so callers "
        +"do not need to remember DaisyUI class tokens."
    }

    installSection("avatar")

    h2 { +"Basic usage" }
    exampleCard(
        code =
            """
            import mosaik.ui.components.mAvatar

            mAvatar(classes = "w-16 rounded-full") {
                img(src = "/static/mosaik-icon.svg", alt = "Mosaik")
            }
            """.trimIndent(),
    ) {
        mAvatar(classes = "w-16 rounded-full") {
            img(src = "/static/mosaik-icon.svg", alt = "Mosaik")
        }
    }

    section {
        h2 { +"Presence and placeholders" }
        exampleCard(
            code =
                """
                mAvatar(status = AvatarStatus.Online, classes = "w-16 rounded-full") {
                    img(src = "/static/mosaik-icon.svg", alt = "Online user")
                }
                mAvatar(placeholder = true, classes = "bg-neutral text-neutral-content w-16 rounded-full") {
                    span("text-xl") { +"JD" }
                }
                """.trimIndent(),
        ) {
            div("flex flex-wrap items-center gap-4") {
                mAvatar(status = AvatarStatus.Online, classes = "w-16 rounded-full") {
                    img(src = "/static/mosaik-icon.svg", alt = "Online user")
                }
                mAvatar(placeholder = true, classes = "bg-neutral text-neutral-content w-16 rounded-full") {
                    span("text-xl") { +"JD" }
                }
            }
        }
    }

    section {
        h2 { +"Groups" }
        exampleCard(
            code =
                """
                mAvatarGroup(classes = "-space-x-4") {
                    mAvatar(classes = "w-12 rounded-full") { img(src = "/a.png", alt = "A") }
                    mAvatar(placeholder = true, classes = "bg-base-300 w-12 rounded-full") { span { +"+3" } }
                }
                """.trimIndent(),
        ) {
            mAvatarGroup(classes = "-space-x-4") {
                mAvatar(classes = "w-12 rounded-full") {
                    img(src = "/static/mosaik-icon.svg", alt = "A")
                }
                mAvatar(placeholder = true, classes = "bg-base-300 w-12 rounded-full") {
                    span { +"+3" }
                }
            }
        }
    }

    apiReference(
        listOf(
            ApiParam("status", "AvatarStatus?", "null", "Optional presence state: Online or Offline."),
            ApiParam("placeholder", "Boolean", "false", "Render the avatar as placeholder content."),
            ApiParam("classes", "String?", "null", "Extra CSS classes appended after generated classes."),
            ApiParam("block", "DIV.() -> Unit", "{}", "Receiver block on the raw kotlinx.html DIV element."),
        ),
    )
}

fun avatarPage(): String = layout(AVATAR) { avatarPageContent() }
