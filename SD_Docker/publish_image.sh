#!/bin/bash

cd ../ByteMarket/backend

# Image name and tag
IMAGE_NAME="webapp04"
IMAGE_TAG=$("./mvnw.cmd" help:evaluate -Dexpression=project.version -q -DforceStdout)

cd ../../SD_Docker

read -p "Docker Hub username: " DOCKER_USER

# Login in Docker Hub
docker login -u $DOCKER_USER

# Tag the image
docker tag $IMAGE_NAME:$IMAGE_TAG $DOCKER_USER/$IMAGE_NAME:$IMAGE_TAG

# Upload the image
docker push $DOCKER_USER/$IMAGE_NAME:$IMAGE_TAG