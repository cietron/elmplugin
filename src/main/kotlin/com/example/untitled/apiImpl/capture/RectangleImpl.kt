package com.example.untitled.apiImpl.capture

import com.example.untitled.api.capture.Rectangle
import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.apiImpl.entity.PlayerImpl
import com.example.untitled.apiImpl.entity.SelectableEntityImpl
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox
import org.joml.Vector3d

class RectangleImpl : Rectangle {
    override fun capture(p1: Vector3d, p2: Vector3d): Collection<SelectableEntity> {
        // Calculate min/max for each axis to define the rectangle (cuboid)
        val minX = minOf(p1.x, p2.x)
        val maxX = maxOf(p1.x, p2.x)
        val minY = minOf(p1.y, p2.y)
        val maxY = maxOf(p1.y, p2.y)
        val minZ = minOf(p1.z, p2.z)
        val maxZ = maxOf(p1.z, p2.z)

        val world = Bukkit.getWorlds().firstOrNull() ?: return emptyList()

        val boundingBox = BoundingBox(minX, minY, minZ, maxX, maxY, maxZ)

        println(boundingBox)

        return world.getNearbyEntities(boundingBox).map {
            return@map when (it) {
                is Player -> PlayerImpl(it.name, it.uniqueId)
                else -> SelectableEntityImpl(it.uniqueId)
            }
        }

    }
}