# ── Pre-built jar approach — no Maven download needed on Render ──
# Build locally with: .\gradlew.bat bootJar
# Then commit build/libs/*.jar and push to GitHub

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY build/libs/university-erp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9090

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
