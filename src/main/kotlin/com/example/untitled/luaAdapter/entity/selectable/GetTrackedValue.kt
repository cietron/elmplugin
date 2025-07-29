package com.example.untitled.luaAdapter.entity.selectable

import com.example.untitled.api.entity.SelectableEntity
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction

class GetTrackedValue(val impl: SelectableEntity) : OneArgFunction() {
    override fun call(valueName: LuaValue): LuaValue? {
        if (!valueName.isstring()) {
            return error("valueName must be a string")
        }

        val value = impl.getTrackedValue(valueName.tojstring())
        value ?: return NIL
        return valueOf(value)
    }
}