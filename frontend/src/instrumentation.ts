/**
 * Next.js Instrumentation for OpenTelemetry.
 * This file is automatically loaded by Next.js when instrumentationHook is enabled.
 *
 * Implements FR-017: Distributed tracing for request correlation.
 */

export async function register() {
  // Only register OpenTelemetry in Node.js runtime (server-side)
  if (process.env.NEXT_RUNTIME === "nodejs") {
    // Dynamically import to avoid bundling issues
    const { registerOTel } = await import("@vercel/otel");

    registerOTel({
      serviceName: process.env.OTEL_SERVICE_NAME || "task-list-frontend",
      // Additional configuration can be added here
      // See: https://vercel.com/docs/observability/otel-overview
    });
  }
}
