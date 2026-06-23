// Convention for Ktor server apps: base library + Ktor server runtime + HTML builder.

plugins {
    id("mosaik.kotlin-library")
}

// Precompiled script plugins can't use the type-safe `libs.` accessor, so resolve the catalog by name.
val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    "implementation"(libs.findLibrary("ktor-server-core").get())
    "implementation"(libs.findLibrary("ktor-server-netty").get())
    "implementation"(libs.findLibrary("ktor-server-html-builder").get())
}
