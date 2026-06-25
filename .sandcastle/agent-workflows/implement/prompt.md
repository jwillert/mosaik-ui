# TASK

Implement issue #{{ISSUE_NUMBER}}: {{ISSUE_TITLE}}

You are on branch `{{BRANCH}}`, already created from `main`.

# ISSUE

{{ISSUE_CONTEXT}}

# CONTEXT

Read the project's domain and architecture docs before changing code:

- `CONTEXT.md`
- `docs/adr/` if relevant
- `.sandcastle/CODING_STANDARDS.md`

Explore the repo and relevant tests before editing.

# EXECUTION

Where a test seam already exists, or a new one is being proposed, do red-green-refactor:

1. RED: write a failing test
2. GREEN: implement the smallest correct change
3. REPEAT until the issue is done
4. REFACTOR

Do not improvise new test seams, such as extracting out a function so that it can be tested in isolation. This creates spaghetti tests.

Run `./gradlew --no-daemon build` before committing. Run focused Gradle tasks where relevant, such as `./gradlew --no-daemon :mosaik-core:test`, `./gradlew --no-daemon :mosaik-components:test`, `./gradlew --no-daemon :mosaik-gradle:test`, or `./gradlew --no-daemon :ktor-vrt:test`.

# COMMIT

Make one or more commits on `{{BRANCH}}` with conventional commit messages.

Do not push the branch.
Do not close the issue.
Do not edit labels.
Do not create or edit PRs.

When complete, output `<promise>COMPLETE</promise>`.
