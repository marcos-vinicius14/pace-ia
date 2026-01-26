# Agent Guide for pace.ia

This document provides essential information for agentic coding assistants working on this repository.

## Build, Lint, and Test Commands

**Note:** All commands must be run from the `backend/` directory.

```bash
cd backend

# Build Commands
./mvnw clean compile          # Compile the project
./mvnw clean package          # Build JAR package
./mvnw clean install          # Install to local Maven repo
./mvnw spring-boot:run         # Run the application (from pace-ai-startup)

# Test Commands
./mvnw test                   # Run all tests across all modules
./mvnw test -pl pace-ai-domain # Run tests for specific module
./mvnw test -Dtest=ClassName  # Run specific test class
./mvnw test -Dtest=ClassName#method  # Run specific test method
./mvnw verify                 # Run all tests including integration tests
```

### Code Quality
- **ArchUnit** is configured for architecture validation (ensures Clean Layer compliance)
- **Flyway** handles database migrations (`src/main/resources/db/migration`)
- No explicit linting plugins - follow Spring Boot conventions and Java best practices

## Technology Stack

- **Framework**: Spring Boot 3.4.1 (WebFlux, Security, Data JPA)
- **Java Version**: 21
- **Build Tool**: Maven (multi-module project with Maven Wrapper)
- **Database**: PostgreSQL 15+ (via Testcontainers in tests, AWS RDS in prod)
- **Cache**: Redis 7+ (AWS ElastiCache for session management)
- **Messaging**: AWS SQS (Spring Cloud AWS)
- **AI**: Spring AI (OpenAI/Anthropic integration)
- **Testing**: JUnit 5, AssertJ, Testcontainers, ArchUnit
- **Frontend**: Vue.js 3 (Composition API, Pinia, Tailwind CSS)
- **Architecture**: Vertical Slice + Clean Architecture (Hexagonal), Reactive WebFlux

## Module Structure

```
backend/
├── pace-ai-domain/          # Domain layer (no infra dependencies)
│   ├── shared/              # Value Objects (Distance, Pace, Zone, Email)
│   ├── athlete/             # Aggregates (Athlete, Profile)
│   ├── training/            # Entities (Plan, Session, Workout)
│   ├── exceptions/          # Domain Exceptions
│   └── ports/               # Interfaces (Repositories, Services)
├── pace-ai-application/     # Use Cases/Orchestration
├── pace-ai-infrastructure/  # Implementations (AWS, DB, AI, Messaging)
├── pace-ai-web/             # REST Controllers, Security, Filters
└── pace-ai-startup/         # Main application entry point
```

## Development Principles

### TDD (Test Driven Development)
All new features must follow the **RED → GREEN → Refactor** workflow.

### Architecture & Design
- **DDD + SOLID:** Domain-Driven Design and SOLID principles
- **Clean Architecture:** Domain layer has ZERO infrastructure dependencies (no Spring, JPA, etc.)
- **Composition over Inheritance:** Prefer composing objects over class inheritance
- **Immutability:** Favor immutable objects; use `final` fields, avoid setters
- **Rich Domain Model:** Domain entities contain behavior, not just data
- **Early Return:** Use early returns to reduce nesting

### Design Patterns

**Static Factory Methods:**
- Use for simple object creation with ≤ 4 mandatory fields
- Use descriptive names: `Distance.ofKilometers()`, `TrainingPlan.create()`, `AthleteId.create()`
- Example: `Distance.ofKilometers(10.5)` instead of `new Distance(10.5, Unit.KM)`

**Builder Pattern:**
- Use when ≥ 5 mandatory fields or mix of required/optional parameters
- Example: `Athlete.builder().id(id).email(email).stravaId(12345L).build()`

**State Machine for Enums:**
- Implement state machine patterns for enums (e.g., `PlanStatus`)
- Use `canTransitionTo()` and `transitionTo()` methods

**Utility Classes:**
- Make class `final` with private constructor throwing `AssertionError`
- Example: `Identifiers` class for UUID generation

### UUID Strategy
- **MUST use UUID v7** for time-ordered, database-friendly identifiers
- Use `Identifiers.newId()` from `com.paceai.domain.shared`
- Never use `UUID.randomUUID()` (v4) - causes index fragmentation
- Library: `uuid-creator` (`UuidCreator.getTimeOrderedEpoch()`)

## Code Style Guidelines

### Package Structure
- Package name: `com.paceai`
- Test classes mirror main package structure
- Each module is a separate Maven artifact

### Naming Conventions
- **Classes:** PascalCase (e.g., `TrainingPlan`, `PaceAiApplication`)
- **Methods:** camelCase (e.g., `shouldCreateAthlete`, `getValueInKilometers`)
- **Variables:** camelCase
- **Constants:** UPPER_SNAKE_CASE
- **Test Classes:** Append `Tests` suffix (e.g., `AthleteTests`)
- **Value Objects:** Singular nouns ending with concept (e.g., `Distance`, `Email`)

