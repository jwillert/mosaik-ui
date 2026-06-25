# Sandcastle GitHub Actions

This repo can run Sandcastle remotely via GitHub Actions.

## Required secrets

Add these repository secrets before using the workflows:

- `CLAUDE_CODE_OAUTH_TOKEN` — required for Claude Code.
- `AGENT_PAT` — recommended. Used when the implementation workflow labels the new PR with `agent:review`, so the review workflow is triggered reliably. The workflow falls back to `GITHUB_TOKEN` if this is absent.

## Required labels

Create these labels in GitHub:

- `agent:implement` — add to an issue to start implementation.
- `agent:review` — added to an internal PR to start automated review.
- `agent:in-progress` — workflow is currently running.
- `agent:blocked` — workflow failed or refused to run.

## Flow

1. Add `agent:implement` to a scoped issue.
2. GitHub Actions checks out `main`, creates `agent/issue-N-slug`, installs Java 21 and Node 22 dependencies, and runs `./gradlew --no-daemon build` before spending agent time.
3. The implementation agent runs with `claude-sonnet-4-5`, commits changes, pushes the branch, and opens a draft PR.
4. The implementation workflow labels the PR `agent:review`.
5. The review workflow only runs for PR branches in this repository, not external forks. It may commit refinements, posts a review, and marks the PR ready.
6. Humans still merge PRs manually. There is no auto-merge step.
