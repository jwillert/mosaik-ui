---
name: mosaik-ui-consumer
description: Use Mosaik UI correctly inside downstream Kotlin/Ktor projects. Use when a project contains `.mosaik/components.json`, mentions Mosaik UI, uses source-installed Mosaik components, or needs UI built with kotlinx.html/Tailwind/DaisyUI through Mosaik components.
---

# Mosaik UI Consumer

Use this skill when working in a consuming project that uses Mosaik UI source-installed components.

## Core model

Mosaik UI is a source-installed component toolkit. Components are copied into the consuming project and then become normal project-owned source. Treat local component files as the source of truth, because they may have been customized after install.

## Startup workflow

1. Read `.mosaik/components.json` first if it exists.
2. Use the inventory to learn the configured package, prefix, installed components, file paths, dependencies, and lightweight API metadata.
3. Verify referenced files exist before relying on the inventory.
4. Inspect relevant component source before non-trivial usage or customization.
5. Run Gradle/status tasks only when needed: missing inventory, stale/missing files, compile failure, or adding/regenerating components.

## Agent rules

- Prefer installed Mosaik component APIs over raw DaisyUI component class tokens.
- Tailwind utility classes are acceptable pass-through styling.
- Avoid directly authoring DaisyUI component/modifier tokens such as `btn`, `card-body`, `loading-spinner`, `footer-title`, `input-bordered`, `table-zebra`, or `menu-active`.
- Use type-safe parameters, enums, and structural sub-components when available.
- Respect constrained DSL context types for child components.
- Do not assume upstream APIs blindly; consuming projects own their installed source.
- Commit `.mosaik/components.json` alongside installed component source files.

## Using existing components

When writing UI:

1. Check `.mosaik/components.json` for the component and API metadata.
2. Open the listed local file if the task requires exact parameters, slots, child functions, or customization.
3. Import/use the local package and configured prefix from the inventory.
4. Prefer local component functions such as `mButton`, `mCard`, etc. using the actual prefix from the inventory.

## Composition guidance

### Navbar with mobile drawer

For responsive site navigation, compose `navbar`, `drawer`, and usually `menu` rather than writing raw DaisyUI class tokens:

1. Install/check all required components: `navbar`, `drawer`, and `menu` (plus `button` if actions are needed).
2. Wrap the page in `mDrawer(toggleId = "...")`.
3. Put the top bar and page body inside `mDrawerContent`.
4. In the navbar, use `mNavbarStart`, `mNavbarCenter`, and `mNavbarEnd`; hide desktop navigation with responsive Tailwind utilities where appropriate.
5. Use an ordinary `label` with `htmlFor` set to the drawer `toggleId` as the mobile trigger.
6. Put mobile links inside `mDrawerSide` with `mMenu`/`mMenuItem`.

## Installing components

If a needed component is not installed:

1. Check available components with `./gradlew mosaikList` when available.
2. Install with `./gradlew mosaikAdd --component=<name>`.
3. Ensure `.mosaik/components.json` is updated.
4. If the inventory is missing or stale, run `./gradlew mosaikInventory` when available.
5. Commit both installed source files and `.mosaik/components.json`.

## Inventory expectations

The inventory is strict JSON at `.mosaik/components.json`. It should include:

- `schemaVersion`
- `generatedBy.tool` and `generatedBy.version`
- project package, prefix, and source set
- `agentHints`
- installed components keyed by name
- file paths and checksums
- dependencies
- lightweight inline API metadata: functions and types

Use the inventory for orientation, but inspect local source if checksums drift, files are missing, compile fails, or behavior depends on implementation details.
