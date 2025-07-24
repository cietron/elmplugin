package com.example.untitled.luaLoader.luaModules

import com.example.untitled.Untitled
import com.example.untitled.luaAdapter.spell.RegisterSpell
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

/**
 * @custom.LuaDoc ---@class spellLib
 * @custom.LuaDoc ---@field registerSpell fun(identifier: string, spell: spell): boolean
 *
 */
class SpellModule() : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val library = tableOf()
        library.set("registerSpell", RegisterSpell(Untitled.spellManager))
        env.set("spellLib", library)
        env["package"]["loaded"].set("spellLib", library)
        return library
    }
}