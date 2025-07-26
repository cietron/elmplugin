package com.example.untitled.apiImpl.spell

import com.example.untitled.api.event.BuiltinEvents
import com.example.untitled.api.event.EventManager
import com.example.untitled.api.player.Player
import java.util.*

class CooldownManager(private val eventManager: EventManager) {
    private val cooldowns = HashMap<UUID, MutableMap<String, Long>>()
    private var currentTick: Long = 0

    init {
        this.registerEvent()
    }

    fun store(player: Player, spellName: String, durationTicks: Int) {
        val playerCooldowns = cooldowns.getOrPut(player.uuid) { mutableMapOf() }
        playerCooldowns[spellName] = currentTick + durationTicks
    }

    fun isCoolingDown(player: Player, spellName: String): Boolean {
        val playerCooldowns = cooldowns[player.uuid] ?: return false
        val endTime = playerCooldowns[spellName] ?: return false

        return currentTick < endTime
    }

    fun getRemainingTicks(player: Player, spellName: String): Int {
        val playerCooldowns = cooldowns[player.uuid] ?: return 0
        val endTime = playerCooldowns[spellName] ?: return 0

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