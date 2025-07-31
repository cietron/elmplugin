package com.example.untitled.screen

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

interface HandledInventory {
    val inventory: Inventory

    fun onClose()
    fun onInventoryClick(e: InventoryClickEvent)
    fun onInventoryDrag(e: InventoryDragEvent)
}