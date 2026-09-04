# syntax=docker/dockerfile:1.10
FROM eclipse-temurin:26-jdk-alpine-3.24@sha256:e5b0a876436e5d1c8ff84dfd091cb530b83a9ed4273317d24acb673695bb7e0c AS build

WORKDIR /workspace
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN --mount=type=cache,target=/root/.m2 \
    chmod +x mvnw \
    && ./mvnw --batch-mode --no-transfer-progress dependency:go-offline

COPY src/ src/
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw --batch-mode --no-transfer-progress package -DskipTests

FROM eclipse-temurin:26.0.2_10-jre-noble@sha256:c12a27c567c4ce00b0caef14900c1bf2f5e997524c3ec73463ae695352d5f34d

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
