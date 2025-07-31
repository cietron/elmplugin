package com.example.untitled.screen

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

/*
 * Minecraft screen slot layout
 * Upper inventory: top left is 0, top right: width-1 and bottom right width*length-1
 * Bottom inventory (player inventory) hot bar 0~8. Inner inventory follow Upper Inventory logic, except offset by hotbar slot number.
 * When presented in combined inventory view, the bottom inventory rawSlot id is offset by the final slot number of upper inventory.
 */
abstract class PartiallyFrozenChest(val frozenRawSlots: Set<Int>) : HandledInventory {

    abstract fun filteredInventoryClick(e: InventoryClickEvent)
    abstract fun filteredInventoryDrag(e: InventoryDragEvent)

    override fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory == this.inventory && e.rawSlot in frozenRawSlots) {
            e.isCancelled = true
            return
        }
        this.filteredInventoryClick(e)
    }

    override fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.inventory == this.inventory && e.rawSlots.intersect(frozenRawSlots).isNotEmpty()) {
            e.isCancelled = true
            return
        }
        this.filteredInventoryDrag(e)
    }
}