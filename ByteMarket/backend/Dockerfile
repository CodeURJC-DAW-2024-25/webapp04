# Imagen base para el contenedor de compilación
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Define el directorio de trabajo donde ejecutar comandos
WORKDIR /ByteMarket/backend/

# Copia las dependencias del proyecto
COPY pom.xml .

# Descarga las dependencias del proyecto
RUN mvn clean verify

# Copia el código del proyecto
COPY src/ ./src

# Compila proyecto
RUN mvn package -DskipTests



# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /ByteMarket/backend/

# Copy the jar file into the container
COPY --from=builder /ByteMarket/backend/target/*.jar ./webapp04.jar

# Expose the port the app runs on
EXPOSE 8443

# Define the command to run the app
ENTRYPOINT ["java", "-jar", "webapp04.jar"]
