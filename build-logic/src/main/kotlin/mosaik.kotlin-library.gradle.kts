// Base convention: Kotlin JVM library with toolchain 21 and Kotest on the JUnit 5 platform.

plugins {
    kotlin("jvm")
}

group = "dev.jwillert.mosaik"
version = providers.gradleProperty("mosaikVersion").get()

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

plugins.withId("maven-publish") {
    extensions.configure<PublishingExtension>("publishing") {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/jwillert/mosaik-ui")
                credentials {
                    username = providers.gradleProperty("gpr.user")
                        .orElse(providers.environmentVariable("GITHUB_ACTOR"))
                        .orNull
                    password = providers.gradleProperty("gpr.token")
                        .orElse(providers.gradleProperty("gpr.key"))
                        .orElse(providers.environmentVariable("GITHUB_TOKEN"))
                        .orNull
                }
            }
        }
    }
}
