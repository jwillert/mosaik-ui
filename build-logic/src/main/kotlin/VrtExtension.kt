import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property

abstract class VrtExtension {
    abstract val css: RegularFileProperty
    abstract val cssTaskDependency: Property<String>
    abstract val goldenDir: DirectoryProperty
    abstract val diffDir: DirectoryProperty
    abstract val htmlAttributes: MapProperty<String, String>
    abstract val wrapperClasses: ListProperty<String>
}
