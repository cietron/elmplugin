package com.example.untitled.luaAdapter.server.cooldownManager

import com.example.untitled.api.spell.CooldownManager
import com.example.untitled.luaAdapter.player.PlayerImplBaseLua
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ThreeArgFunction

class Store(val impl: CooldownManager) : ThreeArgFunction() {
    override fun call(
        playerTable: LuaValue,
        spellIdentifier: LuaValue,
        durationTicks: LuaValue
    ): LuaValue {
        val player =
            PlayerImplBaseLua().fromLuaValue(playerTable) ?: return error("CooldownManager store invalid player table")
        if (!spellIdentifier.isstring()) {
            return error("CooldownManager store invalid spellIdentifier")
        }

        if (!durationTicks.isint()) {
            return error("CooldownManager store invalid duration ticks (must be int)")
        }

        val spellID = spellIdentifier.tojstring()
        val ticks = durationTicks.toint()

        impl.store(player.player, spellID, ticks)
        return NIL
    }
}