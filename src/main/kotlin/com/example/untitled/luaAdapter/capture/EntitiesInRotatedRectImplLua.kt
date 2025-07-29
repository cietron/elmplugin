package com.example.untitled.luaAdapter.capture

import com.example.untitled.api.capture.EntitiesInRotatedRect
import com.example.untitled.api.player.Player
import com.example.untitled.luaAdapter.entity.selectable.SelectableEntityImplLua
import com.example.untitled.luaAdapter.player.PlayerImplBaseLua
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction

class EntitiesInRotatedRectImplLua(val impl: EntitiesInRotatedRect) : OneArgFunction() {
    override fun call(arg: LuaValue?): LuaValue? {

        if (arg == null || !arg.istable() || BaseLuaTable.isInstanceOf(arg as LuaTable, PlayerImplBaseLua.CLASS_NAME)) {
            return NIL
        }

        val luaPlayer = PlayerImplBaseLua().fromLuaValue(arg)
        luaPlayer ?: return error("Player not found")

        val player = luaPlayer.player

        val entities = impl.get(player)
        val table = LuaTable()
        entities.forEachIndexed { i, entity ->
            table.set(
                i + 1, when (entity) {
                    is Player -> PlayerImplBaseLua().getTable(LuaTable(), PlayerImplBaseLua.Container(entity))
                    else -> SelectableEntityImplLua().getTable(
                        LuaTable(),
                        SelectableEntityImplLua.Container(entity)
                    )
                }
            )
        }

        return table
    }
}