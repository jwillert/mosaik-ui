# Issue #41 Status

## Completed Work

### 1. Removed Stale Scope References
- ✅ Updated `registry.json` to remove "MosaikScope contract" reference
- ✅ Updated `CODING_STANDARDS.md` to reflect parameter-based API (ADR 0003)
- ✅ Added forward reference to ADR 0009 for compound components

### 2. Created ADR 0009
- ✅ Documented DSL context types decision for compound components
- ✅ Established trade-off between raw receivers and semantic constraints
- ✅ Clarified that simple components continue using raw receivers

### 3. Verified Current State
- ✅ All simple components (Button, Badge, Alert, Footer) use raw receivers
- ✅ Full test suite passes (`./gradlew build`)
- ✅ Documentation compiles successfully
- ✅ All existing tests verify current behavior

## Blocked Work (Requires #39 and #40)

The following acceptance criteria CANNOT be completed until issues #39 and #40 implement
the DSL context types (`MCard`, `MCardBody`, `MNavbar`, etc.):

### 1. Update Component Source Comments
Card.kt and Navbar.kt currently document "extensions on the raw element" but will need
to document the DSL context types after #39/#40. The comments will need to:
- Explain what `MCard`, `MCardBody`, etc. context types are
- Clarify why compound components use context types vs simple components
- Remain understandable for users editing installed source

### 2. Verify Documentation Examples Compile
Current docs compile against raw `DIV` receivers. After #39/#40:
- Card examples need to verify they compile with `MCard.() -> Unit` receivers
- Navbar examples need to verify they compile with `MNavbar.() -> Unit` receivers
- API reference tables need to show context types instead of `DIV.()`

### 3. Update Tests for New API
Tests in `PagesTest.kt` currently check for `DIV.()` in API reference tables
(lines 124, 160). After #39/#40:
- Update to verify context type receivers are documented
- Add compile-time tests for invalid child placement (if not done in #39/#40)
- Verify error messages guide users to correct usage

## Next Steps

1. Wait for #39 (Card constraints) to be completed
2. Wait for #40 (Navbar constraints) to be completed
3. Then complete remaining verification work:
   - Update component source comments
   - Verify all doc examples compile with new DSL
   - Update test assertions for context types
   - Run full build and verify all acceptance criteria
