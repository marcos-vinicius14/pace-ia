# Agent Guide for pace.ia

This document provides essential information for agentic coding assistants working on this repository.

## Build, Lint, and Test Commands

### Build Commands
```bash
./mvnw clean compile          # Compile the project
./mvnw clean package          # Build JAR package
./mvnw clean install         # Install to local Maven repo
./mvnw spring-boot:run        # Run the application
```

### Test Commands
```bash
./mvnw test                          # Run all tests
./mvnw test -Dtest=ClassName         # Run specific test class
./mvnw test -Dtest=ClassName#method  # Run specific test method
./mvnw verify                        # Run all tests including integration tests
```

### Code Quality
No explicit linting or formatting plugins configured. Follow Spring Boot conventions and Java best practices.

## Technology Stack

- **Framework**: Spring Boot 4.0.1 (WebFlux, Security, Data JPA)
- **Java Version**: 21
- **Build Tool**: Maven (using Maven Wrapper)
- **Database**: PostgreSQL (via Testcontainers in tests)
- **Testing**: JUnit 5, Spring Boot Test, Testcontainers
- **Architecture**: Reactive WebFlux + JPA for persistence

## Development Principles

### TDD (Test Driven Development)
All new features must follow the TDD workflow: **RED → GREEN → Refactor**.
1. **RED:** Write a failing test first.
2. **GREEN:** Write the minimum code to make the test pass.
3. **REFACTOR:** Improve the code while keeping tests green.

### Architecture & Design
- **DDD + SOLID:** Follow Domain-Driven Design and SOLID principles.
- **Composition over Inheritance:** Prefer composing objects over class inheritance.
- **Immutability:** Favor immutable objects; use `final` fields and avoid setters where possible.
- **Rich Domain Model:** Domain entities should contain behavior, not just data (avoid anemic domain models).
- **Early Return:** Use early returns to reduce nesting and improve readability.
- **State Machine for Enums:** Use state machine patterns for enums where it makes sense (e.g., `TrainingSessionStatus`).

### Design Patterns
- **Static Factory Methods vs Builder Pattern:**
  - Use **Static Factory Methods** for simple object creation with few parameters (≤ 4 mandatory fields)
  - Use **Static Factory Methods** when you can provide descriptive names (e.g., `createForNewAthlete()`)
  - Use **Builder Pattern** only when you have many mandatory fields (≥ 5) or a mix of required/optional parameters
  - Example: `TrainingPlan.create(id, athleteId, volume, date)` instead of `TrainingPlan.builder().id(id).build()`

- **Utility Classes:**
  - Enforce noninstantiability for utility classes containing only static members
  - Make class `final`, add a private constructor that throws `AssertionError`
  - Example:
    ```java
    public final class Identifiers {
        private Identifiers() {
            throw new AssertionError("Identifiers is a utility class and cannot be instantiated");
        }
        public static UUID newId() { /* ... */ }
    }
    ```

### UUID Strategy
- **UUID Version 7 (Time-Ordered):**
  - All entity IDs must use **UUID v7** for time-ordered, database-friendly identifiers
  - Use `Identifiers.newId()` utility class from `com.paceai.domain.shared`
  - Never use `UUID.randomUUID()` (v4) as it causes index fragmentation in databases
  - UUID v7 provides natural sorting by creation time and better database index performance
  - Dependency: `uuid-creator` library provides `UuidCreator.getTimeOrderedEpoch()`

### Documentation
- **Always consult the `docs/` folder** before implementing new features. It contains the PRD, requirements, and architectural decisions.

## Code Style Guidelines

### Package Structure
- Package name follows reverse domain: `org.paceia`
- Subpackages follow feature/domain organization
- Test classes mirror main package structure

### Naming Conventions
- **Classes**: PascalCase (e.g., `Application`, `TestcontainersConfiguration`)
- **Methods**: camelCase (e.g., `contextLoads`, `postgresContainer`)
- **Variables**: camelCase
- **Constants**: UPPER_SNAKE_CASE
- **Test Classes**: Append `Tests` suffix (e.g., `ApplicationTests`)

### Imports
- Organize imports alphabetically
- Separate standard library, third-party, and internal imports with blank lines
- Use wildcard imports sparingly (prefer explicit imports)
- Spring Boot annotations typically imported before other classes

### Class Structure
```java
package org.paceia;

// Imports (alphabetically ordered)
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Annotations (each on separate line if multiple)
@SpringBootApplication
public class Application {

    // Static fields first
    // Instance fields
    // Constructors
    // Public methods
    // Protected methods
    // Private methods
}
```

### Method Visibility
- Default (package-private) for Spring configuration classes (e.g., `TestcontainersConfiguration`)
- Public for main entry points and service methods
- Private for internal helper methods
- Test methods omit visibility modifier (JUnit 5 default)

### Spring Framework Conventions
- Use `@SpringBootApplication` on main application class
- Use `@TestConfiguration` for test-specific beans
- Use `@Bean` for bean definitions
- Use `@ServiceConnection` for Testcontainers integration
- Reactive controllers use `@RestController` with reactive types

### Error Handling
- Use Spring's reactive error handling: `Mono.error()` or `Flux.error()`
- Implement global exception handlers via `@ControllerAdvice`
- Validate inputs with `@Valid` and Jakarta Bean Validation annotations

### Testing Patterns
- Test classes annotated with `@SpringBootTest`
- Use `@Import` to include test configurations
- Test methods annotated with `@Test`
- Test method names should be descriptive camelCase
- Leverage Testcontainers for database-dependent tests
- Use `TestcontainersConfiguration` class for container setup

### Configuration
- Properties stored in `src/main/resources/application.properties`
- Use Spring Boot's configuration properties for typed access
- Sensitive configuration should use environment variables

### Reactivity Guidelines
- Return `Mono<T>` for single-value operations
- Return `Flux<T>` for multi-value operations
- Use `subscribe()` only at the edge layers (controllers, scheduled tasks)
- Avoid blocking calls in reactive pipelines

### Database Access
- Use Spring Data JPA repositories for entity persistence
- Transaction management via `@Transactional`
- Entity classes use JPA annotations (e.g., `@Entity`, `@Id`)
- Use Testcontainers for integration testing with PostgreSQL

## Development Workflow

1. Create feature branch from main
2. Implement changes following code style guidelines
3. Run tests with `./mvnw test`
4. Build package with `./mvnw clean package`
5. Commit changes with clear, descriptive messages
6. Ensure all tests pass before pushing

## Key Files

- `src/main/java/org/paceia/Application.java` - Main application entry point
- `src/test/java/org/paceia/TestcontainersConfiguration.java` - Testcontainers setup
- `pom.xml` - Maven project configuration
- `src/main/resources/application.properties` - Application configuration

## Notes

- The project uses Spring Boot Dev Services for Testcontainers at development time
- PostgreSQL container uses `postgres:latest` image - pin to specific version for production
- Maven Wrapper (`./mvnw`) is committed for consistent builds across environments
- Java 21 features are available and encouraged when appropriate
