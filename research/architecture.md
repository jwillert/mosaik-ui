# Mosaik UI — Architecture

## Repo Structure

```
mosaik-ui/
├── mosaik-core/               → Registry, Resolver, FileWriter logic
├── mosaik-gradle/             → Gradle Plugin (tasks, extension)
├── mosaik-components/         → The components themselves (compiled Kotlin)
├── mosaik-docs/               → Ktor documentation app (dogfooding)
├── ktor-vrt/                  → Visual Regression Testing library (independent)
├── ktor-vrt-gradle-plugin/    → VRT Gradle Plugin (independent)
├── build-logic/               → Convention plugins for this repo
└── research/                  → Design decisions & reference material
```

## Module Responsibilities

### mosaik-core

- Registry abstraction: reads component definitions from JSON
- Dependency resolver: given a component, returns all transitive dependencies
- File writer: copies component files, replaces package name and prefix
- Conflict detection: checks if target files already exist

### mosaik-gradle

- Gradle Plugin with `mosaikUi {}` extension
- Registers tasks: `mosaikAdd`, `mosaikList`, `mosaikStatus`
- Reads config (packageName, prefix) from extension
- Delegates to mosaik-core for actual logic
- Bundles the component registry JSON as a resource

### mosaik-components

- Each component is a single `.kt` file with one or more functions
- Compiled under placeholder package `mosaik.ui.components`
- `Theme.kt` as shared foundation (enums, utilities, MosaikScope interface)
- Per-component Scope class with HTML property delegation
- Unit tests for HTML output
- VRT scenarios for visual regression

### mosaik-docs

- Ktor server-side rendered app
- `implementation(project(":mosaik-components"))` for fast dev iteration
- Pages per component showing all variants
- Shared layout with sidebar navigation
- Serves as both documentation and integration test

### ktor-vrt (independent)

- `VrtHarness`: renders scenarios in browser, diffs against goldens
- `Scenario`: data class with name, render function, optional beforeShot JS
- `VisualRegressionSpec`: reusable Kotest base class
- `PlaywrightServerContainer`: Docker-based deterministic rendering
- Published as `dev.jwillert:ktor-vrt` — usable without Mosaik

### ktor-vrt-gradle-plugin (independent)

- Creates `vrt` source set
- Registers `vrtTest` (local) and `vrtTestDocker` (deterministic) tasks
- Configurable via `ktorVrt {}` extension (CSS file, golden dir, diff dir, html attributes)
- Published as `dev.jwillert:ktor-vrt-gradle-plugin`

## Component Anatomy

A single component file (e.g. `Button.kt`):

```kotlin
package mosaik.ui.components

import kotlinx.html.*

enum class ButtonVariant(val css: String) {
    // Uses shared Variant for color, but may have component-specific variants
}

class ButtonScope(private val element: BUTTON) : MosaikScope {
    override var variant = Variant.Primary
    override var size = Size.Md
    override var classes: String? = null

    // Delegated HTML properties
    var id by element::id
    var type by element::type
    var disabled by element::disabled
    var name by element::name

    // Content support
    operator fun String.unaryPlus() { element.run { +this@unaryPlus } }
}

fun FlowContent.mButton(block: ButtonScope.() -> Unit = {}) {
    val scope = ButtonScope(/* element */)
    // Apply block, resolve classes, render
}
```

## Data Flow: mosaikAdd

```
User: ./gradlew mosaikAdd --component=modal

1. MosaikAddTask receives --component=modal
2. Reads registry.json from plugin resources
3. Resolves dependencies: modal → [theme, button, modal]
4. For each dependency (topological order):
   a. Check if target file exists → abort or skip
   b. Read component source from bundled resources
   c. Replace package: mosaik.ui.components → user's packageName
   d. Replace prefix: m → user's prefix
   e. Write to src/main/kotlin/<package-path>/
5. Log summary
```

## v1 Components

| Component | File | Dependencies | Description |
|-----------|------|--------------|-------------|
| theme | Theme.kt | — | Shared enums, utilities, MosaikScope |
| button | Button.kt | theme | DaisyUI button with variants |
| card | Card.kt | theme | Card container with title/body/actions |
| navbar | Navbar.kt | theme | Navigation bar |
| footer | Footer.kt | theme | Page footer |
| badge | Badge.kt | theme | Small status indicator |
| alert | Alert.kt | theme | Feedback messages |
