package com.example.presentation.service

import com.example.domain.order.OrderInterractor
import com.example.domain.order.OrderRepo
import org.springframework.stereotype.Service

@Service class OrderService(private val repo: OrderRepo) : OrderInterractor(repo) {}
