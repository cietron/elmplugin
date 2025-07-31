package com.example.untitled.events

import com.example.untitled.Untitled
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent

class InventoryListener : Listener {

    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        val ent = e.player
        when (ent) {
            is Player -> Untitled.screenManager.onScreenClose(ent)
            else -> return
        }
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val viewers = e.viewers
        if (viewers.size > 1) {
            return // Not our custom screen
        }

        val ent = viewers.first()!!
        when (ent) {
            is Player -> Untitled.screenManager.onInventoryClick(ent, e)
        }
    }

    @EventHandler
    fun onInventoryDrag(e: InventoryDragEvent) {
        val viewers = e.viewers
        if (viewers.size > 1) {
            return // Not our custom screen
        }

        val ent = viewers.first()!!
        when (ent) {
            is Player -> Untitled.screenManager.onInventoryDrag(ent, e)
        }
    }
}