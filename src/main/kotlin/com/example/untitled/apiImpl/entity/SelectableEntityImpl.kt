package com.example.untitled.apiImpl.entity

import com.example.untitled.api.attribute.AttributeManager
import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.api.event.BuiltinEvents
import com.example.untitled.api.event.EventManager
import com.example.untitled.storage.Storage
import com.example.untitled.storage.StorageValue
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import org.joml.Vector3d
import org.joml.Vector4d
import java.util.*

open class SelectableEntityImpl(
    override val uuid: UUID,
    val storage: Storage,
    val eventManager: EventManager,
    val attributeManager: AttributeManager
) : SelectableEntity {
    companion object {
        const val DEFAULT_HEALTH = 20.0
        const val DEFAULT_MANA = 100.0
    }

    override val isPlayer = false

    override val position: Vector3d
        get() {
            val ent = Bukkit.getEntity(uuid)

            ent ?: return Vector3d(0.0, 0.0, 0.0)

            val loc = ent.location
            return Vector3d(loc.x, loc.y, loc.z)
        }

    override fun getAttribute(): List<Map.Entry<String, Double>>? {
        val rawEntity = Bukkit.getEntity(uuid)
        val ent = when (rawEntity) {
            is LivingEntity -> rawEntity
            else -> return null
        }

        return attributeManager.get(ent)
    }

    override fun setAttribute(attributeName: String, value: Double): Boolean {
        val rawEntity = Bukkit.getEntity(uuid)
        val ent = when (rawEntity) {
            is LivingEntity -> rawEntity
            else -> return false
        }

        attributeManager.notifyChange(ent, attributeName, value)
        return true
    }

    private fun postAttributeChange(attributeName: String) {
    }

    override fun getTrackedValue(name: String): Double? {
        val key = "$uuid#trackedValue#$name"
        val value = storage.retrieve(key)
        return when (value) {
            is StorageValue.DoubleValue -> value.value
            is StorageValue.IntValue -> value.value.toDouble()
            is StorageValue.StringValue -> throw AssertionError("Stored tracked value unexcepted type string. Requested $uuid: $name. Got $value")
            null -> null
        }
    }

    override fun setTrackedValue(name: String, value: Double): Boolean {
        val key = "$uuid#trackedValue#$name"
        val results = storage.store(key, StorageValue.DoubleValue(value))
        this.postTrackedValueChange(name)
        return results
    }

    private fun postTrackedValueChange(name: String) {
        if (name == "health" && this.health <= 0) {
            this.kill()
            this.health = DEFAULT_HEALTH.toInt()
        }
    }

    override fun kill() {
        val ent = Bukkit.getEntity(uuid)
        when (ent) {
            is LivingEntity -> ent.damage(10000000.0)
            else -> null
        }
    }

    override fun setVelocity(velocity: Vector3d): Boolean {
        val ent = Bukkit.getEntity(uuid)
        when (ent) {
            is LivingEntity -> ent.velocity = Vector.fromJOML(velocity)
            else -> return false
        }

        return true
    }

    override fun getVelocity(): Vector3d {
        val ent = Bukkit.getEntity(uuid)
        val velocity = when (ent) {
            is LivingEntity -> ent.velocity.toVector3d()
            else -> Vector3d(0.0, 0.0, 0.0)
        }

        return velocity
    }

    override var health: Int
        get() {
            val key = "health"

            if (this.getTrackedValue(key) == null) {
                this.setTrackedValue(key, DEFAULT_HEALTH)
            }

            val value = getTrackedValue(key)!!
            return value.toInt()
        }
        set(value) {
            val key = "health"
            this.setTrackedValue(key, value.toDouble())
        }

    override var mana: Int
        get() {
            val key = "mana"

            if (this.getTrackedValue(key) == null) {
                this.setTrackedValue(key, DEFAULT_MANA)
            }

            val value = getTrackedValue(key)!!
            return value.toInt()
        }
        set(value) {
            val key = "mana"
            this.setTrackedValue(key, value.toDouble())
        }

    override val normalizedFacingVector: Vector3d
        get() {
            val player = Bukkit.getPlayer(uuid)

            player ?: return Vector3d(0.0, 0.0, 0.0)

            val yaw = Math.toRadians(player.location.yaw.toDouble())
            val pitch = Math.toRadians(player.location.pitch.toDouble())
            return Vector3d(
                -Math.cos(pitch) * Math.sin(yaw),
                -Math.sin(pitch),
                Math.cos(pitch) * Math.cos(yaw)
            )
        }

    override fun emitSound(soundName: String, volume: Float, pitch: Float): Boolean {
        val world = Bukkit.getServer().worlds.firstOrNull()
        world ?: return false

        val ent = Bukkit.getEntity(uuid)
        ent ?: return false

        world.playSound(Sound.sound(Key.key(soundName), Sound.Source.PLAYER, volume, pitch), ent)
        return true
    }

    override fun easedMove(
        startPoint: Vector3d,
        endPoint: Vector3d,
        bezierPoints: Vector4d,
        durationTick: Int
    ) {

        val ent = Bukkit.getServer().getEntity(uuid) as LivingEntity?
        ent ?: return

        val easing = CubicBezierEasing(bezierPoints)
        var counter = 0

        val startPointCopy = Vector3d(startPoint)
        val endPointCopy = Vector3d(endPoint)

        eventManager.registerEvent(BuiltinEvents.OnTick::class, {
            if (counter >= durationTick) {
                ent.velocity = Vector(0, 0, 0)
                return@registerEvent false
            }

            val currentT = counter / durationTick.toDouble()
            val nextT = ((counter + 1).coerceAtMost(durationTick - 1)) / durationTick.toDouble()

            val posNow = Vector3d(startPointCopy).lerp(endPointCopy, easing.ease(currentT))
            val posNext = Vector3d(startPointCopy).lerp(endPointCopy, easing.ease(nextT))

            val velocity = posNext.sub(posNow)

            ent.velocity = Vector(velocity.x, velocity.y, velocity.z)

            counter++
            return@registerEvent true
        })
    }
}
