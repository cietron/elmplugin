package com.example.untitled.screen

import org.bukkit.entity.Player

object ScreenFactory {
    fun make(player: Player, screenIdentifier: String): HandledInventory? {
        return when (screenIdentifier) {
            "spellEquipScreen" -> SpellEquipScreen(player)
            "obtainSpell" -> ObtainSpellScreen(player)
            else -> null
        }
    }
}