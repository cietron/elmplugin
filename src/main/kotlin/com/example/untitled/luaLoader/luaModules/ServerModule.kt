package com.example.untitled.luaLoader.luaModules

import com.example.untitled.apiImpl.server.ServerImpl
import com.example.untitled.luaAdapter.server.ServerClass
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class ServerModule() : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val library = tableOf()
        library.set("server", ServerClass().getTable(LuaTable(), ServerClass.Container(ServerImpl())))

        env.set("ServerLib", library)
        env["package"]["loaded"].set("ServerLib", library)
        return library
    }
}