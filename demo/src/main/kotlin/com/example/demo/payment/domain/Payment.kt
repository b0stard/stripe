package com.example.demo.payment.domain

import com.example.demo.common.BaseEntity
import com.example.demo.payment.domain.PaymentStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "payments")
class Payment(


    @Column(nullable = false)
    var amount: Long,

    @Column(nullable = false, length = 3)
    var currency: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: PaymentStatus,

    @Column(unique = true)
    var idempotencyKey: String?

) : BaseEntity()