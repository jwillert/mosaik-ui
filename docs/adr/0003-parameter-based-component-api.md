# ADR 0003: Parameter-Based Component API

## Status

Accepted (supersedes [ADR 0001](0001-scope-based-component-api.md))

## Context

ADR 0001 established a scope-based API where each component exposes its design tokens
inside a DSL block via a dedicated Scope class:

```kotlin
mButton {
    variant = Primary
    size = Lg
    +"Click me"
}
```

During implementation it became clear that the Scope class only explicitly delegates a
fixed set of HTML attributes (`id`, `name`, `type`, `disabled`). Attributes added by
third-party library extensions (e.g. htmx adds `hxGet`, `hxTarget` to `BUTTON` as
Kotlin extension properties) are invisible on the Scope class because Kotlin extension
resolution is static — the extension must be defined on the declared receiver type.

Two alternatives were evaluated:

1. **Scope IS-A BUTTON** (`ButtonScope : BUTTON`) — extensions would resolve correctly,
   but creates a lifecycle conflict: `class` must be computed after the block runs (to
   read `variant`/`size`) yet must be written before any content triggers the HTML `>`.
   Solving this requires a `DeferringConsumer` wrapper whose complexity exceeds the
   benefit.

2. **Design tokens as parameters** — `variant`/`size` move out of the block into
   function parameters; the block receives the raw HTML tag as its receiver. No Scope
   class, no lifecycle conflict, full extension compatibility.

## Decision

Use a parameter-based API. Design tokens (`variant`, `size`, extra `classes`) are
function parameters. The block is typed as the raw kotlinx.html element (e.g.
`BUTTON.() -> Unit`):

```kotlin
mButton(Variant.Primary, Size.Lg) {
    hxGet = "/save"   // extension property — works natively
    disabled = true
    +"Save"
}
```

No Scope class is needed. The Scope term is removed from the domain glossary.

## Consequences

- All HTML attributes and library extensions (htmx, etc.) work natively — the block IS
  the underlying HTML element.
- No Scope class boilerplate per component.
- Design tokens are not inside the block; callers use named parameters
  (`mButton(variant = Primary)`) which remain readable.
- `MosaikScope` interface and per-component Scope classes are removed.
- Sub-components (e.g. card sections) follow the same pattern.
