package net.earelin.tasklist.infrastructure.health;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import java.time.Duration;
import java.time.Instant;
import net.earelin.tasklist.domain.health.ApplicationInfoProvider;

/**
 * Micronaut-based implementation of ApplicationInfoProvider.
 * Provides application runtime information from Micronaut context.
 */
@Singleton
public class MicronautApplicationInfoProvider implements ApplicationInfoProvider {

  private final Instant startTime;
  private final String version;

  /**
   * Constructs a new MicronautApplicationInfoProvider.
   *
   * @param version the application version from configuration
   */
  public MicronautApplicationInfoProvider(
      @Value("${micronaut.application.version:1.0.0}") String version) {
    this.startTime = Instant.now();
    this.version = version;
  }

  @Override
  public Instant getStartTime() {
    return startTime;
  }

  @Override
  public Duration getUptime() {
    return Duration.between(startTime, Instant.now());
  }

  @Override
  public String getVersion() {
    return version;
  }
}
