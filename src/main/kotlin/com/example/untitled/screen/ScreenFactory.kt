package com.example.untitled.screen

import com.example.untitled.Untitled
import org.bukkit.entity.Player

object ScreenFactory {
    fun make(bukkitPlayer: Player, screenIdentifier: String): HandledInventory? {
        return when (screenIdentifier) {
            "obtainSpell" -> ObtainSpellScreen(bukkitPlayer, Untitled.spellRepository)
            "obtainEquipment" -> ObtainEquipmentScreen(bukkitPlayer, Untitled.equipmentRepository)
            "equipmentScreen" -> EquipmentScreen(bukkitPlayer)
            else -> null
        }
    }
}