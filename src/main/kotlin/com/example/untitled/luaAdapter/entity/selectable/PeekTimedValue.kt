package com.example.untitled.luaAdapter.entity.selectable

import com.example.untitled.api.entity.SelectableEntity
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction

class PeekTimedValue(private val impl: SelectableEntity) : OneArgFunction() {
    override fun call(arg: LuaValue): LuaValue? {
        if (!arg.isstring()) {
            return error("SelectableEntity.peekTimedValue name must be a string")
        }

        val value = impl.peekTimedValue(arg.tojstring()) ?: return NIL

        return valueOf(value)
    }
}