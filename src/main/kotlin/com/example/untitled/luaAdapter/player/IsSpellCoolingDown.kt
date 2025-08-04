package com.example.untitled.luaAdapter.player

import com.example.untitled.api.player.Player
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction

class IsSpellCoolingDown(private val player: Player) : OneArgFunction() {
    override fun call(arg: LuaValue?): LuaValue {
        if (arg == null || !arg.isstring()) {
            return error("Argument must be a string (spell identifier)")
        }
        val spellIdentifier = arg.tojstring()
        return LuaValue.valueOf(player.isSpellCoolingDown(spellIdentifier))
    }
}