package com.example.untitled.events

import com.example.untitled.spell.spell
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class onPlayerInteract : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (
            event.hand == null ||
                event.hand != EquipmentSlot.HAND ||
                event.item?.type != Material.STICK
        ) {
            return
        }

        spell.execute(event)
    }
}
