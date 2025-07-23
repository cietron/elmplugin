package com.example.untitled.luaAdapter.player

import com.example.untitled.api.player.Player
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class SetSpellCooldown(val player: Player) : TwoArgFunction() {
    override fun call(spellName: LuaValue, tickDuration: LuaValue): LuaValue? {
        if (!spellName.isstring() || !tickDuration.isint()) {
            return error("Invalid arguments: spellName must be a string and tickDuration must be an integer.")
        }

        val spellNameStr = spellName.tojstring()
        val tickDurationInt = tickDuration.toint()

        player.setCooldown(spellNameStr, tickDurationInt)
        return TRUE
    }
}