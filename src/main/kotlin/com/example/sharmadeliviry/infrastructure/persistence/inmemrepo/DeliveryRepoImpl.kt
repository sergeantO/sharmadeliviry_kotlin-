package com.example.persistence.inmemrepo

import com.example.domain.delivery.DeliveryModel
import com.example.domain.delivery.DeliveryRepo
import java.time.LocalDateTime
import org.springframework.stereotype.Repository

@Repository
class DeliveryRepoImpl : DeliveryRepo {

    private val deliveryList = mutableListOf<DeliveryModel>()

    override fun save(delivery: DeliveryModel): DeliveryModel {
        deliveryList.add(delivery)
        return delivery
    }

    override fun delete(delivery: DeliveryModel): DeliveryModel {
        deliveryList.remove(delivery)
        return delivery
    }

    override fun start(delivery: DeliveryModel): DeliveryModel {
        val found = deliveryList.find { it.id == delivery.id }
        if (found != null) {
            found.startAt = LocalDateTime.now()
        }
        return delivery
    }

    override fun finish(delivery: DeliveryModel): DeliveryModel {
        val found = deliveryList.find { it.id == delivery.id }
        if (found != null) {
            found.closedAt = LocalDateTime.now()
        }
        return delivery
    }

    override fun get(id: Long): DeliveryModel? {
        return deliveryList.find { it.id == id }
    }
}
