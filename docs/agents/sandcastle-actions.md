# Sandcastle GitHub Actions

This repo can run Sandcastle remotely via GitHub Actions.

## Required secrets

Add these repository secrets before using the workflows:

- `CLAUDE_CODE_OAUTH_TOKEN` — required for Claude Code.
- `AGENT_PAT` — recommended. Used when workflows add labels that should trigger other workflows (`agent:review`, automatic unblock requeues). The workflows fall back to `GITHUB_TOKEN` if this is absent, but GitHub may not trigger follow-up workflows from labels added by `GITHUB_TOKEN`.

## Required labels

Create these labels in GitHub:

- `agent:implement` — add to an issue to start implementation.
- `agent:review` — added to an internal PR to start automated review.
- `agent:in-progress` — workflow is currently running.
- `agent:blocked` — workflow failed or refused to run.

## Flow

1. Add `agent:implement` to a scoped issue.
2. GitHub Actions refuses to run if the issue already has an open maintainer PR, or if any prerequisite issue is still open. Prerequisites are read from GitHub's native `blockedBy` relationship and from the issue body's `## Blocked by` fallback section.
3. GitHub Actions checks out `main`, creates `agent/issue-N-slug`, installs Java 21 and Node 22 dependencies, and runs `./gradlew --no-daemon build` before spending agent time.
4. The implementation agent runs with `claude-sonnet-4-5`, commits changes, pushes the branch, and opens a draft PR.
5. The implementation workflow labels the PR `agent:review`.
6. The review workflow only runs for PR branches in this repository, not external forks. It may commit refinements, posts a review, and marks the PR ready.
7. Humans still merge PRs manually. There is no auto-merge step.
8. When an agent PR is merged, the unblock workflow scans open `ready-for-agent` + `agent:blocked` issues that were blocked by the issue closed by that PR. If all tracked blockers are now closed, it removes `agent:blocked` and re-adds `agent:implement` automatically.
