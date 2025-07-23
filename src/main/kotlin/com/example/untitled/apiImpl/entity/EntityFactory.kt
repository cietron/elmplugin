package com.example.untitled.apiImpl.entity

import com.example.untitled.api.player.Player


object EntityFactory {
    fun fromBukkitPlayer(bukkitPlayer: org.bukkit.entity.Player): Player {
        return PlayerImpl(bukkitPlayer.name, bukkitPlayer.uniqueId)
    }
}