# Build Angular project 
FROM node:23-slim AS frontbuilder
WORKDIR /frontend
COPY frontend/ ./
RUN npm install
RUN npx ng build --configuration production --base-href="/new/" 

# Base image for the build container 
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

# Set the working directory to execute commands
WORKDIR /ByteMarket/backend/

# Copy the project dependencies
COPY /ByteMarket/backend/pom.xml .

# Download the project dependencies
RUN mvn clean verify

# Copy the project source code
COPY ByteMarket/backend/src/ ./src
COPY --from=frontbuilder /frontend/dist/frontend/browser/ ./src/main/resources/static/new

# Build the Spring Boot app
RUN mvn package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /ByteMarket/backend/

# Copy the jar file into the container
COPY --from=builder /ByteMarket/backend/target/*.jar ./webapp04.jar

# Expose the application port
EXPOSE 8443

# Run the application
ENTRYPOINT ["java", "-jar", "webapp04.jar"]
