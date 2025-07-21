package com.example.untitled.apiImpl.entity

import com.example.untitled.api.player.Player
import org.bukkit.Bukkit
import java.util.*

class PlayerImpl(override val name: String, override val uuid: UUID) :
    Player, SelectableEntityImpl(uuid) {

    override fun sendMessage(msg: String) {
        val player = Bukkit.getPlayer(uuid)

        player ?: return
        player.sendMessage(msg)
    }
}
