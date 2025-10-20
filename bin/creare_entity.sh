#!/bin/bash

create_dir() {
    local DIR=$1

    if [ ! -d "$DIR" ]; then
        mkdir -p "$DIR"
        echo "Directory $DIR created."
    fi
}

generate_file() {
    local TMPL_PATH=$1
    local TARGET_PATH=$2

    echo "create ${TARGET_PATH} from ${TMPL_PATH}"
    touch "$TARGET_PATH"
    cat "${TMPL_PATH}" | envsubst >> "$TARGET_PATH"
}


PACKAGE=$(./gradlew -q printGroup)
FULL_PATH=$(readlink -f "$0")
SCRIPT_DIR=$(dirname "$FULL_PATH")
PROJECT_PATH="${SCRIPT_DIR}/../src/main/kotlin/com/example/sharmadeliviry"

cd $PROJECT_PATH

echo -n "Enter entity name: "
read ENTITY_RAW

ENTITY_LOWERCASE="${ENTITY_RAW,,}"
ENTITY="${ENTITY_LOWERCASE^}"

export PACKAGE ENTITY_LOWERCASE ENTITY

create_dir "${PROJECT_PATH}/domain/${ENTITY_LOWERCASE}"
create_dir "${PROJECT_PATH}/application/${ENTITY_LOWERCASE}" 
create_dir "${PROJECT_PATH}/application/${ENTITY_LOWERCASE}/dto" 
create_dir "${PROJECT_PATH}/application/${ENTITY_LOWERCASE}/command"
create_dir "${PROJECT_PATH}/application/${ENTITY_LOWERCASE}/query" 
create_dir "${PROJECT_PATH}/infrastructure/persistence/${ENTITY_LOWERCASE}"

TMPL_PATH="${SCRIPT_DIR}/entity_templates"
generate_file "${TMPL_PATH}/domain/Model.tmpl"                              "${PROJECT_PATH}/domain/${ENTITY_LOWERCASE}/${ENTITY}Model.kt"
generate_file "${TMPL_PATH}/domain/WriteRepo.tmpl"                          "${PROJECT_PATH}/domain/${ENTITY_LOWERCASE}/${ENTITY}WriteRepo.kt"
generate_file "${TMPL_PATH}/application/ReadRepo.tmpl"                      "${PROJECT_PATH}/application/${ENTITY_LOWERCASE}/${ENTITY}ReadRepo.kt"
generate_file "${TMPL_PATH}/application/command/CreateCommand.tmpl"         "${PROJECT_PATH}/application/${ENTITY_LOWERCASE}/command/Create${ENTITY}Command.kt"
generate_file "${TMPL_PATH}/application/query/GetQuery.tmpl"                "${PROJECT_PATH}/application/${ENTITY_LOWERCASE}/query/Get${ENTITY}Query.kt"
generate_file "${TMPL_PATH}/infrastructure/controller/Controller.tmpl"      "${PROJECT_PATH}/infrastructure/controller/${ENTITY}Controller.kt"
generate_file "${TMPL_PATH}/infrastructure/persistence/ReadInMemRepo.tmpl"  "${PROJECT_PATH}/infrastructure/persistence/${ENTITY_LOWERCASE}/${ENTITY}ReadInMemRepo.kt"
generate_file "${TMPL_PATH}/infrastructure/persistence/ReadJooqRepo.tmpl"   "${PROJECT_PATH}/infrastructure/persistence/${ENTITY_LOWERCASE}/${ENTITY}ReadJooqRepo.kt"
generate_file "${TMPL_PATH}/infrastructure/persistence/WriteInMemRepo.tmpl" "${PROJECT_PATH}/infrastructure/persistence/${ENTITY_LOWERCASE}/${ENTITY}WriteInMemRepo.kt"
generate_file "${TMPL_PATH}/infrastructure/persistence/WriteJooqRepo.tmpl"  "${PROJECT_PATH}/infrastructure/persistence/${ENTITY_LOWERCASE}/${ENTITY}WriteJooqRepo.kt"
