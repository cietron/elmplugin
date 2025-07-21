package com.example.untitled.luaAdapter.player

import com.example.untitled.api.player.Player
import com.example.untitled.apiImpl.entity.SelectableEntityImpl
import com.example.untitled.luaAdapter.entity.SelectableEntityImplLua
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction

class PlayerImplBaseLua : BaseLuaTable<PlayerImplBaseLua.Container>(CLASS_NAME, true) {

    companion object {
        const val CLASS_NAME = "player"
    }

    override fun modifyTable(table: LuaTable, container: Container) {

        val player = container.player

        val selectable = SelectableEntityImpl(player.uuid)
        SelectableEntityImplLua().getTable(table, SelectableEntityImplLua.Container(selectable))

        table.set("name", player.name)
        table.set("sendMessage", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue? {
                if (!arg.isstring()) {
                    error("Not string")
                }

                player.sendMessage(arg.tojstring())
                return TRUE
            }

        })
    }

    override fun checkParseTable(table: LuaTable): Boolean {
        println(isInstanceOf(table, CLASS_NAME))
        println(table.get("name").isstring())
        return (isInstanceOf(table, CLASS_NAME) && table.get("name").isstring())
    }

    override fun fromTable(table: LuaTable): Container? {
        TODO("not implemented")
    }

    data class Container(val player: Player)
}
