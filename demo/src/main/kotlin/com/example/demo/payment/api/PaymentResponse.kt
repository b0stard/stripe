package com.example.demo.payment.api

import com.example.demo.payment.domain.PaymentStatus
import java.time.LocalDateTime
import java.util.UUID

data class PaymentResponse(
    val id: UUID,
    val amount: Long,
    val currency: String,
    val status: PaymentStatus,
    val createdAt: LocalDateTime,
    val idempotencyKey: String?
)