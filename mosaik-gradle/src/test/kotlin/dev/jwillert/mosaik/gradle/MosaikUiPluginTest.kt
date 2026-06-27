package dev.jwillert.mosaik.gradle

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.gradle.testfixtures.ProjectBuilder

/**
 * Fast, offline unit tests for plugin wiring: tasks are registered and the `css {}` extension
 * exposes the DaisyUI-5 / Tailwind-4 defaults from the issue. No Gradle daemon or npm involved.
 */
class MosaikUiPluginTest :
    FunSpec({

        fun project() = ProjectBuilder.builder().build().also { it.pluginManager.apply(MosaikUiPlugin::class.java) }

        test("registers the four CSS tasks") {
            val project = project()
            project.tasks.findByName("installTailwind") shouldNotBe null
            project.tasks.findByName("generateTailwindConfig") shouldNotBe null
            project.tasks.findByName("buildCss") shouldNotBe null
            project.tasks.findByName("watchCss") shouldNotBe null
        }

        test("css extension exposes DaisyUI 5 / Tailwind 4 defaults") {
            val css = project().extensions.getByType(MosaikUiExtension::class.java).css
            css.tailwindVersion.get() shouldBe "4"
            css.daisyuiVersion.get() shouldBe "5"
            css.scanPaths.get() shouldBe listOf("src")
            css.themes.get() shouldBe listOf("light")
            css.minify.get() shouldBe true
        }

        test("buildCss depends on installTailwind and generateTailwindConfig") {
            val buildCss = project().tasks.getByName("buildCss")
            val deps =
                buildCss.taskDependencies
                    .getDependencies(buildCss)
                    .map { it.name }
                    .toSet()
            deps.contains("installTailwind") shouldBe true
            deps.contains("generateTailwindConfig") shouldBe true
        }
    })
