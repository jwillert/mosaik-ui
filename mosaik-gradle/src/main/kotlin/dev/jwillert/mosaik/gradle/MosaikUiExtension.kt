package dev.jwillert.mosaik.gradle

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

/**
 * The `mosaikUi {}` DSL extension. For now it carries only the nested CSS-tooling block; the
 * registry/install side (packageName, components) is added by a separate issue and slots in here.
 */
abstract class MosaikUiExtension @Inject constructor(objects: ObjectFactory) {
    /** Tailwind/DaisyUI build configuration. */
    val css: CssExtension = objects.newInstance(CssExtension::class.java)

    /** Configure the nested `css {}` block. */
    fun css(action: Action<in CssExtension>) = action.execute(css)
}
