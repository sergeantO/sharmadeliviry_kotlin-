package com.example.domain.delivery

interface DeliveryRepo {
    fun save(delivery: DeliveryModel): DeliveryModel
    fun delete(delivery: DeliveryModel): DeliveryModel
    fun start(delivery: DeliveryModel): DeliveryModel
    fun finish(delivery: DeliveryModel): DeliveryModel
    fun get(id: Long): DeliveryModel?
}
