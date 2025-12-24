package net.earelin.tasklist.application.rest.health;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

/**
 * Tests for the health endpoint verifying FR-001, FR-002, and SC-001.
 */
@MicronautTest
class HealthControllerTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Test
  void healthEndpointReturns200() {
    HttpResponse<HealthResponse> response = client.toBlocking()
        .exchange(HttpRequest.GET("/health"), HealthResponse.class);

    assertEquals(HttpStatus.OK, response.getStatus());
  }

  @Test
  void healthEndpointContainsRequiredFields() {
    HealthResponse response = client.toBlocking()
        .retrieve(HttpRequest.GET("/health"), HealthResponse.class);

    assertNotNull(response.status(), "Response must contain status");
    assertNotNull(response.version(), "Response must contain version");
    assertNotNull(response.uptime(), "Response must contain uptime");
    assertNotNull(response.timestamp(), "Response must contain timestamp");
  }

  @Test
  void healthEndpointReturnsUpStatus() {
    HealthResponse response = client.toBlocking()
        .retrieve(HttpRequest.GET("/health"), HealthResponse.class);

    assertEquals(HealthStatus.UP, response.status());
  }

  @Test
  void healthEndpointReturnsValidVersion() {
    HealthResponse response = client.toBlocking()
        .retrieve(HttpRequest.GET("/health"), HealthResponse.class);

    String version = response.version();
    assertNotNull(version);
    assertTrue(version.matches("\\d+\\.\\d+\\.\\d+.*"),
        "Version should follow semantic versioning pattern");
  }

  @Test
  void healthEndpointReturnsValidUptime() {
    HealthResponse response = client.toBlocking()
        .retrieve(HttpRequest.GET("/health"), HealthResponse.class);

    String uptime = response.uptime();
    assertNotNull(uptime);
    assertTrue(uptime.startsWith("PT"),
        "Uptime should be ISO 8601 duration format");
  }

  @Test
  void healthEndpointReturnsValidTimestamp() {
    HealthResponse response = client.toBlocking()
        .retrieve(HttpRequest.GET("/health"), HealthResponse.class);

    String timestamp = response.timestamp();
    assertNotNull(timestamp);
    assertTrue(timestamp.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*Z"),
        "Timestamp should be ISO 8601 format");
  }

  @Test
  void healthEndpointResponseTimeUnder100ms() {
    long startTime = System.currentTimeMillis();

    client.toBlocking().retrieve(HttpRequest.GET("/health"), HealthResponse.class);

    long responseTime = System.currentTimeMillis() - startTime;
    assertTrue(responseTime < 100,
        "Response time should be under 100ms, was: " + responseTime + "ms");
  }

  @Test
  void livenessEndpointReturnsUp() {
    SimpleHealthResponse response = client.toBlocking()
        .retrieve(HttpRequest.GET("/health/liveness"), SimpleHealthResponse.class);

    assertEquals(HealthStatus.UP, response.status());
  }

  @Test
  void readinessEndpointReturnsStatus() {
    SimpleHealthResponse response = client.toBlocking()
        .retrieve(HttpRequest.GET("/health/readiness"), SimpleHealthResponse.class);

    assertNotNull(response.status());
  }
}
