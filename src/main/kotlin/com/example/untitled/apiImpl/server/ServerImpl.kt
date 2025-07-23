package com.example.untitled.apiImpl.server

import com.example.untitled.api.player.Player
import com.example.untitled.api.server.Server
import com.example.untitled.apiImpl.entity.PlayerImpl
import com.google.common.collect.ImmutableList
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.Bukkit
import org.joml.Vector3d

class ServerImpl : Server {
    override fun getAllPlayers(): Collection<Player> {
        val BukkitPlayers = ImmutableList.copyOf(Bukkit.getServer().onlinePlayers.filterNotNull())

        return BukkitPlayers.map { player -> PlayerImpl(player.name, player.uniqueId) }
    }

    override fun playSound(
        soundName: String,
        location: Vector3d,
        volume: Float,
        pitch: Float
    ): Boolean {
        val world = Bukkit.getServer().worlds.firstOrNull()
        world ?: return false

        world.playSound(
            Sound.sound(Key.key(soundName), Sound.Source.PLAYER, 1f, 1f),
            location.x,
            location.y,
            location.z
        )
        return true
    }

}