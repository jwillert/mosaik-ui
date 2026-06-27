package dev.jwillert.mosaik.gradle

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

/**
 * The `mosaikUi {}` DSL extension. Carries the registry/install configuration ([packageName])
 * plus the nested CSS-tooling block.
 */
abstract class MosaikUiExtension
    @Inject
    constructor(
        objects: ObjectFactory,
    ) {
        /**
         * The package installed component sources are written into, replacing the placeholder
         * `mosaik.ui.components`. Required by `mosaikAdd`/`mosaikStatus`.
         */
        abstract val packageName: Property<String>

        /** Tailwind/DaisyUI build configuration. */
        val css: CssExtension = objects.newInstance(CssExtension::class.java)

        /** Configure the nested `css {}` block. */
        fun css(action: Action<in CssExtension>) = action.execute(css)
    }
