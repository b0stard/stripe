package com.example.demo.kafka.model

import java.time.LocalDateTime
import java.util.UUID

data class PaymentEvent(
    val eventId: UUID = UUID.randomUUID(),
    val eventType: String,
    val paymentId: UUID,
    val amount: Long?,
    val currency: String?,
    val occurredAt: LocalDateTime = LocalDateTime.now()
)
