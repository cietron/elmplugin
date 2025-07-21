package com.example.untitled.luaAdapter.spell

import com.example.untitled.apiImpl.spell.GetSpellCasterImpl
import com.example.untitled.luaAdapter.player.PlayerImplBaseLua
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ZeroArgFunction

class GetSpellCasterImplLua(private val getSpellCasterImpl: GetSpellCasterImpl) :
    ZeroArgFunction() {
    override fun call(): LuaValue? {
        if (getSpellCasterImpl.getSpellCaster() == null) {
            return NIL
        }

        return PlayerImplBaseLua().getTable(
            LuaTable(),
            PlayerImplBaseLua.Container(getSpellCasterImpl.getSpellCaster()!!)
        )
    }
}
