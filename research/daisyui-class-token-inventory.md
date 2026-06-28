# DaisyUI class token inventory

Status: complete (as of 2026-06-28)

## Rule

Raw DaisyUI class tokens should not be authored directly in Kotlin docs or component examples. Mosaik source should expose components, structural sub-components, or type-safe modifiers instead. Utility classes that are not DaisyUI component tokens (for example Tailwind layout/spacing classes such as `w-full`, `gap-2`, `bg-base-100`) may still be passed through `classes`.

This is repo-wide, not limited to user-facing examples. Docs infrastructure should also use Mosaik wrappers once a DaisyUI token is identified.

## Why this exists

The docs should demonstrate Mosaik as the public API. If docs use `div("card-body")`, `span("loading loading-spinner")`, or `a(classes = "btn btn-ghost")`, they teach users to bypass the component layer and make it harder to notice missing abstractions.

## Current scan (2026-06-28)

Command used to verify cleanup completion:

```bash
rg '\b(classes\s*=\s*|[a-zA-Z0-9_]+\()"[^\"]*(btn|badge|alert|card-body|card-title|card-actions|loading loading|navbar-|footer-title|link link|input input|form-control|label-text|select select|table table|tabs|tab-content|menu|menu-title|menu-active)[^\"]*"' mosaik-docs/src/main/kotlin -n
```

**Result**: No raw DaisyUI class token violations found in docs source. The only matches are false positives: route labels like `installSection("badge")` and navigation items, which are not class-token usage.

All component implementations (`mosaik-components/src/main/kotlin`) correctly use DaisyUI tokens only within their own component function definitions (e.g., `"card-body"` appears only inside `mCardBody` implementation). Test files contain expected mentions in test descriptions and a single allowlisted test case (`NavbarTest.kt:122`) demonstrating raw HTML fallback.

## Completed cleanup work

All raw DaisyUI class tokens have been eliminated from docs and source code through the following completed issues:

### Issue #76: Alert dismiss buttons
- **Completed**: Raw `button(classes = "btn btn-sm btn-ghost")` in `AlertPage.kt` replaced with `mButton` API.
- **Component**: `mButton` with `size` and `style` parameters.

### Issue #77: Anchor-compatible button API
- **Completed**: Raw `a(classes = "btn btn-ghost ...")` in navbar examples replaced.
- **Component**: New `mButtonLink` function renders `<a>` with button styling while preserving anchor semantics.
- **Usage**: `mButtonLink(href = "/", style = ButtonStyle.Ghost)`.

### Issue #78: Footer title and link abstractions
- **Completed**: Raw `h6("footer-title")` and `a(..., classes = "link link-hover")` eliminated.
- **Components**: `mFooterTitle` and `mLink` functions.
- **Usage**: Type-safe footer section headers and hover-styled links.

### Issue #79: Form/input/select abstractions
- **Completed**: All form-related raw tokens eliminated from docs.
- **Components**: `mFormControl`, `mLabel`, `mLabelText`, `mInput`, `mSelect`.
- **Features**: Type-safe `size` parameter, `bordered` boolean, full HTML attribute delegation.

### Issue #80: Table abstraction
- **Completed**: Raw `table("table table-zebra")` eliminated from all docs pages.
- **Component**: `mTable` with `zebra` boolean parameter.
- **Usage**: Preserves standard HTML table structure (`thead`, `tbody`, `tr`, `th`, `td`).

### Issue #81: Tabs abstraction
- **Completed**: Raw `tabs`, `tabs-lifted`, `tab`, `tab-content` eliminated.
- **Component**: `mTabs` with `TabsStyle` enum and `mTab` child function.
- **Features**: Radio-based CSS-only tab switching, scoped DSL context.

### Issue #82: Menu abstraction
- **Completed**: Raw `menu`, `menu-title`, `menu-active` eliminated from docs sidebar.
- **Component**: `mMenu` with `mMenuItem` and `mMenuTitle` child functions.
- **Features**: Scoped DSL context, `active` state for current page indication.

