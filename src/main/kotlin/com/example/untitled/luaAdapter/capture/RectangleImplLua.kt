package com.example.untitled.luaAdapter.capture

import com.example.untitled.apiImpl.capture.RectangleImpl
import com.example.untitled.apiImpl.entity.SelectableEntityImpl
import com.example.untitled.luaAdapter.entity.SelectableEntityImplLua
import com.example.untitled.luaAdapter.util.Vector3dTable
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction


class RectangleImplLua(val impl: RectangleImpl) : TwoArgFunction() {

    override fun call(arg1: LuaValue?, arg2: LuaValue?): LuaValue? {

        if (arg1 == null || arg2 == null) {
            return NIL
        }

        if (!arg1.istable() || !arg2.istable()) {
            return error("Parameters not tables: $arg1, $arg2")
        }

        val p1 = Vector3dTable.toJoml(arg1 as LuaTable)
        val p2 = Vector3dTable.toJoml(arg2 as LuaTable)

        if (p1 == null || p2 == null) {
            return error("Table failed converting to joml: $arg1, $arg2")
        }

        val entities = impl.capture(p1, p2)
        val result = LuaTable()
        for ((i, entity) in entities.withIndex()) {
            // Cast is safe if your impl always returns SelectableEntityImpl
            result.set(
                i,
                SelectableEntityImplLua().getTable(
                    LuaTable(),
                    SelectableEntityImplLua.Container(entity as SelectableEntityImpl)
                )
            )
        }
        return result
    }

}