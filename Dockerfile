# syntax=docker/dockerfile:1.10
FROM eclipse-temurin:25.0.3_9-jdk-alpine-3.23@sha256:5ecfde8e5ecde5954ea3721155b345ef56c1d579b940c761318ad4c05959a151 AS build

WORKDIR /workspace
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN --mount=type=cache,target=/root/.m2 \
    chmod +x mvnw \
    && ./mvnw --batch-mode --no-transfer-progress dependency:go-offline

COPY src/ src/
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw --batch-mode --no-transfer-progress package -DskipTests

FROM eclipse-temurin:25.0.3_9-jre-noble@sha256:2f1da100788559b397bcf48c736169ea5b070bde84e55f203bbee8e83d87a175

RUN apt-get update \
    && apt-get upgrade --yes \
    && apt-get install --yes --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --gid 10001 app \
    && useradd --uid 10001 --gid 10001 --no-create-home --home-dir /nonexistent \
        --shell /usr/sbin/nologin app
WORKDIR /app
COPY --from=build --chown=app:app /workspace/target/support-knowledge-base-*.jar app.jar

ENV SERVER_PORT=8080 \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"

USER 10001:10001
EXPOSE 8080

HEALTHCHECK --interval=10s --timeout=3s --start-period=25s --retries=5 \
  CMD curl --fail --silent --show-error --max-time 2 http://127.0.0.1:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
