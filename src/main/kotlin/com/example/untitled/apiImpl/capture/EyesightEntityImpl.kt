package com.example.untitled.apiImpl.capture

import com.example.untitled.api.capture.EyesightEntity
import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.api.player.Player
import com.example.untitled.apiImpl.entity.EntityFactory
import org.bukkit.Bukkit

class EyesightEntityImpl : EyesightEntity {
    override fun getEyesightEntity(player: Player, radius: Int): SelectableEntity? {
        val player = Bukkit.getServer().getPlayer(player.uuid)

        val hit = player!!.rayTraceEntities(radius)?.hitEntity

        if (hit == null) {
            return null
        }

        return EntityFactory.fromEntity(hit)
    }
}
