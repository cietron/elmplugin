package com.example.untitled.apiImpl.entity

import com.example.untitled.Untitled
import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.api.player.Player
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
            Untitled.attributeManager
        )
    }

    fun fromBukkitLivingEntity(entity: LivingEntity): SelectableEntity {
        return SelectableEntityImpl(
            entity.uniqueId,
            Untitled.storageManager,
            Untitled.newEventManager,
            Untitled.attributeManager
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