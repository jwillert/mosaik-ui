# Publishing Mosaik UI

Mosaik UI is published as Maven artifacts to GitHub Packages.

## Artifacts

- `dev.jwillert.mosaik:mosaik-core:<version>`
- Gradle plugin implementation artifact for `:mosaik-gradle`
- Gradle plugin marker for `id("dev.jwillert.mosaik") version "<version>"`

The version is currently defined in `build-logic/src/main/kotlin/mosaik.kotlin-library.gradle.kts`.

## Credentials

Create a GitHub token with package permissions:

- `write:packages` for publishing
- `read:packages` for consuming

Then export credentials before publishing:

```bash
export GITHUB_ACTOR=<github-user>
export GITHUB_TOKEN=<token>
```

Alternatively use Gradle properties:

```properties
gpr.user=<github-user>
gpr.token=<token>
```

## Publish

Publish the core library and the Mosaik Gradle plugin:

```bash
./gradlew :mosaik-core:publishAllPublicationsToGitHubPackagesRepository \
  :mosaik-gradle:publishAllPublicationsToGitHubPackagesRepository
```

## Consume from another project

`settings.gradle.kts`:

```kotlin
pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/jwillert/mosaik-ui")
            credentials {
                username = providers.gradleProperty("gpr.user")
                    .orElse(providers.environmentVariable("GITHUB_ACTOR"))
                    .orNull
                password = providers.gradleProperty("gpr.token")
                    .orElse(providers.gradleProperty("gpr.key"))
                    .orElse(providers.environmentVariable("GITHUB_TOKEN"))
                    .orNull
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/jwillert/mosaik-ui")
            credentials {
                username = providers.gradleProperty("gpr.user")
                    .orElse(providers.environmentVariable("GITHUB_ACTOR"))
                    .orNull
                password = providers.gradleProperty("gpr.token")
                    .orElse(providers.gradleProperty("gpr.key"))
                    .orElse(providers.environmentVariable("GITHUB_TOKEN"))
                    .orNull
            }
        }
        mavenCentral()
    }
}
```

`build.gradle.kts`:

```kotlin
plugins {
    id("dev.jwillert.mosaik") version "0.1.0"
}

mosaikUi {
    packageName.set("your.project.ui.components")
}
```
