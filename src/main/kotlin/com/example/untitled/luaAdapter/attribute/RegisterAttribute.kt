package com.example.untitled.luaAdapter.attribute

import com.example.untitled.api.attribute.AttributeManager
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

/**
 * @custom.LuaDoc ---@field registerAttribute fun(attributeName: string, defaultValue: number)
 */
class RegisterAttribute(val impl: AttributeManager) : TwoArgFunction() {
    override fun call(attributeName: LuaValue, defaultValue: LuaValue): LuaValue? {
        if (!attributeName.isstring()) {
            return error("RegisterAttribute attributeName must be a string")
        }
        if (!defaultValue.isnumber()) {
            return error("RegisterAttribute defaultValue must be a number")
        }


        impl.registerKey(attributeName.tojstring(), defaultValue.todouble())
        return NIL
    }
}