package com.example.untitled.api.item

import com.example.untitled.api.player.Player

interface EquipmentManager {
    fun update(player: Player, newEquipments: List<Equipment>): Boolean
    fun getCurrentEquipments(player: Player): List<Equipment>
}