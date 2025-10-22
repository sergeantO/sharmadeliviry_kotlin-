# Memory Bank. TechContext.md

### Технологический стек

- **Kotlin** - язык разработки
- **Ktlint** - линтер для Kotlin
- **Spring** - фреймворк
- **Jooq** - типобезопасные SQL запросы  
- **Flyway** - миграции БД
- **Openapi-generator-kotlin** - генерация DTO из OpenAPI
- **JUnit 5** - тестирование

### Архитектура

Комбинация **гексагональной архитектуры** и **CQRS**:

- **Domain Layer** - чистая бизнес-логика, value-типы, интерфейсы Write Repository
- **Application Layer** - бизнес-процессы, команды/запросы, интерфейсы Read Repository  
- **Infrastructure Layer** - техническая реализация (контроллеры, репозитории)

### Contract Lock-In

Подход "запирание между контрактами":
- JOOQ генерирует типы из схемы БД
- OpenAPI Generator генерирует DTO из спецификации API  
- Domain Model остается чистым ядром

*Полное описание архитектуры и принципов в `systemPatterns.md`*  
*Процесс разработки и code style в `development.md`*
