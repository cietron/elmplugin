package com.example.untitled.events

import com.example.untitled.Untitled
import com.example.untitled.api.spell.Slot
import com.example.untitled.api.spell.SpellTriggerContext
import com.example.untitled.apiImpl.entity.EntityFactory
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.EquipmentSlot
import java.util.*

class onPlayerInteract : Listener {
    private val lastShiftTick = HashMap<UUID, Int>()
    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (
            event.hand == null ||
                event.hand != EquipmentSlot.HAND ||
                event.item?.type != Material.STICK
        ) {
            return
        }

        val slot = when (event.player.inventory.heldItemSlot) {
            0 -> Slot.ONE
            1 -> Slot.TWO
            2 -> Slot.THREE
            3 -> Slot.FOUR
            else -> null
        }

        slot ?: return

        val player = EntityFactory.fromBukkitPlayer(event.player)
        Untitled.spellManager.handleSpellTrigger(player, SpellTriggerContext.RightClick(player, slot))

        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onEntityDamage(event: EntityDamageByEntityEvent) {

        val bukkitPlayer = when (event.damager) {
            is Player -> event.damager as Player
            else -> return
        }

        val itemstack = bukkitPlayer.inventory.itemInMainHand

        if (
            itemstack.type != Material.STICK
        ) {
            return
        }

        if (bukkitPlayer.inventory.heldItemSlot != 0) {
            event.isCancelled = true
            return
        }

        val player = EntityFactory.fromBukkitPlayer(bukkitPlayer)
        val victim = EntityFactory.fromEntity((event.entity))

        victim ?: return

        event.damage = 0.0
        val success = Untitled.spellManager.handleSpellTrigger(player, SpellTriggerContext.HitEntity(player, victim))
        if (!success) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerToggleSneak(e: PlayerToggleSneakEvent) {

        // measuring sneak down only
        if (e.isSneaking) {
            return
        }

        val TICK_DELTA_THRESHOLD = 5


        if (this.lastShiftTick[e.player.uniqueId] == null) {
            this.lastShiftTick[e.player.uniqueId] = Bukkit.getCurrentTick()
            return
        }

        val lastTick = this.lastShiftTick[e.player.uniqueId]!!
        val tickDelta = Bukkit.getCurrentTick() - lastTick
        if (tickDelta < TICK_DELTA_THRESHOLD) {
            val apiPlayer = EntityFactory.fromBukkitPlayer(e.player)
            Untitled.spellManager.handleSpellTrigger(apiPlayer, SpellTriggerContext.DoubleShift(apiPlayer))
        }

        this.lastShiftTick[e.player.uniqueId] = Bukkit.getCurrentTick()
    }
}
