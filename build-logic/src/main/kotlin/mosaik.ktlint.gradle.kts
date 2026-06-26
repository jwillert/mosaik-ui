// Repo-wide ktlint convention: applies ktlint to .kt and .kts files with standard defaults.
// Provides ktlintCheck and ktlintFormat tasks but does NOT wire into check lifecycle.

plugins {
    id("org.jlleitschuh.gradle.ktlint")
}

ktlint {
    version.set("1.5.0")
    android.set(false)
    ignoreFailures.set(false)
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}

// Do not wire ktlintCheck into the check lifecycle yet.
// This allows the repository to have ktlint tasks available without making them mandatory.
tasks.matching { it.name == "check" }.configureEach {
    setDependsOn(dependsOn.filterNot { dep ->
        when (dep) {
            is TaskProvider<*> -> dep.name.startsWith("ktlint")
            is Task -> dep.name.startsWith("ktlint")
            else -> false
        }
    })
}
