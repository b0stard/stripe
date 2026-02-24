package com.example.demo.payment.repository

import com.example.demo.payment.domain.Payment
import com.example.demo.payment.domain.PaymentStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface TransactionRepository : JpaRepository<Payment, UUID> {

    fun findByIdempotencyKey(idempotencyKey: String): Payment?

    fun findAllByStatus(status: PaymentStatus): List<Payment>

    fun findAllByStatusAndCreatedAtBefore(
        status: PaymentStatus,
        time: LocalDateTime
    ): List<Payment>
}