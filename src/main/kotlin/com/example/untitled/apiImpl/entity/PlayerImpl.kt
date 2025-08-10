package com.example.untitled.apiImpl.entity

import com.example.untitled.api.attribute.AttributeManager
import com.example.untitled.api.event.EventManager
import com.example.untitled.api.item.Equipment
import com.example.untitled.api.item.EquipmentManager
import com.example.untitled.api.message.Message
import com.example.untitled.api.player.Player
import com.example.untitled.api.server.Scheduler
import com.example.untitled.api.spell.CooldownManager
import com.example.untitled.apiImpl.message.MessageProcessor
import com.example.untitled.storage.UnsafeStorage
import net.kyori.adventure.text.Component
import java.util.*

class PlayerImpl(
    override val name: String, override val uuid: UUID, storage: UnsafeStorage, eventManager: EventManager,
    attributeManager: AttributeManager, val equipmentManager: EquipmentManager,
    val cooldownManager: CooldownManager, scheduler: Scheduler
) :
    Player, SelectableEntityImpl(uuid, storage, eventManager, attributeManager, scheduler) {

    override val isPlayer = true

    override fun sendMessage(message: Message) {
        MessageProcessor.process(this, message)
    }

    override fun setCooldown(spellName: String, durationTicks: Int) {
        return cooldownManager.store(this, spellName, durationTicks)
    }

    override fun isSpellCoolingDown(spellIdentifier: String): Boolean {
        return cooldownManager.isCoolingDown(this, spellIdentifier)
    }

    override fun sendActionBarString(msg: String) {
        return this.sendMessage(object : Message {
            override val type = Message.MessageType.ACTION_BAR
            override val identifier = "playerImpl_sendActionBarString"
            override fun get(): Component {
                return Component.text(msg)
            }

            override fun getScoreboardEntries() = null
        })
    }

    override fun getEquipments(): List<Equipment> {
        return equipmentManager.getCurrentEquipments(this)
    }
}
