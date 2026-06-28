# Agent-readable installed component inventory

Mosaik will maintain an agent-readable installed component inventory at `.mosaik/components.json` in consuming projects. Because Mosaik components are source-installed and then become project-owned code, agents need a fast, reliable way to discover the local package, prefix, installed components, file paths, dependencies, and lightweight API metadata without repeatedly reverse-engineering the source tree; the inventory should be committed with the consuming project and updated by `mosaikAdd`, with a separate regeneration task for drift repair.
