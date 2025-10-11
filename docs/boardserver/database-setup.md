# 데이터베이스 설정 가이드

`boardserver`는 프로필에 따라 H2(로컬 개발)와 MySQL(Docker 등 배포 환경)을 사용합니다. 아래 절차를 따르면 환경 전환이 수월합니다.

## 1. 로컬 개발 (기본 `local` 프로필)
- `application.yml`의 `spring.profiles.default`가 `local`로 지정되어 있어 별도 설정 없이 H2 인메모리 DB가 활성화됩니다.
- H2 연결 정보 (`application-local.yml`)
  - JDBC URL: `jdbc:h2:mem:board;MODE=MYSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
  - 사용자/비밀번호: `sa` / 빈 문자열
  - 드라이버: `org.h2.Driver`
- H2 콘솔은 `/h2-console`(기본 포트 8080)에서 접근 가능하며, 실행 중인 애플리케이션에 연결됩니다.
- MySQL 호환 모드를 사용하므로 매퍼 SQL을 그대로 재사용할 수 있습니다.

## 2. Docker 등 외부 DB 사용 (`docker` 프로필)
1. MySQL 인스턴스를 기동하고 데이터베이스를 생성합니다 (예: `board`).
2. 필요한 경우 사용자/비밀번호를 생성하고 권한을 부여합니다.
3. 애플리케이션 실행 시 프로필과 접속 정보를 환경 변수로 지정합니다.

```bash
SPRING_PROFILES_ACTIVE=docker \
SPRING_DATASOURCE_JDBC_URL=jdbc:mysql://<HOST>:3306/board?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&serverTimezone=UTC \
SPRING_DATASOURCE_USERNAME=<USER> \
SPRING_DATASOURCE_PASSWORD=<PASSWORD> \
./gradlew bootRun
```

- `application-docker.yml`은 위 환경 변수를 읽어 MySQL 드라이버(`com.mysql.cj.jdbc.Driver`)를 사용합니다.
- SQL 초기화는 기본적으로 비활성화(`spring.sql.init.mode=never`)되어 있으므로, 스키마/데이터는 별도의 마이그레이션 도구나 SQL 스크립트로 관리해야 합니다.

## 3. 공통 주의 사항
- 비밀번호와 JDBC URL은 Git에 커밋하지 말고 환경 변수나 비추적 설정 파일을 사용하세요.
- 프로필 전환 시 캐시된 세션/커넥션이 남지 않도록 애플리케이션을 완전히 재기동합니다.
- 테스트 실행(`./gradlew test`)은 H2 인메모리 DB를 사용하므로 추가 설정이 필요하지 않습니다.

이 가이드를 `docs/agent/PROJECT_STATUS.md`의 "DB 설정 문서화" 항목에 대응하도록 유지보수하며, 환경 변경 시 본 문서를 업데이트해 주세요.
