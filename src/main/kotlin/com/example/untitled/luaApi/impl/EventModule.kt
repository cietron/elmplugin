package com.example.untitled.luaApi.impl

import com.example.untitled.luaApi.impl.event.RegisterEventImpl
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class EventModule() : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val library = tableOf()
        library.set("registerEventListener", RegisterEventImpl())
        env.set("event", library)
        env["package"]["loaded"].set("event", library)
        return library
    }
}
