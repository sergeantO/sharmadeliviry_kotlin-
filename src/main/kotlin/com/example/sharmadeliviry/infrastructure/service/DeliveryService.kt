package com.example.presentation.service

import com.example.domain.delivery.DeliveryInterractor
import com.example.domain.delivery.DeliveryRepo
import org.springframework.stereotype.Service

@Service class DeliveryService(private val repo: DeliveryRepo) : DeliveryInterractor(repo)
