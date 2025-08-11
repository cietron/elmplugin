package com.example.untitled.apiImpl.item

import com.example.untitled.Untitled
import com.example.untitled.api.item.Equipment
import com.example.untitled.api.spell.Spell
import com.example.untitled.apiImpl.store.Repository
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ItemFactory {
    private val PDCEquipmentIdentifierKey = NamespacedKey.fromString("equipment_id", Untitled.instance)!!
    fun fromEquipment(equipment: Equipment): ItemStack {
        val mat = Material.matchMaterial(equipment.vanillaItemID) ?: Material.PAPER
        val itemStack = ItemStack.of(mat)

        itemStack.editPersistentDataContainer { container ->
            container.set(
                PDCEquipmentIdentifierKey,
                PersistentDataType.STRING, equipment.identifier
            )
        }

        itemStack.editMeta { meta ->
            meta.displayName(equipment.displayName)
            meta.lore(equipment.lore)
        }

        return itemStack
    }

    fun toEquipment(itemStack: ItemStack, equipmentRepository: Repository<Equipment>): Equipment? {
        val equipmentID = itemStack.persistentDataContainer.get(PDCEquipmentIdentifierKey, PersistentDataType.STRING)
        if (equipmentID == null) {
            return null
        }

        return equipmentRepository.get(equipmentID)
    }

    fun fromSpell(spell: Spell<*>): ItemStack {
        val itemStack = fromEquipment(spell.spellItem)

        itemStack.editPersistentDataContainer { container ->
            container.set(
                PDCEquipmentIdentifierKey,
                PersistentDataType.STRING, spell.identifier
            )
        }

        return itemStack
    }

    fun toSpell(itemStack: ItemStack, spellRepository: Repository<Spell<*>>): Spell<*>? {
        val spellID =
            itemStack.persistentDataContainer.get(PDCEquipmentIdentifierKey, PersistentDataType.STRING) ?: return null
        return spellRepository.get(spellID)
    }
}