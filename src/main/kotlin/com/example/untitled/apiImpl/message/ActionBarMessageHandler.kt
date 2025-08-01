package com.example.untitled.apiImpl.message

import com.example.untitled.api.message.Message
import com.example.untitled.api.message.MessageHandler
import com.example.untitled.api.player.Player
import org.bukkit.Bukkit

class ActionBarMessageHandler : MessageHandler {
    override fun handle(player: Player, msg: Message) {
        val bukkitPlayer = Bukkit.getPlayer(player.uuid) ?: return
        bukkitPlayer.sendActionBar(msg.get())
    }
}