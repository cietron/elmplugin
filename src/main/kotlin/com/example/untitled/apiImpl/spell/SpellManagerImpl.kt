package com.example.untitled.apiImpl.spell

import com.example.untitled.api.spell.Spell
import com.example.untitled.api.spell.SpellManager
import com.example.untitled.storage.UnsafeStorage

class SpellManagerImpl(val storage: UnsafeStorage) : SpellManager {
    override fun registerSpell(identifier: String, spell: Spell): Boolean {
        val key = this.getKey(identifier)

        if (storage.retrieveRaw<Spell>(key) != null) {
            return false
        }

        storage.storeRaw(key, spell)
        return true
    }

    override fun getSpell(identifier: String): Spell? {
        val key = this.getKey(identifier)

        return storage.retrieveRaw<Spell>(key)
    }

    private fun getKey(identifier: String): String {
        return "spell#$identifier"
    }
}