package com.example.untitled.api.entity

import org.joml.Vector3d
import org.joml.Vector4d
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

    fun easedMove(startPoint: Vector3d, endPoint: Vector3d, bezierPoints: Vector4d, durationTick: Int)

    var health: Int
    var mana: Int
}
