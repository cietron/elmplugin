package com.example.untitled.luaApi.impl

import com.example.untitled.luaApi.impl.entity.EntityLocationImpl
import com.example.untitled.luaApi.impl.entity.KillEntityImpl
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class EntityModule() : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val library = tableOf()
        library.set("killEntity", KillEntityImpl())
        library.set("getEntityLocation", EntityLocationImpl())
        env.set("entity", library)
        env["package"]["loaded"].set("entity", library)
        return library
    }
}
