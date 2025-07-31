package com.example.untitled.screen

import com.example.untitled.Untitled
import com.example.untitled.api.spell.Slot
import com.example.untitled.api.spell.SpellTriggerType
import com.example.untitled.apiImpl.entity.EntityFactory
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class SpellEquipScreen(val player: Player) : PartiallyFrozenChest((0..26).toSet().minus((0..6).toSet())) {
    override val inventory = Bukkit.createInventory(player, InventoryType.CHEST)
    val savedSlots = (0..6).toSet()
    val baseKey = "spell_equip_screen"

    init {

        for (i in (0..26).toSet().minus(savedSlots)) {
            inventory.setItem(i, ItemStack.of(Material.BLACK_STAINED_GLASS_PANE))
        }

        for (i in savedSlots) {
            val key = NamespacedKey.fromString(baseKey + "$i", Untitled.instance)!!
            val bb = player.persistentDataContainer.get(key, PersistentDataType.BYTE_ARRAY)
            bb ?: continue
            inventory.setItem(i, ItemStack.deserializeBytes(bb))
        }

        val stack = ItemStack.of(Material.PAPER)
        for (i in 9..15) {
            val desc = when (i) {
                9 -> "主武器"
                10 -> "普通攻擊"
                11 -> "第一技能"
                12 -> "第二技能"
                13 -> "第三技能"
                14 -> "第四技能"
                15 -> "副武器"
                else -> "This should not be visible"
            }
            stack.editMeta { meta ->
                meta.displayName(MiniMessage.miniMessage().deserialize(desc))
                meta.lore(
                    listOf(
                        MiniMessage.miniMessage().deserialize("你好世界"),
                        MiniMessage.miniMessage().deserialize("這是技能放置資訊")
                    )
                )
            }
            inventory.setItem(i, stack)
        }

    }

    override fun filteredInventoryClick(e: InventoryClickEvent) {
    }

    override fun filteredInventoryDrag(e: InventoryDragEvent) {
    }

    override fun onClose() {
        val apiPlayer = EntityFactory.fromBukkitPlayer(player)
        Untitled.spellManager.cleanPlayerSpell(apiPlayer)

        for ((i, itemStack) in inventory.withIndex()) {
            if (i !in savedSlots) continue
            val key = NamespacedKey.fromString(baseKey + "$i", Untitled.instance)!!
            if (i in savedSlots && itemStack == null) {
                player.persistentDataContainer.remove(key)
                continue
            }
            if (i in savedSlots && itemStack != null) {
                val NBTbytes = itemStack.serializeAsBytes()
                player.persistentDataContainer.set(key, PersistentDataType.BYTE_ARRAY, NBTbytes)
            }

            if (!itemStack.itemMeta.hasCustomName()) continue
            val spellNameComponent = itemStack.itemMeta.displayName()
            if (spellNameComponent !is TextComponent) continue
            val spellName = spellNameComponent.content()
            Untitled.instance.logger.info("${player.name} attempted equipping spell: ${spellName} at slot$i")
            val spell = Untitled.spellRepository.get(spellName)
            spell ?: continue

            val slot = when (i) {
                2 -> Slot.ONE
                3 -> Slot.TWO
                4 -> Slot.THREE
                5 -> Slot.FOUR
                else -> continue
            }

            when (spell.triggerType) {
                SpellTriggerType.RightClick -> Untitled.spellManager.registerSpell(
                    apiPlayer,
                    spell.triggerType,
                    spell,
                    slot
                )

                else -> Untitled.spellManager.registerSpell(apiPlayer, spell.triggerType, spell, null)
            }


        }

    }
}