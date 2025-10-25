#!/bin/sh

echo "run docker container"
docker run -d --rm\
  --name my-postgres \
  -e POSTGRES_USER=myuser \
  -e POSTGRES_PASSWORD=mypassword \
  -e POSTGRES_DB=mydatabase \
  -p 5432:5432 \
  postgres:16.10-alpine

echo "wait 2 seconds to db run"
sleep 2

echo "run flywayMigrate"
./gradlew flywayMigrate

echo "run jooqCodegen"
./gradlew jooqCodegen

echo "DONE"