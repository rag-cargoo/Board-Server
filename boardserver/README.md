# Board-Server

Spring Boot 3 기반 게시판 서비스입니다. 컨트롤러·서비스·매퍼를 MyBatis로 구성하고, 세션을 이용한 간단한 인증 흐름을 제공합니다.

## 주요 명령
- `./gradlew clean build` : 전체 빌드 및 패키징
- `./gradlew bootRun` : 애플리케이션 실행 (기본 프로필은 `local`)
- `./gradlew test` : JUnit 5 테스트 실행

모든 Gradle 명령은 `boardserver/` 디렉터리에서 실행합니다.

## 실행 프로필
- **local (기본)** : H2 인메모리 DB 사용. 추가 설정 없이 개발 및 테스트 가능.
- **docker** : MySQL 연결. `SPRING_PROFILES_ACTIVE=docker`와 JDBC 정보를 환경 변수로 지정한 뒤 `./gradlew bootRun`을 수행합니다. 자세한 내용은 `docs/boardserver/database-setup.md`를 참고하세요.

## 코드 구조
- `com.example.boardserver.controller` : HTTP 엔드포인트, 세션 기반 인증 처리
- `com.example.boardserver.service` : 비즈니스 로직 인터페이스/구현
- `com.example.boardserver.mapper` : MyBatis 매퍼 인터페이스
- `com.example.boardserver.dto` : 요청/응답, 도메인 전달 객체
- `docs/http` : REST Client(HTTPie/IntelliJ 등)로 호출할 수 있는 사용자·카테고리 시나리오 스크립트
- `src/main/resources/mapper` : MyBatis SQL 매퍼 XML
- `src/test/java` : 서비스/컨트롤러 테스트 (JUnit 5, Mockito, MockMvc)

세부 레이어 설명은 `docs/boardserver/layers.md`에서 확인할 수 있습니다.

## 주요 기능 요약
- **사용자 관리** : 회원가입, 로그인/로그아웃, 비밀번호 변경, 계정 삭제 (샘플 스크립트 `docs/http/user-api.http`)
- **카테고리 관리** : 관리자 전용 등록·수정·삭제 API 제공. `CategoryController`는 `@LoginCheck(Admin)`으로 보호되며 H2 부트 시 `categories` 테이블이 자동 생성됩니다. 호출 예제는 `docs/http/category-api.http`를 참고하세요.

## 테스트 상태
현재 사용자 서비스 단위 테스트와 `UserController` 슬라이스 테스트가 추가되어 있습니다. 신규 기능을 개발하면 테스트를 보강하고 `./gradlew test`를 통해 회귀를 확인하세요.
