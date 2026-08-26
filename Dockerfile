# Stage 1: Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build executable JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-root system user and pre-create the uploads folder with permissions
RUN addgroup -S spring && adduser -S spring -G spring && \
    mkdir -p /app/uploads && \
    chown -R spring:spring /app

USER spring:spring

# Copy JAR from the build stage
COPY --from=build --chown=spring:spring /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]