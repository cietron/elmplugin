package com.example.untitled.api.spell

import net.kyori.adventure.text.Component

// This generic parameter does nothing, but refactoring it takes lots of efforts.
interface Spell<out T : SpellTriggerContext> {
    val identifier: String
    val displayName: Component
    val description: Component
    val cooldownTicks: Int
    val triggerType: SpellTriggerType

    fun preCheck(context: @UnsafeVariance T): Boolean
    fun execute(context: @UnsafeVariance T)
}