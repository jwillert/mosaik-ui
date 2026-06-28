package mosaik.ui.components

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import kotlinx.html.*
import kotlinx.html.stream.createHTML

private fun render(block: FlowContent.() -> Unit): String = createHTML(prettyPrint = false).div { block() }

class FormTest :
    FunSpec({

        // mFormControl tests

        test("mFormControl renders a label with form-control class") {
            val html = render { mFormControl {} }

            html shouldContain "<label class=\"form-control\">"
            html shouldContain "</label>"
        }

        test("mFormControl with w-full adds width utility class") {
            val html = render { mFormControl(classes = "w-full") {} }

            html shouldContain "class=\"form-control w-full\""
        }

        test("mFormControl block receives the raw label element so html attributes apply natively") {
            val html =
                render {
                    mFormControl {
                        id = "test-control"
                        +"Content"
                    }
                }

            html shouldContain "id=\"test-control\""
            html shouldContain ">Content</label>"
        }

        // mLabel tests

        test("mLabel renders a div with label class") {
            val html = render { mLabel {} }

            html shouldContain "<div class=\"label\">"
            html shouldContain "</div>"
        }

        test("mLabel with custom classes appends them") {
            val html = render { mLabel(classes = "mt-2") {} }

            html shouldContain "class=\"label mt-2\""
        }

        test("mLabel block receives the raw div element") {
            val html =
                render {
                    mLabel {
                        id = "label-div"
                        +"Label content"
                    }
                }

            html shouldContain "id=\"label-div\""
            html shouldContain ">Label content</div>"
        }

        // mLabelText tests

        test("mLabelText renders a span with label-text class") {
            val html = render { mLabelText { +"Email" } }

            html shouldContain "<span class=\"label-text\">Email</span>"
        }

        test("mLabelText with custom classes appends them") {
            val html = render { mLabelText(classes = "font-bold") { +"Username" } }

            html shouldContain "class=\"label-text font-bold\""
        }

        test("mLabelText block receives the raw span element") {
            val html =
                render {
                    mLabelText {
                        id = "label-span"
                        +"Password"
                    }
                }

            html shouldContain "id=\"label-span\""
            html shouldContain ">Password</span>"
        }

        // mInput tests

        test("mInput renders an input with input and input-bordered classes") {
            val html = render { mInput(type = InputType.text) }

            html shouldContain "<input type=\"text\" class=\"input input-bordered\">"
        }

        test("mInput with bordered=false renders only input class") {
            val html = render { mInput(type = InputType.text, bordered = false) }

            html shouldContain "class=\"input\""
            html shouldNotContain "input-bordered"
        }

        test("mInput with size applies the input-{size} class") {
            val html = render { mInput(type = InputType.email, size = Size.Lg) }

            html shouldContain "class=\"input input-bordered input-lg\""
        }

        test("mInput with size Md omits the size class as it is the default") {
            val html = render { mInput(type = InputType.text, size = Size.Md) }

            html shouldContain "class=\"input input-bordered\""
            html shouldNotContain "input-md"
        }

        test("every non-default input size maps to its DaisyUI class") {
            val expected =
                mapOf(
                    Size.Xs to "input-xs",
                    Size.Sm to "input-sm",
                    Size.Lg to "input-lg",
                )

            expected.forEach { (size, css) ->
                val html = render { mInput(type = InputType.text, size = size) }
                html shouldContain "class=\"input input-bordered $css\""
            }
        }

        test("mInput with custom classes appends them") {
            val html = render { mInput(type = InputType.password, classes = "w-full mt-2") }

            html shouldContain "class=\"input input-bordered w-full mt-2\""
        }

        test("mInput block receives the raw input element so html attributes apply natively") {
            val html =
                render {
                    mInput(type = InputType.email) {
                        id = "email-input"
                        name = "email"
                        required = true
                        placeholder = "user@example.com"
                    }
                }

            html shouldContain "id=\"email-input\""
            html shouldContain "name=\"email\""
            html shouldContain "required=\"required\""
            html shouldContain "placeholder=\"user@example.com\""
        }

        test("mInput allows arbitrary attributes (e.g. htmx, alpine) via the raw element") {
            val html =
                render {
                    mInput(type = InputType.text) {
                        attributes["x-model"] = "username"
                        attributes["hx-trigger"] = "keyup changed delay:500ms"
                    }
                }

            html shouldContain "x-model=\"username\""
            html shouldContain "hx-trigger=\"keyup changed delay:500ms\""
        }

        // mSelect tests

        test("mSelect renders a select with select and select-bordered classes") {
            val html = render { mSelect {} }

            html shouldContain "<select class=\"select select-bordered\">"
            html shouldContain "</select>"
        }

        test("mSelect with bordered=false renders only select class") {
            val html = render { mSelect(bordered = false) {} }

            html shouldContain "class=\"select\""
            html shouldNotContain "select-bordered"
        }

        test("mSelect with size applies the select-{size} class") {
            val html = render { mSelect(size = Size.Sm) {} }

            html shouldContain "class=\"select select-bordered select-sm\""
        }

        test("mSelect with size Md omits the size class as it is the default") {
            val html = render { mSelect(size = Size.Md) {} }

            html shouldContain "class=\"select select-bordered\""
            html shouldNotContain "select-md"
        }

        test("every non-default select size maps to its DaisyUI class") {
            val expected =
                mapOf(
                    Size.Xs to "select-xs",
                    Size.Sm to "select-sm",
                    Size.Lg to "select-lg",
                )

            expected.forEach { (size, css) ->
                val html = render { mSelect(size = size) {} }
                html shouldContain "class=\"select select-bordered $css\""
            }
        }

        test("mSelect with custom classes appends them") {
            val html = render { mSelect(classes = "w-full max-w-xs") {} }

            html shouldContain "class=\"select select-bordered w-full max-w-xs\""
        }

        test("mSelect block receives the raw select element so html attributes apply natively") {
            val html =
                render {
                    mSelect {
                        id = "theme-select"
                        name = "theme"
                        option {
                            value = "light"
                            +"Light"
                        }
                        option {
                            value = "dark"
                            +"Dark"
                        }
                    }
                }

            html shouldContain "id=\"theme-select\""
            html shouldContain "name=\"theme\""
            html shouldContain "value=\"light\""
            html shouldContain "value=\"dark\""
        }

        test("mSelect allows arbitrary attributes (e.g. htmx, alpine) via the raw element") {
            val html =
                render {
                    mSelect {
                        attributes["x-model"] = "selectedTheme"
                        attributes["onchange"] = "updateTheme()"
                    }
                }

            html shouldContain "x-model=\"selectedTheme\""
            html shouldContain "onchange=\"updateTheme()\""
        }
    })
