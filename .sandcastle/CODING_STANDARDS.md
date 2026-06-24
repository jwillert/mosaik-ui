# Mosaik UI Coding Standards

## Language & Build

- Kotlin 2.1.x targeting JVM 21
- Gradle 8.14 with Kotlin DSL
- Convention plugins in `build-logic/` — don't duplicate build config across modules

## Component API

- Scope-based DSL: each component has a Scope class (e.g. `ButtonScope`) implementing `MosaikScope`
- Top-level components are extension functions on `FlowContent` (e.g. `fun FlowContent.mButton(...)`)
- HTML properties are delegated to the underlying kotlinx.html element, not wrapped
- Function prefix is `m` (fixed, not configurable)
- Placeholder package is `mosaik.ui.components`

## Style

- Follow Kotlin coding conventions (kotlinlang.org/docs/coding-conventions.html)
- Prefer immutable data (`val`, data classes, enums)
- Use `when` expressions over if-else chains for enum/sealed class dispatch
- No wildcard imports
- No comments unless the WHY is non-obvious

## Testing

- Kotest FunSpec with JUnit 5 platform runner
- Test external behavior, not implementation details
- Component tests assert HTML output contains correct CSS classes and structure
- Use `shouldContain`, `shouldBe` matchers from Kotest

## Architecture

- `mosaik-core` has no Gradle API dependency — pure Kotlin only
- `mosaik-gradle` delegates to `mosaik-core` for all non-Gradle logic
- Components in `mosaik-components` compile under the placeholder package
- Convention plugins handle shared config (kotlin version, JVM target, test framework, repositories)
