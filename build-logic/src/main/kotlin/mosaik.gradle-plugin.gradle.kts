// Convention for modules that ship a Gradle plugin: base library + plugin authoring + publishing.

plugins {
    id("mosaik.kotlin-library")
    `java-gradle-plugin`
    `maven-publish`
}
