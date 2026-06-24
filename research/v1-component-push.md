# Mosaik UI — v1 Component Push + Docs Upgrade

## Components (5 new)

| Component | Pattern | Variant Enum | Sub-Components |
|-----------|---------|-------------|----------------|
| Card | FlowContent extension | — (no variant) | mCardBody, mCardTitle, mCardActions |
| Navbar | FlowContent extension | — | mNavbarStart, mNavbarCenter, mNavbarEnd |
| Footer | FlowContent extension | — | — |
| Badge | FlowContent extension | BadgeVariant (primary, secondary, accent, ghost, info, success, warning, error, neutral, outline) | — |
| Alert | FlowContent extension | AlertVariant (info, success, warning, error) | — |

All components follow the existing Button pattern: thin DaisyUI wrapper, parameters for design tokens, block receives raw kotlinx.html element.

## Per-Component Deliverables

- `.kt` source file in `mosaik-components/src/main/kotlin/mosaik/ui/components/`
- Unit test in `mosaik-components/src/test/kotlin/`
- VRT scenarios + visual test in `mosaik-components/src/vrt/kotlin/`
- Entry in `registry.json`
- Docs page in `mosaik-docs`

## Docs Page Template (per component)

1. Title + short description (1-2 sentences, what it is, inspired by DaisyUI docs)
2. Installation (`./gradlew mosaikAdd --component=<name>`)
3. Basic Usage (Kotlin code block with minimal example)
4. Variants/Options Showcase (rendered component + Kotlin code side by side)
5. API Reference (parameter table: name, type, default, description)

Code examples are static strings in the page functions (option A). Code extraction from sources is future work.

## Docs Upgrade

- Theme switcher in sidebar (unlock all DaisyUI themes via `themes: all`)
- Upgrade existing Button page to new template
- Description texts inspired by DaisyUI (what the component is, structure, usage)

## Explicitly Out of Scope

- Form elements (Input, Select, Textarea) — needs FormControl wrapper pattern
- Interactive components (Modal, Dropdown, Tabs, Toast) — needs JS
- Code extraction from source files at build time
- CLI tooling
