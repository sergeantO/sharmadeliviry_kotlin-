package com.example.domain.menuitem

open class MenuItemInterractor(private val repo: MenuItemRepo) {
    fun createMenuItemModel(menuItem: MenuItemModel): MenuItemModel = repo.save(menuItem)
    fun deleteMenuItemModel(menuItem: MenuItemModel): MenuItemModel = repo.delete(menuItem)
    fun updateMenuItemModel(menuItem: MenuItemModel): MenuItemModel = repo.update(menuItem)
    fun get(id: Long): MenuItemModel? = repo.get(id)
    fun findAllBySection(menuSection: MenuSection): List<MenuItemModel> =
            repo.findAllBySection(menuSection)
}
