plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    // Makes `kotlin("jvm")` resolvable inside the precompiled convention plugins.
    implementation(libs.kotlin.gradle.plugin)
}
