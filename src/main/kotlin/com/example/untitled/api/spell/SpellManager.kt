package com.example.untitled.api.spell

interface SpellManager {
    fun registerSpell(identifier: String, spell: Spell): Boolean

    fun getSpell(identifier: String): Spell?
}