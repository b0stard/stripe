package com.example.demo.payment.mapper

import com.example.demo.payment.domain.Payment
import tools.jackson.databind.ObjectMapper

object PaymentMapper {

    private val mapper: ObjectMapper = ObjectMapper()

    fun toJson(payment: Payment): String {
        return mapper.writeValueAsString(payment)
    }
}