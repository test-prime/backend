#!/bin/sh
mvn clean package
docker build --platform=linux/amd64 -t phamduchuy92/test-prime-backend:latest -f Dockerfile .
