package net.earelin.tasklist.application.rest.health;

import io.micronaut.serde.annotation.Serdeable;

/**
 * Health check response DTO per contracts/health-api.yaml.
 */
@Serdeable
public record HealthResponse(
    HealthStatus status,
    String version,
    String uptime,
    String timestamp
) {}
