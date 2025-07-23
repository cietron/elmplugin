package com.example.untitled.api.entity

import org.joml.Vector3d
import java.util.*

interface SelectableEntity {
    val uuid: UUID
    val position: Vector3d
    val normalizedFacingVector: Vector3d
    val isPlayer: Boolean

    fun getAttribute(attributeName: String): Any?

    fun setAttribute(attributeName: String, value: Any): Boolean

    fun kill()

    fun setVelocity(velocity: Vector3d): Boolean

    fun getVelocity(): Vector3d

    fun emitSound(soundName: String, volume: Float, pitch: Float): Boolean

    var health: Int
    var mana: Int
}
