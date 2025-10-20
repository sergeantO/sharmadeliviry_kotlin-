#!/bin/sh

docker run -d --rm\
  --name my-postgres \
  -e POSTGRES_USER=myuser \
  -e POSTGRES_PASSWORD=mypassword \
  -e POSTGRES_DB=mydatabase \
  -p 5432:5432 \
  postgres:16.10-alpine

echo "wait db run"
sleep 5

../gradlew flywayMigrate
../gradlew jooqCodegen

echo "DONE"