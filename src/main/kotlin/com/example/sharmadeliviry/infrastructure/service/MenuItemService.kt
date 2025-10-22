package com.example.presentation.service

import com.example.domain.menuitem.MenuItemInterractor
import com.example.domain.menuitem.MenuItemRepo
import org.springframework.stereotype.Service

@Service class MenuItemService(private val repo: MenuItemRepo) : MenuItemInterractor(repo) {}
