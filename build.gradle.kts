import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

buildscript {
    dependencies {
        // без этого не работает миграция flyway
        classpath("org.flywaydb:flyway-core:11.14.+")
        classpath("org.flywaydb:flyway-database-postgresql:11.14.+")
        classpath("org.postgresql:postgresql:42.7.+")
    }
}

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.+"
    id("io.spring.dependency-management") version "1.1.+"
    id("org.jetbrains.kotlinx.kover") version "0.9.+"
    id("org.jooq.jooq-codegen-gradle") version "3.20.+"
    id("org.flywaydb.flyway") version "11.14.+"
    id("org.openapi.generator") version "7.16.+"
    id("org.jlleitschuh.gradle.ktlint") version "13.1.+"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Project for Service for Deliviry shawarma"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

// Добавляем сгенерированный код в исходные наборы проекта
// Я не поддерживаю идеалогию того, что сгенерированый код должен размещаться в build
sourceSets {
    this.main {
        kotlin.srcDir("$rootDir/generated/jooq")
        kotlin.srcDir("$rootDir/generated/api")
    }
}

// Очистка сгенерированных файлов при clean
tasks.clean {
    delete("generated")
}

dependencies {
    // base
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jooq:jooq-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // reactive
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    runtimeOnly("org.postgresql:r2dbc-postgresql")

    // DB
    implementation("org.flywaydb:flyway-core:11.14.+")
    implementation("org.flywaydb:flyway-database-postgresql:11.14.+")
    runtimeOnly("org.postgresql:postgresql:42.7.+")
    jooqCodegen("org.postgresql:postgresql:42.7.+")

    // to generated classes
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.0")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.39")
    implementation("io.swagger.core.v3:swagger-models:2.2.39")

    // TESTS
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Настраиваем компилятор Kotlin для всего основного кода
// Применяется ко всем исходным файлам Kotlin, включая сгенерированные jooq и openApi
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("spring.profiles.active", "test")
}

// Отчет о покрытии будет генерироваться всегда после запуска тестов
tasks.test {
    finalizedBy(tasks.koverXmlReport)
}

ktlint {
    android.set(false)
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    filter {
        exclude("**/generated/**")
    }
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.JSON)
    }
    // see: https://pinterest.github.io/ktlint/1.7.1/rules/standard/
    // see: https://pinterest.github.io/ktlint/1.7.1/rules/configuration-ktlint/
    additionalEditorconfig.set(
        mapOf(
            "indent_size" to "4",
            "indent_style" to "space",
            "max_line_length" to "120",
            "ij_kotlin_imports_layout" to
                "java.**,javax.**,kotlin.**,kotlinx.**,org.springframework.**,|,org.junit.**,|,*,^",
            "ij_kotlin_packages_to_use_import_on_demand" to "java.util.*,org.springframework.web.bind.annotation.*",
        ),
    )
}

// TODO: вынести в .env
val dbUrl = "jdbc:postgresql://localhost:5432/mydatabase"
val dbUser = "myuser"
val dbPassword = "mypassword"

flyway {
    url = dbUrl
    user = dbUser
    password = dbPassword
    baselineOnMigrate = true
}

jooq {
    configuration {
        jdbc {
            driver = "org.postgresql.Driver"
            url = dbUrl
            user = dbUser
            password = dbPassword
        }
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                inputSchema = "public"
            }
            target {
                packageName = "org.example.generated.jooq"
                directory = "$rootDir/generated/jooq"
            }
            generate {
                isPojosAsKotlinDataClasses = true
                // без этого все поля будут nullable
                kotlinNotNullPojoAttributes = true
                kotlinNotNullRecordAttributes = true
                kotlinNotNullInterfaceAttributes = true
            }
        }
    }
}

openApiGenerate {
    generatorName = "kotlin-spring"
    inputSpec = "$rootDir/src/main/resources/sharmadeliviry_kotlin_openapi.json"
    outputDir = "$rootDir/generated/api"
    apiPackage = "com.example.generated.api"
    modelPackage = "com.example.generated.model"
    configOptions =
        mapOf(
            "reactive" to "true",
            "interfaceOnly" to "true",
            "skipDefaultInterface" to "true",
            "useSpringBoot3" to "true",
            "useTags" to "true",
        )
}

// компиляция зависит от генерации openApi и jooqCodegen
// tasks.named("compileKotlin") {
//     dependsOn("openApiGenerate")
//     dependsOn(tasks.filter { it.name.startsWith("jooqCodegen") }.toTypedArray())
// }

tasks.register("printGroup") {
    doLast {
        println(project.group)
    }
}
