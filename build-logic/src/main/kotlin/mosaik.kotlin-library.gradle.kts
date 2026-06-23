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

dependencies {
    "testImplementation"("io.kotest:kotest-runner-junit5:5.9.1")
    "testImplementation"("io.kotest:kotest-assertions-core:5.9.1")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
