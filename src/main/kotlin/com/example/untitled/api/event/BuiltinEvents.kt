package com.example.untitled.api.event

import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.api.player.Player
import com.example.untitled.api.spell.SpellInfo

class BuiltinEvents {
    companion object {
        class SpellHit(val spellInfo: SpellInfo, val caster: Player, val target: SelectableEntity) : Event {}
        class OnTick : Event {}
    }
}