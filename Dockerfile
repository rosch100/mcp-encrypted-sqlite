# Multi-stage build for MCP SQLite Server
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY libs libs

# Copy source code
COPY src src

# Download dependencies and build
RUN chmod +x gradlew && \
    ./gradlew downloadSqliteJdbc && \
    ./gradlew build -x test && \
    ./gradlew installDist

# Runtime image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built application
COPY --from=builder /build/build/install/mcp-sqlite /app/mcp-sqlite

# Create a non-root user
RUN addgroup -S mcp && adduser -S mcp -G mcp && \
    chown -R mcp:mcp /app

USER mcp

# Set the entrypoint
ENTRYPOINT ["/app/mcp-sqlite/bin/mcp-sqlite"]

