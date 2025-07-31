package com.example.untitled.api.spell

import com.example.untitled.api.player.Player

interface SpellManager {
    fun registerSpell(player: Player, triggerType: SpellTriggerType, spell: Spell<*>, slot: Slot?): Boolean

    fun cleanPlayerSpell(player: Player)

    fun handleSpellTrigger(player: Player, context: SpellTriggerContext): Boolean
}