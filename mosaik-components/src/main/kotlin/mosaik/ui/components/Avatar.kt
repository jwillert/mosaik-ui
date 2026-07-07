package mosaik.ui.components

import kotlinx.html.*

enum class AvatarStatus(
    val token: String,
) {
    Online("online"),
    Offline("offline"),
}

fun FlowContent.mAvatar(
    status: AvatarStatus? = null,
    placeholder: Boolean = false,
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(
        classes =
            buildClasses(
                "avatar",
                status?.token?.let { "avatar-$it" },
                if (placeholder) "avatar-placeholder" else null,
                classes,
            ),
        block = block,
    )
}

fun FlowContent.mAvatarGroup(
    classes: String? = null,
    block: DIV.() -> Unit = {},
) {
    div(classes = buildClasses("avatar-group", classes), block = block)
}
