# PRD: DaisyUI Class Token Static Check

## Problem

ADR 0013 makes raw DaisyUI class tokens forbidden in Mosaik-authored source, but the rule is currently documented only. Without an executable check, new raw tokens can enter docs and examples unnoticed.

## Goal

Add a repo check that reports raw DaisyUI component/modifier tokens outside approved implementation and test locations, then wire it into the normal verification path once the inventory is clean.

## Non-goals

- Do not forbid ordinary Tailwind utility classes such as spacing, sizing, layout, or colors.
- Do not forbid DaisyUI tokens inside component implementation files that intentionally emit those classes.
- Do not parse Kotlin perfectly; a conservative text scanner is acceptable if false positives are understandable and documented.

## Requirements

- Scan Kotlin docs/source for raw class-token usage in likely class-bearing contexts:
  - `classes = "..."`
  - element shorthand calls such as `div("...")`, `span("...")`, `table("...")`, `ul("...")`, `li("...")`
  - code snippets embedded in docs strings.
- Detect known token families from `research/daisyui-class-token-inventory.md`:
  - `btn*`, `card*`, `loading*`, `alert*`, `badge*`, `navbar*`, `footer*`, `link*`, `input*`, `select*`, `form-control`, `label*`, `table*`, `tabs*`, `tab*`, `menu*`.
- Allowlist component implementation files under `mosaik-components/src/main/kotlin/mosaik/ui/components/`.
- Allowlist component tests that assert generated HTML.
- Produce actionable output with file, line, token, and suggested component family.
- Initially support report-only mode for cleanup work.
- Add a failing Gradle task after all known violations are resolved.

## Acceptance criteria

- `./gradlew checkDaisyUiTokens` prints a stable report.
- The checker fails on a newly introduced `div("card-body")` in docs.
- The checker does not fail on `Card.kt` emitting `card-body`.
- The checker does not fail on `div("flex gap-2")`.
- A follow-up commit wires the task into `check` only after existing violations are fixed.
