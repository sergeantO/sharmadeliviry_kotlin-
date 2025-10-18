#!/bin/sh

docker run -it --rm -d -p 8180:8080 \
  -e "JAVA_OPTS=-Dapicurio.rest.mutability.artifact-version-content.enabled=true" \
  quay.io/apicurio/apicurio-registry:latest

docker run -it --rm -d  -p 8080:8080 \
  -e "APICURIO_UI_HUB_API_URL=http://localhost:8080/api" \
  quay.io/apicurio/apicurio-studio:latest

docker run -it --rm -d -p 8888:8080 \
  -e "APICURIO_UI_HUB_API_URL=http://localhost:8080/api" \
  quay.io/apicurio/apicurio-studio-ui:latest