# syntax=docker/dockerfile:1

# ----------------------------------------------------------------------------
# Build stage
# JDK 11 is REQUIRED: Lombok 1.18.12 (used by this project) crashes on newer
# JDKs. Do not bump this to 17/21 without first upgrading Lombok.
# ----------------------------------------------------------------------------
FROM maven:3.8.6-eclipse-temurin-11 AS build
WORKDIR /src

# Copy the reactor and build only grocery-web plus the modules it needs (-am).
COPY pom.xml ./
COPY grocery-data/pom.xml grocery-data/
COPY grocery-web/pom.xml grocery-web/
COPY grocery-data grocery-data
COPY grocery-web grocery-web

RUN mvn -B -ntp -pl grocery-web -am clean package -DskipTests

# ----------------------------------------------------------------------------
# Runtime stage: slim JRE only
# ----------------------------------------------------------------------------
FROM eclipse-temurin:11-jre
WORKDIR /app

COPY --from=build /src/grocery-web/target/grocery-web-*.jar app.jar

# Use the postgres profile by default (points at grocery_db:5432/grocery).
ENV SPRING_PROFILES_ACTIVE=postgres
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
