plugins {
    id("mosaik.gradle-plugin")
}

dependencies {
    // Gradle TestKit drives a real build in a temp dir for the CSS-tooling integration tests.
    testImplementation(gradleTestKit())
}

gradlePlugin {
    plugins {
        create("mosaikUi") {
            id = "dev.jwillert.mosaik"
            implementationClass = "dev.jwillert.mosaik.gradle.MosaikUiPlugin"
        }
    }
}
