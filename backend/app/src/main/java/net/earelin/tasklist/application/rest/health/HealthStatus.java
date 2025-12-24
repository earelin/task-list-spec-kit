package net.earelin.tasklist.application.rest.health;

import io.micronaut.serde.annotation.Serdeable;

/**
 * Health status enum for health endpoint responses.
 */
@Serdeable
public enum HealthStatus {
    UP,
    DOWN
}
