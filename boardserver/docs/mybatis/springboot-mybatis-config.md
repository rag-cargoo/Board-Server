# Spring Boot MyBatis Config

## 개요
- Spring Boot 3.5.x에서 `org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3` 의존성 하나로 MyBatis 자동 설정을 사용합니다.
- 추가 `@Configuration`이나 `@MapperScan` 클래스는 필요 없으며, 매퍼 인터페이스에 `@Mapper`만 선언하면 됩니다.

## 설정 구조
- 공통 설정(`src/main/resources/application.yml`):
  - 앱 이름(`boardserver`)과 기본 프로파일(`local`)
  - MyBatis 옵션 `map-underscore-to-camel-case=true`
- 로컬(H2) 프로파일(`application-local.yml`):
  - `jdbc-url=jdbc:h2:mem:board;MODE=MYSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
  - `driver-class-name=org.h2.Driver`, `username=sa`, 비밀번호 없음
  - H2 콘솔 `/h2-console` 허용
- 도커(MySQL) 프로파일(`application-docker.yml`):
  - 기본 JDBC URL `jdbc:mysql://localhost:3306/board`
  - 기본 계정 `root` / `1111`
  - `SPRING_DATASOURCE_JDBC_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` 환경변수로 덮어쓰기 가능

## 매퍼 작성 가이드
1. `src/main/java/...`에 매퍼 인터페이스를 만들고 `@Mapper`를 붙입니다.
2. XML 매퍼가 필요하면 `src/main/resources/mapper/`에 `<이름>Mapper.xml`을 추가하면 기본 경로에서 자동 인식합니다. 어노테이션 기반(`@Select`, `@Insert`)만 사용할 예정이라면 XML과 폴더를 생략해도 됩니다.
3. SQL과 컬럼은 스네이크 케이스로 작성해도 카멜 케이스 DTO/VO에 매핑됩니다(공통 설정 덕분).

## 실행 팁
- 로컬 H2: `./gradlew bootRun`
- 도커 MySQL: 컨테이너를 예시처럼 띄운 뒤 `SPRING_PROFILES_ACTIVE=docker ./gradlew bootRun`
  - `docker run --name board-mysql -e MYSQL_ROOT_PASSWORD=1111 -e MYSQL_DATABASE=board -p 3306:3306 mysql:8`

## 참고
- 현재 저장소에는 매퍼 인터페이스/SQL XML이 없습니다. 실제 구현 시 DTO 또는 도메인 객체와 매퍼 결과가 일치하도록 주석을 남겨 주세요.
