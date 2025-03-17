# Imagen base para el contenedor de compilación
FROM maven:3.9.8-openjdk-21 AS builder

# Define el directorio de trabajo donde ejecutar comandos
WORKDIR /ByteMarket/backend/

# Copia las dependencias del proyecto
COPY ../ByteMarket/backend/pom.xml /

# Descarga las dependencias del proyecto
RUN mvn clean verify

# Copia el código del proyecto
COPY /src /webapp/src

# Compila proyecto
RUN mvn package -o



# Use an official OpenJDK runtime as a parent image
FROM openjdk:8-jre-slim

# Set the working directory
WORKDIR /ByteMarket/backend/

# Copy the jar file into the container
COPY /ByteMarket/backend/target/*.jar ./webapp04.jar

# Expose the port the app runs on
EXPOSE 8443

# Define the command to run the app
ENTRYPOINT ["java", "-jar", "webapp04.jar"]
