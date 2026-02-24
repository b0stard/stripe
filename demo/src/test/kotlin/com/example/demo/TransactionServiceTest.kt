package com.example.demo


import com.example.demo.outbox.repository.OutboxRepository
import com.example.demo.payment.api.CreatePaymentRequest
import com.example.demo.payment.domain.PaymentStatus
import com.example.demo.payment.repository.TransactionRepository
import com.example.demo.payment.service.TransactionService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*


class TransactionServiceTest {

    private val transactionRepository: TransactionRepository = mock()
    private val outboxRepository: OutboxRepository = mock()

    private val service = TransactionService(transactionRepository, outboxRepository)

    @Test
    fun `should create payment successfully`() {
        val request = CreatePaymentRequest(
            amount = 100L,
            currency = "USD",
            idempotencyKey = "test key"
        )

        whenever(transactionRepository.save(any())).thenAnswer { it.arguments[0] }

        val result = service.createPayment(request)

        assertEquals(PaymentStatus.CREATED, result.status)
        verify(transactionRepository).save(any())
        verify(outboxRepository).save(any())
    }

    @Test
    fun `should throw exception when amount is negative`() {
        val request = CreatePaymentRequest(
            amount = -10L,
            currency = "USD",
            idempotencyKey = "test-key"
        )

        assertThrows(IllegalArgumentException::class.java) {
            service.createPayment(request)
        }

        verify(transactionRepository, never()).save(any())
        verify(outboxRepository, never()).save(any())
    }

}