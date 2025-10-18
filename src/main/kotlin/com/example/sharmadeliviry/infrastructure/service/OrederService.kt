package com.example.sharmadeliviry.presentation.service

import com.example.sharmadeliviry.domain.order.OrderInterractor
import com.example.sharmadeliviry.domain.order.OrderRepo
import org.springframework.stereotype.Service

@Service class OrderService(private val repo: OrderRepo) : OrderInterractor(repo) {}
