# Mosaik UI Consumer Agent Integration

Mosaik UI ships a portable consumer skill for agentic coding projects. The skill teaches agents how to use Mosaik's source-installed components from downstream Kotlin/Ktor projects without repeatedly reverse-engineering the component tree.

## Canonical skill

Source in this repo:

```text
agent-skills/mosaik-ui-consumer/SKILL.md
```

Recommended global Pi install target:

```text
~/.pi/agent/skills/mosaik-ui-consumer/SKILL.md
```

A human or agent can copy the skill from this repo into the global skills directory. After installation, any project can reference the globally installed `mosaik-ui-consumer` skill.

## Consuming project setup

A consuming project should commit the Mosaik inventory file:

```text
.mosaik/components.json
```

Agents should read that file first. It records the configured package, prefix, installed components, file paths, dependencies, checksums, and lightweight API metadata.

## AGENTS.md snippet template

Add this to a consuming project's `AGENTS.md`:

```md
## Mosaik UI

This project uses Mosaik UI source-installed components.

Agent workflow:

1. Read `.mosaik/components.json` first.
2. Follow the globally installed `mosaik-ui-consumer` skill.
3. Prefer installed Mosaik component APIs over raw DaisyUI component class tokens.
4. Inspect local component source before non-trivial usage or customization.
5. When adding components, use `./gradlew mosaikAdd --component=<name>` and commit the installed source files plus `.mosaik/components.json`.
6. If the inventory is missing or stale, run `./gradlew mosaikInventory` when available.
```

## Agent adoption prompt

Use this prompt in a consuming project when asking an agent to configure Mosaik support:

```text
This project uses Mosaik UI. Please install or use the `mosaik-ui-consumer` skill from the Mosaik UI repo, add the Mosaik UI section to this project's AGENTS.md, and start UI work by reading `.mosaik/components.json`.
```

## Expected workflow

- Read `.mosaik/components.json` for fast orientation.
- Use installed component APIs and type-safe modifiers.
- Avoid raw DaisyUI component/modifier class tokens in project-authored UI.
- Use `./gradlew mosaikAdd --component=<name>` when a component is missing.
- Use `./gradlew mosaikInventory` to regenerate inventory when it is missing or stale.
