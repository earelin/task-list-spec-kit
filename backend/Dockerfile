FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="earelin"
LABEL description="Task List Backend Service"

# Set shell to ash with pipefail for safe piping in RUN commands
SHELL ["/bin/ash", "-eo", "pipefail", "-c"]

# Create non-root user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

# Download and verify OpenTelemetry Java Agent
ARG OTEL_VERSION=2.13.0
ARG OTEL_SHA256=0af6e63e8e10fc8efa3e26daebbf7a78ad25c8c35f2e80f3abe2de82fd35fa4e
ADD --chmod=644 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_VERSION}/opentelemetry-javaagent.jar /tmp/opentelemetry-javaagent.jar
RUN echo "${OTEL_SHA256}  /tmp/opentelemetry-javaagent.jar" | sha256sum -c - && \
    mv /tmp/opentelemetry-javaagent.jar /app/opentelemetry-javaagent.jar

# Copy application jar
COPY --chown=appuser:appgroup app/build/libs/app-*-all.jar /app/application.jar

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD wget -qO- http://localhost:8080/health/liveness || exit 1

# Set JVM options
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run with OpenTelemetry agent
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -javaagent:/app/opentelemetry-javaagent.jar -jar /app/application.jar"]
