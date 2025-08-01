package com.example.untitled.apiImpl.item

import com.example.untitled.Untitled
import com.example.untitled.api.attribute.AttributeSet
import com.example.untitled.api.item.Equipment
import com.example.untitled.api.player.Player
import org.bukkit.Bukkit

object AttributeCalculator {
    fun calculate(player: Player, equipments: List<Equipment>): AttributeSet {
        val bukkitPlayer = Bukkit.getPlayer(player.uuid) ?: return AttributeSet.empty()
        val playerAttributeSet = Untitled.attributeManager.getDefault(bukkitPlayer)

        val accumulatedSet =
            listOf(playerAttributeSet).plus(equipments.map { equipment -> equipment.attributeModifiers })
                .reduce { acc, set ->
                    acc.plus(set)
                }

        return accumulatedSet
    }
}