# PRD: Components Needed to Remove Raw DaisyUI Tokens

## Problem

The docs still need raw DaisyUI class tokens because several DaisyUI concepts do not yet have Mosaik wrappers. ADR 0013 requires those tokens to be hidden behind components, sub-components, or type-safe modifiers across the repo.

## Goal

Create the missing Mosaik abstractions needed to remove raw DaisyUI tokens from docs and docs infrastructure.

## Scope

### Link/button semantics

Navbar examples use anchors styled as buttons (`btn btn-ghost`). Add an API that preserves anchor semantics while hiding DaisyUI button tokens.

Possible shapes:

- `mButtonLink(href = ..., variant/style/size...) { ... }`
- `mNavbarLink(href = ..., active = ...) { ... }`
- generic button component with render target selection.

Decision needed before implementation.

### Form controls

Interactivity examples and layout selectors use form-related tokens:

- `form-control`
- `label`
- `label-text`
- `input input-bordered`
- `select select-sm select-bordered`

Add components/sub-components with type-safe modifiers for common DaisyUI form variants and sizes.

### Footer title and links

Footer examples use:

- `footer-title`
- `link link-hover`

Add footer title and link abstractions, or a reusable link component with hover style modifier.

### Docs infrastructure

Docs helpers currently use:

- `table table-zebra`
- `tabs tabs-lifted`, `tab`, `tab-content`
- `menu`, `menu-title`, `menu-active`

Add components or internal Mosaik wrappers so even docs infrastructure follows ADR 0013.

## Non-goals

- Do not wrap every Tailwind utility class.
- Do not add JavaScript behavior to components.
- Do not change rendered HTML unnecessarily; wrappers should preserve current DaisyUI markup.

## Acceptance criteria

- No docs page or docs helper authors raw DaisyUI class tokens for covered concepts.
- Each new public component has tests covering rendered classes and modifier mapping.
- Component docs/examples demonstrate the new Mosaik API rather than raw class strings.
- Existing docs previews remain visually equivalent except where intentionally improved.
