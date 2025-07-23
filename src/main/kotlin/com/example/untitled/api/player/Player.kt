package com.example.untitled.api.player

import com.example.untitled.api.entity.SelectableEntity

interface Player : SelectableEntity {
    val name: String

    fun sendMessage(msg: String)

    fun setCooldown(spellName: String, durationTicks: Int)

    fun sendActionBarString(msg: String)

}
