
# Memory Bank. systemPatterns.md

## Архитектурный подход

Комбинация **гексагональной архитектуры** и **CQRS** для создания расширяемых и адаптивных систем.

### Гексагональная архитектура
- **Domain Layer** - чистое ядро бизнес-логики
- **Application Layer** - оркестрация бизнес-процессов  
- **Infrastructure Layer** - техническая реализация

### CQRS (Command Query Responsibility Segregation)
- Разделение операций записи (Commands) и чтения (Queries)
- **Commands** - изменение состояния, работают с Domain Model
- **Queries** - чтение данных, возвращают DTO/Projections

## Contract Lock-In

Ключевой подход "запирание между контрактами", комбинирующий преимущества contract-first и model-first разработки.

### Трехконтурная типобезопасность
```
Database Schema → JOOQ Generated Types
    ↓
Domain Model (Чистое ядро)
    ↓  
API Specification → OpenAPI Generated DTO
```

### Структура проекта
```
src/main/
├── kotlin/com/example/
│   ├── shared/                          # Общие технические компоненты
│   │   ├── kernel/                      # Ядро системы
│   │   │   ├── Id.kt                    # Value-типы
│   │   └── error/                       # Иерархия ошибок
│   │       ├── AppError.kt
│   │       └── DomainError.kt
│   ├── domain/                          # Чистая бизнес-логика
│   │   ├── Order/
│   │   │   ├── OrderModel.kt
│   │   │   ├── OrderId.kt
│   │   │   └── OrderWriteRepository.kt  # Интерфейс для записи
│   │   ├── Customer/
│   │   └── Product/
│   ├── application/                     # Use Cases & бизнес-процессы
│   │   ├── Order/
│   │   │   ├── command/                 # Команды и обработчики
│   │   │   ├── query/                   # Запросы и обработчики  
│   │   │   ├── OrderReadRepository.kt   # Интерфейс для чтения
│   │   │   └── dto/                     # Модели для чтения
│   │   │       ├── OrderView.kt
│   │   │       └── OrderProjection.kt
│   │   ├── Customer/
│   │   └── Product/
│   └── infrastructure/                  # Техническая реализация
│       ├── config/
│       │  ├── DatabaseConfig.kt
│       │  ├── JooqConfig.kt
│       │  └── HandlerConfig.kt          # Регистрация обработчиков
│       ├── persistence/
│       │   ├── JooqOrderWriteRepository.kt
│       │   └── JooqOrderReadRepository.kt 
│       └── web/
│           └── OrderController.kt 
└── resources/
    ├── db/migration                     # Миграции Flyway
    └── openapi.json                     # API спецификация
```

## Структура слоев

### Domain Layer
**Ответственность:** Чистая бизнес-логика
```kotlin
// Value-типы вместо примитивов
val orderId: OrderId = OrderId(1L)
val amount: Money = Money.of("100.00", "USD")

// Бизнес-правила и инварианты
class OrderModel {
    fun canBeCancelled(): Boolean
    fun calculateTotal(): Money
}

// Интерфейсы для записи
interface OrderWriteRepository {
    suspend fun save(order: OrderModel): Result<OrderModel>
}
```

### Application Layer  
**Ответственность:** Оркестрация бизнес-процессов
1. Валидация входных данных
2. Создание доменных объектов
3. Выполнение бизнес-процесса
4. Сохранение результата
5. Вызов side effects

```kotlin
// Команды
class CreateOrderCommand(val items: List<OrderItem>)
class CancelOrderCommand(val orderId: OrderId)

// Запросы  
class GetOrderQuery(val orderId: OrderId)
class FindOrdersQuery(val status: OrderStatus)

// Read Repository для оптимизированных запросов
interface OrderReadRepository {
    suspend fun findById(id: OrderId): Result<OrderView?>
}
```

### Infrastructure Layer
**Ответственность:** Техническая реализация
- Контроллеры (адаптеры для внешнего мира)
- Репозитории (реализация портов)
- Конфигурация
- Маппинг между внешними и внутренними моделями

## Ключевые технические решения

### Разделение Read/Write Repository
- **Write Repository** в Domain Layer - для команд
- **Read Repository** в Application Layer - для запросов

**Обоснование:** Информация о том, что именно требуется клиенту - это знание о бизнес-кейсе, а не о доменной модели.

### Обработка ошибок через Result
```kotlin
// Явная обработка вместо исключений
fun createOrder(command: CreateOrderCommand): Result<OrderModel, DomainError>

// Предсказуемый поток выполнения
val result = createOrder(command)
result.fold(
    onSuccess = { order -> ... },
    onFailure = { error -> ... }
)
```

### Моделирование домена
- Value-классы для идентификаторов и денежных сумм
- Data-классы для неизменяемых моделей
- Функции расширения для маппинга
- Приватные конструкторы для контроля инвариантов

*Технические детали и настройки в `techContext.md`*  
*Процесс разработки и инструменты в `development.md`*
