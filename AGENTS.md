# Codex Agent Guide

## Repository Layout
- `Board-Server-lab/`: Java/Spring board server code imported via `git subtree`; full lab history lives inside this repo.
- `Board-Server-Locust/`: Locust load-testing project also imported via `git subtree`; develops alongside the lab code under the same root repository.
- Avoid creating new nested Git repositories.

## Branching Strategy
- Lab practice branches follow the `issue/<n>` naming (예: `issue/6`). 체크아웃은 항상 루트에서 수행해 전체 워킹 트리를 그 단계로 전환하세요.
- 로커스트 관련 작업은 필요하다면 `locust/<feature>` 같은 접두사를 사용해 구분하면 됩니다. 어떤 브랜치이든 루트에서 생성·스위치하세요.

## Workflow Notes
- 두 디렉터리 모두 루트 레포의 일부이므로 수정 후에는 루트에서 커밋/푸시하면 됩니다.
- 다른 브랜치로 이동하기 전에 `git status`가 깨끗한지 확인하고, 필요하면 관련 테스트(Gradle 빌드, Locust 시나리오 등)를 실행하세요.
- 히스토리를 깔끔하게 유지하려면 브랜치별로 필요한 변경만 커밋하고, 완료 후 `git push origin <branch>`로 공유합니다.

## Safety Checks
- 주요 브랜치를 업데이트하거나 배포 전에 Gradle 빌드(`./gradlew build`)와 필요한 로커스트 검증을 실행하세요.
- 커밋 전에는 루트에서 `git status`를 확인해 불필요한 산출물이나 임시 파일이 포함되지 않았는지 점검하세요.
