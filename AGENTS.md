# Codex Agent Guide

## Repository Layout
- `Board-Server-lab/`: Java/Spring board server code imported via `git subtree`; full lab history lives inside this repo.
- `Board-Server-Locust/`: Locust load-testing project also imported via `git subtree`; develops alongside the lab code under the same root repository.
- `docs/`: 프로젝트 설명 문서를 모아 두는 상위 디렉터리입니다. 하위에 주제별 폴더를 만들어 문서를 추가하고, 코드 변경과 동일한 커밋에서 함께 갱신하세요.
- 추가 실습이나 도구가 생기면 새로운 디렉터리를 루트에 생성하고 동일한 방식으로 커밋하세요. 중첩 Git 저장소는 만들지 않습니다.

## Branching Strategy
- Lab practice branches follow the `issue/<n>` naming (예: `issue/6`). 체크아웃은 항상 루트에서 수행해 전체 워킹 트리를 해당 단계로 전환하세요.
- 로커스트 및 기타 부가 프로젝트는 `locust/<feature>`, `docs/<topic>`처럼 접두사를 붙여 브랜치를 구분하면 히스토리 파악이 쉽습니다.

## Documentation Workflow
- 단계별 실습 설명은 `docs/projects/lab-practice-guide.md`에서 관리합니다. 새 브랜치를 만들거나 실습 내용을 수정했다면 같은 커밋/PR에서 이 문서를 갱신하세요.
- 새로운 프로젝트 설명 문서를 추가할 경우 `docs/<category>/<name>.md` 형태를 사용하고, 관련 코드를 수정한 커밋에 링크를 남기세요.
- 문서 자산(이미지 등)은 필요 시 `docs/assets/` 이하에 두고, 각 문서에서 상대 경로로 참조합니다.

## General Workflow Notes
- 모든 디렉터리가 루트 레포 일부이므로 수정 후에는 루트에서 커밋/푸시하세요.
- 다른 브랜치로 이동하기 전에 `git status`가 깨끗한지 확인하고, 필요하면 관련 테스트(Gradle 빌드, Locust 시나리오 등)를 실행하세요.
- 히스토리를 깔끔하게 유지하려면 브랜치별로 필요한 변경만 커밋하고, 완료 후 `git push origin <branch>`로 공유합니다.

## Safety Checks
- 주요 브랜치를 업데이트하거나 배포 전에 Gradle 빌드(`./gradlew build`)와 필요한 Locust 검증을 실행하세요.
- 커밋 전에는 루트에서 `git status`를 확인해 불필요한 산출물이나 임시 파일이 포함되지 않았는지 점검하세요.
- 새 디렉터리/실습을 추가했다면 관련 설명을 `docs/` 아래 문서로 남기고, 이 가이드에도 링크를 업데이트하세요.
