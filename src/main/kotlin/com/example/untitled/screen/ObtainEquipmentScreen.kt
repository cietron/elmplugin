package com.example.untitled.screen

import com.example.untitled.api.item.Equipment
import com.example.untitled.apiImpl.item.ItemFactory
import com.example.untitled.apiImpl.store.Repository
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryType

class ObtainEquipmentScreen(val player: Player, equipmentRepository: Repository<Equipment>) : HandledInventory {
    override val inventory = Bukkit.createInventory(player, InventoryType.CHEST)

    init {
        for ((i, equipment) in equipmentRepository.getAll().withIndex()) {
            val stack = ItemFactory.fromEquipment(equipment)
            inventory.setItem(i, stack)
        }
    }

    override fun onClose() {
    }

    override fun onInventoryClick(e: InventoryClickEvent) {
    }

    override fun onInventoryDrag(e: InventoryDragEvent) {
    }
}