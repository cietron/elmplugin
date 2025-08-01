package com.example.untitled.luaApi.impl

import com.example.untitled.luaApi.impl.player.SendPlayerMessageImpl
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class PlayerModule() : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val library = tableOf()
        library.set("sendPlayerMessage", SendPlayerMessageImpl())
        env.set("hostname", library)
        env["package"]["loaded"].set("hostname", library)
        return library
    }
}
