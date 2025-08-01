package com.example.untitled.luaAdapter.attribute

import com.example.untitled.api.attribute.Attribute
import com.example.untitled.apiImpl.store.Repository
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

/**
 * @custom.LuaDoc ---@field registerAttribute fun(attributeName: string, defaultValue: number)
 */
class RegisterAttribute(val impl: Repository<Attribute>) : TwoArgFunction() {
    override fun call(attributeName: LuaValue, defaultValue: LuaValue): LuaValue? {
        if (!attributeName.isstring()) {
            return error("RegisterAttribute attributeName must be a string")
        }
        if (!defaultValue.isnumber()) {
            return error("RegisterAttribute defaultValue must be a number")
        }

        val key = attributeName.tojstring()
        val value = defaultValue.todouble()

        impl.register(key, Attribute(key, value))
        return NIL
    }
}