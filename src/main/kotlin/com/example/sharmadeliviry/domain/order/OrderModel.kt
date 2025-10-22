package com.example.domain.order

import com.example.domain.menuitem.MenuItemModel
import com.example.domain.user.UserModel
import java.time.LocalDateTime

data class OrderModel(
        val id: Long,
        val createdAt: LocalDateTime,
        val closedAt: LocalDateTime,
        var status: OrderStatus,
        val user: UserModel,
        val itemList: MutableList<MenuItemModel>,
        val totlaPrice: Int,
)
