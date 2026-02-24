package com.example.demo.outbox.repository

import com.example.demo.outbox.domain.OutboxEvent
import com.example.demo.outbox.domain.OutboxStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OutboxRepository : JpaRepository<OutboxEvent, UUID> {

    fun findTop100ByStatusOrderByCreatedAtAsc(
        status: OutboxStatus
    ): List<OutboxEvent>

}