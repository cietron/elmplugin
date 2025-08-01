package com.example.untitled.api.player

import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.api.item.Equipment
import com.example.untitled.api.message.Message

interface Player : SelectableEntity {
    val name: String

    fun sendMessage(message: Message)

    fun setCooldown(spellName: String, durationTicks: Int)

    fun sendActionBarString(msg: String)

    fun getEquipments(): List<Equipment>
}
