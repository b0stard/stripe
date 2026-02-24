package com.example.demo.outbox.scheduler

import com.example.demo.outbox.domain.OutboxEvent
import com.example.demo.outbox.domain.OutboxStatus
import com.example.demo.outbox.repository.OutboxRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledPublisher(
    private val outboxRepository: OutboxRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    private val log = LoggerFactory.getLogger(ScheduledPublisher::class.java)

    @Scheduled(fixedDelay = 5000)
    @Transactional
    fun publishEvents() {
        val events: List<OutboxEvent> =
            outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.NEW)

        if (events.isEmpty()) return

        log.info("Publishing ${events.size} outbox events")

        events.forEach { event ->
            try {
                kafkaTemplate.send(event.type, event.payload)
                event.status = OutboxStatus.SENT
                outboxRepository.save(event)
                log.info("Event ${event.id} sent successfully")
            } catch (ex: Exception) {
                log.error("Failed to send event ${event.id}: ${ex.message}")
                event.status = OutboxStatus.ERROR
                outboxRepository.save(event)
            }
        }
    }
}