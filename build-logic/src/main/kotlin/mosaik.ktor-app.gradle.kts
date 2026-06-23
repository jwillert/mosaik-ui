// Convention for Ktor server apps: base library + Ktor server runtime + HTML builder.

plugins {
    id("mosaik.kotlin-library")
}

dependencies {
    "implementation"("io.ktor:ktor-server-core:3.1.3")
    "implementation"("io.ktor:ktor-server-netty:3.1.3")
    "implementation"("io.ktor:ktor-server-html-builder:3.1.3")
}
