package com.example.untitled.events

import com.example.untitled.Untitled
import com.example.untitled.api.event.BuiltinEvents
import com.example.untitled.apiImpl.entity.EntityFactory
import org.bukkit.entity.Arrow
import org.bukkit.entity.Fireball
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.ProjectileHitEvent

class Projectile : Listener {

    @EventHandler
    fun onProjectileHitEvent(event: ProjectileHitEvent) {

        if (event.entity !is Arrow) {
            return
        }

        val arrowUUID = event.entity.uniqueId
        val victim = event.hitEntity

        if (event.hitBlock != null) {
            Untitled.newEventManager.emit(BuiltinEvents.OnArrowHitBlock(arrowUUID))
            return
        }

        victim ?: return
        val apiEntity = when (victim) {
            is Player -> EntityFactory.fromBukkitPlayer(victim)
            is LivingEntity -> EntityFactory.fromBukkitLivingEntity(victim)
            else -> return
        }

        Untitled.newEventManager.emit(BuiltinEvents.OnArrowHitEntity(arrowUUID, apiEntity))
    }


    @EventHandler
    fun captureFireballHit(event: EntityDamageByEntityEvent) {

        val bukkitEntity = event.entity

        if (event.damager !is Fireball || bukkitEntity !is LivingEntity) {
            return
        }

        val ent = when (bukkitEntity) {
            is Player -> EntityFactory.fromBukkitPlayer(bukkitEntity)
            is LivingEntity -> EntityFactory.fromBukkitLivingEntity(bukkitEntity)
        }

        Untitled.newEventManager.emit(BuiltinEvents.OnFireballHitEntity(event.damager.uniqueId, ent))
        event.isCancelled = true // Prevent vanilla fireball damage
    }
}