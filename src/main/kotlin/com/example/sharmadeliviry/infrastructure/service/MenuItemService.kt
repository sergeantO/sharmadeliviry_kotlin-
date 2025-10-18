package com.example.sharmadeliviry.presentation.service

import com.example.sharmadeliviry.domain.menuitem.MenuItemInterractor
import com.example.sharmadeliviry.domain.menuitem.MenuItemRepo
import org.springframework.stereotype.Service

@Service class MenuItemService(private val repo: MenuItemRepo) : MenuItemInterractor(repo) {}
