package com.example.untitled.apiImpl.item

import com.example.untitled.api.item.Equipment
import com.example.untitled.api.item.EquipmentCheckContext
import com.example.untitled.api.item.EquipmentManager
import com.example.untitled.api.player.Player
import com.example.untitled.storage.UnsafeStorage

class EquipmentManagerImpl(private val storage: UnsafeStorage) : EquipmentManager {
    private val storageBaseKey = "EquipmentManager"

    override fun update(
        player: Player,
        newEquipments: List<Equipment>
    ): Boolean {

        val newAttributeSet = AttributeCalculator.calculate(player, newEquipments)
        val context = EquipmentCheckContext(player, newAttributeSet, newEquipments)

        val result = newEquipments.all { equipment -> equipment.checkRequirements(context) }

        this.storePlayerEquipments(player, emptyList())

        if (result) {
            this.storePlayerEquipments(player, newEquipments)
            newAttributeSet.values().forEach { attribute -> player.setAttribute(attribute.identifier, attribute.value) }
        }
        return result
    }

    override fun getCurrentEquipments(player: Player): List<Equipment> {
        return this.getPlayerEquipments(player)
    }

    private fun getPlayerEquipments(player: Player): List<Equipment> {
        val key = "$storageBaseKey#${player.uuid}"
        if (this.storage.retrieveRaw<MutableList<Equipment>>(key) == null) {
            this.storage.storeRaw(key, ArrayList<Equipment>())
        }
        return this.storage.retrieveRaw<MutableList<Equipment>>(key)!!.toList()
    }

    private fun storePlayerEquipments(player: Player, newEquipments: List<Equipment>) {
        val key = "$storageBaseKey#${player.uuid}"
        this.storage.storeRaw(key, ArrayList(newEquipments))
    }
}