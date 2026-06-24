plugins {
    id("mosaik.gradle-plugin")
}

tasks.named<ProcessResources>("processResources") {
    val pluginVersion = project.version.toString()
    inputs.property("ktorVrtVersion", pluginVersion)
    filesMatching("ktor-vrt.properties") {
        expand("ktorVrtVersion" to pluginVersion)
    }
}

gradlePlugin {
    plugins {
        create("ktorVrt") {
            id = "dev.jwillert.ktor-vrt"
            implementationClass = "dev.jwillert.ktor.vrt.gradle.KtorVrtPlugin"
            displayName = "ktor-vrt"
            description = "kotlinx-html visual regression testing: vrt source set + vrtTest/vrtTestDocker tasks"
        }
    }
}

dependencies {
    testImplementation(gradleTestKit())
}
