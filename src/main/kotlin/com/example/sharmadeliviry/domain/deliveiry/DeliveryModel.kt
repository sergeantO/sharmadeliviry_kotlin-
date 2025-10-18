package com.example.sharmadeliviry.domain.delivery

import java.time.LocalDateTime
import org.springframework.core.annotation.Order

data class DeliveryModel(
        val id: Long,
        val order: Order,
        val createdAt: LocalDateTime,
        var startAt: LocalDateTime,
        var closedAt: LocalDateTime,
        val address: String,
)
