package com.example.untitled.luaAdapter.server.cooldownManager

import com.example.untitled.api.spell.CooldownManager
import com.example.untitled.luaAdapter.player.PlayerImplBaseLua
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class IsCoolingDown(val impl: CooldownManager) : TwoArgFunction() {
    override fun call(playerTable: LuaValue?, spellIdentifier: LuaValue): LuaValue? {
        val player = PlayerImplBaseLua().fromLuaValue(playerTable)
            ?: return error("CooldownManager isCoolingDown invalid player table")
        if (!spellIdentifier.isstring()) {
            return error("CooldownManager isCoolingDown invalid spellIdentifier")
        }

        val spellID = spellIdentifier.tojstring()

        return LuaValue.valueOf(impl.isCoolingDown(player.player, spellID))
    }
}