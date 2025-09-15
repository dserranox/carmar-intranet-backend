# ---- build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Copy only pom first for faster layer caching
COPY pom.xml ./
# Pre-fetch dependencies (will use a dummy src to let maven resolve plugins)
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests dependency:go-offline || true

# Copy sources
COPY . /app
# Build the jar
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests package

# ---- runtime stage ----
FROM eclipse-temurin:21-jre
ENV TZ=America/Argentina/Cordoba \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Duser.timezone=America/Argentina/Cordoba"
WORKDIR /opt/app
# Copy the fat jar from the build stage (Spring Boot repackage output)
# adjust name if necessary
COPY --from=build /app/target/*.jar /opt/app/app.jar

# Non-root user for safety
RUN useradd -r -u 10001 -g root appuser && chown -R appuser:root /opt/app
USER appuser

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=5 \
  CMD wget -qO- http://localhost:8080/manage/health || exit 1

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /opt/app/app.jar"]
