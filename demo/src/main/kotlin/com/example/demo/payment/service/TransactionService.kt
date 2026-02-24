package com.example.demo.payment.service

import com.example.demo.outbox.domain.OutboxEvent
import com.example.demo.outbox.repository.OutboxRepository
import com.example.demo.payment.api.CreatePaymentRequest
import com.example.demo.payment.domain.Payment
import com.example.demo.payment.domain.PaymentStatus
import com.example.demo.payment.repository.TransactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val outboxRepository: OutboxRepository
) {

    @Transactional
    fun createPayment(request: CreatePaymentRequest): Payment {

        validatePayment(request)

        val payment = Payment(
            amount = request.amount,
            currency = request.currency,
            status = PaymentStatus.CREATED,
            idempotencyKey =  request.idempotencyKey

        ).apply {
            id = UUID.randomUUID()
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }

        val savedPayment = transactionRepository.save(payment)

        val event = OutboxEvent.createPaymentCreated(savedPayment)
        outboxRepository.save(event)

        return savedPayment
    }

    @Transactional
    fun confirmPayment(paymentId: UUID): Payment {

        val payment = transactionRepository.findById(paymentId)
            .orElseThrow { IllegalArgumentException("Payment not found") }

        expireIfNeeded(payment)

        if (payment.status != PaymentStatus.CREATED) {
            throw IllegalArgumentException(
                "Payment cannot be confirmed from status ${payment.status}"
            )
        }

        payment.status = PaymentStatus.CONFIRMED
        payment.updatedAt = LocalDateTime.now()

        val updatedPayment = transactionRepository.save(payment)

        val event = OutboxEvent.createPaymentConfirmed(updatedPayment)
        outboxRepository.save(event)

        return updatedPayment
    }

    fun getPayment(id: UUID): Payment {

        val payment = transactionRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Payment not found") }

        expireIfNeeded(payment)

        return payment
    }

    private fun expireIfNeeded(payment: Payment) {
        val expiredAt = payment.createdAt.plusMinutes(15)

        if (payment.status == PaymentStatus.CREATED &&
            LocalDateTime.now().isAfter(expiredAt)
        ) {
            payment.status = PaymentStatus.EXPIRED
            payment.updatedAt = LocalDateTime.now()

            transactionRepository.save(payment)
            outboxRepository.save(
                OutboxEvent.createPaymentExpired(payment)
            )
        }
    }

    private fun validatePayment(request: CreatePaymentRequest) {
        if (request.amount <= 0) {
            throw IllegalArgumentException("Amount must be positive")
        }

        if (request.currency.isBlank()) {
            throw IllegalArgumentException("Currency is required")
        }
    }

    @Transactional
    fun cancelPayment(paymentId: UUID): Payment {
        val payment = transactionRepository.findById(paymentId)
            .orElseThrow { IllegalArgumentException("Payment not found") }
        if (payment.status != PaymentStatus.CREATED) {
            throw IllegalArgumentException("Only CREATED payments can be cancelled")
        }
        payment.status = PaymentStatus.CANCELLED
        payment.updatedAt = LocalDateTime.now()

        val cancelledPayment = transactionRepository.save(payment)
        outboxRepository.save(OutboxEvent.createPaymentCancelled(cancelledPayment))

        return cancelledPayment
    }
}