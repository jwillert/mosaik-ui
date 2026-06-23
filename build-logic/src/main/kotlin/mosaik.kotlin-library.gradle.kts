// Base convention: Kotlin JVM library with toolchain 21 and Kotest on the JUnit 5 platform.

plugins {
    kotlin("jvm")
}

group = "dev.jwillert.mosaik"
version = "0.1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

// Precompiled script plugins can't use the type-safe `libs.` accessor, so resolve the catalog by name.
val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    "testImplementation"(libs.findLibrary("kotest-runner-junit5").get())
    "testImplementation"(libs.findLibrary("kotest-assertions-core").get())
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
