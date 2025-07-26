package com.example.untitled.api.entity

import org.joml.Vector3d
import java.util.*

interface HomingArrow {
    /**
     * Spawns an arrowing chasing at victim.
     * Spawned arrow will delete itself if
     * a. it has lived over 60 seconds
     * b. the victim is nowhere to be found
     * @param speed multiplier to the base speed.
     * @return uuid of the spawned arrow. Null if failed to spawn.
     */
    fun spawn(victim: SelectableEntity, spawnPoint: Vector3d, speed: Double): UUID?
}