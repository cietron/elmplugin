package com.example.untitled.api.server

import com.example.untitled.api.player.Player
import org.joml.Vector3d

interface Server {

    fun getAllPlayers(): Collection<Player>

    /**
     * @param soundName Minecraft Resource location key.
     * @param volume A capped double between 0.0 and 1.0
     */
    fun playSound(soundName: String, location: Vector3d, volume: Float, pitch: Float): Boolean
}