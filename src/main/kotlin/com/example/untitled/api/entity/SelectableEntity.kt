package com.example.untitled.api.entity

import com.example.untitled.api.attribute.AttributeSet
import org.joml.Vector3d
import org.joml.Vector4d
import java.util.*

interface SelectableEntity {
    val uuid: UUID
    val position: Vector3d
    val normalizedFacingVector: Vector3d
    val isPlayer: Boolean
    val isOnGround: Boolean

    fun getAttribute(): AttributeSet?

    fun setAttribute(attributeName: String, value: Double): Boolean

    fun getTrackedValue(name: String): Double?

    fun setTrackedValue(name: String, value: Double): Boolean

    fun pushTimedValue(name: String, value: Double, afterTicks: Long)

    fun popTimedValue(name: String): Double?

    fun peekTimedValue(name: String): Double?

    fun kill()

    fun setVelocity(velocity: Vector3d): Boolean

    fun getVelocity(): Vector3d

    fun emitSound(soundName: String, volume: Float, pitch: Float): Boolean

    fun easedMove(startPoint: Vector3d, endPoint: Vector3d, bezierPoints: Vector4d, durationTick: Int)

    fun setNoDamageTick(tick: Int)

    var health: Int
    var mana: Int
}
