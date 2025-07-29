package com.example.untitled.apiImpl.capture

import com.example.untitled.api.capture.EntitiesInRotatedRect
import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.api.player.Player
import com.example.untitled.apiImpl.entity.EntityFactory
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.joml.Vector3d
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class EntitiesInRotatedRectImpl : EntitiesInRotatedRect {
    override fun get(player: Player): Collection<SelectableEntity> {

        val length = 5.0
        val width = 5.0
        val height = 5.0

        val bukkitPlayer = Bukkit.getPlayer(player.uuid)

        bukkitPlayer ?: return ArrayList()

        val world = bukkitPlayer.world
        val origin = bukkitPlayer.location.clone()
        val yaw = Math.toRadians(origin.yaw.toDouble()) // Convert yaw to radians and invert for correct rotation
        val center = origin.toVector().add(bukkitPlayer.location.direction.multiply(length / 2))

        val cos = cos(yaw)
        val sin = sin(yaw)

        val halfLength = length / 2
        val halfWidth = width / 2

        val nearby = world.getNearbyEntities(origin, length, height, width)


        val localCorners = listOf(
            Vector3d(halfLength, 0.0, halfWidth),
            Vector3d(-halfLength, 0.0, halfWidth),
            Vector3d(-halfLength, 0.0, -halfWidth),
            Vector3d(halfLength, 0.0, -halfWidth),
            Vector3d(halfLength, height, halfWidth),
            Vector3d(-halfLength, height, halfWidth),
            Vector3d(-halfLength, height, -halfWidth),
            Vector3d(halfLength, height, -halfWidth)
        )

        // Rotate and translate corners to world space
        val worldCorners = localCorners.map { local ->
            val x = local.x * cos(yaw) - local.z * sin(yaw)
            val z = local.x * sin(yaw) + local.z * cos(yaw)
            val y = local.y
            val worldVec = center.clone().add(org.bukkit.util.Vector(x, y, z))
            Vector3d(worldVec.x, worldVec.y, worldVec.z)
        }

        // Define edges by index pairs
        val edges = listOf(
            // Bottom face
            Pair(0, 1), Pair(1, 2), Pair(2, 3), Pair(3, 0),
            // Top face
            Pair(4, 5), Pair(5, 6), Pair(6, 7), Pair(7, 4),
            // Vertical edges
            Pair(0, 4), Pair(1, 5), Pair(2, 6), Pair(3, 7)
        )

        val space = 0.5 // or whatever spacing you want

        for ((startIdx, endIdx) in edges) {
            particleLine(worldCorners[startIdx], worldCorners[endIdx], space)
        }

        return nearby.filter { entity ->
            if (entity == player) return@filter false

            val pos = entity.location.toVector()
            val relative = pos.clone().subtract(center)

            // Rotate point around origin by -yaw (unrotate)
            val x = relative.x * cos - relative.z * sin
            val z = relative.x * sin + relative.z * cos

            // Check if point is within bounds
            abs(x) <= halfLength && abs(z) <= halfWidth
        }.map { entity ->
            return@map EntityFactory.fromEntity(entity)
        }.filterNotNull()
    }

    private fun particleLine(start: Vector3d, end: Vector3d, space: Double) {
        val len = Vector3d(start).sub(end).length()
        val interval = abs(space / len)

        var i = 0.0
        while (i < 1) {
            val pos = Vector3d(start).lerp(end, i)
            Bukkit.getWorlds().firstOrNull()?.spawnParticle(Particle.HEART, pos.x, pos.y, pos.z, 1)
            i += interval
        }
    }
}