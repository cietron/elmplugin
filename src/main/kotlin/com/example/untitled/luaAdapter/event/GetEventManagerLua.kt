package com.example.untitled.luaAdapter.event

import com.example.untitled.api.event.EventManager
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ZeroArgFunction

class GetEventManagerLua(val impl: EventManager) : ZeroArgFunction() {
    override fun call(): LuaValue? {
        return EventManagerImplLua(impl).getTable(LuaTable(), Unit)
    }
}