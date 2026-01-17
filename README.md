# Pace AI

**Proof of Work - High-End Engineering Portfolio Case**

Pace AI is an intelligent street running planning and coaching platform that demonstrates proficiency in high-complexity software architecture, data engineering (vectors), cloud computing (AWS IaaS), and reactive full-stack development.

The system uses Generative AI (LLMs) and RAG (Retrieval-Augmented Generation) to create personalized training plans based on sports science, validated by real athlete data via Strava integration.

## Technical Overview

### Architecture Pattern
- **Vertical Slice + Clean Architecture (Hexagonal)**
- Code organized by business capabilities with strict layer separation
- Domain layer is infrastructure-agnostic (no Spring/JPA dependencies)
- Multi-module Maven structure enforces dependency boundaries

### Reactive Flow
1. **Request**: User requests a plan via Vue.js
2. **Dispatch**: API sends message to AWS SQS, returns ID (202 Accepted)
3. **Processing**: Worker (EC2) consumes queue, invokes Spring AI
4. **Persistence**: Results saved to RDS (Relational + JSONB) and cached in Redis
5. **Notification**: SSE (Server-Sent Events) notifies frontend of completion

## Technology Stack

### Backend
- **Language**: Java 21
- **Framework**: Spring Boot 3.x
- **Web**: Spring WebFlux (Reactive/Non-Blocking for SSE)
- **AI**: Spring AI (OpenAI/Anthropic integration)
- **Messaging**: AWS SQS (via Spring Cloud AWS)
- **Security**: Spring Security + OAuth2 (Strava) + Redis Session Management
- **Migration**: Flyway

### Data & Caching
- **Relational Database**: PostgreSQL 15+ (AWS RDS)
- **Vector Extension**: pgvector (hosted on same RDS)
- **Cache**: Redis 7+ (AWS ElastiCache)
- **Pattern**: Distributed stateful session architecture (not stateless JWTs)

### Frontend
- **Framework**: Vue.js 3 (Composition API)
- **State**: Pinia
- **Style**: Tailwind CSS
- **Calendar**: V-Calendar or FullCalendar

### Infrastructure as Code (IaC)
- **Provider**: AWS
- **Tool**: Terraform
- **Services**:
  - EC2 (Auto Scaling Group)
  - Application Load Balancer (SSL Termination)
  - ElastiCache (Redis Cluster)
  - RDS (PostgreSQL in Private Subnets)
  - SQS (Standard Queues + DLQ)
  - ECR (Docker Registry)
  - IAM (Granular roles for EC2 and RDS)

### Quality
- **Integration Tests**: Testcontainers (Local Docker)
- **Architecture Validation**: ArchUnit (ensures Clean Architecture compliance)
- **CI/CD**: GitHub Actions (Docker Build, Push to ECR, Terraform Apply)

## Module Structure

```
pace-ai/
├── backend/
│   ├── pace-ai-domain/          # [MODULE 1] The Brain (No infra dependencies)
│   │   ├── shared/              # Value Objects (Distance, Pace, Zone, Email)
│   │   ├── athlete/             # Aggregates Athlete, Profile
│   │   ├── training/            # Entities Plan, Session, Workout
│   │   ├── exceptions/          # Domain Exceptions
│   │   └── ports/               # Interfaces (Repositories, Services)
│   │
│   ├── pace-ai-application/     # [MODULE 2] Orchestration (Use Cases)
│   │   ├── services/            # GeneratePlanService, SyncStravaService, AskCoachRagService
│   │   ├── dtos/                # Input/Output DTOs
│   │   └── mappers/             # Converters DTO <-> Entity
│   │
│   ├── pace-ai-infrastructure/ # [MODULE 3] Reality (AWS, DB, AI)
│   │   ├── persistence/
│   │   │   ├── postgres/        # JPA/Hibernate Implementations
│   │   │   └── redis/           # Cache/Session Implementation
│   │   ├── messaging/
│   │   │   ├── sqs/             # SQS Producer
│   │   │   └── listeners/       # SQS Consumer (Worker)
│   │   ├── ai/
│   │   │   ├── springai/        # Adapter for OpenAI/Anthropic
│   │   │   └── pgvector/        # Adapter for RAG (pgvector)
│   │   ├── external/
│   │   │   └── strava/          # Strava HTTP Client
│   │   └── config/              # Spring Configs
│   │
│   ├── pace-ai-web/             # [MODULE 4] The Interface (REST/SSE)
│   │   ├── controllers/         # RestControllers
│   │   ├── security/            # SecurityConfig, JWT/OAuth2
│   │   └── filters/             # Custom Filters
│   │
│   └── pace-ai-startup/         # [MODULE 5] Entry Point (Final Jar)
│
├── frontend/                    # Vue.js Project
│   ├── src/
│   │   ├── components/          # UI Components
│   │   ├── composables/         # Pinia Stores
│   │   ├── services/            # API Calls
│   │   └── views/               # Pages
│   └── package.json
│
└── terraform/                   # Infrastructure as Code
```


### Cache Strategy (Redis)
- **Key**: `plan:{athlete_id}:{date}`
- **TTL**: 24 hours (non-critical) or 1 hour (daily workout data)
- **Use**: SSE connection tokens and plan summaries for Dashboard

## Key Features

### 1. AI Plan Generation
- **Input**: Target distance, race date, current level, available days per week
- **Process**: Prompt Engineering + Domain Validation (15% volume rule)
- **Output**: Structured JSON saved in `training_sessions` and `details_jsonb`
- **Tech**: SQS + Spring AI + WebFlux (SSE)

