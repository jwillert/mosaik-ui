# TASK

Fix issue #{{ISSUE_NUMBER}}: {{ISSUE_TITLE}}

Pull in the issue using `gh issue view`, with comments. If it has a parent PRD, pull that in too.

Only work on the issue specified.

Work on branch {{BRANCH}}. Make commits, run tests, and close the issue when done.

# CONTEXT

Here are the last 10 commits:

<recent-commits>

!`git log -n 10 --format="%H%n%ad%n%B---" --date=short`

</recent-commits>

Read `CONTEXT.md` for the domain glossary and `docs/adr/` for architectural decisions before starting.

# EXPLORATION

Explore the repo and fill your context window with relevant information that will allow you to complete the task.

Pay extra attention to:
- The `research/` directory for design decisions
- Existing component files for API patterns
- Test files that touch the relevant parts of the code
- The `build-logic/` convention plugins for build configuration patterns

# EXECUTION

If applicable, use RGR to complete the task.

1. RED: write one test
2. GREEN: write the implementation to pass that test
3. REPEAT until done
4. REFACTOR the code

# FEEDBACK LOOPS

Before committing, run `./gradlew --no-daemon build` to ensure everything compiles and tests pass.

IMPORTANT: Always use `--no-daemon` with Gradle in this environment. The daemon mode causes stdout buffering issues that make builds appear to hang.

For module-specific checks:
- `./gradlew --no-daemon :mosaik-core:test` — core logic unit tests
- `./gradlew --no-daemon :mosaik-components:test` — component HTML output tests
- `./gradlew --no-daemon :mosaik-gradle:test` — Gradle plugin integration tests
- `./gradlew --no-daemon :ktor-vrt:test` — VRT library tests

# COMMIT

Make a git commit. The commit message must:

1. Start with `SANDCASTLE:` prefix
2. Include task completed + PRD reference
3. Key decisions made
4. Files changed
5. Blockers or notes for next iteration

Keep it concise.

# THE ISSUE

If the task is not complete, leave a comment on the GitHub issue with what was done.

Do not close the issue - this will be done later.

Once complete, output <promise>COMPLETE</promise>.

# FINAL RULES

ONLY WORK ON A SINGLE TASK.
