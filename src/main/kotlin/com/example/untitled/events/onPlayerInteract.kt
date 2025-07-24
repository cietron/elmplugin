package com.example.untitled.events

import com.example.untitled.apiImpl.entity.EntityFactory
import com.example.untitled.spell.spell
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class onPlayerInteract : Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (
            event.hand == null ||
                event.hand != EquipmentSlot.HAND ||
                event.item?.type != Material.STICK
        ) {
            return
        }

        val itemstack = when (event.item) {
            is ItemStack -> event.item as ItemStack
            null -> return
        }
        val parsed = spell.parseSpellname(itemstack) ?: return

        spell.tryExecute(parsed, EntityFactory.fromBukkitPlayer(event.player))

        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onEntityDamage(event: EntityDamageByEntityEvent) {

        val player = when (event.damager) {
            is Player -> event.damager as Player
            else -> return
        }

        val itemstack = player.inventory.itemInMainHand

        if (
            itemstack.type != Material.STICK
        ) {
            return
        }

        val spellName = spell.parseSpellname(itemstack) ?: return

        spell.tryExecute(spellName, EntityFactory.fromBukkitPlayer(player))

        event.isCancelled = true
    }
}
