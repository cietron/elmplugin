package com.example.untitled.apiImpl.message

import com.example.untitled.api.message.Message
import com.example.untitled.api.player.Player

object MessageProcessor {
    private val handlers = mapOf(
        Message.MessageType.SCOREBOARD to ScoreboardMessageHandler(),
        Message.MessageType.ACTION_BAR to ActionBarMessageHandler(),
        Message.MessageType.CHAT to ChatMessageHandler()
    )

    fun process(player: Player, message: Message) {
        handlers[message.type]?.handle(player, message)
    }
}