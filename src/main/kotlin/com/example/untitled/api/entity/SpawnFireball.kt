package com.example.untitled.api.entity

import org.joml.Vector3d
import java.util.*

interface SpawnFireball {
    fun spawn(spawnPoint: Vector3d, velocity: Vector3d): UUID?
}