### 2. Interactive Dashboard (Vue.js)
- Monthly calendar visualization
- Workout details on click (Modal)
- Status indicators (Completed, Pending, Missed)
- Real-time feedback during plan generation (SSE)

### 3. Strava Synchronization (2-Way Sync)
- **Import**: Strava Webhook triggers sync upon activity creation
- **Validation**: Fuzzy matching algorithm (Strava date/distance vs daily plan)
- **Action**: Automatically marks session as `COMPLETED` if there is a match
- **Handling**: Saves raw data in `strava_activities`

### 4. Scientific Coach (RAG Feature)
- **Functionality**: "Ask the Coach" Chatbot
- **Knowledge Base**: Physiology articles, classic training plans (Pfitzinger, Daniels)
- **Context**: RAG searches `pgvector` for relevant articles + athlete's recent history
- **Output**: Science-based answers, not generic ones

## Business Rules & Domain Logic

### Safe Progression Rule (10-15%)
- Domain (`TrainingPlan` entity) calculates total weekly load
- If `week N > week N-1 * 1.15`, the plan is rejected or AI is instructed to recalculate
- Fail-safe implemented in Java layer

### Target Zones (VDOT)
- Training paces (Easy, Tempo, Interval) are calculated mathematically based on athlete's `vdot_score`
- Not hardcoded values

### Adaptive Feedback
- If user reports excessive fatigue, the system suggests a "regeneration week" via AI

## Security Architecture

### Authentication Strategy: Server-Side Session with Redis
- **Client (Vue.js)**: Maintains only a session identifier (Cookie)
- **Server (Spring Boot + Redis)**: Maintains authentication state and access/refresh tokens in Redis memory (ElastiCache)

### Security Stack
- **Framework**: Spring Security 6
- **Session Management**: Spring Session Data Redis
- **Transport**: Cookie HttpOnly, Secure (HTTPS), SameSite=Strict
- **CSRF Protection**: Enabled and customized for SPA (Vue.js)
- **CORS**: Configured for Frontend domain

### RBAC (Role-Based Access Control)

**ROLE_ATHLETE (Default)**:
- `plan:read`: View custom plans
- `plan:create`: Request new plans
- `activity:write`: Import/Sync Strava
- `chat:ask`: Ask the RAG Coach

**ROLE_ADMIN**:
- `admin:logs`: View error and audit logs
- `system:monitor`: View application health metrics

## Non-Functional Requirements

### Security
- RDS and ElastiCache in Private Subnets (no public IP)
- EC2s accessible only via ALB (Strict Security Groups)
- Secrets (API Keys, DB Password) stored in AWS Parameter Store or Secrets Manager
- Encrypted communication (HTTPS/TLS 1.3)

### Scalability
- ASG configured to scale horizontally if CPU > 70% for 5 minutes
- SQS queues handle plan generation peaks

### Observability
- Structured Logs (JSON)
- Health Metrics (Health Checks) exposed via Actuator

### Availability
- Multi-AZ for RDS and Redis (ElastiCache)

## Development

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

### Development Roadmap

**Phase 1: Foundation and Clean Arch (Local)**
- Project setup Maven with modules
- Implementation of Domain Entities and Unit Tests
- Testcontainers configuration (Local Postgres)

**Phase 2: AWS Infrastructure (Terraform)**
- Terraform modules: VPC, EC2 (ASG/ALB), RDS, SQS, ElastiCache
- Dockerfile configuration and pipeline (Build & Push ECR)
- Deploy "Hello World" infrastructure on AWS

**Phase 3: AI and Plan Generation**
- Integration with Spring AI
- Implementation of Producer (API) -> SQS -> Worker -> Consumer flow
- SSE for status updates in Frontend
- Initial population of JSONB in Postgres

**Phase 4: Frontend and Strava Integration**
- Vue.js Dashboard development
- OAuth2 Integration with Strava
- Strava Webhook and activity matching logic

**Phase 5: RAG and Intelligent Coach**
- Setup `pgvector` extension in RDS
- Ingestion pipeline (PDFs/Texts -> Vectors)
- `/ask-coach` endpoint using Spring AI Vector Store

**Phase 6: Polish and Portfolio**
- Observability (Advanced Logging)
- Load testing (K6) to validate ASG
- Architecture documentation (C4 Diagrams, detailed README)

## Success Criteria

- System generates a complete marathon plan in less than 30 seconds
- Dashboard updates plan status without page refresh (SSE)
- AWS infrastructure survives an EC2 instance failure (Auto Recovery)
- RAG Coach answers physiology questions with citations from the knowledge base
- All code follows Clean Arch principles validated by ArchUnit

## Design Patterns & Best Practices

### Architecture
- **DDD + SOLID**: Domain-Driven Design and SOLID principles
- **Composition over Inheritance**: Prefer composing objects over class inheritance
- **Immutability**: Favor immutable objects; use `final` fields and avoid setters
- **Rich Domain Model**: Domain entities contain behavior, not just data

### Design Patterns
- **Static Factory Methods**: For simple object creation (≤ 4 mandatory fields)
- **Builder Pattern**: Only when ≥ 5 mandatory fields or mix of required/optional
- **State Machine for Enums**: Use state machine patterns (e.g., `TrainingSessionStatus`)

### Code Style
- **UUID v7**: Use `Identifiers.newId()` for time-ordered, database-friendly identifiers
- **Utility Classes**: Enforce noninstantiability with private constructor
- **Early Return**: Reduce nesting and improve readability

## License

This is a Proof of Work portfolio project demonstrating high-end software engineering capabilities.

---

**Author**: Marcos Vinicius | **Version**: 1.0 | **Status**: Planning Approved
