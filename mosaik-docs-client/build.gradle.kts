plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("mosaik.ktlint")
}

group = "dev.jwillert.mosaik"
version = "0.1.0"

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
        browser {
            binaries.executable()
        }
    }

    sourceSets {
        val jsMain by getting
    }
}
