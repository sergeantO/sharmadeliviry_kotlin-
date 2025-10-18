


```
src/main/kotlin/com/example/
├── shared/                          # Общие технические
│   ├── kernel/                      # Ядро (ID, Money, Email и т.д.)
│   │   ├── Id.kt
│   │   ├── Money.kt
│   │   └── Result.kt                # Either<Error, T>
│   └── error/                       # Общие ошибки
│       ├── AppError.kt
│       └── DomainError.kt
├── domain/                          # Чистый домен
│   ├── Order/
│   │   ├── OrderModel.kt
│   │   ├── OrderId.kt
│   │   └── OrderWriteRepository.kt
│   ├── Customer/
│   │   ├── CustomerModel.kt
│   │   ├── CustomerId.kt
│   │   └── CustomerWriteRepository.kt # Write интерфейс!
│   └── Product/
│       ├── ProductModel.kt
│       └── ProductWriteRepository.kt
├── application/
│   ├── Order/
│   │   ├── command/                 # Команды
│   │   ├── query/                   # Запросы
│   │   ├── OrderReadRepository.kt   # ReadRepo интерфейс!
│   │   └── dto/                     # Read модели
│   │       ├── OrderView.kt
│   │       └── OrderProjection.kt
│   ├── Customer/
│   │   ├── command/
│   │   ├── query/
│   │   └── dto/
│   └── Product/
│       ├── command/
│       ├── query/
│       └── dto/
└── infrastructure/
    ├── config/                      # Конфигурационные классы
    │  ├── DatabaseConfig.kt
    │  ├── JooqConfig.kt
    │  └── HandlerConfig.kt          # Регистрация обработчиков
    ├── persistence/                 # Реализации
    │   ├── JooqOrderWriteRepository.kt
    │   └── JooqOrderReadRepository.kt 
    └── web/                         # Контроллеры
        └── OrderController.kt 
```

Application Layer должен содержать БИЗНЕС-ЛОГИКУ

1. ВАЛИДАЦИЯ бизнес-правил
2. СОЗДАНИЕ доменного объекта
3. ВЫПОЛНЕНИЕ бизнес-процесса
4. СОХРАНЕНИЕ результата
5. ВЫЗОВ side effects (через порты!)

Infrastructure Layer - ТОЛЬКО техническая реализация