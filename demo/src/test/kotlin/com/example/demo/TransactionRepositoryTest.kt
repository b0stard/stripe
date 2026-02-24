package com.example.demo

import com.example.demo.payment.repository.TransactionRepository
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import com.example.demo.payment.domain.Payment
import com.example.demo.payment.domain.PaymentStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    lateinit var repository: TransactionRepository

    @Test
    fun `should save and find payment`() {
        val payment = Payment(
            amount = 100L,
            currency = "EUR",
            idempotencyKey = "test key",
            status = PaymentStatus.CREATED,

            )

        repository.save(payment)

        val found = repository.findById(payment.id)

        assertEquals(true, found.isPresent)
        assertEquals("EUR", found.get().currency)
    }
}