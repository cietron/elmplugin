package com.example.untitled.luaAdapter.capture

import com.example.untitled.apiImpl.capture.EyesightEntityImpl
import com.example.untitled.apiImpl.entity.PlayerImpl
import com.example.untitled.apiImpl.entity.SelectableEntityImpl
import com.example.untitled.luaAdapter.entity.SelectableEntityImplLua
import com.example.untitled.luaAdapter.player.PlayerImplBaseLua
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import java.util.*

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

        val name = arg1.get("name").tojstring()
        val uuid = arg1.get("uuid").tojstring()

        val radius = arg2.toint()

        val result = impl.getEyesightEntity(PlayerImpl(name, UUID.fromString(uuid)), radius)

        if (result == null) {
            return NIL
        }

        return SelectableEntityImplLua().getTable(
            LuaTable(),
            SelectableEntityImplLua.Container(SelectableEntityImpl(result.uuid))
        )
    }
}
