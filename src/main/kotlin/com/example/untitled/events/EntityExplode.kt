package com.example.untitled.events

import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent

class EntityExplode : Listener {

    @EventHandler
    fun onEntityExplode(event: EntityExplodeEvent) {
        if (event.entityType == EntityType.FIREBALL) {
            event.isCancelled = true
            return
        }
    }
}