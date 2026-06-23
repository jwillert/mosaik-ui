# Mosaik UI — Future Work (post v1)

## v1.1 — More Components

- Modal/Dialog (needs JS or CSS-only trick)
- Tabs
- Dropdown
- Table
- Input / TextArea / Select
- Toast

## v2 — Interactivity

- Evaluate: HTMX, Alpine.js, or minimal vanilla JS
- Interactive component states (dialog open/close, dropdown toggle)
- Server-driven interactions via HTMX as primary approach (fits Ktor philosophy)

## CLI

- Standalone CLI via picocli for interactive experience
- Distribution via SDKMAN (`sdk install mosaik`)
- Interactive prompts for dependency confirmation
- Colored output, progress bars

## Remote Registry

- JSON registry hosted remotely (GitHub raw, dedicated API)
- Plugin fetches from URL instead of bundled resources
- Allows component updates without plugin version bump
- Registry abstraction in mosaik-core already supports this (swap implementation)

## Publishing

- Maven Central for ktor-vrt and ktor-vrt-gradle-plugin
- Gradle Plugin Portal for mosaik-gradle
- Dedicated domain and Maven group (e.g. `dev.mosaikui` from `mosaikui.dev`)

## Additional Component Packs

- Beyond DaisyUI: headless components (no CSS framework dependency)
- Compose HTML / Compose Web components
- HTMX-enhanced components
- Ktor route templates / infrastructure patterns

## Documentation

- Dokka for API reference alongside the Ktor docs app
- Integration test that installs components via the actual plugin (CI step)
- Component playground with live editing
