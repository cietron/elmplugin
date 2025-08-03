package com.example.untitled.luaAdapter.entity.selectable

import com.example.untitled.api.entity.SelectableEntity
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction

class SetNoDamageTick(private val impl: SelectableEntity) : OneArgFunction() {
    override fun call(arg: LuaValue?): LuaValue? {
        if (arg == null || !arg.isnumber()) {
            return error("Argument must be a number (tick count)")
        }
        val tick = arg.toint()
        impl.setNoDamageTick(tick)
        return NIL
    }
}