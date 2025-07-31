package com.example.untitled.luaAdapter.spell

import com.example.untitled.api.spell.SpellRepository
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class RegisterSpell(val impl: SpellRepository) : TwoArgFunction() {
    override fun call(identifier: LuaValue, spellClass: LuaValue): LuaValue? {
        if (identifier.isnil() || !identifier.isstring()) {
            return error("registerSpell: first argument must be a string identifier")
        }
        if (spellClass.isnil() || !spellClass.istable()) {
            return error("registerSpell: second argument must be a spell table")
        }

        val spellTable = spellClass.checktable()
        val spellContainer = SpellLuaClass().fromLuaValue(spellTable)
            ?: return error("registerSpell: invalid spell table")

        val success = impl.register(identifier.tojstring(), spellContainer.spell)
        return LuaValue.valueOf(success)
    }
}