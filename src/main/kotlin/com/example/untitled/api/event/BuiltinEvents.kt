package com.example.untitled.api.event

import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.api.player.Player
import com.example.untitled.api.spell.SpellInfo
import java.util.*

object BuiltinEvents {
    class SpellHit(val spellInfo: SpellInfo, val caster: Player, val target: SelectableEntity) : Event {}
    class OnTick : Event {}
    class OnArrowHitEntity(val arrowUUID: UUID, val victim: SelectableEntity) : Event
    class OnArrowHitBlock(val arrowUUID: UUID) : Event
    class OnFireballHitEntity(val fireballUUID: UUID, val victim: SelectableEntity) : Event
}