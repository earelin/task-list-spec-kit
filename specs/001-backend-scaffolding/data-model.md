# Data Model: Backend Scaffolding

**Feature**: 001-backend-scaffolding
**Date**: 2025-12-24

## Overview

This document defines the data model for the backend scaffolding feature. Since this is
scaffolding (foundation setup), the data model is minimal - focused on the health endpoint
response structure.

## Entities

### HealthStatus

Represents the current operational state of the service.

**Purpose**: Returned by the health check endpoint to indicate service status, version,
and operational metrics.

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| status | enum | Yes | Current health state: `UP`, `DOWN`, `STARTING` |
| version | string | Yes | Application version (semantic versioning) |
| uptime | string | Yes | ISO 8601 duration since service start (e.g., `PT1H30M`) |
| timestamp | string | Yes | ISO 8601 timestamp of health check |
| details | object | No | Additional health indicator details |

**Validation Rules**:
- `status` must be one of: `UP`, `DOWN`, `STARTING`
- `version` must follow semantic versioning format (e.g., `1.0.0`)
- `uptime` must be valid ISO 8601 duration
- `timestamp` must be valid ISO 8601 datetime with timezone

**State Transitions**:
```
STARTING → UP (when all health indicators pass)
STARTING → DOWN (when any critical health indicator fails)
UP → DOWN (when any critical health indicator fails)
DOWN → UP (when all health indicators recover)
```

**Example**:
```json
{
  "status": "UP",
  "version": "1.0.0",
  "uptime": "PT2H15M30S",
  "timestamp": "2025-12-24T10:30:00Z",
  "details": {
    "diskSpace": {
      "status": "UP",
      "total": 107374182400,
      "free": 53687091200
    }
  }
}
```

### HealthIndicator (Internal)

Represents an individual health check component. Not exposed directly via API but
aggregated into HealthStatus.details.

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| name | string | Yes | Indicator name (e.g., `diskSpace`, `db`) |
| status | enum | Yes | Individual status: `UP`, `DOWN`, `UNKNOWN` |
| details | object | No | Indicator-specific details |

**Built-in Indicators** (provided by Micronaut):
- `diskSpace`: Disk space availability
- `service`: Basic service liveness

## Metrics Data

The following metrics are exposed via the Prometheus endpoint (FR-011):

### HTTP Server Metrics

| Metric | Type | Labels | Description |
|--------|------|--------|-------------|
| `http_server_requests_seconds` | histogram | method, uri, status | Request latency distribution |
| `http_server_requests_seconds_count` | counter | method, uri, status | Total request count |
| `http_server_requests_seconds_sum` | counter | method, uri, status | Total request duration |

**Histogram Buckets** (configurable):
- 0.1s (100ms)
- 0.2s (200ms)
- 0.5s (500ms)

### JVM Metrics (built-in)

| Metric | Type | Description |
|--------|------|-------------|
| `jvm_memory_used_bytes` | gauge | Current memory usage |
| `jvm_threads_live_threads` | gauge | Current live threads |
| `process_uptime_seconds` | gauge | Application uptime |

## Configuration Schema

### application.yml Structure

```yaml
micronaut:
  application:
    name: tasklist-backend
  server:
    port: 8080

endpoints:
  health:
    enabled: true
    sensitive: false
    details-visible: ANONYMOUS
  prometheus:
    enabled: true
    sensitive: false
```

### Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| MICRONAUT_SERVER_PORT | No | 8080 | HTTP server port |
| MICRONAUT_APPLICATION_NAME | No | tasklist-backend | Application name |

## Layer Boundaries

### Domain Layer
- No entities in scaffolding phase (domain logic deferred to feature implementation)
- Package: `com.tasklist.domain`

### Application Layer
- No use cases in scaffolding phase
- Package: `com.tasklist.application`

### Infrastructure Layer
- HealthController (REST endpoint)
- Configuration classes
- Package: `com.tasklist.infrastructure`

## Future Considerations

When implementing task list features, the data model will expand to include:
- Task entity (domain)
- TaskList entity (domain)
- User entity (if authentication added)

This scaffolding establishes the package structure and architectural constraints that
future entities must follow.
