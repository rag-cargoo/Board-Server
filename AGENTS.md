# Codex Agent Guide

## Repository Layout
- `Board-Server-lab/`: Java/Spring board server. Imported via `git subtree` from the former `Board-Server-lab` repository so its full history lives in this repo.
- `Board-Server-Locust/`: Locust load test project. Still tracked as a Git submodule; manage its commits inside the submodule.
- No other nested Git repositories should be introduced.

## Branching Strategy
- Lab practice branches are named `issue/<n>` (e.g., `issue/6`). Each branch corresponds to a stage of the board server exercises and affects the entire working tree.
- Always switch branches from the repository root (`git checkout issue/6`). Do not attempt to change branches from inside `Board-Server-lab/` because it is no longer an independent repo.
- When updating the Locust project, check out the desired branch in the root, then edit files inside `Board-Server-Locust/` and commit from there (the submodule keeps its own history).

## Workflow Notes
- After editing the lab code, commit from the root repository so the subtree history stays linear.
- If the Locust submodule changes, push its repo first, then update and commit the submodule pointer here (`git add Board-Server-Locust`).
- Run `git submodule update --init --recursive` after cloning so the Locust code is available.

## Safety Checks
- Before pushing, run relevant tests/builds for the branch you are on (e.g., Gradle tasks inside `Board-Server-lab/`).
- Verify `git status` is clean in both the root and the submodule before finishing.
