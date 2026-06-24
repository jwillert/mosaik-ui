# ADR 0001: Scope-Based Component API

## Status

Superseded by [ADR 0003](0003-parameter-based-component-api.md)

## Context

The predecessor project (kopetal) used function parameters for component APIs:

```kotlin
koButton(label = "Click", variant = PRIMARY, size = MD, disabled = false) { ... }
```

This works for simple components but becomes unwieldy as property count grows. It also separates content (the `label` parameter) from the component block.

## Decision

Use a scope-based API where each component has its own Scope class:

```kotlin
mButton {
    variant = Primary
    size = Lg
    id = "submit"
    disabled = true
    +"Click me"
}
```

The Scope class exposes Mosaik properties (`variant`, `size`) and delegates HTML element properties (`id`, `type`, `disabled`) via Kotlin property delegation to the underlying kotlinx.html element.

## Consequences

- Scales consistently from 2 to 10+ properties per component
- Content and configuration live together in one block
- Follows kotlinx.html's own conventions (attribute setting inside blocks)
- Requires a Scope class per component (more boilerplate than function parameters)
- Sub-components (e.g. `cardTitle`, `cardBody`) are naturally scoped to their parent's Scope class, giving IDE autocomplete correctness for free
