package com.example.untitled.apiImpl.spell

import com.example.untitled.api.event.BuiltinEvents
import com.example.untitled.api.event.EventManager
import com.example.untitled.api.player.Player
import com.example.untitled.api.spell.CooldownManager
import java.util.*


class CooldownManagerImpl(private val eventManager: EventManager) : CooldownManager {
    private val cooldowns = HashMap<UUID, MutableMap<String, Long>>()
    private var currentTick: Long = 0

    init {
        this.registerEvent()
    }

    override fun store(player: Player, spellIdentifier: String, durationTicks: Int) {
        val playerCooldowns = cooldowns.getOrPut(player.uuid) { mutableMapOf() }
        playerCooldowns[spellIdentifier] = currentTick + durationTicks
    }

    override fun isCoolingDown(player: Player, spellIdentifier: String): Boolean {
        val playerCooldowns = cooldowns[player.uuid] ?: return false
        val endTime = playerCooldowns[spellIdentifier] ?: return false

        return currentTick < endTime
    }

    override fun getRemainingTicks(player: Player, spellIdentifier: String): Int {
        val playerCooldowns = cooldowns[player.uuid] ?: return 0
        val endTime = playerCooldowns[spellIdentifier] ?: return 0

        return maxOf(0, (endTime - currentTick).toInt())
    }

    fun registerEvent() {
        eventManager.registerEvent(BuiltinEvents.OnTick::class, {
            this.currentTick++
            return@registerEvent true // keep listening
        })
    }

    fun clean() {
        cooldowns.clear()
        currentTick = 0
    }

}