package com.example.untitled.luaAdapter.server.cooldownManager

import com.example.untitled.api.spell.CooldownManager
import com.example.untitled.luaAdapter.player.PlayerImplBaseLua
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class GetRemainingTicks(private val impl: CooldownManager) : TwoArgFunction() {
    override fun call(playerTable: LuaValue, spellIdentifier: LuaValue): LuaValue? {
        val player =
            PlayerImplBaseLua().fromLuaValue(playerTable) ?: return error("CooldownManager store invalid player table")
        if (!spellIdentifier.isstring()) {
            return error("CooldownManager store invalid spellIdentifier")
        }

        val spellID = spellIdentifier.tojstring()
        return valueOf(impl.getRemainingTicks(player.player, spellID))
    }
}