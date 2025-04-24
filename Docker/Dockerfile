# Stage 1: Build Angular frontend
FROM node:22-slim AS frontend-build
WORKDIR /frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build -- --configuration production --base-href=/spa/

# Stage 2: Build Spring Boot backend with Maven
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Cache dependencies to speed up builds
COPY ByteMarket/backend/pom.xml ./
RUN mvn dependency:go-offline

# Copy source code and compiled frontend
COPY ByteMarket/backend/src /app/src
COPY --from=frontend-build /frontend/dist/frontend/browser/ /app/src/main/resources/static/spa

# Build the Spring Boot app
RUN mvn clean package -DskipTests

# Stage 3: Final image with Java and required fonts
FROM eclipse-temurin:21-jre

# Install fonts for PDF/chart generation
RUN apt-get update && apt-get install -y \
    libfreetype6 \
    libfontconfig1 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy the final JAR from builder stage
COPY --from=builder /app/target/*.jar webapp04.jar

# Expose application port
EXPOSE 8443

# Run the application
ENTRYPOINT ["java", "-jar", "webapp04.jar"]
