package com.example.untitled.api.capture

import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.api.player.Player

interface EntitiesInRotatedRect {

    fun get(player: Player): Collection<SelectableEntity>
}