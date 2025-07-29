package com.example.untitled.luaLoader.luaModules

import com.example.untitled.Untitled
import com.example.untitled.luaAdapter.attribute.RegisterAttribute
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class ServerModule() : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val library = tableOf()
        library.set("registerAttribute", RegisterAttribute(Untitled.attributeManager))

        env.set("ServerLib", library)
        env["package"]["loaded"].set("ServerLib", library)
        return library
    }
}