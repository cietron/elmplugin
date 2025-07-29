package com.example.untitled.luaAdapter.entity

import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.api.player.Player
import com.example.untitled.luaAdapter.entity.selectable.SelectableEntityImplLua
import com.example.untitled.luaAdapter.player.PlayerImplBaseLua
import org.luaj.vm2.LuaValue

object LuaEntityFactory {
    fun getLuaValue(entity: SelectableEntity): LuaValue {
        return when (entity) {
            is Player -> PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(entity))
            else -> SelectableEntityImplLua().getNewTable(SelectableEntityImplLua.Container(entity))
        }
    }
}