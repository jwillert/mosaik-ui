// Repo-wide ktlint convention: applies ktlint to .kt and .kts files with standard defaults.
// ktlintCheck is wired into the check lifecycle to make lint feedback mandatory.

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

// Wire ktlintCheck into the check lifecycle to make lint mandatory (ADR 0011).
tasks.named("check") {
    dependsOn(tasks.named("ktlintCheck"))
}
