plugins {
    id("mosaik.kotlin-library")
}

dependencies {
    api(libs.kotlinx.html)
    api(libs.playwright)
    api(libs.testcontainers.core)
    api(libs.image.comparison)
    api(libs.kotest.runner.junit5)
    api(libs.kotest.assertions.core)
    api(libs.kotest.framework.datatest)
}
