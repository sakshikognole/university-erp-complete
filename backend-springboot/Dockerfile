# ── Stage 1: Build with Gradle ─────────────────────────────
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# Copy everything at once — ensures src changes always invalidate cache
COPY . .

# Build the fat jar (--no-build-cache forces full recompile)
RUN gradle bootJar --no-daemon --no-build-cache

# ── Stage 2: Minimal production runtime ────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
