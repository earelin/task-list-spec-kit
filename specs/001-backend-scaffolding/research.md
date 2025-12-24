# Research: Backend Scaffolding

**Feature**: 001-backend-scaffolding
**Date**: 2025-12-24

## Technology Decisions

### 1. Framework: Micronaut 4.x

**Decision**: Micronaut Framework 4.10.6 with Micronaut Platform BOM

**Rationale**:
- User specified Micronaut as the framework
- Version 4.10.6 is the current stable release (December 2024)
- Native support for GraalVM, excellent startup time, low memory footprint
- Built-in management endpoints for health checks
- Micrometer integration for metrics
- Compile-time dependency injection (no reflection)

**Alternatives Considered**:
- Spring Boot: More mature but heavier runtime, reflection-based DI
- Quarkus: Similar benefits to Micronaut but less familiar to team

### 2. Java Version: Java 21 LTS

**Decision**: Java 21 (Eclipse Temurin distribution)

**Rationale**:
- Current Long-Term Support version
- Required by SonarQube Cloud plugin (v7.x requires Java 21+)
- Virtual threads available for future scalability
- Pattern matching and record patterns for cleaner code

**Alternatives Considered**:
- Java 17 LTS: Supported but missing latest features, deprecated by SonarQube
- Java 23: Not LTS, shorter support window

### 3. Build Tool: Gradle 8.x with Kotlin DSL

**Decision**: Gradle 8.x with Kotlin DSL (per clarification)

**Rationale**:
- Clarification session selected Gradle with Kotlin DSL
- Better IDE support and type safety than Groovy DSL
- Faster incremental builds than Maven
- Configuration cache support for CI performance

**Configuration**:
```kotlin
// settings.gradle.kts
plugins {
    id("io.micronaut.platform.catalog") version "4.6.1"
}
```

### 4. Health Endpoint: Micronaut Management

**Decision**: Use `micronaut-management` module

**Rationale**:
- Native Micronaut integration
- Built-in health indicators for common resources
- Supports readiness and liveness probes
- Customizable health aggregation

**Dependencies**:
```kotlin
implementation("io.micronaut:micronaut-management")
```

**Configuration**:
```yaml
endpoints:
  health:
    enabled: true
    sensitive: false
    details-visible: ANONYMOUS
```

### 5. Observability: OpenTelemetry Java Agent

**Decision**: OpenTelemetry Java Agent for automatic instrumentation

**Rationale**:
- User requested OpenTelemetry instead of Prometheus
- Java Agent provides zero-code instrumentation
- Automatic capture of HTTP requests, JDBC, logging, and 100+ libraries
- OTLP export to any OpenTelemetry-compatible backend
- Centralized configuration via environment variables

**Agent Version**: Latest stable (auto-downloaded in Dockerfile)

**No code dependencies required** - the agent instruments at bytecode level.

**Configuration** (via environment variables):
```bash
OTEL_SERVICE_NAME=tasklist-backend
OTEL_EXPORTER_OTLP_ENDPOINT=http://otel-collector:4317
OTEL_TRACES_EXPORTER=otlp
OTEL_METRICS_EXPORTER=otlp
OTEL_LOGS_EXPORTER=otlp
```

**JVM Startup**:
```bash
java -javaagent:opentelemetry-javaagent.jar -jar app.jar
```

**Alternatives Considered**:
- Micrometer + Prometheus: Requires code instrumentation, Prometheus-specific
- Micronaut Tracing SDK: Code dependencies, less automatic coverage
- OpenTelemetry SDK (manual): More control but requires code changes

### 6. Logging: Logback with Logstash JSON Encoder

**Decision**: Logback with `logstash-logback-encoder` 9.0

**Rationale**:
- FR-010 requires structured logs to stdout
- JSON format enables log aggregation (ELK, Splunk, CloudWatch)
- Version 9.0 requires Java 17+ (compatible with Java 21)
- Automatic MDC inclusion for request tracing

**Dependencies**:
```kotlin
runtimeOnly("net.logstash.logback:logstash-logback-encoder:9.0")
```

### 7. Architecture Fitness: ArchUnit 1.4.1

**Decision**: ArchUnit 1.4.1 with JUnit 5 integration

**Rationale**:
- FR-004, FR-008 require architectural fitness tests
- Constitution mandates ArchUnit for Java projects
- Validates layer dependencies, naming conventions, circular dependencies

**Dependencies**:
```kotlin
testImplementation("com.tngtech.archunit:archunit-junit5:1.4.1")
```

**Layer Rules**:
```java
layeredArchitecture()
    .consideringAllDependencies()
    .layer("Domain").definedBy("..domain..")
    .layer("Application").definedBy("..application..")
    .layer("Infrastructure").definedBy("..infrastructure..")
    .whereLayer("Domain").mayNotAccessAnyLayer()
    .whereLayer("Application").mayOnlyAccessLayers("Domain")
    .whereLayer("Infrastructure").mayOnlyAccessLayers("Application", "Domain")
```

