#!/bin/bash
# Check for raw DaisyUI class tokens in docs source files
# Per ADR-0013, docs should use Mosaik component abstractions, not raw DaisyUI tokens

set -e

echo "Scanning for raw DaisyUI class tokens in docs..."

# Pattern matches common DaisyUI component tokens
# Uses grep -E for extended regex, -n for line numbers, -r for recursive
PATTERN='(classes\s*=\s*|[a-zA-Z0-9_]+\()"[^"]*(btn|badge|alert|card-body|card-title|card-actions|loading loading|navbar-|footer-title|link link|input input|form-control|label-text|select select|table table|tabs|tab-content|menu|menu-title|menu-active)[^"]*"'

# Scan docs source
RESULTS=$(grep -rEn "$PATTERN" mosaik-docs/src/main/kotlin || true)

# Filter out false positives: route labels and navigation items
FILTERED=$(echo "$RESULTS" | grep -v 'installSection(' | grep -v 'NavItem(' | grep -v '/components/' || true)

if [ -n "$FILTERED" ]; then
    echo "❌ FAILED: Found raw DaisyUI class tokens in docs source:"
    echo "$FILTERED"
    echo ""
    echo "Please use Mosaik component abstractions instead of raw DaisyUI tokens."
    echo "See research/daisyui-class-token-inventory.md for component mappings."
    exit 1
else
    echo "✅ PASSED: No raw DaisyUI class token violations found in docs source."
    echo ""
    echo "Note: Component implementations and tests are allowed to use DaisyUI tokens."
    exit 0
fi
