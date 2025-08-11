package com.example.untitled.api.spell

import com.example.untitled.api.item.Equipment

// This generic parameter does nothing, but refactoring it takes lots of efforts.
interface Spell<out T : SpellTriggerContext> {
    val identifier: String
    val spellItem: Equipment
    val cooldownTicks: Int
    val triggerType: SpellTriggerType

    fun preCheck(context: @UnsafeVariance T): Boolean
    fun execute(context: @UnsafeVariance T)
}