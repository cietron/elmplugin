package com.example.untitled.screen

import com.example.untitled.api.spell.Spell
import com.example.untitled.apiImpl.item.ItemFactory
import com.example.untitled.apiImpl.store.Repository
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryType

class ObtainSpellScreen(val player: Player, spellRepository: Repository<Spell<*>>) : HandledInventory {
    override val inventory = Bukkit.createInventory(player, InventoryType.CHEST)

    init {
        for ((i, spell) in spellRepository.getAll().withIndex()) {
            val itemStack = ItemFactory.fromSpell(spell)
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