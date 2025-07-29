package com.example.untitled.luaAdapter.capture

import com.example.untitled.api.player.Player
import com.example.untitled.apiImpl.capture.EyesightEntityImpl
import com.example.untitled.luaAdapter.entity.selectable.SelectableEntityImplLua
import com.example.untitled.luaAdapter.player.PlayerImplBaseLua
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class EyesightEntityImplLua(val impl: EyesightEntityImpl) : TwoArgFunction() {
    override fun call(arg1: LuaValue?, arg2: LuaValue?): LuaValue? {

        if (
            arg1 == null ||
                !arg1.istable() ||
                BaseLuaTable.isInstanceOf(arg1 as LuaTable, PlayerImplBaseLua.CLASS_NAME)
        ) {
            return error("EyesightEntityImplLua bad arguments: $arg1")
        }

        if (arg2 == null || !arg2.isint()) {
            return error("EyesightEntityImplLua bad arguments: $arg2")
        }

        arg1.get("name").tojstring()
        arg1.get("uuid").tojstring()

        val radius = arg2.toint()

        val luaPlayer = PlayerImplBaseLua().fromLuaValue(arg1)
        luaPlayer ?: return error("Player not found")

        val result = impl.getEyesightEntity(luaPlayer.player, radius)

        if (result == null) {
            return NIL
        }

        return when (result) {
            is Player -> PlayerImplBaseLua().getTable(LuaTable(), PlayerImplBaseLua.Container(result))
            else -> SelectableEntityImplLua().getTable(
                LuaTable(),
                SelectableEntityImplLua.Container(result)
            )
        }
    }
}
