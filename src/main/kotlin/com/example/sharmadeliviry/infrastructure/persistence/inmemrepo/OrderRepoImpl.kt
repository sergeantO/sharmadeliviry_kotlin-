package com.example.persistence.inmemrepo

import com.example.domain.menuitem.MenuItemModel
import com.example.domain.order.OrderModel
import com.example.domain.order.OrderRepo
import com.example.domain.order.OrderStatus
import com.example.domain.user.UserModel
import org.springframework.stereotype.Repository

@Repository
class OrderRepoImpl : OrderRepo {

    private val orders = mutableListOf<OrderModel>()

    override fun save(order: OrderModel): OrderModel {
        orders.add(order)
        return order
    }

    override fun delete(order: OrderModel): OrderModel {
        orders.remove(order)
        return order
    }

    override fun addItems(order: OrderModel, newMenuItems: List<MenuItemModel>): OrderModel {
        val foundIdx = orders.indexOfFirst { it.id == order.id }
        if (foundIdx != -1) {
            orders[foundIdx].itemList.addAll(0, newMenuItems)
        }
        return order
    }

    override fun cancel(order: OrderModel): OrderModel {
        val foundIdx = orders.indexOfFirst { it.id == order.id }

        if (foundIdx != -1) {
            val item = orders[foundIdx]
            item.status = OrderStatus.CANCELLED
        }
        return order
    }

    override fun findAllByUser(user: UserModel): List<OrderModel> {
        return orders.filter { it.user == user }
    }

    override fun findAllByStatus(orderStatus: OrderStatus): List<OrderModel> {
        return orders.filter { it.status == orderStatus }
    }

    override fun get(id: Long): OrderModel? {
        return orders.find { it.id == id }
    }
}
