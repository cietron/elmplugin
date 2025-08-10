package com.example.untitled.apiImpl.entity

import com.example.untitled.Untitled
import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.api.player.Player
import com.example.untitled.apiImpl.server.SchedulerImpl
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import java.util.*


object EntityFactory {
    fun fromBukkitPlayer(bukkitPlayer: org.bukkit.entity.Player): Player {
        return PlayerImpl(
            bukkitPlayer.name,
            bukkitPlayer.uniqueId,
            Untitled.storageManager,
            Untitled.newEventManager,
            Untitled.attributeManager,
            Untitled.equipmentManager,
            Untitled.cooldownManager,
            SchedulerImpl(Untitled.instance)
        )
    }

    fun fromBukkitLivingEntity(entity: LivingEntity): SelectableEntity {
        return SelectableEntityImpl(
            entity.uniqueId,
            Untitled.storageManager,
            Untitled.newEventManager,
            Untitled.attributeManager,
            SchedulerImpl(Untitled.instance)
        )
    }

    fun fromEntity(entity: Entity): SelectableEntity? {
        return when (entity) {
            is org.bukkit.entity.Player -> this.fromBukkitPlayer(entity)
            is LivingEntity -> this.fromBukkitLivingEntity(entity)
            else -> null
        }
    }

    fun fromEntityUUID(uuid: UUID): SelectableEntity? {
        val ent = Bukkit.getEntity(uuid)
        return when (ent) {
            is org.bukkit.entity.Player -> this.fromBukkitPlayer(ent)
            is LivingEntity -> this.fromBukkitLivingEntity(ent)
            else -> null
        }
    }
}