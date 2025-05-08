#!/bin/bash

cd ../ByteMarket/backend

# Compose name and tag
COMPOSE_NAME="bytemarket-compose"
COMPOSE_TAG=$("./mvnw.cmd" help:evaluate -Dexpression=project.version -q -DforceStdout)

cd ../../SD_Docker

read -p "Docker Hub username: " DOCKER_USER

# Login in Docker Hub
docker login -u $DOCKER_USER

# Publish the compose file
docker compose -f docker-compose.prod.yml publish $DOCKER_USER/$COMPOSE_NAME:$COMPOSE_TAG