# Memory Bank. development.md

## Процесс разработки

### Минималистичные контроллеры

Контроллеры содержат только логику маршрутизации и преобразования ответов:

```kotlin
@RestController
class OrderController : OrderApi {
    
    override suspend fun createOrder(request: CreateOrderRequest): ResponseEntity<Order> {
        return orderService.createOrder(request)
            .let { responseMapper.toResponseEntity(it) }
    }
    
    override suspend fun getOrder(id: Long): ResponseEntity<Order> {
        return orderService.getOrder(OrderId(id))
            .let { responseMapper.toResponseEntity(it) }
    }
}
```

### Унифицированная обработка ответов

**ResponseMapper** - преобразует Result<T> в ResponseEntity:
```kotlin
@Component
class ResponseMapper(private val exceptionMapper: ExceptionMapper) {
    
    fun <T> Result<T>.toResponseEntity(): ResponseEntity<T> {
        return fold(
            onSuccess = { ResponseEntity.ok(it) },
            onFailure = { exceptionMapper.mapException(it) }
        )
    }
}
```

**ExceptionMapper** - централизованная обработка ошибок:
```kotlin
@Component
class ExceptionMapper {
    fun <T> mapException(exception: Throwable): ResponseEntity<T> {
        return when (exception) {
            is NotFoundException -> ResponseEntity.notFound().build()
            is ValidationException -> ResponseEntity.badRequest().build()
            else -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
```

## Вспомогательные скрипты

### create_entity.sh
Генерирует базовые заготовки для новой сущности:
- Domain модель и value-типы
- Интерфейсы репозиториев (Read/Write)
- Команды, запросы и обработчики
- InMemory реализации для тестов

### run_apicurio.sh
Запускает Apicurio для разработки OpenAPI спецификации:
- Локальный сервер редактора спецификаций
- Визуальное проектирование API
- Экспорт в JSON/YAML

### run_pg.sh
Запускает PostgreSQL в Docker:
- Готовая к работе база данных
- Автоматическая настройка

### run_openapi_editor.sh
Альтернативный редактор OpenAPI для тонкой настройки спецификаций.

## Code Style и стандарты

### Типобезопасность
```kotlin
// Вместо примитивов - доменные типы
val orderId: OrderId = OrderId(1L)        // вместо Long
val amount: Money = Money.of(100, "USD")  // вместо BigDecimal
```

### Именование
- **Commands** - CreateOrderCommand, CancelOrderCommand  
- **Queries** - GetOrderQuery, FindOrdersQuery
- **DTO** - OrderResponse, CreateOrderRequest

## Рекомендации по разработке

### При добавлении новой функциональности:
1. Обновить OpenAPI спецификацию
2. Сгенерировать DTO через openapi-generator
3. Создать заготовки через create_entity.sh
4. Реализовать бизнес-логику в Domain слое
5. Добавить обработчики в Application слое
6. Реализовать репозитории в Infrastructure

### При рефакторинге:
- Изменения в доменной модели требуют обновления маппингов
- Изменения API требуют обновления спецификации
- Компилятор укажет на все несоответствия типов

*Архитектурные детали в `systemPatterns.md`*  
*Организация тестирования в `testing.md`*
