# ADR 0005: Interactivity as Documentation, Not Components

## Status

Accepted

## Context

Mosaik UI components are pure HTML emitted from Kotlin — they carry no JavaScript. When
a developer wants interactivity (AJAX form submit, dismissible alerts, lazy loading),
they must pair the components with a client-side library. Three libraries are viable for
the server-rendered, HTML-attribute-driven style Mosaik promotes: **htmx**, **Alpine.js**,
and **Datastar**.

Two approaches were considered:

1. **Ship framework-specific component variants** — e.g. `mButtonHtmx`, `mButtonAlpine`,
   `mButtonDatastar` — each emitting the right attributes for its library. This gives
   users a copy-paste component but triples the component surface, ties Mosaik to three
   external libraries' APIs, and requires keeping up with their releases.

2. **Document interactivity as usage patterns** — the components stay unchanged (ADR-0003
   already ensures the raw HTML element is the block receiver, so any attribute works
   natively). The docs show how to wire each library's attributes onto existing
   components, with side-by-side code examples and a comparison of trade-offs.

## Decision

Document interactivity as usage patterns on existing components. No new component
functions are created for htmx, Alpine.js, or Datastar.

The docs show three levels of examples:

- **Per-component sections** — small, focused examples on each component's page (e.g.
  "Button: form submit with loading state") shown in a tabbed code switcher with a
  matching working preview for each implementation style.
- **Composed examples** — full-page examples (login form, register form) on a standalone
  Interactivity guide page, showing how multiple components compose with each library and
  including a matching working preview for each implementation style.
- **Building blocks overview** — a comparison of each library's primitives, best-fit use
  cases, and limitations.

## Consequences

- Components remain library-agnostic — no coupling to htmx, Alpine.js, or Datastar APIs.
- No maintenance burden when external libraries release breaking changes.
- Users can mix approaches (e.g. htmx for server-driven forms, Alpine.js for client-side
  toggles) because the components impose no opinion.
- The docs site itself may use JavaScript for the code toggle tabs — this is docs
  infrastructure, not a component concern (same precedent as the theme switcher).
- Adding a new interactivity library later means adding a tab to existing examples, not
  building new components.
- Live documentation demos that issue network requests must be backed by real docs-app
  Ktor routes; see ADR-0007.
- Interactivity examples must include matching working previews for each implementation
  style; see ADR-0008.
- Users who want a ready-made interactive component must copy the code example and adapt
  it — there is no `mosaikAdd --component=button-htmx`. This is intentional: the ShadCN
  model means the user owns and customises the code.
