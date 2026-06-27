# PRD: Repo-wide DaisyUI Token Cleanup

## Problem

`research/daisyui-class-token-inventory.md` identifies raw DaisyUI class tokens in docs pages and shared docs helpers. These usages conflict with ADR 0013 and teach users to bypass the Mosaik component API.

## Goal

Replace all raw DaisyUI class-token usages in Mosaik-authored docs and docs infrastructure with Mosaik components, sub-components, or type-safe modifiers.

## Cleanup targets

1. `AlertPage.kt`
   - Replace raw close-button `btn btn-sm btn-ghost` usage with the Button API.

2. `NavbarPage.kt`
   - Replace anchor-as-button class strings after the link/button semantics API exists.

3. `FooterPage.kt`
   - Replace `footer-title` and `link link-hover` after footer/link abstractions exist.

4. `InteractivityPage.kt`
   - Replace form/input/label/select class strings after form abstractions exist.

5. `DocumentationBlocks.kt`
   - Replace table and tab class strings after table/tabs abstractions exist.

6. `Layout.kt`
   - Replace menu/select class strings after menu/select abstractions exist.

## Non-goals

- Do not remove explanatory prose that names DaisyUI concepts in API/reference text.
- Do not remove Tailwind utility classes such as `w-full`, `gap-2`, `bg-base-100`, `rounded-box`, or `text-sm` unless they become part of a type-safe modifier.
- Do not introduce behavior changes to interactivity examples.

## Acceptance criteria

- The inventory document has no unresolved source-code violations for docs/source.
- A raw-token scan finds no forbidden class-token authoring outside allowlisted component implementations/tests.
- Docs compile successfully.
- Visual output remains equivalent enough for existing VRT expectations or updated goldens with intentional changes.
