package com.example.untitled.luaAdapter.server

import com.example.untitled.api.server.Server
import com.example.untitled.luaAdapter.player.PlayerImplBaseLua
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ZeroArgFunction

class GetAllPlayers(val impl: Server) : ZeroArgFunction() {
    override fun call(): LuaValue? {
        val LuaPlayerArray = impl.getAllPlayers().map { player ->
            PlayerImplBaseLua().getNewTable(
                PlayerImplBaseLua.Container(player)
            )
        }.toTypedArray()

        return LuaTable.listOf(LuaPlayerArray)
    }
}