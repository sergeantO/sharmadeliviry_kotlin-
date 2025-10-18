package com.example.sharmadeliviry.domain.order

import com.example.sharmadeliviry.domain.menuitem.MenuItemModel
import com.example.sharmadeliviry.domain.user.UserModel

interface OrderRepo {
    fun save(order: OrderModel): OrderModel
    fun delete(order: OrderModel): OrderModel
    fun addItems(order: OrderModel, newMenuItems: List<MenuItemModel>): OrderModel
    fun cancel(order: OrderModel): OrderModel

    fun findAllByUser(user: UserModel): List<OrderModel>
    fun findAllByStatus(orderStatus: OrderStatus): List<OrderModel>

    fun get(id: Long): OrderModel?
}
