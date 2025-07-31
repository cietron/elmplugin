package com.example.untitled.screen

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import java.util.*

class ScreenManager {
    private val currentScreen = HashMap<UUID, HandledInventory>()

    fun openScreen(player: Player, screenIdentifier: String) {
        val inventory = ScreenFactory.make(player, screenIdentifier)
        inventory ?: return

        val playerCurrentScreen = this.currentScreen[player.uniqueId]

        if (playerCurrentScreen != null) {
            playerCurrentScreen.onClose()
            this.currentScreen.remove(player.uniqueId)
        }

        this.currentScreen.put(player.uniqueId, inventory)

        player.openInventory(inventory.inventory)
    }

    fun onScreenClose(player: Player) {
        val playerScreen = this.currentScreen[player.uniqueId]
        playerScreen ?: return

        playerScreen.onClose()
        this.currentScreen.remove(player.uniqueId)
    }

    fun onInventoryClick(player: Player, e: InventoryClickEvent) {
        val playerScreen = this.currentScreen[player.uniqueId]
        playerScreen ?: return
        playerScreen.onInventoryClick(e)
    }

    fun onInventoryDrag(player: Player, e: InventoryDragEvent) {
        val playerScreen = this.currentScreen[player.uniqueId]
        playerScreen ?: return
        playerScreen.onInventoryDrag(e)
    }

}