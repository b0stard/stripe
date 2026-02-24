package com.example.demo.payment.mapper

import com.example.demo.payment.domain.Payment
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Component
class PaymentMapper(

    private val objectMapper: ObjectMapper

) {

    fun toJson(payment: Payment): String = objectMapper.writeValueAsString(payment)

}
