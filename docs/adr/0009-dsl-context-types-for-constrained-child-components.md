# ADR 0009: DSL Context Types for Constrained Child Components

## Status

Accepted

## Context

ADR 0003 established a parameter-based API where design tokens are function parameters
and the DSL block receives the raw kotlinx.html element. This works well for simple
components like Button, Badge, and Alert, enabling native access to HTML attributes
and third-party library extensions (e.g. htmx).

However, for compound components like Card and Navbar that have child components,
the raw receiver model is too permissive. Child components implemented as extensions
on `DIV` are available inside **any** div, not just their semantic parent. For example,
`mCardTitle` and `mCardActions` should only be callable inside a card body, but with
raw receivers they appear in IDE autocomplete everywhere a div is used.

This makes invalid DaisyUI structures easy to write and discover through autocomplete,
even though the user's intent is that child components should only be usable in their
correct semantic context.

## Decision

Introduce public Mosaik DSL context types for compound components that have constrained
child components. Simple components continue to use raw kotlinx.html receivers.

For compound components:

- Create prefixed public context types (e.g. `MCard`, `MCardBody` for Card; `MNavbar`
  for Navbar) that wrap the underlying HTML element
- Context types preserve valid HTML operations by delegating to or behaving like the
  underlying element where possible
- Child components are extension functions on the narrowest valid DSL context, not the
  broad parent context
- Use a shared `@MosaikDsl` marker annotation to prevent Mosaik child components from
  leaking into nested ordinary HTML blocks
- Public context type names use the `M` prefix aligned with the fixed `m` function
  prefix (per ADR 0002)

For Card specifically:
- `mCard` receives `MCard.() -> Unit`
- `mCardBody` is an extension on `MCard`, receives `MCardBody.() -> Unit`
- `mCardTitle` and `mCardActions` are extensions on `MCardBody`
- Context types are div-backed to preserve valid HTML operations

For Navbar specifically:
- `mNavbar` receives `MNavbar.() -> Unit`
- `mNavbarStart`, `mNavbarCenter`, `mNavbarEnd` are extensions on `MNavbar`
- Context types are div-backed to preserve valid HTML operations

The DSL marker prevents child-component functions from being callable inside nested
ordinary HTML blocks when users enter nested HTML builders.

Design tokens remain as function parameters; this does not reintroduce scope-based
mutable design-token APIs.

## Consequences

### Benefits

- Child components are only callable in their valid semantic context
- IDE autocomplete guides users toward correct structure
- Invalid child-component placement fails at compile time
- Ordinary HTML elements and third-party extensions continue to work where the context
  type behaves like the raw element
- Simple components keep minimal APIs with raw receivers
- Generated HTML and visual output remain unchanged for valid structures
- Pattern is reusable for future compound components

### Costs

- Compound components require public context type definitions
- Context types appear in installed source that users may edit
- ADR 0003's "raw receiver everywhere" model is narrowed for compound components
- Third-party extensions may require the context type to explicitly delegate or extend
  the underlying element's interface

### Trade-offs

This deliberately trades ADR 0003's universal raw receiver model for semantic
correctness in compound components. The motivation in ADR 0003 (native extension
compatibility) is preserved **where possible** through context types that behave like
their underlying element. But compile-time child constraints are valued over
universal extension transparency for these specific components.

Simple components without constrained children continue to follow ADR 0003's raw
receiver pattern without modification.
