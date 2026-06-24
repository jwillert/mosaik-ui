package dev.jwillert.mosaik.core

/**
 * Resolves a component's full install set: the component itself plus every transitive
 * dependency, returned in topological order so that dependencies always precede the
 * components that need them (e.g. `theme` before `button`).
 */
class Resolver(private val registry: Registry) {

    /**
     * Returns [name] and all of its transitive dependencies in topological order
     * (dependencies first). The requested component is always last.
     *
     * @throws IllegalArgumentException if [name] or any dependency is unknown.
     * @throws IllegalStateException if the dependency graph contains a cycle.
     */
    fun resolve(name: String): List<ComponentEntry> {
        val ordered = LinkedHashMap<String, ComponentEntry>()
        val visiting = LinkedHashSet<String>()

        fun visit(componentName: String) {
            if (ordered.containsKey(componentName)) return
            if (!visiting.add(componentName)) {
                val cycle = (visiting.toList() + componentName).joinToString(" → ")
                throw IllegalStateException("Dependency cycle detected: $cycle")
            }
            val entry = registry.require(componentName)
            entry.dependencies.forEach(::visit)
            visiting.remove(componentName)
            ordered[componentName] = entry
        }

        visit(name)
        return ordered.values.toList()
    }
}
