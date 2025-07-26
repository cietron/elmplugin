package com.example.untitled.apiImpl.entity

import com.example.untitled.api.entity.HomingArrow
import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.api.event.BuiltinEvents
import com.example.untitled.api.event.EventManager
import org.bukkit.Bukkit
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.EntityType
import org.bukkit.util.Vector
import org.joml.Vector3d
import java.util.*

class HomingArrowImpl(val eventManager: EventManager) : HomingArrow {
    override fun spawn(victim: SelectableEntity, spawnPoint: Vector3d, speed: Double): UUID? {
        val victim = Bukkit.getEntity(victim.uuid)

        victim ?: return null

        val spawnLocation = Vector.fromJOML(spawnPoint).toLocation(victim.world)
        val arrow = victim.world.spawnEntity(spawnLocation, EntityType.ARROW) as AbstractArrow

        arrow.damage = 0.0

        var timer = 0
        var hitBlock = false

        eventManager.registerEvent(BuiltinEvents.OnArrowHitBlock::class, { arrowUUID ->
            if (arrow.uniqueId == arrowUUID) {
                hitBlock = true
                return@registerEvent false
            }
            if (timer >= 60 * 20) {
                return@registerEvent false
            }
            return@registerEvent true
        })

        eventManager.registerEvent(BuiltinEvents.OnTick::class, {
            val nowArrow = Bukkit.getEntity(arrow.uniqueId)
            val nowVictim = Bukkit.getEntity(victim.uniqueId)

            if (hitBlock) {
                return@registerEvent false
            }

            if (timer >= 60 * 20) {
                return@registerEvent false
            }

            if (nowArrow == null || nowVictim == null) {
                return@registerEvent false
            }

            if (nowArrow.world.uid != nowVictim.world.uid) {
                return@registerEvent false
            }

            val newVelocity =
                nowVictim.location.toVector().clone().subtract(nowArrow.location.toVector()).normalize().multiply(speed)
            nowArrow.velocity = newVelocity

            timer++
            return@registerEvent true
        })

        return arrow.uniqueId
    }
}