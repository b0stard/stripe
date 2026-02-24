package com.example.demo.payment.api

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class CreatePaymentRequest(

    @field:Min(1)
    val amount: Long,

    @field:NotBlank
    val currency: String,

    @field:NotBlank
    val idempotencyKey: String?

)