package com.example.untitled.luaAdapter.entity.selectable

import com.example.untitled.api.entity.SelectableEntity
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ThreeArgFunction

class PushTimedValues(private val impl: SelectableEntity) : ThreeArgFunction() {
    override fun call(
        arg1: LuaValue,
        arg2: LuaValue,
        arg3: LuaValue
    ): LuaValue {
        if (!arg1.isstring()) {
            return error("SelectableEntity.pushTimedValue name must be a string")
        }

        if (!arg2.isnumber()) {
            return error("SelectableEntity.pushTimedValue value must be a number")
        }

        if (!arg3.isnumber()) {
            return error("SelectableEntity.pushTimedValue expireAfterTicks must be a number")
        }
        val name = arg1.tojstring()
        val value = arg2.todouble()
        val expireAfterTicks = arg3.tolong()
        impl.pushTimedValue(name, value, expireAfterTicks)
        return NIL
    }
}