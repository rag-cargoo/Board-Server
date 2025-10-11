# boardserver 계층 구조 정리

`boardserver` 모듈은 전형적인 Spring MVC + MyBatis 구조를 따릅니다. 각 레이어는 아래 패키지에 배치되어 있으며, 역할을 분리해 변경과 테스트를 용이하게 합니다.

## Controller 레이어 (`com.example.boardserver.controller`)
- HTTP 요청/응답을 처리합니다. 세션을 통해 로그인 상태를 확인하고 `LoginResponse` DTO를 사용해 일관된 JSON 스키마를 반환합니다.
- 비즈니스 규칙은 서비스에 위임하며, 입력 검증 실패나 인증 오류에 대해서만 즉시 응답 코드를 결정합니다.
- 예시: `UserController`는 로그인·회원가입·비밀번호 변경과 같은 사용자 흐름 엔드포인트를 정의합니다.

## Service 레이어 (`com.example.boardserver.service`)
- 컨트롤러에서 받은 도메인 요청을 조합하고 트랜잭션 단위를 결정합니다.
- 인터페이스(`UserService`)와 구현(`service.impl`)을 분리해 테스트와 확장성을 확보합니다.
- 예외는 도메인 의미에 맞는 커스텀 예외(`InvalidPasswordException`, `DuplicatedIdException`)로 던져 컨트롤러에서 세밀하게 매핑할 수 있도록 합니다.

## Mapper 레이어 (`com.example.boardserver.mapper` + `src/main/resources/mapper`)
- MyBatis 매퍼 인터페이스와 XML을 통해 DB CRUD를 수행합니다. SQL은 XML에 정의하고, 매퍼 인터페이스는 서비스에서 주입받아 사용합니다.
- `UserProfileMapper`는 사용자 CRUD, 비밀번호 갱신, 중복 ID 검사 등을 제공합니다.

## DTO & 예외 (`com.example.boardserver.dto`, `com.example.boardserver.exception`)
- DTO는 요청/응답과 매퍼 반환값을 담는 값 객체입니다. 예: `UserDTO`, `UserLoginRequest`, `PasswordChangeRequest`, `LoginResponse`.
- 예외 패키지는 서비스에서 도메인별 오류를 표현하는 커스텀 런타임 예외를 둡니다.

## 유틸리티 (`com.example.boardserver.utils`)
- 공용 기능을 분리합니다. 현재는 `SHA256Util`이 비밀번호 해시화에 사용됩니다.

## 테스트 (`src/test/java/com/example/boardserver/...`)
- 서비스 단위 테스트는 매퍼를 목킹해 비즈니스 로직을 검증합니다.
- 컨트롤러 슬라이스 테스트는 `@WebMvcTest`로 HTTP 응답 코드와 JSON 스키마를 확인합니다.
- Gradle 명령: `./gradlew test`.

이 구조를 유지하면 새로운 엔드포인트를 추가할 때 컨트롤러 → 서비스 → 매퍼 → SQL 순으로 책임을 분리해 안전하게 확장할 수 있습니다.
