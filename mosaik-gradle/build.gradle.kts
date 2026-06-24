import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    id("mosaik.gradle-plugin")
}

dependencies {
    // Registry/Resolver/FileWriter live in mosaik-core; the plugin only orchestrates them.
    implementation(project(":mosaik-core"))

    // Gradle TestKit drives a real build in a temp dir for the integration tests.
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

// Bundle the component registry and sources into the plugin jar so mosaikAdd works offline.
// They are read back from the classpath at "/mosaik/registry.json" and "/mosaik/components/*.kt".
val componentsDir = rootDir.resolve("mosaik-components")
tasks.named<ProcessResources>("processResources") {
    from(componentsDir.resolve("registry.json")) { into("mosaik") }
    from(componentsDir.resolve("src/main/kotlin/mosaik/ui/components")) {
        include("*.kt")
        into("mosaik/components")
    }
}
