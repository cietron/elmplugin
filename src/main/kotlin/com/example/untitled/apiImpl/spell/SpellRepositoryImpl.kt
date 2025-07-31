package com.example.untitled.apiImpl.spell

import com.example.untitled.api.spell.Spell
import com.example.untitled.api.spell.SpellRepository
import com.example.untitled.storage.UnsafeStorage

class SpellRepositoryImpl(val storage: UnsafeStorage) : SpellRepository {
    private val registeredSpellKey = "spellRepository_registeredSpell"
    override fun register(identifier: String, spell: Spell<*>): Boolean {
        val key = this.getKey(identifier)

        if (storage.retrieveRaw<Spell<*>>(key) != null) {
            return false
        }

        storage.storeRaw(key, spell)
        this.getRegisteredSpell().add(key)
        return true
    }

    override fun get(identifier: String): Spell<*>? {
        val key = this.getKey(identifier)

        return storage.retrieveRaw<Spell<*>>(key)
    }

    override fun getAll(): List<Spell<*>> {
        val spells = ArrayList<Spell<*>>()
        for (spellKey in this.getRegisteredSpell()) {
            spells.add(storage.retrieveRaw<Spell<*>>(spellKey)!!)
        }
        return spells.toList()
    }


    private fun getRegisteredSpell(): MutableList<String> {
        if (storage.retrieveRaw<ArrayList<String>>(registeredSpellKey) == null) {
            storage.storeRaw(registeredSpellKey, ArrayList<String>())
        }
        return storage.retrieveRaw<ArrayList<String>>(registeredSpellKey)!!
    }

    private fun getKey(identifier: String): String {
        return "spellRepository#$identifier"
    }
}