package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.img
import kotlinx.html.stream.createHTML

private fun renderAvatar(block: FlowContent.() -> Unit): String = createHTML(prettyPrint = false).div { block() }

class AvatarTest :
    FunSpec({
        test("avatar renders a container with nested image content") {
            val html = renderAvatar { mAvatar { img(src = "/user.png", alt = "User") } }

            html shouldContain "<div class=\"avatar\"><img alt=\"User\" src=\"/user.png\"></div>"
        }

        test("avatar supports presence and placeholder modifiers") {
            val html =
                renderAvatar {
                    mAvatar(
                        status = AvatarStatus.Online,
                        placeholder = true,
                        classes = "w-12",
                    ) {
                        +"JD"
                    }
                }

            html shouldContain "class=\"avatar avatar-online avatar-placeholder w-12\""
        }

        test("avatar group wraps multiple avatars") {
            val html = renderAvatar { mAvatarGroup("-space-x-4") { mAvatar { +"A" } } }

            html shouldContain "<div class=\"avatar-group -space-x-4\"><div class=\"avatar\">A</div></div>"
        }
    })
