# syntax=docker/dockerfile:1

# ---------- Build-Stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Zuerst nur das POM kopieren -> Layer-Caching fuer Dependencies
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Danach den Quellcode kopieren und das Jar bauen
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---------- Runtime-Stage ----------
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Applikation nicht als root betreiben
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/fahrzeugverwaltung.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=5s --start-period=30s --retries=5 \
    CMD wget -q -O- http://localhost:8080/actuator/health | grep -q '"status":"UP"' || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
