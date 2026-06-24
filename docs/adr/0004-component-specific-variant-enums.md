# ADR 0004: Component-Specific Variant Enums

## Status

Accepted

## Context

Theme.kt defines a shared `Variant` enum with entries: Primary, Secondary, Accent, Ghost, Link, Error, Success, Warning. Button uses this enum directly, and it maps well because DaisyUI buttons support all these color roles.

Other DaisyUI components have different variant palettes:

- **Badge** supports: primary, secondary, accent, ghost, info, success, warning, error, neutral, outline. It adds `info`, `neutral`, and `outline` which don't exist in the shared enum.
- **Alert** supports only: info, success, warning, error. Accepting `Ghost`, `Link`, or `Accent` would compile but produce no visual distinction — a silent bug.

Two approaches were considered:

1. **Extend the shared `Variant` enum** to be the union of all component palettes, and accept that some variants are no-ops on some components. This keeps one enum but makes the API misleading — `Alert(variant = Variant.Ghost)` compiles and does nothing.

2. **Component-specific enums** where each component declares exactly the variants it supports. The shared `Variant` remains for components where the full color role set applies (like Button). Components with a different palette define their own enum (e.g. `BadgeVariant`, `AlertVariant`).

## Decision

Use component-specific variant enums. Each component declares an enum containing exactly the DaisyUI variants it supports. The shared `Variant` in Theme.kt is used only by components where the full set applies.

Component-specific enums live in their respective component files, not in Theme.kt.

## Consequences

- The compiler enforces variant validity per component — no silent no-op variants.
- Each component's API surface documents exactly what it supports.
- Theme.kt stays stable as new components are added — no accumulation of component-specific concerns.
- Components that share the full DaisyUI color role set (like Button) continue using the shared `Variant` without duplication.
- Developers must learn which enum to import per component, but IDE autocomplete resolves this naturally.
