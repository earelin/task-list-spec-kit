# Implementation Plan: Backend Scaffolding

**Branch**: `001-backend-scaffolding` | **Date**: 2025-12-24 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-backend-scaffolding/spec.md`

## Summary

Create the foundational scaffolding for a Java Micronaut REST service with a Gradle
multiproject structure. The scaffolding includes a health check endpoint with version and
uptime reporting, layered architecture with ArchUnit fitness tests, CheckStyle integration
with XML output for SonarQube, structured logging with OpenTelemetry metrics via OTLP, and
a GitHub Actions CI pipeline. Application code resides in the `app` subproject.

## Technical Context

**Language/Version**: Java 21 (LTS)
**Framework**: Micronaut 4.x
**Primary Dependencies**: Micronaut HTTP Server, Micronaut Management, OpenTelemetry Java Agent, Logback, ArchUnit
**Build Tool**: Gradle 8.x with Kotlin DSL
**Storage**: N/A (scaffolding only - no persistence layer)
**Testing**: JUnit 5, Micronaut Test, ArchUnit
**Target Platform**: Linux server (containerized deployment)
**Project Type**: Multiproject Gradle (app subproject for application code)
**Performance Goals**: Health endpoint <100ms p99 (per SC-001)
**Constraints**: <200ms p95 API response (per constitution), XML output for CheckStyle→SonarQube
**Scale/Scope**: Single service scaffolding, foundation for future task list features

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Requirement | Scaffolding Compliance | Status |
|-----------|-------------|------------------------|--------|
| 1. Quality First | 80% code coverage, SonarQube Cloud, CheckStyle | CheckStyle configured with XML output for SonarQube import | ✅ |
| 2. DevOps | GitHub Actions CI/CD, 3 pipelines | CI pipeline in scope (FR-007), CD/Release deferred to deployment feature | ✅ |
| 3. SRE | SLOs, monitoring <5min detection | Health endpoint with OpenTelemetry metrics (FR-011), logging (FR-010) | ✅ |
| 4. Evolutionary Architecture | ArchUnit fitness functions, ADRs | ArchUnit tests required (FR-004, FR-008) | ✅ |
| 5. Clean Code | CheckStyle Google Java Format | Configured in build with XML reports (FR-005, FR-009) | ✅ |
| 6. Infrastructure as Code | Checkov linting, version control | Dockerfile linting in CI pipeline | ✅ |
| 7. Continuous Delivery | Trunk deployable, semantic versioning | Version in health response (FR-002), trunk-based workflow | ✅ |
| 8. Continuous Refactoring | SonarQube metrics, test safety net | SonarQube integration via CheckStyle XML, test foundation | ✅ |

**Gate Status**: PASSED - All constitutional principles addressed in scaffolding scope.

**Note on SARIF vs XML**: The constitution mentions SARIF format, but SonarQube Cloud natively
imports CheckStyle XML reports via `sonar.java.checkstyle.reportPaths`. This is the standard
integration pattern and meets the spirit of the requirement (automated quality gate integration).

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
| Observability | OpenTelemetry Java Agent | User requested OpenTelemetry Java Agent for automatic instrumentation |
| CheckStyle output | XML (not SARIF) | SonarQube natively imports CheckStyle XML via `sonar.java.checkstyle.reportPaths` |

## Key Technology Changes (from previous plan)

### 1. Observability: OpenTelemetry Java Agent

**Previous**: Micrometer + Prometheus Registry (in-code instrumentation)
**Updated**: OpenTelemetry Java Agent (automatic bytecode instrumentation)

**Approach**: Use the OpenTelemetry Java Agent for zero-code instrumentation. The agent
automatically instruments HTTP requests, database calls, and other common libraries
without requiring code changes or SDK dependencies.

**Agent Download** (in Dockerfile or CI):
```bash
# Download OpenTelemetry Java Agent
curl -L -o opentelemetry-javaagent.jar \
  https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar
```

**Runtime Configuration** (via environment variables):
```bash
# Required
OTEL_SERVICE_NAME=tasklist-backend
OTEL_EXPORTER_OTLP_ENDPOINT=http://otel-collector:4317

# Optional
OTEL_TRACES_EXPORTER=otlp
OTEL_METRICS_EXPORTER=otlp
OTEL_LOGS_EXPORTER=otlp
OTEL_RESOURCE_ATTRIBUTES=service.version=1.0.0,deployment.environment=dev
```

**JVM Startup**:
```bash
java -javaagent:opentelemetry-javaagent.jar -jar app.jar
```

**Dockerfile Integration**:
```dockerfile
FROM eclipse-temurin:21-jre-alpine

# Download OpenTelemetry Java Agent
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar /opt/opentelemetry-javaagent.jar

COPY app/build/libs/app-*-all.jar /app/app.jar

ENTRYPOINT ["java", "-javaagent:/opt/opentelemetry-javaagent.jar", "-jar", "/app/app.jar"]
```

**Benefits of Java Agent approach**:
- Zero code changes required
- Automatic instrumentation of HTTP, JDBC, logging, and 100+ libraries
- Consistent telemetry across all services
- Easy to enable/disable via JVM flag
- Centralized configuration via environment variables

### 2. SonarQube: CheckStyle XML instead of SARIF

**Previous**: CheckStyle SARIF output → `sonar.sarifReportPaths`
**Updated**: CheckStyle XML output → `sonar.java.checkstyle.reportPaths`

**Gradle Configuration**:
```kotlin
tasks.withType<Checkstyle> {
    reports {
        xml.required.set(true)
        html.required.set(true)
        sarif.required.set(false)  // Disabled - using XML for SonarQube
    }
}

sonar {
    properties {
        property("sonar.java.checkstyle.reportPaths",
            "app/build/reports/checkstyle/main.xml,app/build/reports/checkstyle/test.xml")
    }
}
```

**Rationale**: SonarQube Cloud has native CheckStyle XML import support. This is the
standard integration pattern documented by SonarSource and provides better rule
mapping than generic SARIF import.
