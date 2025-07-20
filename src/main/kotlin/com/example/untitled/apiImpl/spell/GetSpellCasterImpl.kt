package com.example.untitled.apiImpl.spell

import com.example.untitled.api.player.Player
import com.example.untitled.api.spell.GetSpellCaster
import com.example.untitled.api.spell.SpellContext

class GetSpellCasterImpl(val spellContext: SpellContext?) : GetSpellCaster {
    override fun getSpellCaster(): Player? {
        if (spellContext == null) {
            return null
        }

        return spellContext.caster
    }
}
