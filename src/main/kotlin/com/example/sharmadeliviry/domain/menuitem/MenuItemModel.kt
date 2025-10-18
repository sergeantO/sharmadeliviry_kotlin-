package com.example.sharmadeliviry.domain.menuitem

data class MenuItemModel(
        val id: Long,
        val name: String,
        val menuSection: MenuSection,
        val price: Int
)
