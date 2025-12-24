package net.earelin.tasklist.application.rest.health;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import net.earelin.tasklist.domain.health.ApplicationInfoProvider;

/**
 * Custom health endpoint that provides version and uptime information
 * per contracts/health-api.yaml.
 */
@Controller("/health")
public class HealthController {

  private final ApplicationInfoProvider applicationInfoProvider;

  /**
   * Constructs a new HealthController.
   *
   * @param applicationInfoProvider the application info provider
   */
  @Inject
  public HealthController(ApplicationInfoProvider applicationInfoProvider) {
    this.applicationInfoProvider = applicationInfoProvider;
  }

  /**
   * Returns the full health status of the application.
   *
   * @return the health response with status, version, uptime, and timestamp
   */
  @Get
  public HealthResponse health() {
    return new HealthResponse(
        HealthStatus.UP,
        applicationInfoProvider.getVersion(),
        formatUptime(applicationInfoProvider.getUptime()),
        formatTimestamp(Instant.now())
    );
  }

  /**
   * Returns the liveness probe status.
   *
   * @return a simple health response indicating the application is alive
   */
  @Get("/liveness")
  public SimpleHealthResponse liveness() {
    return new SimpleHealthResponse(HealthStatus.UP);
  }

  /**
   * Returns the readiness probe status.
   *
   * @return a simple health response indicating the application is ready
   */
  @Get("/readiness")
  public SimpleHealthResponse readiness() {
    return new SimpleHealthResponse(HealthStatus.UP);
  }

  private String formatUptime(Duration uptime) {
    return uptime.toString();
  }

  private String formatTimestamp(Instant timestamp) {
    return DateTimeFormatter.ISO_INSTANT.format(timestamp.atOffset(ZoneOffset.UTC));
  }
}
