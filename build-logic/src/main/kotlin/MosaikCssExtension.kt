import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

abstract class MosaikCssExtension {
    abstract val tailwindVersion: Property<String>
    abstract val daisyuiVersion: Property<String>
    abstract val scanPaths: ListProperty<String>
    abstract val themes: ListProperty<String>
    abstract val minify: Property<Boolean>
}
