# Repository Guidelines (저장소 가이드라인)

## 프로젝트 구조 & 모듈 구성
이 저장소의 주요 서비스는 `boardserver/`에 있는 Spring Boot 애플리케이션입니다. Java 소스는 `boardserver/src/main/java/com/example/boardserver` 아래에서 레이어(컨트롤러, 서비스, 매퍼, DTO, 예외, 유틸)별로 정리합니다. SQL 매퍼는 `boardserver/src/main/resources/mapper`, 화면 템플릿과 정적 자산은 `.../templates`, `.../static`에 둡니다. 환경 프로필(`application.yml`, `application-local.yml`, `application-docker.yml`)은 `src/main/resources`에, 테스트 코드는 `boardserver/src/test/java`에 배치합니다. 과거 산출물은 `boot-ggangtong/`, 참고 자료는 `lecture-notes/`에 보관하며 필요 시에만 수정합니다.

## 빌드 · 테스트 · 개발 명령
모든 Gradle 명령은 `boardserver/` 디렉터리 안에서 실행합니다. `./gradlew clean build`는 전체 컴파일과 패키징을 수행하고, `./gradlew bootRun`은 로컬 서버를 기동하며, `./gradlew test`는 JUnit 테스트를 실행합니다. 빠른 확인이 필요하면 `./gradlew build -x test`로 테스트를 생략할 수 있지만, 공유하거나 푸시하기 전에 반드시 `./gradlew test`를 다시 수행합니다.

## 코딩 스타일 & 네이밍 규칙
Java 17과 Spring Boot 3.5를 표준으로 사용합니다. 들여쓰기는 스페이스 4칸, 최대 줄 길이는 120자를 권장합니다. 클래스는 PascalCase(`UserController`, `UserServiceImpl`), 인터페이스는 명사형 이름, DTO는 `*DTO` 접미사를 붙입니다. 매퍼 XML 파일명은 Java 매퍼와 동일하게 유지하며 패키지 구조는 `com.example.boardserver.<layer>`를 따릅니다. 반복 코드에는 Lombok을 활용하고 IDE 자동 정렬을 사용해 불필요한 변경을 줄입니다.

## 테스트 가이드
`useJUnitPlatform()` 덕분에 JUnit 5가 이미 설정되어 있습니다. 테스트 코드는 메인 패키지 구조를 반영해 `src/test/java`에 배치하고 파일명은 `*Test`로 끝나도록 합니다. 서비스 로직, 매퍼(H2 임베디드 프로필 활용), 컨트롤러 슬라이스 테스트를 우선 추가합니다. 커밋 전에 `./gradlew test`를 실행하고, 스킵한 케이스가 있다면 PR 설명에 기록합니다.

> **주의:** 테스트 코드 작성은 사용자 요청이 있을 때에만 수행합니다.

## 커밋 & PR 지침
커밋 메시지는 명령형 현재 시제로 작성합니다(예: `Add user registration flow`). 관련 변경은 한 커밋에 묶고, 이슈가 있다면 `Add profile mapper (#12)`처럼 번호를 참조합니다. PR에는 요약, 테스트 결과(`./gradlew test` 출력 근거), 설정 영향, UI 변경 시 스크린샷을 포함하고 최소 한 명 이상의 리뷰를 요청합니다.

## 보안 & 설정 유의사항
운영 비밀 값은 버전에 포함하지 말고 환경 변수나 추적되지 않는 `application-local.yml`을 사용하세요. Docker 환경 설정은 `application-docker.yml`에 유지하고 포트나 자격 증명 변경 시 `docs/`에 문서화합니다. 배포 전에는 디버그 로그를 제거하고 신규 엔드포인트에 입력 검증을 추가합니다.

## 워크플로 & 진행 기록
세션별 진행 상황은 `docs/agent/PROJECT_STATUS.md`에 남깁니다. 작업을 끝낼 때마다 **Recent Changes**에 날짜를 추가하고, **Next Steps** 체크박스를 갱신하며, 새로 생긴 결정이나 위험 요소를 적습니다. 마일스톤이 끝나면 내용을 `docs/changelog/`(없다면 생성)로 옮겨 아카이브하고 상태 파일은 최신 정보만 유지하세요.
