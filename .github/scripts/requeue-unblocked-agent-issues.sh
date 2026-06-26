#!/usr/bin/env bash
set -euo pipefail

: "${GH_REPO:?GH_REPO is required}"
: "${PR_NUMBER:?PR_NUMBER is required}"

pr_body=$(gh pr view "$PR_NUMBER" --json body -q '.body // ""')
closed_issues=$(
  printf '%s\n' "$pr_body" |
    grep -Eio '(close[sd]?|fix(e[sd])?|resolve[sd]?)[^#\r\n]*(#[0-9]+[ ,;]*)+' |
    grep -Eo '#[0-9]+' |
    tr -d '#' |
    sort -nu || true
)

if [ -z "$closed_issues" ]; then
  echo "Merged PR #${PR_NUMBER} does not declare any closed issues; nothing to requeue."
  exit 0
fi

echo "Merged PR #${PR_NUMBER} closed issue(s):"
printf '  #%s\n' $closed_issues

candidates=$(gh issue list \
  --state open \
  --label ready-for-agent \
  --label agent:blocked \
  --limit 100 \
  --json number,title)

if [ "$(printf '%s' "$candidates" | jq 'length')" -eq 0 ]; then
  echo "No blocked ready-for-agent issues found."
  exit 0
fi

is_closed_issue() {
  local issue_number=$1
  local state
  state=$(gh issue view "$issue_number" --json state -q '.state' 2>/dev/null || true)
  [ "$state" = "CLOSED" ]
}

body_blockers_for() {
  local issue_number=$1
  gh issue view "$issue_number" --json body -q '.body // ""' |
    awk '
      BEGIN { in_blocked_by = 0 }
      /^##[[:space:]]+Blocked by[[:space:]]*$/ { in_blocked_by = 1; next }
      /^##[[:space:]]+/ { if (in_blocked_by) exit }
      in_blocked_by { print }
    ' |
    grep -Eoi '#[0-9]+' |
    tr -d '#' || true
}

native_blockers_for() {
  local issue_number=$1
  gh issue view "$issue_number" --json blockedBy -q '.blockedBy.nodes[].number' 2>/dev/null || true
}

contains_closed_trigger() {
  local blocker
  while IFS= read -r blocker; do
    [ -n "$blocker" ] || continue
    if printf '%s\n' "$closed_issues" | grep -qx "$blocker"; then
      return 0
    fi
  done
  return 1
}

while IFS= read -r candidate; do
  issue_number=$(printf '%s' "$candidate" | jq -r '.number')
  issue_title=$(printf '%s' "$candidate" | jq -r '.title')

  blockers=$(
    {
      native_blockers_for "$issue_number"
      body_blockers_for "$issue_number"
    } | grep -E '^[0-9]+$' | sort -nu || true
  )

  if [ -z "$blockers" ]; then
    echo "Skipping #${issue_number}: agent:blocked is not tied to issue blockers."
    continue
  fi

  if ! contains_closed_trigger <<< "$blockers"; then
    echo "Skipping #${issue_number}: it was not blocked by an issue closed by PR #${PR_NUMBER}."
    continue
  fi

  open_blockers=""
  while IFS= read -r blocker; do
    [ -n "$blocker" ] || continue
    if ! is_closed_issue "$blocker"; then
      line=$(gh issue view "$blocker" --json number,title,state,url \
        -q '"- #\(.number) \(.title) (\(.state)): \(.url)"' 2>/dev/null || true)
      open_blockers="${open_blockers}${line}"$'\n'
    fi
  done <<< "$blockers"

  if [ -n "$open_blockers" ]; then
    echo "Skipping #${issue_number}: still blocked by:"
    printf '%s' "$open_blockers"
    continue
  fi

  echo "Requeueing #${issue_number}: ${issue_title}"
  gh issue edit "$issue_number" --remove-label "agent:blocked" || true
  gh issue edit "$issue_number" --add-label "agent:implement"

  body_file=$(mktemp)
  {
    echo "All tracked prerequisite issues are now closed, including one closed by PR #${PR_NUMBER}."
    echo
    echo "Re-added \`agent:implement\` automatically so the agent can try this issue again."
  } > "$body_file"
  gh issue comment "$issue_number" --body-file "$body_file" || true
  rm -f "$body_file"
done < <(printf '%s' "$candidates" | jq -c '.[]')
