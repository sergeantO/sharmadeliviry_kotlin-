package com.example.sharmadeliviry.domain.order

import com.example.sharmadeliviry.domain.menuitem.MenuItemModel
import com.example.sharmadeliviry.domain.user.UserModel
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
