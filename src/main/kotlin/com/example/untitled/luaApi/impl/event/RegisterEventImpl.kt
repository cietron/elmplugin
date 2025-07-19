package com.example.untitled.luaApi.impl.event

import com.example.untitled.Untitled
import com.example.untitled.luaLoader.EventManager
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class RegisterEventImpl : TwoArgFunction() {
    override fun call(arg1: LuaValue?, arg2: LuaValue?): LuaValue? {

        if (arg1 == null || arg2 == null || !arg1.isstring() || !arg2.isfunction()) {
            return error("RegisterEvent Bad function call")
        }

        val type = EventManager.EventType.fromIdentifier(arg1.tojstring())

        if (type == null) {
            return error("Register event type $arg1 not found")
        }

        Untitled.eventManager.addListener(type, arg2 as LuaFunction)

        return TRUE
    }
}
