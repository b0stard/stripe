# Payment Service (Spring Boot + Outbox + Kafka Ready)

## 📌 О проекте

Учебный backend‑проект платежного сервиса с реализацией:

* Создания платежа
* Подтверждения платежа
* Отмены платежа
* Авто‑истечения (TTL 15 минут)
* Паттерна Outbox
* Unit и JPA тестов
* H2 для тестов
* Подготовки к Kafka интеграции

Проект написан на Kotlin + Spring Boot.

---

# 🏗 Архитектура

Проект разделён по слоям:

```
payment
 ├── api
 ├── domain
 ├── repository
 └── service

outbox
 ├── domain
 └── repository
```

## Слои:

### 1️⃣ Domain

Сущности:

* Payment
* OutboxEvent

Enums:

* PaymentStatus (CREATED, CONFIRMED, CANCELLED, FAILED, EXPIRED)

---

### 2️⃣ Repository

Spring Data JPA:

* TransactionRepository
* OutboxRepository

---

### 3️⃣ Service

Основная бизнес-логика — `TransactionService`.

Реализованы методы:

* `createPayment()`
* `confirmPayment()`
* `cancelPayment()`
* `getPayment()`

Логика включает:

* Валидацию входных данных
* Проверку допустимых переходов статусов
* TTL (15 минут до EXPIRED)
* Создание Outbox событий

---

# 🔄 Payment Flow

## Создание

1. Валидация запроса
2. Создание Payment (status = CREATED)
3. Сохранение в БД
4. Созение OutboxEvent

---

## Подтверждение

1. Проверка существования
2. Проверка TTL
3. Проверка статуса
4. Перевод в CONFIRMED
5. Создание события

---

## Отмена

Разрешена только из статуса CREATED.

---

# 📦 Outbox Pattern

При каждом изменении статуса создаётся запись в таблице `outbox_events`.

Это позволяет:

* Гарантировать доставку событий
* Позже публиковать их в Kafka
* Избежать distributed transactions

---

# 🧪 Тестирование

## Unit тесты

* TransactionServiceTest
* Используются Mockito моки
* Проверяется:

  * Валидация
  * Корректные переходы статусов
  * Вызовы repository

Unit тесты не поднимают Spring Context.

---

## JPA тесты

* TransactionRepositoryTest
* Используется H2 in‑memory
* ddl-auto=create-drop

Пример test datasource:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: create-drop
```

---

# 🗄 База данных

## Таблица payments

Поля:

* id (UUID)
* amount
* currency
* status
* idempotency_key (unique)
* created_at
* updated_at

---

## Таблица outbox_events

Поля:

* id
* type
* payload
* status (NEW, SENT, ERROR)
* created_at

---

# ⚙ Технологии

* Kotlin
* Spring Boot
* Spring Data JPA
* Hibernate
* H2 (tests)
* Mockito
* JUnit 5
* Kafka (готовность к интеграции)

---

# 🚀 Что уже сделано

✅ Бизнес‑логика платежей
✅ TTL истечения
✅ Outbox паттерн
✅ Unit тесты
✅ JPA тесты
✅ In‑memory БД
✅ Подготовка к Kafka

---

# 📈 Возможные улучшения

* Добавить REST Controller
* Добавить Integration тесты (@SpringBootTest)
* Подключить Testcontainers PostgreSQL
* Реализовать Kafka Publisher
* Добавить retry механизм для outbox
* Добавить Liquibase/Flyway
* Логирование через structured logging
* Метрики (Micrometer)

---

# 🎯 Цель проекта

Получить production‑ready skeleton микросервиса с:

* Чёткой архитектурой
* Разделением ответственности
* Тестируемостью
* Event‑driven readiness

---

# 👨‍💻 Автор

Backend learning project

---

# 🧠 Статус

Проект стабилен. Тесты проходят. Готов к расширению.

