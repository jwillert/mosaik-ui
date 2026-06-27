# DaisyUI class token inventory

Status: working inventory

## Rule

Raw DaisyUI class tokens should not be authored directly in Kotlin docs or component examples. Mosaik source should expose components, structural sub-components, or type-safe modifiers instead. Utility classes that are not DaisyUI component tokens (for example Tailwind layout/spacing classes such as `w-full`, `gap-2`, `bg-base-100`) may still be passed through `classes`.

This is repo-wide, not limited to user-facing examples. Docs infrastructure should also use Mosaik wrappers once a DaisyUI token is identified.

## Why this exists

The docs should demonstrate Mosaik as the public API. If docs use `div("card-body")`, `span("loading loading-spinner")`, or `a(classes = "btn btn-ghost")`, they teach users to bypass the component layer and make it harder to notice missing abstractions.

## Current scan

Command used for the first pass:

```bash
rg '\b(classes\s*=\s*|[a-zA-Z0-9_]+\()"[^\"]*(btn|badge|alert|card-body|card-title|card-actions|loading loading|navbar-|footer-title|link link|input input|form-control|label-text|select select|table table|tabs|tab-content|menu|menu-title|menu-active)[^\"]*"' mosaik-docs/src/main/kotlin -n
```

False positives to ignore in later scans: route labels or installation strings such as `installSection("badge")` are not class-token usage.

## Findings by area

### Already replaced in this session

- `InteractivityPage.kt`: `card-body`, `card-title`, `card-actions`, and `loading loading-spinner` were replaced with `mCardBody`, `mCardTitle`, `mCardActions`, and `mLoading`.
- `Card.kt`: flow-position overloads were added for card sub-components so card bodies can be rendered inside wrappers such as `<form>` without raw `div("card-body")`.

### Existing component, docs should switch to it

- `AlertPage.kt`
  - Raw: `button(classes = "btn btn-sm btn-ghost")`
  - Replacement direction: `mButton(size = Size.Sm, style = ButtonStyle.Ghost)` once the example does not require a raw `<button>` beyond what `mButton` supports.

### Existing component is close, but element semantics need a design decision

- `NavbarPage.kt`
  - Raw: `a(classes = "btn btn-ghost ...")`
  - Problem: current `mButton` renders `<button>`, while navbar navigation needs anchors.
  - Replacement direction: add a button-as-anchor API, link-button component, or navbar link sub-component.

### Missing component candidates

- Forms in `InteractivityPage.kt` and `Layout.kt`
  - Raw: `label("form-control w-full")`
  - Raw: `div("label")`
  - Raw: `span("label-text")`
  - Raw: `input(..., classes = "input input-bordered w-full")`
  - Raw: `select(classes = "select select-sm select-bordered ...")`
  - Replacement direction: form-control, label text, input, and select components with type-safe size/style modifiers.

- Footer links/titles in `FooterPage.kt`
  - Raw: `h6("footer-title")`
  - Raw: `a(..., classes = "link link-hover")`
  - Replacement direction: footer title sub-component plus link component/modifier.

- Tables in docs pages/helpers
  - Raw: `table("table table-zebra")`
  - Files: `DocumentationBlocks.kt`, `CardPage.kt`, `NavbarPage.kt`, `InteractivityPage.kt`
  - Replacement direction: table component with zebra modifier.

- Tabs in `DocumentationBlocks.kt`
  - Raw: `tabs`, `tabs-lifted`, `tab`, `tab-content`
  - Replacement direction: tabs component or docs-only tab abstraction backed by component tokens.

- Menus in `Layout.kt`
  - Raw: `menu`, `menu-title`, `menu-active`
  - Replacement direction: menu component with title/item sub-components and active-state modifier.

## Working classification

| Token family | Current abstraction | Action |
| --- | --- | --- |
| `card`, `card-body`, `card-title`, `card-actions` | `mCard`, `mCardBody`, `mCardTitle`, `mCardActions` | Keep replacing any raw usage. |
| `loading`, `loading-*` | `mLoading` | Keep replacing any raw usage. |
| `btn`, `btn-*` | `mButton` for `<button>` only | Add anchor/link-button story before navbar cleanup. |
| `alert`, `alert-*` | `mAlert` | Replace raw alert buttons with `mButton`; raw alert container usage should not appear. |
| `badge`, `badge-*` | `mBadge` | Raw usage should not appear. |
| `navbar-*` | `mNavbarStart/Center/End` | Raw usage should not appear. |
| `footer`, `footer-center` | `mFooter`, but `footer-center` is still a raw modifier | Consider `FooterLayout.Center` or similar type-safe modifier. |
| `footer-title` | none | Add footer title sub-component. |
| `link`, `link-hover` | none | Add link component/modifiers. |
| `input`, `input-bordered` | none | Add input component. |
| `select`, `select-bordered`, `select-sm` | none | Add select component. |
| `form-control`, `label`, `label-text` | none | Add form-control/label sub-components. |
| `table`, `table-zebra` | none | Add table component/modifiers. |
| `tabs`, `tab`, `tab-content` | none | Add tabs component/sub-components. |
| `menu`, `menu-title`, `menu-active` | none | Add menu component/sub-components. |

## Suggested cleanup order

1. Replace easy raw `btn` usages in `AlertPage.kt` with `mButton`.
2. Decide the anchor-button API for navbar links.
3. Add form/input/select abstractions; then clean `InteractivityPage.kt` and `Layout.kt`.
4. Add footer title/link abstractions; then clean `FooterPage.kt`.
5. Add docs infrastructure abstractions for table, tabs, and menu.
6. Add a CI/static check that fails on raw DaisyUI component tokens outside the component implementations and this inventory.
