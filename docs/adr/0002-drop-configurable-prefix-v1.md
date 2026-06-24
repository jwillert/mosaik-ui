# ADR 0002: Drop Configurable Prefix for v1

## Status

Accepted

## Context

The original design allowed users to configure the function prefix via `mosaikUi { prefix = "d" }`, changing `mButton` to `dButton`. The install task would string-replace the prefix in component source files.

The prefix `m` is a dangerous search token — it matches everywhere. Safe replacement requires either a marker-based approach (unnatural source code) or regex-based convention matching (fragile).

## Decision

Fix the prefix to `m` in v1. Do not support prefix configuration.

## Consequences

- Simpler install logic — only package name replacement, no prefix replacement
- Component source code reads naturally and compiles as-is
- Users can still rename functions manually after install (copy-paste philosophy)
- Revisit with a marker-based approach if demand appears
