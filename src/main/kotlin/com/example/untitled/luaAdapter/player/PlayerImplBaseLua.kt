package com.example.untitled.luaAdapter.player

import com.example.untitled.api.player.Player
import com.example.untitled.apiImpl.entity.SelectableEntityImpl
import com.example.untitled.luaAdapter.entity.SelectableEntityImplLua
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaTable

class PlayerImplBaseLua(val player: Player) : BaseLuaTable(CLASS_NAME) {

    companion object {
        const val CLASS_NAME = "player"
    }

    override fun modifyTable(table: LuaTable) {

        val selectable = SelectableEntityImplLua(SelectableEntityImpl(player.uuid))
        selectable.getTable(table)

        table.set("name", player.name)
    }
}
