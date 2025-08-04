package com.example.untitled.api.spell

import com.example.untitled.api.player.Player

interface CooldownManager {
    fun store(player: Player, spellIdentifier: String, durationTicks: Int)
    fun isCoolingDown(player: Player, spellIdentifier: String): Boolean
    fun getRemainingTicks(player: Player, spellIdentifier: String): Int
}