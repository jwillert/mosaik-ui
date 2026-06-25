# ADR 0008: Interactivity Examples Require Matching Previews

## Status

Accepted

## Context

ADR-0005 documents htmx, Alpine.js, and Datastar as usage patterns rather than new
Mosaik component variants. ADR-0007 requires live networked examples to call real Ktor
routes in the docs app.

The interactivity docs can still be incomplete if they only show code. A reader needs to
see the example working and compare the visible behaviour across htmx, Alpine.js, and
Datastar. Without a rendered preview, the docs do not prove that the example integrates
with the component API, DaisyUI styling, and any docs-app routes it depends on.

## Decision

Every interactivity example that compares htmx, Alpine.js, and Datastar must include a
matching working preview for each implementation style.

Each tab is self-contained:

1. A rendered preview for that style.
2. The code snippet for that style.

For example, the htmx tab shows the htmx preview above the htmx code; the Alpine.js tab
shows the Alpine.js preview above the Alpine.js code; and the Datastar tab shows the
Datastar preview above the Datastar code.

The preview and code snippet should come from a shared source where practical, but docs
clarity can justify duplication. When duplicated, they must stay semantically aligned:
the preview should demonstrate the same component usage and interaction pattern described
by the snippet.

This applies to both:

- per-component interactivity sections; and
- composed examples in the Interactivity guide.

## Consequences

- An interactivity section is not complete if it contains only code blocks.
- The `interactivityTabs` docs helper must support per-style preview content, not only
  code strings.
- Reviewers and agents can reject interactivity docs that add request attributes or
  behaviour snippets without visible working previews.
- More docs code may be needed, but the examples become easier to understand and verify.
