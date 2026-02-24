package com.example.demo.common

import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime
import java.util.UUID

@MappedSuperclass
abstract class BaseEntity(

    @Id
    @Column(nullable = false, updatable = false)
    open var id: UUID = UUID.randomUUID(),

    @Column(nullable = false, updatable = false)
    open var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    open var updatedAt: LocalDateTime = LocalDateTime.now()
)