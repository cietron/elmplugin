package com.example.untitled.luaAdapter.entity.selectable

import com.example.untitled.api.entity.SelectableEntity
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class SetAttribute(val impl: SelectableEntity) : TwoArgFunction() {
    override fun call(attributeName: LuaValue, value: LuaValue): LuaValue? {
        if (!attributeName.isstring()) {
            return error("AttributeName must be a string")
        }

        if (!value.isnumber()) {
            return error("value must be a number")
        }

        return valueOf(impl.setAttribute(attributeName.tojstring(), value.todouble()))
    }
}