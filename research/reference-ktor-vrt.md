# Reference: ktor-vrt (existing code)

## Source

Originally built in `/home/jwillert/Downloads/ktor-auth-plugin/ktor-vrt` and `ktor-vrt-gradle-plugin`.
Currently published to GitHub Packages. Will be migrated into the mosaik-ui mono-repo and published to Maven Central.

## Key Classes

### ktor-vrt (library)

- **`Scenario`** — data class: name, captureSelector, optional beforeShot JS, render function (`FlowContent.() -> Unit`)
- **`VrtHarness`** — renders scenarios in Playwright browser, diffs against golden images using `image-comparison` library
- **`VisualRegressionSpec`** — abstract Kotest `FunSpec` base class, one-liner to create a VRT test suite
- **`PlaywrightServerContainer`** — Testcontainers wrapper for deterministic Docker-based rendering

### ktor-vrt-gradle-plugin

- **`KtorVrtPlugin`** — creates `vrt` source set, registers `vrtTest` and `vrtTestDocker` tasks
- **`KtorVrtExtension`** — config: css file, cssTaskDependency, goldenDir, diffDir, htmlAttributes, wrapperClasses

## Features

- **Dual mode:** local (tolerant, 5% pixel threshold) and Docker (strict, 0.1% threshold)
- **Golden management:** first run creates baseline, subsequent runs compare
- **`-PupdateGoldens`** flag to regenerate baselines
- **`beforeShot` JS:** execute JavaScript before screenshot (e.g. open a dialog)
- **Custom capture selector:** screenshot specific elements, not full page
- **HTML attributes & wrapper classes:** configurable via extension (e.g. `data-theme=light`)

## Integration with Mosaik

mosaik-components will use ktor-vrt to test every component variant visually:

```kotlin
class ButtonVisualTest : VisualRegressionSpec(listOf(
    Scenario("button-primary-md") { mButton { variant = Primary; size = Md; +"Click" } },
    Scenario("button-secondary-lg") { mButton { variant = Secondary; size = Lg; +"Click" } },
    // ... all variant combinations
))
```
