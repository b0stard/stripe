package com.example.demo.outbox.domain


import com.example.demo.payment.domain.Payment
import com.example.demo.payment.mapper.PaymentMapper
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "outbox_events")
class OutboxEvent(

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "CHAR(36)")
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var type: String = "",

    @Column(nullable = false, columnDefinition = "TEXT")
    var payload: String = "",

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    var status: OutboxStatus = OutboxStatus.NEW

) {
    constructor() : this(UUID.randomUUID(), "", "", LocalDateTime.now(), OutboxStatus.NEW)

    companion object {
        fun createPaymentCreated(payment: Payment) = OutboxEvent(
            type = "PAYMENT_CREATED",
            payload = PaymentMapper.toJson(payment)
        )

        fun createPaymentConfirmed(payment: Payment) = OutboxEvent(
            type = "PAYMENT_CONFIRMED",
            payload = PaymentMapper.toJson(payment)
        )

        fun createPaymentCancelled(payment: Payment) = OutboxEvent(
            type = "PAYMENT_CANCELLED",
            payload = PaymentMapper.toJson(payment)
        )

        fun createPaymentExpired(payment: Payment) = OutboxEvent(
            type = "PAYMENT_EXPIRED",
            payload = PaymentMapper.toJson(payment)
        )
    }
}
