# ADR 0007: Live Interactivity Demo Routes

## Status

Accepted

## Context

ADR-0005 keeps Mosaik components JavaScript-free and documents interactivity as usage
patterns with htmx, Alpine.js, and Datastar. Some docs examples are rendered as live HTML
inside the Ktor docs app, not only as inert code snippets.

When a live example includes request-producing attributes such as `hx-post`, `hx-get`,
`data-post`, `data-get`, or JavaScript `fetch(...)`, the browser can actually call those
URLs. If the corresponding Ktor route does not exist, the documentation teaches an
incomplete integration and the live demo is broken.

At the same time, copyable code snippets should stay readable. A snippet that teaches a
login form is clearer when it uses `/login` than when it exposes the docs site's internal
example URL structure.

## Decision

Every live interactive demo in the docs app must call a route that exists in the Ktor docs
application.

Docs-app-only endpoints for live demos are namespaced under `/_examples/...`, for example:

- `/_examples/button/submit`
- `/_examples/card/content`
- `/_examples/login`
- `/_examples/register`

Copyable code snippets may use simplified app-local paths such as `/login` or
`/card-content` when that makes the example easier to understand. The surrounding
explanation must make it clear that the route name is application-specific: the client
attribute and the Ktor route must match.

## Consequences

- Live documentation examples do not contain dead `hx-*`, `data-*`, or `fetch(...)` URLs.
- Docs routes used only for examples are visually separated from real documentation pages.
- Snippets remain copyable and teaching-focused instead of leaking docs-site implementation
  details.
- Adding a new live networked example requires updating both the rendered markup and the
  Ktor docs routing table.
