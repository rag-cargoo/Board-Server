# Repository Guidelines

## Project Structure & Module Organization
- Application code lives under `src/main/java/com/example/boardserver` following a layered layout: `controller` for REST endpoints, `service` and `service/impl` for interfaces and implementations, and `dto` for transport objects.
- Configuration files belong in `src/main/resources`; `application.properties` seeds Spring Boot defaults.
- Tests mirror the main package in `src/test/java`, keeping class names and directory hierarchy aligned (e.g., `BoardserverApplicationTests` beside `BoardserverApplication`).

## Build, Test, and Development Commands
- `./gradlew bootRun` starts the application locally; use it for quick endpoint smoke checks.
- `./gradlew build` compiles the project, runs tests, and assembles the runnable jar in `build/libs/`.
- `./gradlew test` executes the JUnit suite without producing artifacts, ideal for fast validation before commits.

## Coding Style & Naming Conventions
- The project targets Java 17 with Lombok; enable annotation processing in your IDE to avoid false-positive errors.
- Use four-space indentation, `UpperCamelCase` for classes (`UserController`), and `lowerCamelCase` for fields and method names; keep DTOs, services, and controllers in their respective packages.
- Prefer Lombok-backed logging (`@Log4j2`) or Spring’s `LoggerFactory` and avoid `System.out` prints in production code.

## Testing Guidelines
- Tests rely on Spring Boot Test and JUnit 5; extend coverage beyond the current `contextLoads` by mirroring service flows in `*Tests` classes.
- Keep tests colocated with the code they exercise, use descriptive method names such as `register_rejectsDuplicateUserId()`, and run `./gradlew test` before every push.

## Commit & Pull Request Guidelines
- Follow the existing short, imperative subject style (`first commit`); keep subjects under 72 characters and add details in the body when context is needed.
- Each PR should summarize the change set, list automated/manual test results, and link related tickets or samples (screenshots, cURL commands) for API updates.

## Environment & Configuration
- Default properties live in `src/main/resources/application.properties`; never commit secrets—use environment variables or ignored `application-*.yml` overrides.
- After booting locally, confirm the app responds on `http://localhost:8080` and document any external service assumptions (e.g., Redis endpoints) in the PR description.
