package com.example.untitled.apiImpl.entity

import com.example.untitled.Untitled
import com.example.untitled.api.player.Player
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import java.util.*

class PlayerImpl(override val name: String, override val uuid: UUID) :
    Player, SelectableEntityImpl(uuid) {

    override val isPlayer = true

    override fun sendMessage(msg: String) {
        val player = Bukkit.getPlayer(uuid)

        player ?: return
        player.sendMessage(msg)
    }

    override fun setCooldown(spellName: String, durationTicks: Int) {
        Untitled.cooldownManager.store(this, spellName, durationTicks)
    }

    override fun sendActionBarString(msg: String) {
        val player = Bukkit.getPlayer(uuid)

        player ?: return
        player.sendActionBar(Component.text(msg))
    }
}
