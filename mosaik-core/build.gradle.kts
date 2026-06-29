plugins {
    id("mosaik.kotlin-library")
    id("mosaik.ktlint")
    `maven-publish`
}

dependencies {
    // Registry parsing uses the kotlinx-serialization JsonElement API (no compiler plugin needed).
    implementation(libs.kotlinx.serialization.json)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
