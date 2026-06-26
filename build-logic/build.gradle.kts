plugins {
    `kotlin-dsl`
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    // Makes `kotlin("jvm")` resolvable inside the precompiled convention plugins.
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.ktlint.gradle)
}

ktlint {
    version.set("1.5.0")
    android.set(false)
    ignoreFailures.set(false)
}
