package com.example.untitled.apiImpl.Misc

import com.example.untitled.Untitled
import com.example.untitled.api.misc.VisualizeBoundingBox
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.BoundingBox
import org.joml.Vector3d
import kotlin.math.abs

class VisualizeBoundingBoxImpl : VisualizeBoundingBox {

    override fun with(p1: Vector3d, p2: Vector3d, seconds: Long) {
        object : BukkitRunnable() {
            var elapsedTicks = 0
            val endTick = 100

            override fun run() {
                if (elapsedTicks >= endTick) {
                    cancel()
                    return
                }

                perTick(p1, p2)
                elapsedTicks++
            }
        }.runTaskTimer(Untitled.instance, 0L, 0L)
    }

    private fun perTick(p1: Vector3d, p2: Vector3d) {

        val box = BoundingBox(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z)

        val space = 1.0

        val minX = box.minX
        val maxX = box.maxX
        val minY = box.minY
        val maxY = box.maxY
        val minZ = box.minZ
        val maxZ = box.maxZ

        val corners = listOf(
            Vector3d(minX, minY, minZ),
            Vector3d(maxX, minY, minZ),
            Vector3d(maxX, maxY, minZ),
            Vector3d(minX, maxY, minZ),
            Vector3d(minX, minY, maxZ),
            Vector3d(maxX, minY, maxZ),
            Vector3d(maxX, maxY, maxZ),
            Vector3d(minX, maxY, maxZ)
        )

        val edges = listOf(
            // Bottom face
            Pair(corners[0], corners[1]),
            Pair(corners[1], corners[2]),
            Pair(corners[2], corners[3]),
            Pair(corners[3], corners[0]),
            // Top face
            Pair(corners[4], corners[5]),
            Pair(corners[5], corners[6]),
            Pair(corners[6], corners[7]),
            Pair(corners[7], corners[4]),
            // Vertical edges
            Pair(corners[0], corners[4]),
            Pair(corners[1], corners[5]),
            Pair(corners[2], corners[6]),
            Pair(corners[3], corners[7])
        )

        for ((start, end) in edges) {
            particleLine(start, end, space)
        }


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