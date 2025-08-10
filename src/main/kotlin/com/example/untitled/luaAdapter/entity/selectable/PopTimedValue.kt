package com.example.untitled.luaAdapter.entity.selectable

import com.example.untitled.api.entity.SelectableEntity
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction

class PopTimedValue(private val impl: SelectableEntity) : OneArgFunction() {
    override fun call(arg: LuaValue): LuaValue {
        if (!arg.isstring()) {
            return error("SelectableEntity.popTimedValue name must be a string")
        }

        val name = arg.tojstring()
        val value = impl.popTimedValue(name) ?: return NIL
        return valueOf(value)
    }
}