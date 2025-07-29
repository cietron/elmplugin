package com.example.untitled.luaAdapter.entity.selectable

import com.example.untitled.api.entity.SelectableEntity
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class SetTrackedValue(val impl: SelectableEntity) : TwoArgFunction() {
    override fun call(trackedValueName: LuaValue, value: LuaValue): LuaValue? {
        if (!trackedValueName.isstring()) {
            return error("SelectableEntity SetTrackedValue trackedValueName must be a string")
        }
        if (!value.isnumber()) {
            return error("SelectableEntity SetTrackedValue value must be a double")
        }

        return valueOf(impl.setTrackedValue(trackedValueName.tojstring(), value.todouble()))

    }
}