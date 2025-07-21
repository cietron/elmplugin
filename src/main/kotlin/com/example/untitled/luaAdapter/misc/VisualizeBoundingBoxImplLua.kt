package com.example.untitled.luaAdapter.misc

import com.example.untitled.apiImpl.Misc.VisualizeBoundingBoxImpl
import com.example.untitled.luaAdapter.util.Vector3dTable
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ThreeArgFunction

class VisualizeBoundingBoxImplLua(val impl: VisualizeBoundingBoxImpl) : ThreeArgFunction() {
    override fun call(arg1: LuaValue?, arg2: LuaValue?, duration: LuaValue?): LuaValue? {

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

        if (duration == null || !duration.islong()) {
            return error("Duration is not an long number")
        }

        impl.with(p1, p2, duration.tolong())
        return NIL
    }
}