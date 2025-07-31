package com.example.untitled.api.spell

import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.api.player.Player
import kotlin.reflect.KClass

sealed class SpellTriggerContext {
    abstract val player: Player
    abstract val triggerType: SpellTriggerType

    data class DoubleShift(override val player: Player) : SpellTriggerContext() {
        override val triggerType = SpellTriggerType.DoubleShift
    }

    data class RightClick(
        override val player: Player,
        val slot: Slot
    ) : SpellTriggerContext() {
        override val triggerType = SpellTriggerType.RightClick
    }

    data class HitEntity(
        override val player: Player,
        val victim: SelectableEntity
    ) : SpellTriggerContext() {
        override val triggerType = SpellTriggerType.HitEntity
    }
}


enum class Slot {
    ONE,
    TWO,
    THREE,
    FOUR
}

enum class SpellTriggerType(val key: String, val contextClass: KClass<*>) {
    DoubleShift("double_shift", SpellTriggerContext.DoubleShift::class),
    RightClick("right_click", SpellTriggerContext.RightClick::class),
    HitEntity("hit_entity", SpellTriggerContext.HitEntity::class);

    companion object {
        fun fromKey(key: String): SpellTriggerType? {
            return entries.find { it.key == key }
        }
    }
}