plugins {
    id("mosaik.ktor-app")
    application
}

dependencies {
    // Module dependency (not the installed-source flow) so component edits are
    // picked up immediately when iterating on the docs app — see PRD user story 22.
    implementation(project(":mosaik-components"))
    implementation(libs.kotlinx.html)
}

application {
    mainClass = "mosaik.docs.ApplicationKt"
}
