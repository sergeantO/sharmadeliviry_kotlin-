package com.example.sharmadeliviry.persistence.inmemrepo

import com.example.sharmadeliviry.domain.menuitem.MenuItemModel
import com.example.sharmadeliviry.domain.menuitem.MenuItemRepo
import com.example.sharmadeliviry.domain.menuitem.MenuSection
import org.springframework.stereotype.Repository

@Repository
class MenuItemRepoImpl : MenuItemRepo {

    private val items = mutableListOf<MenuItemModel>()

    override fun save(menuItem: MenuItemModel): MenuItemModel {
        items.add(menuItem)
        return menuItem
    }

    override fun delete(menuItem: MenuItemModel): MenuItemModel {
        items.remove(menuItem)
        return menuItem
    }

    override fun update(menuItem: MenuItemModel): MenuItemModel {
        val foundIdx = items.indexOfFirst { it.id == menuItem.id }
        if (foundIdx != -1) {
            items[foundIdx] = menuItem
        }
        return menuItem
    }

    override fun get(id: Long): MenuItemModel? {
        return items.find { it.id == id }
    }

    override fun findAllBySection(menuSection: MenuSection): List<MenuItemModel> {
        return items.filter { it.menuSection == menuSection }
    }
}