### 8. Code Style: CheckStyle 12.3.0 with Google Java Format

**Decision**: CheckStyle 12.3.0 with Google Java Format rules, XML output for SonarQube

**Rationale**:
- Constitution requires CheckStyle with Google Java Format
- FR-005, FR-009 require style validation
- SonarQube natively imports CheckStyle XML via `sonar.java.checkstyle.reportPaths`
- XML output provides better rule mapping than SARIF for CheckStyle issues

**Gradle Configuration**:
```kotlin
plugins {
    checkstyle
}

checkstyle {
    toolVersion = "12.3.0"
    configFile = file("config/checkstyle/checkstyle.xml")
    maxWarnings = 0
    maxErrors = 0
}

tasks.withType<Checkstyle> {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
```

### 9. SonarQube Integration

**Decision**: SonarQube Gradle Plugin 7.2.2.6593 with CheckStyle XML import

**Rationale**:
- Constitution requires SonarQube Cloud as central quality gate
- Version 7.x requires Java 21 (aligned with project)
- Native CheckStyle XML import via `sonar.java.checkstyle.reportPaths`

**Gradle Configuration**:
```kotlin
plugins {
    id("org.sonarqube") version "7.2.2.6593"
}

sonar {
    properties {
        property("sonar.projectKey", "tasklist-backend")
        property("sonar.organization", "your-org")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.java.checkstyle.reportPaths",
            "app/build/reports/checkstyle/main.xml,app/build/reports/checkstyle/test.xml")
    }
}
```

**Note**: SonarQube Cloud has native CheckStyle XML support which provides better
rule mapping than generic SARIF import. Issues appear with proper CheckStyle rule IDs.

### 10. CI Pipeline: GitHub Actions

**Decision**: GitHub Actions with specific action versions

**Rationale**:
- Constitution mandates GitHub Actions for CI/CD
- FR-007 requires CI pipeline for pull requests

**Action Versions**:
| Action | Version | Purpose |
|--------|---------|---------|
| actions/checkout | v5 | Code checkout |
| actions/setup-java | v5 | Java 21 + Gradle cache |
| gradle/actions/setup-gradle | v5 | Advanced Gradle caching |
| gradle/wrapper-validation-action | v3 | Security validation |
| github/codeql-action/upload-sarif | v4 | SARIF upload |

### 11. Testing: JUnit 5 with Micronaut Test

**Decision**: JUnit 5 with Micronaut Test framework

**Rationale**:
- Native Micronaut test support
- Embedded server for integration tests
- Dependency injection in tests

**Dependencies**:
```kotlin
testImplementation("io.micronaut.test:micronaut-test-junit5")
testImplementation("org.junit.jupiter:junit-jupiter-api")
testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
```

## Version Summary

| Component | Version | Notes |
|-----------|---------|-------|
| Java | 21 (Temurin) | LTS |
| Micronaut Platform | 4.10.6 | Current stable |
| Micronaut Gradle Plugin | 4.6.1 | Multiproject support |
| Gradle | 8.x | Kotlin DSL |
| OpenTelemetry Java Agent | Latest | Auto-downloaded in Dockerfile |
| ArchUnit | 1.4.1 | Architecture tests |
| CheckStyle | 12.3.0 | XML output for SonarQube |
| Logstash Logback Encoder | 9.0 | Java 17+ required |
| SonarQube Plugin | 7.2.2.6593 | Java 21 required |
| JUnit | 5.x | Via Micronaut BOM |

## Package Structure

Based on hexagonal architecture pattern:

```
com.tasklist
├── domain/                    # Pure business logic, no framework deps
│   ├── model/                 # Domain entities
│   └── service/               # Domain services
├── application/               # Use cases, orchestration
│   ├── port/
│   │   ├── in/               # Inbound port interfaces
│   │   └── out/              # Outbound port interfaces
│   └── service/              # Use case implementations
└── infrastructure/            # Framework adapters
    ├── web/                   # REST controllers
    ├── config/                # Micronaut configuration
    └── adapter/               # External service adapters
```

**ArchUnit Enforcement**:
- Domain layer: May not access any other layer
- Application layer: May only access Domain
- Infrastructure layer: May access Application and Domain

## References

- [Micronaut Health Endpoint Guide](https://guides.micronaut.io/latest/micronaut-health-endpoint-gradle-java.html)
- [Micronaut Micrometer Documentation](https://micronaut-projects.github.io/micronaut-micrometer/latest/guide/)
- [ArchUnit User Guide](https://www.archunit.org/userguide/html/000_Index.html)
- [CheckStyle Google Style Coverage](https://checkstyle.org/google_style.html)
- [SonarQube Gradle Scanner](https://docs.sonarsource.com/sonarqube-cloud/advanced-setup/ci-based-analysis/sonarscanner-for-gradle/)
- [Logstash Logback Encoder](https://github.com/logfellow/logstash-logback-encoder)
