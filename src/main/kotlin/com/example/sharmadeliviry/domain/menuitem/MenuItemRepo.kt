package com.example.sharmadeliviry.domain.menuitem

interface MenuItemRepo {
    fun save(menuItem: MenuItemModel): MenuItemModel
    fun delete(menuItem: MenuItemModel): MenuItemModel
    fun update(menuItem: MenuItemModel): MenuItemModel
    fun get(id: Long): MenuItemModel?
    fun findAllBySection(menuSection: MenuSection): List<MenuItemModel>
}