## Component abstraction inventory

All DaisyUI class token families now have complete abstractions:

| Token family | Component abstraction | Status |
| --- | --- | --- |
| `card`, `card-body`, `card-title`, `card-actions` | `mCard`, `mCardBody`, `mCardTitle`, `mCardActions` | ✅ Complete |
| `loading`, `loading-*` | `mLoading` with `Variant` enum | ✅ Complete |
| `btn`, `btn-*` | `mButton` (for `<button>`), `mButtonLink` (for `<a>`) | ✅ Complete |
| `alert`, `alert-*` | `mAlert` with `AlertVariant` enum | ✅ Complete |
| `badge`, `badge-*` | `mBadge` with `BadgeVariant` and `Size` | ✅ Complete |
| `navbar`, `navbar-start`, `navbar-center`, `navbar-end` | `mNavbar`, `mNavbarStart`, `mNavbarCenter`, `mNavbarEnd` | ✅ Complete |
| `footer`, `footer-title` | `mFooter`, `mFooterTitle` | ✅ Complete |
| `link`, `link-hover` | `mLink` | ✅ Complete |
| `input`, `input-bordered`, `input-*` | `mInput` with `bordered`, `size` | ✅ Complete |
| `select`, `select-bordered`, `select-*` | `mSelect` with `bordered`, `size` | ✅ Complete |
| `form-control`, `label`, `label-text` | `mFormControl`, `mLabel`, `mLabelText` | ✅ Complete |
| `table`, `table-zebra` | `mTable` with `zebra` boolean | ✅ Complete |
| `tabs`, `tabs-*`, `tab`, `tab-content` | `mTabs` with `TabsStyle`, `mTab` | ✅ Complete |
| `menu`, `menu-title`, `menu-active` | `mMenu`, `mMenuItem`, `mMenuTitle` | ✅ Complete |

## Allowlisted usage

The following raw DaisyUI token usages are intentional and acceptable:

### Component implementations
All component source files in `mosaik-components/src/main/kotlin/mosaik/ui/components/` use DaisyUI tokens within their own function implementations. This is the expected pattern — tokens are encapsulated behind the component API.

Examples:
- `Card.kt`: Uses `"card-body"` inside `mCardBody` implementation
- `Button.kt`: Uses `"btn"` and `"btn-*"` inside `mButton` and `mButtonLink` implementations
- `Form.kt`: Uses `"form-control"`, `"label-text"`, `"input"`, `"select"` inside respective component implementations
- `Footer.kt`: Uses `"footer-title"` inside `mFooterTitle` implementation
- `Tabs.kt`: Uses `"tabs"`, `"tab-content"` inside `mTabs`/`mTab` implementations
- `Menu.kt`: Uses `"menu"`, `"menu-title"` inside `mMenu`/`mMenuItem`/`mMenuTitle` implementations

### Test files
Component tests verify that the correct DaisyUI classes are rendered. Test descriptions and assertions naturally mention these tokens:
- Test descriptions: `test("mCardBody renders the card-body class inside the card")`
- Assertion strings: `html shouldContain "class=\"navbar-start\""`
- Test case demonstrating HTML fallback: `NavbarTest.kt:122` uses `a(classes = "btn")` to verify raw HTML remains callable in navbar context

These test usages validate the component implementations and are not user-facing code.

## Next steps

The cleanup phase is complete. Future work:

1. **CI enforcement**: Add a static check that fails on raw DaisyUI component tokens outside of:
   - Component implementation files (`mosaik-components/src/main/kotlin/mosaik/ui/components/*.kt`)
   - Test files (`mosaik-components/src/test/kotlin/**/*Test.kt`)
   - This inventory document

2. **Documentation**: Ensure all component reference pages demonstrate the type-safe APIs rather than raw class tokens.

3. **Monitoring**: Watch for new DaisyUI token families added in future DaisyUI releases and create corresponding Mosaik abstractions as needed.
