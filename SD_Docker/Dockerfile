# Build Angular project 
FROM node:23-slim AS frontbuilder
WORKDIR /frontend
COPY frontend/ ./
RUN npm install
RUN npx ng build --configuration production --base-href="/new/" 

# Build Spring Boot project 
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /ByteMarket/backend/

# Copy and download dependencies
COPY /ByteMarket/backend/pom.xml .
RUN mvn clean verify

# Copy the project source code
COPY ByteMarket/backend/src/ ./src
COPY --from=frontbuilder /frontend/dist/frontend/browser/ ./src/main/resources/static/new

# Build the Spring Boot app
RUN mvn package -DskipTests

# Running the webapp
FROM eclipse-temurin:21-jre-alpine
WORKDIR /ByteMarket/backend/
COPY --from=builder /ByteMarket/backend/target/*.jar ./webapp04.jar
EXPOSE 8443
ENTRYPOINT ["java", "-jar", "webapp04.jar"]
