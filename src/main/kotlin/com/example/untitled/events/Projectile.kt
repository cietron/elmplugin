package com.example.untitled.events

import com.example.untitled.Untitled
import com.example.untitled.api.event.BuiltinEvents
import com.example.untitled.api.spell.Slot
import com.example.untitled.api.spell.SpellTriggerContext
import com.example.untitled.apiImpl.entity.EntityFactory
import org.bukkit.entity.Arrow
import org.bukkit.entity.Fireball
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.EquipmentSlot

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


    @EventHandler
    fun onPlayerShootBowEvent(e: EntityShootBowEvent) {
        val arrow = when (e.projectile) {
            is Arrow -> e.projectile as Arrow
            else -> return
        }
        val player = when (e.entity) {
            is Player -> e.entity as Player
            else -> return
        }

        if (e.hand == EquipmentSlot.OFF_HAND) {
            return
        }


        val slot = when (player.inventory.heldItemSlot) {
            0 -> Slot.ONE
            1 -> Slot.TWO
            2 -> Slot.THREE
            3 -> Slot.FOUR
            else -> return
        }

        var counter = 0
        val apiPlayer = EntityFactory.fromBukkitPlayer(player)
        Untitled.newEventManager.registerEvent(BuiltinEvents.OnArrowHitEntity::class, { ctx ->
            if (counter >= 100) { // unregister after 100 failed events.
                return@registerEvent false
            }
            if (ctx.arrowUUID != arrow.uniqueId) {
                counter++
                return@registerEvent true
            }

            Untitled.spellManager.handleSpellTrigger(
                apiPlayer,
                SpellTriggerContext.ArrowHitEntity(apiPlayer, ctx.victim, slot)
            )
            counter++
            return@registerEvent false
        })
    }
}