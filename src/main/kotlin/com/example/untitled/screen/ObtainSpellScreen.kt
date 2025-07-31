package com.example.untitled.screen

import com.example.untitled.Untitled
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

class ObtainSpellScreen(val player: Player) : HandledInventory {
    override val inventory = Bukkit.createInventory(player, InventoryType.CHEST)

    init {
        for ((i, spell) in Untitled.spellRepository.getAll().withIndex()) {
            val itemStack = ItemStack.of(Material.STICK)
            val itemMeta = itemStack.itemMeta
            itemMeta.customName(Component.text(spell.identifier))
            itemStack.itemMeta = itemMeta
            inventory.setItem(i, itemStack)
        }
    }


    override fun onClose() {
    }

    override fun onInventoryClick(e: InventoryClickEvent) {
    }

    override fun onInventoryDrag(e: InventoryDragEvent) {
    }
}