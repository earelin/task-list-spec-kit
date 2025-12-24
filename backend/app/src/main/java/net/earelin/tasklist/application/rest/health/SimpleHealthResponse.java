package net.earelin.tasklist.application.rest.health;

import io.micronaut.serde.annotation.Serdeable;

/**
 * Simple health response DTO for liveness and readiness endpoints.
 */
@Serdeable
public record SimpleHealthResponse(HealthStatus status) {}
