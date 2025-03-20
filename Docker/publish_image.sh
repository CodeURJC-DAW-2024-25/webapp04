#!/bin/bash

# Image name and tag
IMAGE_NAME="webapp04"
IMAGE_TAG="latest"

read -p "Docker Hub username: " DOCKER_USER

# Iniciar sesión en Docker Hub
docker login -u $DOCKER_USER

# Etiquetar la imagen
docker tag $IMAGE_NAME:$IMAGE_TAG $DOCKER_USER/$IMAGE_NAME:$IMAGE_TAG

# Subir la imagen
docker push $DOCKER_USER/$IMAGE_NAME:$IMAGE_TAG