### Imports
- Organize alphabetically
- Standard library, third-party, internal imports separated by blank lines
- Prefer explicit imports over wildcards
- Spring Boot annotations imported before other classes

### Class Structure
```java
package com.paceai.domain.shared;

// Imports (alphabetically ordered)
import com.github.f4b6a3.uuid.UuidCreator;
import java.util.UUID;

/**
 * Class description following JavaDoc conventions.
 */
public final class Distance {

    // Static fields
    // Instance fields (final for immutability)
    // Private constructor
    // Static factory methods
    // Public methods
    // Private methods
    // equals(), hashCode(), toString()
}
```

### Method Visibility
- Package-private (default) for test-specific beans/configurations
- Public for service methods, factory methods, getters
- Private for internal helper methods
- Test methods omit visibility modifier (JUnit 5 default)

### Spring Framework Conventions
- `@SpringBootApplication` on main entry point (`PaceAiApplication`)
- `@TestConfiguration` for test-specific beans
- `@Bean` for bean definitions in configuration classes
- `@RestController` with reactive types (`Mono<T>`, `Flux<T>`)
- `@ServiceConnection` for Testcontainers integration
- Use `subscribe()` only at edge layers (controllers, scheduled tasks)

### Error Handling
- **Result Pattern:** Use Result type for expected failures (validation errors, not found, business rule violations)
- **Exceptions:** Use only for truly exceptional cases (system failures, unexpected errors)


### Functional Design Principles
- Favor pure functions without side effects where possible
- Use immutable data structures and final fields
- Prefer method chaining and fluent APIs
- Use Optional and Result types instead of null
- Leverage Java 21 functional features (records, pattern matching, switch expressions)
- Avoid mutable state in domain objects

### 9 Rules of Object Calisthenics
1. **Only 1 level of indentation per method** - Extract methods to reduce nesting
2. **Don't use ELSE** - Use early returns, guard clauses, or polymorphism
3. **Wrap all primitives and strings** - Use Value Objects (e.g., `Distance`, `Email`, `Pace`)
4. **First class collections** - Encapsulate collection behaviors in dedicated types (e.g., `Sessions`)
5. **One dot per line** - Avoid chaining calls; use temporary variables or tell-don't-ask
6. **Don't abbreviate** - Use descriptive names throughout
7. **Keep all entities small** - Classes with ≤ 50 lines
8. **No classes with more than 2 instance variables** - Extract smaller classes/compose objects
9. **No getters/setters** - Expose behavior, not data (tell-don't-ask principle)

### Testing Patterns
- Test classes annotated with `@SpringBootTest` (for integration tests)
- Use `@Import` for test-specific configurations
- Use `@Test` annotation on test methods (JUnit 5)
- Use AssertJ: `assertThat(actual).isEqualTo(expected)`
- Test method names: `shouldXxxWhenYyy()` or `shouldXxx()`
- Use Testcontainers for database-dependent tests
- Domain tests use plain JUnit (no Spring context)

### Configuration
- Properties in `backend/pace-ai-startup/src/main/resources/application.properties`
- Environment variables for sensitive config (DB, Redis, API keys)
- Flyway migrations in `src/main/resources/db/migration`

### Reactivity Guidelines
- Return `Mono<T>` for single-value operations
- Return `Flux<T>` for multi-value operations
- Avoid blocking calls in reactive pipelines
- Use SSE (Server-Sent Events) for real-time updates

### Database Access
- Spring Data JPA repositories for persistence
- Entity classes use JPA annotations (`@Entity`, `@Id`, etc.)
- Transaction management via `@Transactional`
- JPA entities in `pace-ai-infrastructure` (separate from domain entities)
- Mapper classes convert between domain and JPA entities

## Development Workflow

1. Create feature branch from `main`
2. Write failing test first (TDD - RED)
3. Implement minimum code to pass test (GREEN)
4. Refactor while keeping tests green
5. Run `./mvnw test` from `backend/` directory
6. Build with `./mvnw clean package`
7. Commit with clear, descriptive messages
8. Ensure all tests pass before pushing

## Key Files

- `backend/pace-ai-startup/src/main/java/com/paceai/PaceAiApplication.java` - Main entry point
- `backend/pom.xml` - Parent Maven configuration
- `backend/pace-ai-domain/src/main/java/com/paceai/domain/shared/Identifiers.java` - UUID v7 generator
- `backend/pace-ai-startup/src/main/resources/application.properties` - Application config

## Notes

- Always consult `docs/prd.md` before implementing new features
- The project uses Spring Boot Dev Services for Testcontainers at development time
- Multi-module Maven requires commands to be run from `backend/` directory
- Java 21 features (records, pattern matching, switch expressions) encouraged
- Code comments should follow JavaDoc conventions for public APIs
- Portuguese error messages are acceptable (e.g., `Distance.java`)
