package com.example.untitled.player

import com.example.untitled.Untitled
import com.example.untitled.apiImpl.entity.PlayerImpl
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit

class BuiltinStatsDisplay {
    companion object {
        fun register() {

            Bukkit.getServer().scheduler.runTaskTimer(Untitled.instance, Runnable {
                Bukkit.getOnlinePlayers().forEach { player ->
                    player?.let {
                        val playerInstance = PlayerImpl(player.name, player.uniqueId)
                        player.sendActionBar(Component.text("Health: ${playerInstance.health}, Mana: ${playerInstance.mana}"))
                    }
                }
            }, 0L, 20L)
        }
    }
}