# Quickstart: Backend Scaffolding

**Feature**: 001-backend-scaffolding
**Date**: 2025-12-24

## Prerequisites

- Java 21 (Eclipse Temurin recommended)
- Gradle 8.x (wrapper included)
- Git

## Quick Setup

### 1. Clone and Build

```bash
# Navigate to backend directory
cd backend

# Build the project
./gradlew build
```

### 2. Run the Application

```bash
# Run with Gradle
./gradlew :app:run

# Or run the JAR directly
java -jar app/build/libs/app-1.0.0-all.jar
```

### 3. Verify Health Endpoint

```bash
# Check health status
curl http://localhost:8080/health

# Expected response:
# {
#   "status": "UP",
#   "version": "1.0.0",
#   "uptime": "PT0S",
#   "timestamp": "2025-12-24T10:30:00Z"
# }
```

### 4. Run with OpenTelemetry (Optional)

The application uses the OpenTelemetry Java Agent for automatic instrumentation.
When running locally without an OTEL collector, telemetry is simply disabled.

```bash
# Download the OpenTelemetry Java Agent
curl -L -o opentelemetry-javaagent.jar \
  https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar

# Run with the agent (exports to local collector)
OTEL_SERVICE_NAME=tasklist-backend \
OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4317 \
java -javaagent:opentelemetry-javaagent.jar -jar app/build/libs/app-1.0.0-all.jar

# Or run without telemetry (agent not required for local development)
java -jar app/build/libs/app-1.0.0-all.jar
```

## Development Workflow

### Run Quality Checks

```bash
# Run all checks
./gradlew check

# Individual checks
./gradlew checkstyleMain      # Code style
./gradlew test                # Unit tests
./gradlew :app:archTest       # Architecture fitness tests
```

### Run with Live Reload

```bash
# Continuous build mode
./gradlew :app:run --continuous
```

### Generate Reports

```bash
# CheckStyle XML report (for SonarQube)
./gradlew checkstyleMain
# Output: app/build/reports/checkstyle/main.xml

# Test coverage report
./gradlew test jacocoTestReport
# Output: app/build/reports/jacoco/test/html/index.html
```

## Project Structure

```
backend/
├── build.gradle.kts           # Root build configuration
├── settings.gradle.kts        # Multiproject settings
├── config/
│   └── checkstyle/
│       └── checkstyle.xml     # Google Java Format rules
└── app/
    ├── build.gradle.kts       # App build configuration
    └── src/
        ├── main/
        │   ├── java/com/tasklist/
        │   │   ├── Application.java
        │   │   ├── domain/           # Domain layer
        │   │   ├── application/      # Application layer
        │   │   └── infrastructure/   # Infrastructure layer
        │   │       └── web/
        │   │           └── HealthController.java
        │   └── resources/
        │       ├── application.yml
        │       └── logback.xml
        └── test/
            └── java/com/tasklist/
                ├── architecture/
                │   └── LayerDependencyTest.java
                └── infrastructure/web/
                    └── HealthControllerTest.java
```

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `MICRONAUT_SERVER_PORT` | 8080 | HTTP server port |
| `MICRONAUT_APPLICATION_NAME` | tasklist-backend | Application name |
| `OTEL_SERVICE_NAME` | tasklist-backend | OpenTelemetry service name |
| `OTEL_EXPORTER_OTLP_ENDPOINT` | (none) | OTLP collector endpoint |
| `OTEL_TRACES_EXPORTER` | otlp | Trace exporter type |
| `OTEL_METRICS_EXPORTER` | otlp | Metrics exporter type |
| `OTEL_LOGS_EXPORTER` | otlp | Logs exporter type |

### application.yml

```yaml
micronaut:
  application:
    name: tasklist-backend
  server:
    port: ${MICRONAUT_SERVER_PORT:8080}

endpoints:
  health:
    enabled: true
    sensitive: false
    details-visible: ANONYMOUS
```

**Note**: OpenTelemetry is configured via the Java Agent and environment variables,
not through application.yml. See the OpenTelemetry section above.

## Architecture Rules

The project enforces hexagonal architecture via ArchUnit:

1. **Domain layer** (`com.tasklist.domain`): No external dependencies
2. **Application layer** (`com.tasklist.application`): Only depends on Domain
3. **Infrastructure layer** (`com.tasklist.infrastructure`): Depends on Application and Domain

Violations fail the build automatically.

## Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/health` | GET | Full health status with details |
| `/health/liveness` | GET | Liveness probe (container restart) |
| `/health/readiness` | GET | Readiness probe (traffic routing) |

**Note**: Metrics are exported via OpenTelemetry OTLP protocol to an external collector,
not via a `/prometheus` endpoint. Configure `OTEL_EXPORTER_OTLP_ENDPOINT` to point
to your OpenTelemetry collector.

## Troubleshooting

### Build Fails with CheckStyle Errors

```bash
# View detailed report
cat build/reports/checkstyle/main.html

# Auto-format code (if using IDE)
# IntelliJ: Code → Reformat Code (with Google Java Format plugin)
```

### Architecture Test Fails

```bash
# View test output
./gradlew :app:test --tests "*LayerDependencyTest*" --info

# Common fixes:
# - Move class to correct package
# - Remove forbidden import
# - Use dependency injection instead of direct instantiation
```

### Port Already in Use

```bash
# Run on different port
MICRONAUT_SERVER_PORT=8081 ./gradlew :app:run
```

## Next Steps

After scaffolding is complete:

1. Run `/speckit.tasks` to generate implementation tasks
2. Implement tasks in priority order
3. Each PR triggers CI pipeline validation
4. Merge to trunk triggers CD pipeline
