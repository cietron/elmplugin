package com.example.untitled.screen

import com.example.untitled.Untitled
import com.example.untitled.api.spell.Slot
import com.example.untitled.apiImpl.entity.EntityFactory
import com.example.untitled.apiImpl.item.ItemFactory
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

private val FROZEN_SLOTS = (0..8).toSet().plus((23..26))
private val SPELL_SLOTS = (18..22).toSet()
private val EQUIPMENT_SLOTS = (9..17).toSet()

class EquipmentScreen(val bukkitPlayer: Player) : PartiallyFrozenChest(FROZEN_SLOTS) {
    override val inventory = Bukkit.createInventory(bukkitPlayer, InventoryType.CHEST)
    private val PDCBasekey = "equipment_screen"

    init {
        for (i in FROZEN_SLOTS) {
            val itemStack = ItemStack.of(Material.BLACK_STAINED_GLASS_PANE)
            inventory.setItem(i, itemStack)
        }
        for (i in SPELL_SLOTS.union(EQUIPMENT_SLOTS)) {
            val key = NamespacedKey.fromString("$PDCBasekey$i", Untitled.instance)!!
            val NBTBytes = bukkitPlayer.persistentDataContainer.get(key, PersistentDataType.BYTE_ARRAY) ?: continue
            val itemStack = ItemStack.deserializeBytes(NBTBytes)
            inventory.setItem(i, itemStack)
        }

    }

    override fun filteredInventoryClick(e: InventoryClickEvent) {
    }

    override fun filteredInventoryDrag(e: InventoryDragEvent) {
    }

    override fun onClose() {
        val equipmentItems = ArrayList<ItemStack>()
        val spellItems = ArrayList<Pair<Slot?, ItemStack>>()

        for ((i, itemStack) in inventory.withIndex()) {
            if (i in FROZEN_SLOTS) continue
            if (itemStack == null || itemStack.isEmpty) continue

            if (i in SPELL_SLOTS) {
                val slot = when (i) {
                    18 -> Slot.ONE
                    19 -> Slot.ONE
                    20 -> Slot.TWO
                    21 -> Slot.THREE
                    22 -> Slot.FOUR
                    else -> null
                }
                spellItems.add(Pair(slot, itemStack))
            }

            if (i in EQUIPMENT_SLOTS) {
                equipmentItems.add(itemStack)
            }

            val key = NamespacedKey.fromString("$PDCBasekey$i", Untitled.instance)!!
            if (i in SPELL_SLOTS.union(EQUIPMENT_SLOTS)) {
                val NBTbytes = itemStack.serializeAsBytes()
                bukkitPlayer.persistentDataContainer.set(key, PersistentDataType.BYTE_ARRAY, NBTbytes)
            }

        }

        val apiPlayer = EntityFactory.fromBukkitPlayer(bukkitPlayer)

        val equipments = equipmentItems.mapNotNull { itemStack ->
            ItemFactory.toEquipment(
                itemStack, Untitled.equipmentRepository
            )
        }


        Untitled.equipmentManager.update(apiPlayer, equipments)

        Untitled.spellManager.cleanPlayerSpell(apiPlayer)
        val spells =
            spellItems.map { (slot, itemStack) -> Pair(slot, ItemFactory.toSpell(itemStack, Untitled.spellRepository)) }
                .filter { (_, spell) -> spell != null }.map { (slot, spell) -> Pair(slot, spell!!) }

        println(spells)
        for ((slot, spell) in spells) {
            Untitled.spellManager.registerSpell(apiPlayer, spell.triggerType, spell, slot)
        }

    }
}