rootProject.name = "mosaik-ui"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

// Convention plugins live in an included build so product modules stay minimal.
includeBuild("build-logic")

include(
    ":mosaik-core",
    ":mosaik-gradle",
    ":mosaik-components",
    ":mosaik-docs",
    ":ktor-vrt",
    ":ktor-vrt-gradle-plugin",
)
