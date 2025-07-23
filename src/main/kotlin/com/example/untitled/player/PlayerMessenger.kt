package com.example.untitled.player

import com.example.untitled.api.player.Player

object PlayerMessenger {
    fun sendCooldownMessage(player: Player, spellName: String, remainingTicks: Int) {
        val seconds = remainingTicks / 20.0
        val secondFormated = "%.2f".format(seconds)
        player.sendActionBarString("$spellName is on cooldown for ${secondFormated}s")
    }

    fun sendSpellCast(player: Player, spellName: String, target: Player?) {
        val targetName = target?.name ?: "the area"
        player.sendActionBarString("You cast $spellName on $targetName")
    }

}