# testing.md

## Принципы тестирования

### Именование тестов
- Описательные названия на естественном языке
- Шаблон: `should_[ожидаемое поведение]_when_[условия]`
- Группировка связанных тестов с помощью `@Nested`

### Test Doubles/InMemory реализации

Шаблон предоставляет InMemory реализации для тестирования:
```kotlin
class InMemoryOrderRepository : OrderWriteRepository {
    private val orders = mutableMapOf<OrderId, OrderModel>()
    
    override suspend fun save(order: OrderModel): Result<OrderModel> {
        orders[order.id] = order
        return Result.success(order)
    }
    
    override suspend fun findById(id: OrderId): Result<OrderModel?> {
        return Result.success(orders[id])
    }
}
```

### Что мокировать
✅ **Мокировать:**
- Внешние сервисы и API
- Базы данных (в интеграционных тестах)
- Сложные/медленные зависимости
- Сторонние библиотеки

❌ **Не мокировать:**
- Другие доменные классы (используйте компонентные тесты)
- Внутренние преобразования данных
- Pure functions

### Покрытие тестами
- **70-80%** - оптимальный показатель
- **Фокус на критической бизнес-логике**

## Структура тестов

### Unit-тесты доменного слоя
```kotlin
class OrderModelTest {
    
    @Test
    fun `should calculate total amount correctly`() {
        val order = createOrderWithItems(
            Item(price = Money.of(100, "USD"), quantity = 2),
            Item(price = Money.of(50, "USD"), quantity = 1)
        )
        
        assertEquals(Money.of(250, "USD"), order.calculateTotal())
    }
    
    @Test
    fun `should not allow cancellation of shipped order`() {
        val shippedOrder = createShippedOrder()
        
        assertFalse(shippedOrder.canBeCancelled())
    }
}
```

### Компонентные тесты бизнес-кейсов
```kotlin
@SpringBootTest
class CreateOrderHandlerTest {
    
    @Autowired
    private lateinit var handler: CreateOrderHandler
    
    @Test
    fun `should create order successfully`() {
        val command = CreateOrderCommand(items = listOf(orderItem))
        
        val result = handler.handle(command)
        
        assertTrue(result.isSuccess)
        result.onSuccess { order ->
            assertEquals(OrderStatus.CREATED, order.status)
        }
    }
    
    @Test
    fun `should fail when product not found`() {
        val command = CreateOrderCommand(items = listOf(invalidOrderItem))
        
        val result = handler.handle(command)
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ProductNotFoundError)
    }
}
```

### Интеграционные тесты репозиториев
```kotlin
@DataJpaTest
class JooqOrderRepositoryTest {
    
    @Autowired
    private lateinit var repository: JooqOrderWriteRepository
    
    @Test
    fun `should save and retrieve order`() {
        val order = createTestOrder()
        
        val savedOrder = repository.save(order)
        val retrievedOrder = repository.findById(savedOrder.id)
        
        assertEquals(savedOrder, retrievedOrder)
    }
}
```

### Использование в тестах
```kotlin
class OrderServiceTest {
    
    private val orderRepository = InMemoryOrderRepository()
    private val orderService = OrderService(orderRepository)
    
    @Test
    fun `should process order using inmemory repository`() {
        // Тест без поднятия базы данных
        val result = orderService.processOrder(createOrderCommand())
        
        assertTrue(result.isSuccess)
    }
}
```

## Антипаттерны тестирования

### ❌ Избегайте хрупких тестов
```kotlin
// ПЛОХО - тестирует внутреннее состояние
@Test
fun `should set internal flags correctly`() {
    val order = OrderModel()
    order.process()
    
    assertTrue(order.internalFlags.contains("PROCESSED")) // Хрупкий тест
}

// ХОРОШО - тестирует наблюдаемое поведение
@Test
fun `should change status after processing`() {
    val order = createOrder()
    
    order.process()
    
    assertEquals(OrderStatus.PROCESSED, order.status) // Стабильный тест
}
```

### ❌ Не дублируйте бизнес-логику в тестах
```kotlin
// ПЛОХО - копирование логики
@Test
fun `should calculate tax`() {
    val amount = 100.0
    val expectedTax = amount * 0.2 // Дублирование формулы
    
    val result = calculateTax(amount)
    
    assertEquals(expectedTax, result)
}

// ХОРОШО - известные ожидаемые значения
@Test
fun `should calculate tax`() {
    val result = calculateTax(100.0)
    
    assertEquals(20.0, result) // Явное ожидаемое значение
}
```

*Архитектурные основы в `systemPatterns.md`*  
*Инструменты разработки в `development.md`*
