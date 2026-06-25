# ADR 0006: CSS-Only Code Toggle Tabs

## Status

Accepted

## Context

The interactivity documentation (ADR-0005) needs a mechanism to show the same example
implemented three ways (htmx, Alpine.js, Datastar) without overwhelming the page. The
standard pattern for this is tabbed code blocks — click a tab to switch between code
snippets.

Three approaches were considered:

1. **JavaScript tabs** — a small inline script that toggles visibility on click. Works
   everywhere but adds JS where it isn't structurally necessary.

2. **DaisyUI radio tabs** — `input[type=radio]` elements with class `tab` whose adjacent
   `.tab-content` divs are shown/hidden via CSS (`:checked + .tab-content`). No
   JavaScript. Each tab group uses a unique `name` attribute to prevent interference
   between multiple tab groups on the same page.

3. **Show all three inline** — no tabs, just three consecutive code blocks labelled
   "htmx", "Alpine.js", "Datastar". Simplest, but makes the page very long and harder
   to compare.

## Decision

Use DaisyUI radio tabs (option 2). The `interactivityTabs` helper function in the docs
takes a unique `id` (used as the radio group `name`), three code strings, and renders a
`tabs tabs-lift` container with three `tab-content` panels. The first tab (htmx) is
checked by default.

The tabs wrapper carries the `not-prose` class to prevent Tailwind Typography from
overriding DaisyUI's tab styling.

## Consequences

- No JavaScript added — the tabs are pure CSS, matching the project's "minimal JS" stance.
- Multiple tab groups on the same page work correctly because each has a unique `name`.
- The `interactivityTabs` helper is reusable across component pages and the guide page.
- Tab state does not persist across navigation — switching to another page and back resets
  to the htmx tab. This is acceptable; a localStorage-based persistence would require JS
  and is out of scope.
- If DaisyUI changes its tab markup in a future major version, only the helper function
  needs updating.
