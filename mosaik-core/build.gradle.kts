plugins {
    id("mosaik.kotlin-library")
}

dependencies {
    // Registry parsing uses the kotlinx-serialization JsonElement API (no compiler plugin needed).
    implementation(libs.kotlinx.serialization.json)
}
