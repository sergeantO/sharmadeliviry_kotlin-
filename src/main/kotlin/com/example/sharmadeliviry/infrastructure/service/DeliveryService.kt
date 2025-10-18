package com.example.sharmadeliviry.presentation.service

import com.example.sharmadeliviry.domain.delivery.DeliveryInterractor
import com.example.sharmadeliviry.domain.delivery.DeliveryRepo
import org.springframework.stereotype.Service

@Service class DeliveryService(private val repo: DeliveryRepo) : DeliveryInterractor(repo)
