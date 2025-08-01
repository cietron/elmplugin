package com.example.untitled.api.message

import com.example.untitled.api.player.Player

interface MessageHandler {
    fun handle(player: Player, msg: Message)
}