package com.example.untitled.apiImpl.entity

import com.example.untitled.api.entity.SpawnFireball
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.Fireball
import org.bukkit.util.Vector
import org.joml.Vector3d
import java.util.*

class SpawnFireballImpl : SpawnFireball {
    override fun spawn(spawnPoint: Vector3d, velocity: Vector3d): UUID? {

        val world = Bukkit.getServer().worlds.firstOrNull()
        world ?: return null

        val fireballEntity =
            world.spawnEntity(Vector.fromJOML(spawnPoint).toLocation(world), EntityType.FIREBALL) as Fireball

        fireballEntity.velocity = Vector.fromJOML(velocity)
        return fireballEntity.uniqueId
    }
}