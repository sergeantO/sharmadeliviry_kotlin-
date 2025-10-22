package com.example.domain.delivery

open class DeliveryInterractor(private val repo: DeliveryRepo) {
    fun createDelivery(delivery: DeliveryModel): DeliveryModel = repo.save(delivery)
    fun deleteDelivery(delivery: DeliveryModel): DeliveryModel = repo.delete(delivery)
    fun start(delivery: DeliveryModel): DeliveryModel = repo.start(delivery)
    fun finish(delivery: DeliveryModel): DeliveryModel = repo.finish(delivery)
    fun get(id: Long): DeliveryModel? = repo.get(id)
}
