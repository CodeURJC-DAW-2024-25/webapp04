#!/bin/bash

cd ../ByteMarket/backend

# Image name and tag
IMAGE_NAME="webapp04"
IMAGE_TAG=$("./mvnw.cmd" help:evaluate -Dexpression=project.version -q -DforceStdout)

cd ../../SD_Docker

#build image
docker build -t $IMAGE_NAME:$IMAGE_TAG -f ./Dockerfile ..