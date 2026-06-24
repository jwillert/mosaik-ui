# Mosaik UI — Domain Glossary

## What is Mosaik UI?

A ShadCN-inspired component toolkit for the Kotlin/Ktor/Gradle ecosystem. Components are installed as source files into the user's project — not consumed as a library dependency.

## Terms

- **Component** — A single `.kt` file containing one or more kotlinx.html extension functions that render HTML with DaisyUI classes. Once installed, the file belongs to the user.
- **Design tokens** — The Mosaik-specific properties (`variant`, `size`, extra `classes`) passed as function parameters to a component function. The block receives the raw kotlinx.html element as its receiver, so all HTML attributes and library extensions (e.g. htmx) work natively. See ADR 0003.
- **Theme** — The shared foundation (`Theme.kt`). Contains shared enums (`Variant`, `Size`) and `buildClasses` utility. Installed automatically as a dependency of every component.
- **Registry** — A JSON manifest listing all available components with their names, descriptions, files, and dependencies. Bundled as a resource in the Gradle plugin for v1.
- **Resolver** — Pure Kotlin logic in `mosaik-core` that takes a component name, reads the registry, and returns all transitive dependencies in topological order.
- **File Writer** — Pure Kotlin logic in `mosaik-core` that copies component source files into the user's project, replacing the placeholder package with the user's configured package name.
- **Placeholder Package** — `mosaik.ui.components` — the package used in component source templates. Rewritten to the user's package at install time.
- **Prefix** — `m` — the function name prefix for all components (`mButton`, `mCard`). Fixed in v1, not configurable.
- **Install** — The act of copying a component's source file(s) into the user's project via `mosaikAdd`. After installation, the user owns the code.
- **Golden** — A baseline screenshot used by ktor-vrt for visual regression comparison.
- **Scenario** — A data class in ktor-vrt describing a single visual test case: a name, a render function, and optional pre-screenshot JavaScript.
- **VRT** — Visual Regression Testing. Renders components in a browser, screenshots them, and diffs against goldens.
