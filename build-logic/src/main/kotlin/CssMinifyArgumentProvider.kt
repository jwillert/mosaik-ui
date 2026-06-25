import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.process.CommandLineArgumentProvider

/** Supplies optional Tailwind CLI arguments in a configuration-cache-safe way. */
abstract class CssMinifyArgumentProvider : CommandLineArgumentProvider {
    @get:Input
    abstract val minify: Property<Boolean>

    override fun asArguments(): Iterable<String> = if (minify.get()) listOf("--minify") else emptyList()
}
