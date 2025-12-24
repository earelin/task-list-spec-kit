# Implementation Plan: Backend Scaffolding

**Branch**: `001-backend-scaffolding` | **Date**: 2025-12-24 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-backend-scaffolding/spec.md`

## Summary

Create the foundational scaffolding for a Java Micronaut REST service with a Gradle
multiproject structure. The scaffolding includes a health check endpoint with version and
uptime reporting, layered architecture with ArchUnit fitness tests, CheckStyle integration
with SARIF output, structured logging with Micrometer metrics, and a GitHub Actions CI
pipeline. Application code resides in the `app` subproject.

## Technical Context

**Language/Version**: Java 21 (LTS)
**Framework**: Micronaut 4.x
**Primary Dependencies**: Micronaut HTTP Server, Micronaut Management, Micrometer, Logback, ArchUnit
**Build Tool**: Gradle 8.x with Kotlin DSL
**Storage**: N/A (scaffolding only - no persistence layer)
**Testing**: JUnit 5, Micronaut Test, ArchUnit
**Target Platform**: Linux server (containerized deployment)
**Project Type**: Multiproject Gradle (app subproject for application code)
**Performance Goals**: Health endpoint <100ms p99 (per SC-001)
**Constraints**: <200ms p95 API response (per constitution), SARIF output for quality tools
**Scale/Scope**: Single service scaffolding, foundation for future task list features

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Requirement | Scaffolding Compliance | Status |
|-----------|-------------|------------------------|--------|
| 1. Quality First | 80% code coverage, SonarQube Cloud, CheckStyle SARIF | CheckStyle configured, coverage target set, SonarQube integration planned | ✅ |
| 2. DevOps | GitHub Actions CI/CD, 3 pipelines | CI pipeline in scope (FR-007), CD/Release deferred to deployment feature | ✅ |
| 3. SRE | SLOs, monitoring <5min detection | Health endpoint with metrics (FR-011), logging (FR-010) | ✅ |
| 4. Evolutionary Architecture | ArchUnit fitness functions, ADRs | ArchUnit tests required (FR-004, FR-008) | ✅ |
| 5. Clean Code | CheckStyle Google Java Format, SARIF | Configured in build (FR-005, FR-006, FR-009) | ✅ |
| 6. Infrastructure as Code | Checkov linting, version control | Dockerfile linting in CI pipeline | ✅ |
| 7. Continuous Delivery | Trunk deployable, semantic versioning | Version in health response (FR-002), trunk-based workflow | ✅ |
| 8. Continuous Refactoring | SonarQube metrics, test safety net | SonarQube integration, test foundation | ✅ |

**Gate Status**: PASSED - All constitutional principles addressed in scaffolding scope.

## Project Structure

### Documentation (this feature)

```text
specs/001-backend-scaffolding/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
│   └── health-api.yaml  # OpenAPI spec for health endpoint
└── tasks.md             # Phase 2 output (/speckit.tasks command)
```

### Source Code (repository root)

```text
backend/
├── build.gradle.kts           # Root build configuration
├── settings.gradle.kts        # Multiproject settings
├── gradle.properties          # Gradle properties
├── gradle/
│   └── wrapper/               # Gradle wrapper
├── config/
│   └── checkstyle/
│       └── checkstyle.xml     # Google Java Format rules
├── app/
│   ├── build.gradle.kts       # App subproject build
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── com/tasklist/
│       │   │       ├── Application.java
│       │   │       ├── domain/           # Domain layer (no dependencies)
│       │   │       ├── application/      # Application services
│       │   │       └── infrastructure/   # Controllers, config, adapters
│       │   │           └── web/
│       │   │               └── HealthController.java
│       │   └── resources/
│       │       ├── application.yml
│       │       └── logback.xml
│       └── test/
│           └── java/
│               └── com/tasklist/
│                   ├── architecture/     # ArchUnit fitness tests
│                   │   └── LayerDependencyTest.java
│                   └── infrastructure/
│                       └── web/
│                           └── HealthControllerTest.java
├── .github/
│   └── workflows/
│       └── ci.yml             # CI pipeline
└── Dockerfile                 # Container image definition
```

**Structure Decision**: Gradle multiproject with `app` subproject following hexagonal
architecture layers (domain → application → infrastructure). This structure supports
future subprojects (e.g., shared libraries, additional services) while keeping the
primary application code isolated and independently buildable.

## Complexity Tracking

> No constitution violations requiring justification. Structure follows standard patterns.

| Aspect | Decision | Rationale |
|--------|----------|-----------|
| Multiproject vs Single | Multiproject with `app` | User requirement; enables future modularity |
| Package structure | Hexagonal layers | Constitution principle 4 (Evolutionary Architecture) |
| Metrics library | Micrometer | Micronaut native integration, constitution SRE principle |
