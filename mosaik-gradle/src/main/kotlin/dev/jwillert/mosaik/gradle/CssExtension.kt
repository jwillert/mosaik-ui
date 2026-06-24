package dev.jwillert.mosaik.gradle

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

/**
 * CSS-tooling configuration, nested under the [MosaikUiExtension] `css {}` block.
 *
 * Defaults target the stack pinned by the PRD: Tailwind CSS 4.x and DaisyUI 5.x, scanning `src`
 * for utility classes and emitting the `light` DaisyUI theme. All properties are lazy so the
 * generated `input.css` and the npm coordinates reflect the user's final configuration.
 */
abstract class CssExtension {
    /** npm version (or range) of `tailwindcss` / `@tailwindcss/cli` to install. Default: `4`. */
    abstract val tailwindVersion: Property<String>

    /** npm version (or range) of `daisyui` to install. Default: `5`. */
    abstract val daisyuiVersion: Property<String>

    /** Directories scanned for Tailwind classes, emitted as `@source` directives. Default: `["src"]`. */
    abstract val scanPaths: ListProperty<String>

    /** DaisyUI themes to bundle into the generated `input.css`. Default: `["light"]`. */
    abstract val themes: ListProperty<String>

    /** Whether `buildCss` minifies its output. Default: `true`. */
    abstract val minify: Property<Boolean>
}
