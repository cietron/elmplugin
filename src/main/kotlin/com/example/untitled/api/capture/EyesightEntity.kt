package com.example.untitled.api.capture

import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.api.player.Player

interface EyesightEntity {
    fun getEyesightEntity(player: Player, radius: Int): SelectableEntity?
}
