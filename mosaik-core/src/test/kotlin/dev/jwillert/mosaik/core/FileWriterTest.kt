package dev.jwillert.mosaik.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import java.io.File

private const val SOURCE = """package mosaik.ui.components

import kotlinx.html.*

fun FlowContent.mButton(block: ButtonScope.() -> Unit = {}) {}
"""

class FileWriterTest : FunSpec({

    test("replaces the placeholder package with the user's package") {
        val rendered = FileWriter("com.example.app.ui").transform(SOURCE)

        rendered shouldContain "package com.example.app.ui"
        rendered shouldNotContain "mosaik.ui.components"
    }

    test("writes a new file and reports CREATED") {
        val target = File(tempdir(), "ui/Button.kt")

        val outcome = FileWriter("com.example.ui").write(target, SOURCE)

        outcome shouldBe WriteOutcome.CREATED
        target.readText() shouldContain "package com.example.ui"
    }

    test("aborts (leaves the file untouched) when the target exists and force is off") {
        val target = File(tempdir(), "Button.kt")
        val existing = "/* the user's own edits */"
        target.writeText(existing)

        val outcome = FileWriter("com.example.ui").write(target, SOURCE, force = false)

        outcome shouldBe WriteOutcome.ALREADY_EXISTS
        target.readText() shouldBe existing
    }

    test("overwrites an existing file when force is on") {
        val target = File(tempdir(), "Button.kt")
        target.writeText("/* the user's own edits */")

        val outcome = FileWriter("com.example.ui").write(target, SOURCE, force = true)

        outcome shouldBe WriteOutcome.OVERWRITTEN
        target.readText() shouldContain "package com.example.ui"
    }
})
