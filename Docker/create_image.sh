#!/bin/bash

#Image name and tag
IMAGE_NAME="webapp04"
IMAGE_TAG="latest"

#build image
docker build -t $IMAGE_NAME:$IMAGE_TAG -f Dockerfile ..