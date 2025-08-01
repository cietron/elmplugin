package com.example.untitled.api.item

import com.example.untitled.api.attribute.AttributeSet
import com.example.untitled.api.player.Player
import net.kyori.adventure.text.Component

interface Equipment {
    val identifier: String
    val vanillaItemID: String
    val type: EquipmentType
    val displayName: Component
    val attributeModifiers: AttributeSet
    fun checkRequirements(context: EquipmentCheckContext): Boolean
}

data class EquipmentCheckContext(
    val player: Player,
    val newAttributeSet: AttributeSet,
    val currentEquipments: List<Equipment>
)

enum class EquipmentType {
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS,
    WEAPON,
    RING,
    AMULET,
}