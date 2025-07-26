package com.example.untitled.apiImpl.entity

import com.example.untitled.Untitled
import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.api.event.BuiltinEvents
import com.example.untitled.storage.SafeAttributeValue
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import org.joml.Vector3d
import org.joml.Vector4d
import java.util.*

open class SelectableEntityImpl(override val uuid: UUID) : SelectableEntity {
    companion object {
        const val DEFAULT_HEALTH = 20
        const val DEFAULT_MANA = 100
    }

    override val isPlayer = false

    override val position: Vector3d
        get() {
            val ent = Bukkit.getEntity(uuid)

            ent ?: return Vector3d(0.0, 0.0, 0.0)

            val loc = ent.location
            return Vector3d(loc.x, loc.y, loc.z)
        }

    override fun getAttribute(attributeName: String): SafeAttributeValue? {
        val key = "$uuid#$attributeName"
        return Untitled.simpleStorage.retrieve(key)
    }

    override fun setAttribute(attributeName: String, value: Any): Boolean {
        val key = "$uuid#$attributeName"
        val results = Untitled.simpleStorage.store(key, value)
        this.postAttributeChange(attributeName)
        return results
    }

    private fun postAttributeChange(attributeName: String) {
        if (attributeName == "health" && this.health <= 0) {
            this.kill()
            this.health = DEFAULT_HEALTH
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
            val key = "$uuid#health"

            if (Untitled.simpleStorage.retrieve(key) == null) {
                Untitled.simpleStorage.store(key, DEFAULT_HEALTH)
            }

            val value = Untitled.simpleStorage.retrieve(key)
            return when (value) {
                is SafeAttributeValue.DoubleValue -> null
                is SafeAttributeValue.IntValue -> value.value
                is SafeAttributeValue.StringValue -> null
                null -> null
            }!!
        }
        set(value) {
            val key = "$uuid#health"
            Untitled.simpleStorage.store(key, value)
        }

    override var mana: Int
        get() {
            val key = "$uuid#mana"

            if (Untitled.simpleStorage.retrieve(key) == null) {
                Untitled.simpleStorage.store(key, DEFAULT_MANA)
            }

            val value = Untitled.simpleStorage.retrieve(key)

            return when (value) {
                is SafeAttributeValue.DoubleValue -> null
                is SafeAttributeValue.IntValue -> value.value
                is SafeAttributeValue.StringValue -> null
                null -> null
            }!!
        }
        set(value) {
            val key = "$uuid#mana"
            Untitled.simpleStorage.store(key, value)
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

        Untitled.newEventManager.registerEvent(BuiltinEvents.OnTick::class, {
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
