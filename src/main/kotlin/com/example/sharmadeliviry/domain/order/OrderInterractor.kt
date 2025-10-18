package com.example.sharmadeliviry.domain.order

import com.example.sharmadeliviry.domain.menuitem.MenuItemModel
import com.example.sharmadeliviry.domain.user.UserModel

open class OrderInterractor(private val repo: OrderRepo) {
        fun create(order: OrderModel): OrderModel = repo.save(order)
        fun delete(order: OrderModel): OrderModel = repo.delete(order)
        fun addItems(order: OrderModel, newMenuItems: List<MenuItemModel>): OrderModel =
                repo.addItems(order, newMenuItems)
        fun cancel(order: OrderModel): OrderModel = repo.cancel(order)

        fun findAllByUser(user: UserModel): List<OrderModel> = repo.findAllByUser(user)
        fun findAllByStatus(orderStatus: OrderStatus): List<OrderModel> =
                repo.findAllByStatus(orderStatus)

        fun get(id: Long): OrderModel? = repo.get(id)
}
