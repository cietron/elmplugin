package com.example.untitled.api.spell

import com.example.untitled.api.player.Player
import net.kyori.adventure.text.Component

interface Spell {
    val displayName: Component
    val description: Component
    val cooldownTicks: Int

    fun preCheck(player: Player): Boolean
    fun execute(player: Player)
}