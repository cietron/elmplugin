package com.example.untitled.luaAdapter.server

import com.example.untitled.api.server.Server
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaTable

class ServerClass : BaseLuaTable<ServerClass.Container>(CLASS_NAME, true) {

    companion object {
        const val CLASS_NAME = "serverInstance"
    }

    override fun modifyTable(
        table: LuaTable,
        container: Container
    ) {

        table.set("getAllPlayers", GetAllPlayers(container.impl))
    }

    override fun checkParseTable(table: LuaTable): Boolean {
        TODO("Not yet implemented")
    }

    override fun fromTable(table: LuaTable): Container? {
        TODO("Not yet implemented")
    }

    data class Container(val impl: Server)
}