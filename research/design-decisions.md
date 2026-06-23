# Mosaik UI — Design Decisions

## Concept

ShadCN-like CLI/tooling for the Kotlin/Ktor/Gradle ecosystem.
Copy-paste-able UI components installed via Gradle Plugin into your project — not a library dependency.

## v1 Scope

- UI components only (kotlinx.html + Tailwind/DaisyUI)
- CSS-only, no JavaScript
- Gradle Plugin only (CLI is future-work)
- Local distribution via `includeBuild` (Maven Central later)

## Component API

**Scope-Ansatz** — each component has its own Scope class that exposes both Mosaik properties (variant, size) and HTML element properties (id, type, disabled) in a flat structure.

```kotlin
dButton {
    // Mosaik props
    variant = Primary
    size = Lg

    // HTML props — directly available, no element. prefix
    id = "submit"
    type = ButtonType.submit
    disabled = true

    // Content
    +"Click me"
}
```

Rationale: scales consistently from 2 to 10+ properties per component. Properties and children live together. Follows kotlinx.html principles (set attributes both via function params and inside the scope block).

## Component Scoping

- **Top-level components** (`mButton`, `mCard`, `mNavbar`) are extension functions on `FlowContent`
- **Sub-components** (`cardTitle`, `cardBody`, `cardActions`) are only available on their parent scope (e.g. `CardScope`)
- IDE autocomplete shows only what's valid in the current context

## Shared Foundation: Theme.kt

Installed automatically with the first component. Contains:

- Shared enums: `Size` (Xs, Sm, Md, Lg), `Variant` (Primary, Secondary, Accent, ...)
- Utility: `buildClasses(vararg classes: String?): String`
- Base interface: `MosaikScope` with `variant`, `size`, `classes` properties

## Naming

- **Project name:** Mosaik UI
- **Function prefix:** `m` (default, configurable via Gradle extension)
- **Placeholder package:** `mosaik.ui.components` (replaced at install time)
- Prefix is configurable: `mosaikUi { prefix = "d" }` changes `mButton` to `dButton`

## Registry

- JSON format, bundled as resource in the Gradle Plugin for v1
- Same format works for remote registry later — swap source, no format change
- Each component entry: name, files, dependencies, description

## Gradle Plugin

```kotlin
plugins {
    id("io.mosaik-ui") version "0.1.0"
}

mosaikUi {
    packageName = "com.example.app.ui"
    prefix = "m" // default
}
```

### Tasks

| Task | Purpose |
|------|---------|
| `mosaikAdd --component=<name>` | Install a component |
| `mosaikList` | List all available components |
| `mosaikStatus` | Show which components are installed |

### Conflict Handling

- If file already exists: abort with message
- `--force` flag to overwrite intentionally
- Rationale: once installed, the file belongs to the user (copy-paste philosophy)

### Dependency Resolution

- Automatic install of transitive dependencies
- Log what was installed:
  ```
  > Task :mosaikAdd
  Installing: modal
    → Dependency: theme (installing)
    → Dependency: button (already installed)
    → Installed: Modal.kt
  Done. 2 components installed, 1 already present.
  ```

### Installation Mechanics

1. Replace placeholder package `mosaik.ui.components` → user's `packageName`
2. Replace prefix `m` → user's `prefix`
3. Write files to `src/main/kotlin/<package-path>/`

## Documentation

- Ktor app in `mosaik-docs/` module that dogfoods the components
- Uses `mosaik-components` as module dependency for fast iteration
- Each component gets a page showing all variants
- The docs site IS the proof that components work

## Testing Strategy

| Level | What | How |
|-------|------|-----|
| Compile | Syntax, imports, types | `mosaik-components` is a compiled Kotlin module |
| Unit | HTML output, CSS classes | Assert generated HTML contains correct classes/structure |
| Visual Regression | Rendering, layout, spacing | ktor-vrt with Playwright screenshots + golden image comparison |

## Distribution

- **v1:** Local / `includeBuild` — no publishing infrastructure needed
- **Later:** Gradle Plugin Portal for the plugin, Maven Central for libraries
- **Maven Group v1:** `dev.jwillert` (migrate to dedicated domain group when publishing)
