package net.earelin.tasklist.domain.health;

import java.time.Duration;
import java.time.Instant;

/**
 * Domain interface for providing application runtime information.
 * Used for health checks and monitoring.
 */
public interface ApplicationInfoProvider {

  /**
   * Returns the instant when the application was started.
   *
   * @return the application start time
   */
  Instant getStartTime();

  /**
   * Returns the duration since the application was started.
   *
   * @return the application uptime as a Duration
   */
  Duration getUptime();

  /**
   * Returns the application version.
   *
   * @return the semantic version string
   */
  String getVersion();
}
