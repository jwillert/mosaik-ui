# Button Component Reference Gallery Slice

## Goal

Create the first vertical slice of the new Mosaik documentation model by making Button's public API match the desired docs experience, then redesigning the Button docs page around a visual-first Component Reference Gallery.

## Decisions this plan depends on

- Component docs use a **Component Reference Gallery** shape: visual-first hero, install/usage, visual examples, interactive examples, API reference.
- DaisyUI component semantics are exposed as **Type-safe Modifiers**, not normal raw class usage.
- `classes` remains an escape hatch for layout and one-off styling utilities, not the primary way to use DaisyUI component modifiers.
- Interactive examples use a docs-wide **Interaction Style**: htmx, Alpine.js, or Datastar.
- Static and interactive examples use a reusable **Example Card** with preview, code, copy button, and client-side syntax highlighting where feasible.
- Docs stay server-rendered Kotlin/kotlinx.html with Tailwind/DaisyUI and small vanilla JavaScript.

## Scope

### 1. Button API

Replace Button's shared `Variant` usage with a component-specific `ButtonVariant`:

- `Default` maps to no color class.
- `Neutral`, `Primary`, `Secondary`, `Accent`, `Info`, `Success`, `Warning`, `Error` map to DaisyUI button color classes.

Add orthogonal type-safe modifiers:

- `ButtonStyle`: `Solid`, `Outline`, `Dash`, `Soft`, `Ghost`, `Link`.
- `ButtonShape`: `Default`, `Square`, `Circle`.
- `ButtonWidth`: `Default`, `Wide`, `Block`.

Do not add loading as a Button modifier. Loading is content.

### 2. Loading component

Add reusable `mLoading` so Button docs can show loading content without leaking DaisyUI classes.

Proposed API:

- `LoadingType`: `Spinner`, `Dots`, `Ring`, `Ball`, `Bars`, `Infinity`.
- `size`: reuse existing `Size`.
- `classes`: escape hatch only.

### 3. Tests and visual coverage

Update/add tests for generated classes:

- Button variants.
- Button styles.
- Button shape and width modifiers.
- Loading type and size classes.

Update VRT scenarios enough to cover the new Button docs examples and the new loading component where practical.

### 4. Docs shell helpers

Introduce reusable docs helpers before rewriting all pages:

- `ExampleCard`: preview + Kotlin code + copy button + syntax-highlightable code markup.
- Global/local Interaction Style switch backed by `localStorage`.
- Highlight.js from CDN for the first slice, with markup structured so assets can be vendored later.

Keep helpers compatible with ADR 0011: shared blocks should be extracted because they will be repeated across component pages, while page-owned content remains page-owned.

### 5. Button page redesign

Rewrite only the Button docs page first.

Target page order:

1. Title and short description.
2. Polished hero preview.
3. Installation.
4. Basic usage Example Card.
5. Visual modifier gallery:
   - Default.
   - Color variants.
   - Sizes.
   - Styles: outline, dash, soft, ghost, link.
   - Shapes: square, circle.
   - Widths: wide, block.
   - Disabled.
   - Icon/content composition.
   - Loading content using `mLoading`.
6. Interactive usage Example Card driven by selected Interaction Style.
7. API reference.

Avoid raw DaisyUI component classes in normal examples when a Mosaik abstraction exists. Raw `classes` may still appear for layout utilities such as spacing, width, shadow, or flex alignment.

## Out of scope for this slice

- Migrating Card/Navbar/Footer/Badge/Alert docs.
- Adding `FooterLayout` implementation, though it is the chosen direction for Footer.
- Adding an icon abstraction.
- Vendoring highlight.js locally.
- Full docs app visual redesign beyond what Button and shared helpers require.

## Acceptance criteria

- Button docs demonstrate the new visual-first Component Reference Gallery without using raw DaisyUI button modifier classes.
- Button API exposes the documented DaisyUI button modifiers type-safely.
- Loading button examples use `mLoading`, not raw `loading loading-spinner` classes.
- Copy buttons work for Example Card code blocks.
- Kotlin code blocks are syntax-highlighted in the browser via highlight.js.
- Existing docs routes still render.
- Relevant tests pass.
