package com.example.demo.outbox.domain

enum class OutboxStatus {
   NEW,
   SENT,
   ERROR
}