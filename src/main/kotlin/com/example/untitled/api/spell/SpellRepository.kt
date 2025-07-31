package com.example.untitled.api.spell

interface SpellRepository {
    fun register(identifier: String, spell: Spell<*>): Boolean
    fun get(identifier: String): Spell<*>?
    fun getAll(): List<Spell<*>>
}