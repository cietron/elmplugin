package com.example.untitled.apiImpl.spell

import com.example.untitled.Untitled
import com.example.untitled.api.player.Player
import com.example.untitled.api.spell.*
import com.example.untitled.player.PlayerMessenger
import com.example.untitled.storage.UnsafeStorage

class SpellManagerImpl(val storage: UnsafeStorage, val cooldownManager: CooldownManager) : SpellManager {
    private fun getMap(player: Player): MutableMap<SpellKey, Spell<*>> {
        val key = "SpellManager#player#${player.uuid}"
        if (this.storage.retrieveRaw<HashMap<SpellKey, Spell<*>>>(key) == null) {
            this.storage.storeRaw(key, HashMap<SpellKey, Spell<*>>())
        }

        return this.storage.retrieveRaw<HashMap<SpellKey, Spell<*>>>(key)!!
    }

    override fun registerSpell(
        player: Player, triggerType: SpellTriggerType, spell: Spell<*>, slot: Slot?
    ): Boolean {
        val storeKey = when (triggerType) {
            SpellTriggerType.DoubleShift -> SpellKey(triggerType, null)
            SpellTriggerType.HitEntity -> SpellKey(triggerType, null)
            SpellTriggerType.RightClick -> SpellKey(triggerType, slot)
            SpellTriggerType.ArrowHitEntity -> SpellKey(triggerType, slot)
        }
        Untitled.instance.logger.info("registered spell ${spell.identifier} at slot ${slot} for ${player.name}")
        val map = this.getMap(player)
        map.put(storeKey, spell)
        return true
    }

    override fun cleanPlayerSpell(player: Player) {
        this.getMap(player).clear()
    }

    override fun handleSpellTrigger(player: Player, context: SpellTriggerContext): Boolean {
        val map = this.getMap(player)
        val spell = when (context) {
            is SpellTriggerContext.DoubleShift -> map[SpellKey(context.triggerType, null)]
            is SpellTriggerContext.HitEntity -> map[SpellKey(context.triggerType, null)]
            is SpellTriggerContext.RightClick -> map[SpellKey(context.triggerType, context.slot)]
            is SpellTriggerContext.ArrowHitEntity -> map[SpellKey(context.triggerType, context.slot)]
        }

        spell ?: return false

        return this.executeSpell(spell, context)
    }

    private fun executeSpell(spell: Spell<*>, context: SpellTriggerContext): Boolean {

        if (!spell.preCheck(context)) {
            return false
        }

        val identifier = spell.identifier
        val caster = context.player

        if (cooldownManager.isCoolingDown(caster, identifier)) {
            val remainingTicks = cooldownManager.getRemainingTicks(caster, identifier)
            PlayerMessenger.sendCooldownMessage(caster, identifier, remainingTicks)
            return false
        }

        spell.execute(context)
        cooldownManager.store(caster, identifier, spell.cooldownTicks)
        return true
    }

    private data class SpellKey(val triggerType: SpellTriggerType, val slot: Slot?)
}